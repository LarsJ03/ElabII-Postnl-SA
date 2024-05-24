package SimulatedAnnealing;

import java.util.*;

public class Test {
    public static void main(String[] args) {
        // Reading data from files
        ArrayList<Node> intersections = ReadData.readIntersectionsFromFile("src/main/Data/nodes.csv");
        ArrayList<Road> roads = ReadData.readRoadsFromFile("src/main/Data/edges.csv");
        ArrayList<ServiceLocation> serviceLocations = ReadData.readServiceLocationsFromFile("src/main/Data/ServicePointLocations.csv");
        double[][] distances = ReadData.loadDistances("src/main/Data/distances.csv");

        // Create an OrderConfig instance to update the roads
        OrderConfig orderConfig = new OrderConfig(roads);
        roads = orderConfig.getRoads(); // Retrieve the updated roads

        System.out.println("Data Loaded");

        double startTemprature = 1000;
        double endingTemprature = 1;
        double coolingRate = 0.99;

        // Pass the updated roads to the SimulatedAnnealing class
        SimulatedAnnealing simulationFinal = new SimulatedAnnealing(intersections, roads, serviceLocations, distances, startTemprature, endingTemprature, coolingRate);

        System.out.println("Simulated Annealing finished");
    }
}
