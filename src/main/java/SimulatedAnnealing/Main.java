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
                .region(Region.EU_WEST_1) // Specify the appropriate region
                .build();

        // Continuous polling loop
        while (true) {
            try {
                List<Message> messages = pollMessages(REQUEST_QUEUE_URL);
                for (Message message : messages) {
                    String[] params = message.body().split(",");
                    if (params.length == 3) {
                        double startingTemperature = Double.parseDouble(params[0]);
                        double endingTemperature = Double.parseDouble(params[1]);
                        double coolingRate = Double.parseDouble(params[2]);

                        System.out.println(startingTemperature + " " + endingTemperature + " " + coolingRate);

                        // Process the message using Simulated Annealing
                        processMessage(startingTemperature, endingTemperature, coolingRate);

                        // Send completion message to another SQS queue
                        sendMessage(RESPONSE_QUEUE_URL, "Simulated Annealing Finished");

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

    private static void processMessage(double startingTemp, double endingTemp, double coolingRate) throws IOException {
        ArrayList<Node> intersections = ReadData.readIntersectionsFromFile("src/main/Data/nodes.csv");
        ArrayList<Road> roads = ReadData.readRoadsFromFile("src/main/Data/edges.csv");
        ArrayList<ServiceLocation> serviceLocations = ReadData.readServiceLocationsFromFile("src/main/Data/ServicePointLocations.csv");
        double[][] distances = ReadData.loadDistances("src/main/Data/distances.csv");

        OrderConfig orderConfig = new OrderConfig(roads);
        roads = orderConfig.getRoads(); // Retrieve the updated roads

        SimulatedAnnealing simulationFinal = new SimulatedAnnealing(intersections, roads, serviceLocations, distances, startingTemp, endingTemp, coolingRate);
        System.out.println("Simulated Annealing finished");
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
