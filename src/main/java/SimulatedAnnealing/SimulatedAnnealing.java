package SimulatedAnnealing;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class SimulatedAnnealing {
    private ArrayList<ServiceLocation> serviceLocations;
    private ArrayList<Road> roads;
    private double[][] distances;
    private ArrayList<Double> packageIndex;
    private ArrayList<Node> nodes;
    private ServiceLocationConfig initialConfig;
    private ServiceLocationConfig finalConfig;
    private double finalCost;
    private int counter;
    public SimulatedAnnealing(ArrayList<ServiceLocation> serviceLocations, ArrayList<Road> roads, double[][] distances, ArrayList<Node> nodes) {
        this.serviceLocations = serviceLocations;
        this.roads = roads;
        this.distances = distances;
        this.nodes = nodes;
    }

    public ServiceLocationConfig getServiceLocationConfig() {
        return this.finalConfig;
    }



    public void optimize(double startingTemperature, double endingTemperature, double coolingRate) throws IOException {
        ServiceLocationConfig config = new ServiceLocationConfig(serviceLocations, roads, distances, false);
        config.addRandomServiceLocation(1);
        config.addRandomServiceLocation(1000);
        double currentCost = config.getTotalCost();
        double temperature = startingTemperature;
        Random random = new Random();

        counter = 0;
        while (temperature > endingTemperature && counter < 700) {
            ServiceLocationConfig newConfig = Utils.deepCopy(config);

            if (random.nextDouble() < 0.35) {
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
                System.out.println("Best cost = " + currentCost + " Temperature = " + temperature + " Nr. Service Locations = " + newConfig.getServicelocations().size() + " New Cost = " + newCost + " Counter = "+ counter);
            }

            counter++;
            temperature *= 1 - coolingRate;
        }

        finalCost = currentCost;
        finalConfig = config;
        System.out.println("Optimized total cost: " + finalCost);
    }

    private static double acceptanceProbability(double currentCost, double newCost, double temperature) {
        if (newCost < currentCost) {
            return 1.0;
        }
        return Math.exp((currentCost - newCost) / temperature);
    }

    public String getSimulationResult() {
        return "Initial Configuration: " + initialConfig.getServicelocations() +
                ", Final Cost: " + finalCost +
                ", Iterations: " + counter;
    }
}
