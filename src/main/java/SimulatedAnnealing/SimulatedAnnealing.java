package SimulatedAnnealing;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class SimulatedAnnealing {
    public static void main(String[] args) throws IOException {
        ArrayList<ServiceLocation> servicelocations = ReadData.readServiceLocationsFromFile("src/main/Data/ServicePointLocations.csv");
        ArrayList<Road> roads = ReadData.readRoadsFromFile("src/main/Data/edges.csv");
        ArrayList<Node> nodes = ReadData.readNodes("src/main/Data/nodes.csv");
        ArrayList<Double> packageIndex = ReadData.readPackageIndex("src/main/Data/DayOfYearIndex.csv");
        double[][] distances = ReadData.loadDistances("src/main/Data/distances.csv");

        ServiceLocationConfig original = new ServiceLocationConfig(servicelocations, roads, distances, packageIndex);
        System.out.println("Original cost = " + original.getTotalCost());


        ServiceLocationConfig config = new ServiceLocationConfig(new ArrayList<>(), roads, distances, packageIndex);
        config.addRandomServiceLocation(1);
        config.addRandomServiceLocation(1000);
        double currentCost = config.getTotalCost();
        double temperature = 10000;
        double coolingRate = 0.015;
        Random random = new Random();

        int counter = 0;
        while (temperature > 1) {
            ServiceLocationConfig newConfig = Utils.deepCopy(config);

            if (random.nextDouble() < 0.2) {
                newConfig.removeRandomServiceLocation();
            } else {
                int randomNodeIndex = random.nextInt(nodes.size());
                int randomNodeID = nodes.get(randomNodeIndex).getNodeID();
                newConfig.addRandomServiceLocation(randomNodeID);
            }

            double newCost = newConfig.getTotalCost();
            if (acceptanceProbability(currentCost, newCost, temperature) > random.nextDouble()) {
                config = Utils.deepCopy(newConfig);
                currentCost = newCost;
            }

            if (counter % 30 == 0) {
                System.out.println("Best cost = " + currentCost + " Temperature = " + temperature + " Nr. Service Locations = " + newConfig.getServicelocations().size() + " New Cost = " + newCost);
            }

            counter++;
            temperature *= 1 - coolingRate;
        }

        System.out.println("Optimized total cost: " + currentCost);
    }

    private static double acceptanceProbability(double currentCost, double newCost, double temperature) {
        if (newCost < currentCost) {
            return 1.0;
        }
        return Math.exp((currentCost - newCost) / temperature);
    }
}
