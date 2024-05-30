package SimulatedAnnealing;

import java.io.IOException;
import java.util.ArrayList;

public class Test {
    public static void main(String[] args) throws IOException {
        double startingTemp = 1000.0;
        double endingTemp = 1000.0;
        double coolingRate = 0.02;
        double kValue = 0.005;
        ArrayList<ServiceLocation> serviceLocations = ReadData.readServiceLocationsFromFile("src/main/Data/ServicePointLocations.csv");
        ArrayList<Road> roads = ReadData.readRoadsFromFile("src/main/Data/edges.csv", kValue);
        ArrayList<Node> nodes = ReadData.readNodes("src/main/Data/nodes.csv");
        ArrayList<Double> packageIndex = ReadData.readPackageIndex("src/main/Data/DayOfYearIndex.csv");
        double[][] distances = ReadData.loadDistances("src/main/Data/distances.csv");

        SimulatedAnnealing simulatedAnnealing = new SimulatedAnnealing(serviceLocations, roads, distances, nodes, packageIndex);
        simulatedAnnealing.optimize(startingTemp, endingTemp, coolingRate);
        System.out.println("Simulated Annealing finished");
    }
}
