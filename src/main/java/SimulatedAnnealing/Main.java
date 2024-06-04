package SimulatedAnnealing;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    private static final String REQUEST_QUEUE_URL = "https://sqs.eu-west-1.amazonaws.com/427305459303/PostNL_INPUT";
    private static final String RESPONSE_QUEUE_URL = "https://sqs.eu-west-1.amazonaws.com/427305459303/PostNL_OUTPUT";
    private static SqsClient sqsClient;

    public static void main(String[] args) {
        sqsClient = SqsClient.builder()
                .region(Region.EU_WEST_1)
                .build();

        // Continuous polling loop
        while (true) {
            try {
                List<Message> messages = pollMessages(REQUEST_QUEUE_URL);
                for (Message message : messages) {
                    System.out.println("Received message: " + message);
                    String[] params = message.body().split(";");

                    double startingTemperature = Double.parseDouble(params[0]);
                    double endingTemperature = Double.parseDouble(params[1]);
                    double coolingRate = Double.parseDouble(params[2]);
                    int optimizeDay = Integer.parseInt(params[3]);
                    boolean onlyRemove = Boolean.parseBoolean(params[5]);

                    System.out.println("Starting Temperature: " + startingTemperature + ", Ending Temperature: " + endingTemperature + ", Cooling Rate: " + coolingRate + ", Optimize Day: " + optimizeDay + ", Only Remove: " + onlyRemove);

                    // Process the message using Simulated Annealing
                    String result = processMessage(startingTemperature, endingTemperature, coolingRate, optimizeDay, params[4], onlyRemove);

                    // Send completion message to another SQS queue with result details
                    sendMessage(RESPONSE_QUEUE_URL, result);

                    // Optionally delete the message after processing
                    deleteMessage(REQUEST_QUEUE_URL, message.receiptHandle());
                }
            } catch (Exception e) {
                System.err.println("Error processing messages: " + e.getMessage());
            }
        }
        // sqsClient.close(); // Remove or handle outside the loop for graceful shutdown
    }

    public static String processMessage(double startingTemp, double endingTemp, double coolingRate, int optimizeDay, String checkDaysStr, boolean optimizeCurrent) throws IOException {
        long startTimeTotal = System.nanoTime();

        ArrayList<Integer> checkDays = (ArrayList<Integer>) Arrays.stream(checkDaysStr.split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toList());

        System.out.println("Parsed checkDays: " + checkDays);


        ArrayList<Double> packageIndex = ReadData.readPackageIndex("src/main/Data/DayOfYearIndex.csv");

        double dayIndex = packageIndex.get(optimizeDay);
        System.out.println("Optimizing for day " + optimizeDay + " with day index = " + dayIndex);


        ArrayList<ServiceLocation> serviceLocations = ReadData.readServiceLocationsFromFile("src/main/Data/ServicePointLocations.csv");

        ArrayList<Road> roads = ReadData.readRoadsFromFile("src/main/Data/edges.csv", dayIndex);

        ArrayList<Node> nodes = ReadData.readNodes("src/main/Data/nodes.csv");


        double[][] distances = ReadData.loadDistances("src/main/Data/distances.csv");


        ArrayList<Double> bounceRatesBefore = new ArrayList<>();
        ArrayList<Double> costsBefore = new ArrayList<>();

        ArrayList<ServiceLocation> serviceLocationsInitial = Utils.deepCopy(serviceLocations);
        ArrayList<Road> roadsDayInitial = Utils.deepCopy(ReadData.readRoadsFromFile("src/main/Data/edges.csv", packageIndex.get(optimizeDay)));

        ServiceLocationConfig configInitial = new ServiceLocationConfig(serviceLocationsInitial, roadsDayInitial, distances, false);
        System.out.println("Initial Total Costs: " + configInitial.getTotalCost());

        System.out.println("Calculating metrics before optimization...");

        for (int day : checkDays) {
            ArrayList<ServiceLocation> serviceLocations2 = Utils.deepCopy(serviceLocations);
            ArrayList<Road> roadsDay = Utils.deepCopy(ReadData.readRoadsFromFile("src/main/Data/edges.csv", packageIndex.get(day)));

            ServiceLocationConfig config = new ServiceLocationConfig(serviceLocations2, roadsDay, distances, true);
            bounceRatesBefore.add(config.getGlobalBounceRate());
            costsBefore.add(config.getTotalCost());


            config = null;
        }



        configInitial = null;

        System.out.println("Metrics before optimization calculated.");
        double maxBounceRateBefore = bounceRatesBefore.stream().max(Double::compareTo).orElse(0.0);
        double averageBounceRateBefore = bounceRatesBefore.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        double maxCostBefore = costsBefore.stream().max(Double::compareTo).orElse(0.0);
        double minCostBefore = costsBefore.stream().min(Double::compareTo).orElse(0.0);
        double averageCostBefore = costsBefore.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);

        System.out.println("Before Optimization:");
        System.out.println("Max Bounce Rate: " + maxBounceRateBefore);
        System.out.println("Average Bounce Rate: " + averageBounceRateBefore);
        System.out.println("Max Cost: " + maxCostBefore);
        System.out.println("Min Cost: " + minCostBefore);
        System.out.println("Average Cost: " + averageCostBefore);

        long startTimeSA = System.nanoTime();
        System.out.println("Starting Simulated Annealing optimization...");

        SimulatedAnnealing simulatedAnnealing = new SimulatedAnnealing(serviceLocations, roads, distances, nodes);
        simulatedAnnealing.optimize(startingTemp, endingTemp, coolingRate, optimizeCurrent);
        System.out.println("Simulated Annealing optimization completed");

        long endTimeSA = System.nanoTime();
        long timeSA = endTimeSA - startTimeSA;

        ServiceLocationConfig optimizedServiceLocationConfig = simulatedAnnealing.getServiceLocationConfig();
        ArrayList<ServiceLocation> optimizedServiceLocations = optimizedServiceLocationConfig.getServicelocations();

        ArrayList<Double> bounceRatesAfter = new ArrayList<>();
        ArrayList<Double> costsAfter = new ArrayList<>();

        System.out.println("Calculating metrics after optimization...");
        for (int day : checkDays) {
            ArrayList<ServiceLocation> optimizedServiceLocationsCopy = Utils.deepCopy(optimizedServiceLocations);
            ArrayList<Road> roadsDay = Utils.deepCopy(ReadData.readRoadsFromFile("src/main/Data/edges.csv", packageIndex.get(day)));
            ArrayList<Node> nodesCopy = Utils.deepCopy(nodes);

            ServiceLocationConfig config = new ServiceLocationConfig(optimizedServiceLocationsCopy, roadsDay, distances, true);
            bounceRatesAfter.add(config.getGlobalBounceRate());
            costsAfter.add(config.getTotalCost());

        }



        System.out.println("Metrics after optimization calculated.");
        double maxBounceRateAfter = bounceRatesAfter.stream().max(Double::compareTo).orElse(0.0);
        double averageBounceRateAfter = bounceRatesAfter.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        double maxCostAfter = costsAfter.stream().max(Double::compareTo).orElse(0.0);
        double minCostAfter = costsAfter.stream().min(Double::compareTo).orElse(0.0);
        double averageCostAfter = costsAfter.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);

        ArrayList<Integer> serviceLocationsNodes = new ArrayList<>();
        for (ServiceLocation serviceLocation : optimizedServiceLocations) {
            serviceLocationsNodes.add(serviceLocation.getNodeID());
        }

        System.out.println("After Optimization:");
        System.out.println("Max Bounce Rate: " + maxBounceRateAfter);
        System.out.println("Average Bounce Rate: " + averageBounceRateAfter);
        System.out.println("Max Cost: " + maxCostAfter);
        System.out.println("Min Cost: " + minCostAfter);
        System.out.println("Average Cost: " + averageCostAfter);

        long endTimeTotal = System.nanoTime();
        long timeTotal = endTimeTotal - startTimeTotal;

        System.out.println("TimeTotal: " + timeTotal);
        System.out.println("TimeSA: " + timeSA);

        return "Input Parameters:\n" +
                "Starting Temperature: " + startingTemp + "\n" +
                "Ending Temperature: " + endingTemp + "\n" +
                "Cooling Rate: " + coolingRate + "\n" +
                "Optimize Day: " + optimizeDay + "\n" +
                "Check Days: " + checkDaysStr + "\n" +
                "Optimize Current: " + optimizeCurrent + "\n" +
                "Before Optimization:\n" +
                "Max Bounce Rate: " + maxBounceRateBefore + "\n" +
                "Average Bounce Rate: " + averageBounceRateBefore + "\n" +
                "Max Cost: " + maxCostBefore + "\n" +
                "Min Cost: " + minCostBefore + "\n" +
                "Average Cost: " + averageCostBefore + "\n" +
                "After Optimization:\n" +
                "Max Bounce Rate: " + maxBounceRateAfter + "\n" +
                "Average Bounce Rate: " + averageBounceRateAfter + "\n" +
                "Max Cost: " + maxCostAfter + "\n" +
                "Min Cost: " + minCostAfter + "\n" +
                "Average Cost: " + averageCostAfter + "\n" +
                "ServiceLocations: " + serviceLocationsNodes + "\n" +
                "TimeTotal: " + timeTotal + " ns\n" +
                "TimeSA: " + timeSA + " ns\n";
    }

    private static List<Message> pollMessages(String queueUrl) {
        ReceiveMessageRequest receiveMessageRequest = ReceiveMessageRequest.builder()
                .queueUrl(queueUrl)
                .waitTimeSeconds(20) // Long polling
                .maxNumberOfMessages(1)
                .build();
        return sqsClient.receiveMessage(receiveMessageRequest).messages();
    }

    private static void sendMessage(String queueUrl, String messageBody) {
        SendMessageRequest sendMsgRequest = SendMessageRequest.builder()
                .queueUrl(queueUrl)
                .messageBody(messageBody)
                .build();
        sqsClient.sendMessage(sendMsgRequest);
    }

    private static void deleteMessage(String queueUrl, String receiptHandle) {
        DeleteMessageRequest deleteMessageRequest = DeleteMessageRequest.builder()
                .queueUrl(queueUrl)
                .receiptHandle(receiptHandle)
                .build();
        sqsClient.deleteMessage(deleteMessageRequest);
    }
}

