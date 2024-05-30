package SimulatedAnnealing;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
                    String[] params = message.body().split(",");
                    if (params.length == 4) {
                        double startingTemperature = Double.parseDouble(params[0]);
                        double endingTemperature = Double.parseDouble(params[1]);
                        double coolingRate = Double.parseDouble(params[2]);
                        double kValue = Double.parseDouble(params[3]);

                        System.out.println(startingTemperature + " " + endingTemperature + " " + coolingRate);

                        // Process the message using Simulated Annealing
                        String result = processMessage(startingTemperature, endingTemperature, coolingRate, kValue);

                        // Send completion message to another SQS queue with result details
                        sendMessage(RESPONSE_QUEUE_URL, result);

                        // Optionally delete the message after processing
                        deleteMessage(REQUEST_QUEUE_URL, message.receiptHandle());
                    }
                }
            } catch (Exception e) {
                System.err.println("Error processing messages: " + e.getMessage());
                // Handle exceptions (optional)
            }
        }
        // sqsClient.close(); // Remove or handle outside the loop for graceful shutdown
    }

    private static String processMessage(double startingTemp, double endingTemp, double coolingRate, double kValue) throws IOException {
        ArrayList<ServiceLocation> serviceLocations = ReadData.readServiceLocationsFromFile("src/main/Data/ServicePointLocations.csv");
        ArrayList<Road> roads = ReadData.readRoadsFromFile("src/main/Data/edges.csv", kValue);
        ArrayList<Node> nodes = ReadData.readNodes("src/main/Data/nodes.csv");
        ArrayList<Double> packageIndex = ReadData.readPackageIndex("src/main/Data/DayOfYearIndex.csv");
        double[][] distances = ReadData.loadDistances("src/main/Data/distances.csv");

        SimulatedAnnealing simulatedAnnealing = new SimulatedAnnealing(serviceLocations, roads, distances, nodes, packageIndex);
        simulatedAnnealing.optimize(startingTemp, endingTemp, coolingRate);
        System.out.println("Simulated Annealing finished");

        return simulatedAnnealing.getSimulationResult();
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
