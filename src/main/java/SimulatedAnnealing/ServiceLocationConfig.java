package SimulatedAnnealing;

import java.util.ArrayList;
import java.util.HashMap;

public class ServiceLocationConfig {
    private ArrayList<ServiceLocation> serviceLocations;
    private double[][] distances;
    private ArrayList<Node> nodes;
    private HashMap<Integer, Integer> nodeToFacility;
    private ArrayList<Road> roads;

    public ServiceLocationConfig(ArrayList<ServiceLocation> serviceLocations, double[][] distances, ArrayList<Node> nodes, ArrayList<Road> roads) {
        this.serviceLocations = serviceLocations;
        this.distances = distances;
        this.nodes = nodes;
        this.roads = roads;
        this.nodeToFacility = new HashMap<>();
    }

    public void setCapacity() {
        for (ServiceLocation serviceLocation : serviceLocations) {
            int numberOrders = serviceLocation.getOrders().size();
            serviceLocation.setCapacity(numberOrders);
        }
    }

    private void allocateNodes() {
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
        }
    }

    private void allocateRoads() {
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
                            order.setDistanceServiceLocation(DistanceCalc.calculateDist(distances, node1.getNodeId(), serviceLocation.getClosestNodeId()));
                        }
                    }
                }
            }
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

    private void assignFacilitiesToNodes() {
        for (Node node : nodes) {
            int nodeId = node.getNodeId();
            if (nodeToFacility.containsKey(nodeId)) {
                node.setAssignedFacility(nodeToFacility.get(nodeId));
            }
        }
    }

    public void clearOrders() {
        for (ServiceLocation serviceLocation : serviceLocations) {
            serviceLocation.clearOrders();
        }
    }
    public void updateDeliveryStatus() {
        OrderConfig orderConfig = new OrderConfig(roads);
        for (Road road : roads) {
            double totalDistance = 0;
            for (Order order : road.getOrders()) {
                double distance = order.getDistanceServiceLocation();
                totalDistance += distance;
                boolean delivery = orderConfig.random.nextDouble() < orderConfig.probabilityOfPickup(distance);
                order.setDelivery(delivery);
            }
        }
    }


    public void reconfigure() {
        allocateNodes();
        clearOrders();
        assignFacilitiesToNodes();
        allocateRoads();
        setCapacity();
        updateDeliveryStatus();  // Update the delivery status of orders
    }
}
