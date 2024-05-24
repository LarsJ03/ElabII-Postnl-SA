package SimulatedAnnealing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class ServiceLocationConfig {
    private ArrayList<ServiceLocation> serviceLocations;
    private double[][] distances;
    private ArrayList<Node> nodes;
    private HashMap<Integer, Integer> nodeToFacility;
    private ArrayList<Road> roads;
    private Random random;

    public ServiceLocationConfig(ArrayList<ServiceLocation> serviceLocations, double[][] distances, ArrayList<Node> nodes, ArrayList<Road> roads) {
        this.serviceLocations = serviceLocations;
        this.distances = distances;
        this.nodes = nodes;
        this.roads = roads;
        this.nodeToFacility = new HashMap<>();
        this.random = new Random();
    }

    private void allocateNodesAndRoads() {
        // Clear previous allocations
        nodeToFacility.clear();
        for (ServiceLocation serviceLocation : serviceLocations) {
            serviceLocation.clearOrders();
        }

        // Allocate nodes to the nearest service location and update roads simultaneously
        for (Node node : nodes) {
            int nodeId = node.getNodeId();
            double minDistance = Double.MAX_VALUE;
            int closestFacility = -1;
            for (ServiceLocation serviceLocation : serviceLocations) {
                int serviceLocationID = serviceLocation.getClosestNodeId();
                double distance = distances[nodeId][serviceLocationID];
                if (distance < minDistance) {
                    minDistance = distance;
                    closestFacility = serviceLocationID;
                }
            }
            nodeToFacility.put(nodeId, closestFacility);
            node.setAssignedFacility(closestFacility);
        }

        for (Road road : roads) {
            Node node1 = findNodeById(road.getV1());
            Node node2 = findNodeById(road.getV2());

            if (node1 != null && node2 != null) {
                int facility1 = nodeToFacility.get(node1.getNodeId());
                int facility2 = nodeToFacility.get(node2.getNodeId());

                if (facility1 == facility2) {
                    ServiceLocation serviceLocation = findServiceLocationByNodeId(facility1);
                    if (serviceLocation != null) {
                        road.setServiceLocation(serviceLocation, distances);
                        serviceLocation.addOrdersFromRoad(road.getOrders());
                        for (Order order : road.getOrders()) {
                            double distance = DistanceCalc.calculateDist(distances, node1.getNodeId(), serviceLocation.getClosestNodeId());
                            order.setDistanceServiceLocation(distance);
                            order.setDelivery(random.nextDouble() > probabilityOfPickup(distance));
                        }
                    }
                }
            }
        }

        // Update capacities
        for (ServiceLocation serviceLocation : serviceLocations) {
            int numberOrders = serviceLocation.getOrders().size();
            serviceLocation.setCapacity(numberOrders);
            serviceLocation.updateCost();
        }
    }

    private Node findNodeById(int nodeId) {
        for (Node node : nodes) {
            if (node.getNodeId() == nodeId) {
                return node;
            }
        }
        return null;
    }

    private ServiceLocation findServiceLocationByNodeId(int nodeId) {
        for (ServiceLocation serviceLocation : serviceLocations) {
            if (serviceLocation.getClosestNodeId() == nodeId) {
                return serviceLocation;
            }
        }
        return null;
    }

    private double probabilityOfPickup(double distance) {
        double P0 = 0.8;
        double d0 = 1100;
        double k = 0.005;
        return P0 * (1 - 1 / (1 + Math.exp(-k * (distance - d0))));
    }

    public void reconfigure() {
        allocateNodesAndRoads();
    }
}
