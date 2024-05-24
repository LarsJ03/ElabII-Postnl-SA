package SimulatedAnnealing;

import java.util.ArrayList;
import java.util.Random;

public class SimulatedAnnealing {
    // Temperature parameters
    private static final double START_TEMPERATURE = 10000000.0;
    private static final double END_TEMPERATURE = 30.0;
    private static final double COOLING_RATE = 0.95;
    private ArrayList<Node> nodes;
    private ArrayList<Road> roads;
    private ArrayList<ServiceLocation> serviceLocations;
    private double[][] distances;

    // Random generator for the simulated annealing
    private static final Random random = new Random();

    public SimulatedAnnealing(ArrayList<Node> nodes, ArrayList<Road> roads, ArrayList<ServiceLocation> serviceLocations, double[][] distances) {
        this.nodes = nodes;
        this.roads = roads;
        this.serviceLocations = serviceLocations;
        this.distances = distances;

        optimize();
    }

    public void optimize() {
        double temperature = START_TEMPERATURE;
        int counter = 0;
        double currentCost = 100000000000.0;
        ArrayList<ServiceLocation> bestSolution = new ArrayList<>(serviceLocations);
        double bestCost = currentCost;

        OrderConfig orderConfig = new OrderConfig(roads);
        ServiceLocationConfig config = new ServiceLocationConfig(serviceLocations, distances, nodes, roads);
        config.reconfigure();

        while (temperature > END_TEMPERATURE) {
            ArrayList<ServiceLocation> currentSolution = new ArrayList<>(serviceLocations);
            double randomProb = random.nextDouble();

            if (randomProb < 0.4 && serviceLocations.size() > 1) {
                removeLocation();
            } else {
                addLocation();
            }

            ServiceLocationConfig serviceLocationConfig = new ServiceLocationConfig(currentSolution, distances, nodes, roads);
            serviceLocationConfig.reconfigure();

            double newCost = calculateCosts(counter);

            if (acceptanceProbability(currentCost, newCost, temperature) > random.nextDouble()) {
                currentCost = newCost;
            } else {
                serviceLocations = currentSolution;
            }

            if (currentCost < bestCost) {
                System.out.println("Improved from " + bestCost + " to " + currentCost);
                bestSolution = new ArrayList<>(serviceLocations);
                bestCost = currentCost;
            }

            if (counter % 10 == 0) {
                System.out.println("Current temperature = " + temperature + " Current cost = " + currentCost + " Best cost = " + bestCost + " Amount of service locations = " + serviceLocations.size());
            }

            temperature *= COOLING_RATE;
            counter += 1;
        }

        System.out.println("Final cost = " + bestCost);
        serviceLocations = bestSolution;
    }

    private double calculateCosts(int counter) {
        double cost = 0.0;
        for (ServiceLocation serviceLocation : serviceLocations) {
            cost += serviceLocation.getCost();
        }
        return cost;
    }

    private void removeLocation() {
        if (serviceLocations.size() > 1) {
            int indexToRemove = random.nextInt(serviceLocations.size());
            serviceLocations.remove(indexToRemove);
        }
    }

    private void addLocation() {
        int randomNodeIndex = random.nextInt(nodes.size());
        Node randomNode = nodes.get(randomNodeIndex);
        int nodeIndex = randomNode.getNodeId();
        double x = randomNode.getX();
        double y = randomNode.getY();
        String square = randomNode.getSquare();

        ServiceLocation newLocation = new ServiceLocation(x, y, square, nodeIndex);
        serviceLocations.add(newLocation);
    }

    private static double acceptanceProbability(double currentCost, double newCost, double temperature) {
        if (newCost < currentCost) {
            return 1.0;
        }
        return Math.exp((currentCost - newCost) / temperature);
    }
}
