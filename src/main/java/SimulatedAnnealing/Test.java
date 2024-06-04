package SimulatedAnnealing;
import java.util.concurrent.*;
import java.io.IOException;
import java.util.ArrayList;

public class Test {
    public static void main(String[] args) throws IOException {
        double startingTemp = 50.0;
        double endingTemp = 8.0;
        double coolingRate = 0.02;
        int optimizeDay = 356;
        boolean removeOnly = true;

        ArrayList<Integer> checkDays = new ArrayList<>();
        for (int i = 0; i <= 364; i++) {
            checkDays.add(i);
        }

        createConfiguration(startingTemp, endingTemp, coolingRate, optimizeDay, checkDays, removeOnly);
    }

    public static void createConfiguration(double startingTemp, double endingTemp, double coolingRate, int optimizeDay, ArrayList<Integer> checkDays, boolean removeOnly) throws IOException {
        MemoryLogger.logMemoryUsage("Before packageIndex");
        ArrayList<Double> packageIndex = ReadData.readPackageIndex("src/main/Data/DayOfYearIndex.csv");

        double dayIndex = packageIndex.get(optimizeDay);

        System.out.println("Optimizing for day " + optimizeDay + " with = " + dayIndex);

        MemoryLogger.logMemoryUsage("Before serviceLocations");
        ArrayList<ServiceLocation> serviceLocations = ReadData.readServiceLocationsFromFile("src/main/Data/ServicePointLocations.csv");
        MemoryLogger.logMemoryUsage("Before roads");
        ArrayList<Road> roads = ReadData.readRoadsFromFile("src/main/Data/edges.csv", dayIndex);
        MemoryLogger.logMemoryUsage("Before nodes");
        ArrayList<Node> nodes = ReadData.readNodes("src/main/Data/nodes.csv");

        MemoryLogger.logMemoryUsage("Before distances");
        double[][] distances = ReadData.loadDistances("src/main/Data/distances.csv");
        MemoryLogger.logMemoryUsage("After distances");

        ArrayList<Double> bounceRatesBefore = new ArrayList<>();
        ArrayList<Double> costsBefore = new ArrayList<>();

        ArrayList<ServiceLocation> serviceLocationsInitial = Utils.deepCopy(serviceLocations);
        ArrayList<Road> roadsDayInitial = Utils.deepCopy(ReadData.readRoadsFromFile("src/main/Data/edges.csv", packageIndex.get(optimizeDay)));
        MemoryLogger.logMemoryUsage("Before checks");
        ServiceLocationConfig configInitial = new ServiceLocationConfig(serviceLocationsInitial, roadsDayInitial, distances, false);
        System.out.println("Initial Total Costs: " + configInitial.getTotalCost());

        for (int day : checkDays) {
            MemoryLogger.logMemoryUsage("Memory usage at " + day);
            ArrayList<ServiceLocation> serviceLocations2 = Utils.deepCopy(serviceLocations);
            ArrayList<Road> roadsDay = Utils.deepCopy(ReadData.readRoadsFromFile("src/main/Data/edges.csv", packageIndex.get(day)));

            ServiceLocationConfig config = new ServiceLocationConfig(serviceLocations2, roadsDay, distances, true);
            System.out.println("Total Costs for day " + day + ": " + config.getTotalCost());

            // Assuming getGlobalBounceRate and getTotalCost methods exist in ServiceLocationConfig
            bounceRatesBefore.add(config.getGlobalBounceRate());
            costsBefore.add(config.getTotalCost());
        }
        MemoryLogger.logMemoryUsage("After checks");
        // Calculate metrics before optimization
        double maxBounceRateBefore = bounceRatesBefore.stream().max(Double::compareTo).orElse(0.0);
        double averageBounceRateBefore = bounceRatesBefore.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        double maxCostBefore = costsBefore.stream().max(Double::compareTo).orElse(0.0);
        double minCostBefore = costsBefore.stream().min(Double::compareTo).orElse(0.0);
        double averageCostBefore = costsBefore.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);

        configInitial = null;

        System.out.println("Before Optimization:");
        System.out.println("Max Bounce Rate: " + maxBounceRateBefore);
        System.out.println("Average Bounce Rate: " + averageBounceRateBefore);
        System.out.println("Max Cost: " + maxCostBefore);
        System.out.println("Min Cost: " + minCostBefore);
        System.out.println("Average Cost: " + averageCostBefore);

        // Perform Simulated Annealing
        SimulatedAnnealing simulatedAnnealing = new SimulatedAnnealing(serviceLocations, roads, distances, nodes);
        simulatedAnnealing.optimize(startingTemp, endingTemp, coolingRate, removeOnly);
        System.out.println("Simulated Annealing finished");
        MemoryLogger.logMemoryUsage("After Simulated Annealing");

        ServiceLocationConfig optimizedServiceLocationConfig = simulatedAnnealing.getServiceLocationConfig();
        ArrayList<ServiceLocation> optimizedServiceLocations = optimizedServiceLocationConfig.getServicelocations();

        // Calculate metrics for the optimized configuration
        ArrayList<Double> bounceRatesAfter = new ArrayList<>();
        ArrayList<Double> costsAfter = new ArrayList<>();

        for (int day : checkDays) {
            MemoryLogger.logMemoryUsage("During = " + day);
            // Make sure to deep copy all necessary objects for each day
            ArrayList<ServiceLocation> optimizedServiceLocationsCopy = Utils.deepCopy(optimizedServiceLocations);
            ArrayList<Road> roadsDay = Utils.deepCopy(ReadData.readRoadsFromFile("src/main/Data/edges.csv", packageIndex.get(day)));
            ArrayList<Node> nodesCopy = Utils.deepCopy(nodes);

            ServiceLocationConfig config = new ServiceLocationConfig(optimizedServiceLocationsCopy, roadsDay, distances, true);
            bounceRatesAfter.add(config.getGlobalBounceRate());
            costsAfter.add(config.getTotalCost());
        }

        MemoryLogger.logMemoryUsage("After after checks");

        // Calculate metrics after optimization
        double maxBounceRateAfter = bounceRatesAfter.stream().max(Double::compareTo).orElse(0.0);
        double averageBounceRateAfter = bounceRatesAfter.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        double maxCostAfter = costsAfter.stream().max(Double::compareTo).orElse(0.0);
        double minCostAfter = costsAfter.stream().min(Double::compareTo).orElse(0.0);
        double averageCostAfter = costsAfter.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);

        System.out.println("After Optimization:");
        System.out.println("Max Bounce Rate: " + maxBounceRateAfter);
        System.out.println("Average Bounce Rate: " + averageBounceRateAfter);
        System.out.println("Max Cost: " + maxCostAfter);
        System.out.println("Min Cost: " + minCostAfter);
        System.out.println("Average Cost: " + averageCostAfter);
    }
}
