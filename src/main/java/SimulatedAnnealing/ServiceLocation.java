package SimulatedAnnealing;

import java.util.ArrayList;
import java.util.Collections;

public class ServiceLocation {
    private int nodeID;
    private ArrayList<DeliveryDriver> deliveryDrivers;
    private ArrayList<Road> roads;
    private int capacity;
    private final int locationCost = 75000;
    private final double capacityCost = 0.1;
    private final double distanceCost = 0.5;
    private double bounceRate;
    private double totalCost;

    public ServiceLocation(int nodeID) {
        this.nodeID = nodeID;
        this.deliveryDrivers = new ArrayList<>();
        this.roads = new ArrayList<>();
        this.capacity = 0;
        this.bounceRate = 0;
        this.totalCost = 0;
    }

    public int getNodeID() {
        return nodeID;
    }

    public void addRoad(Road road) {
        roads.add(road);
    }

    public ArrayList<Road> getRoads() {
        return this.roads;
    }

    public void clearProperties() {
        this.deliveryDrivers.clear();
        this.roads.clear();
        this.capacity = 0;
        this.bounceRate = 0;
        this.totalCost = 0;
    }

    public void setCapacity(ArrayList<Double> packageIndex) {
        int packages = 0;
        for (Road road : roads) {
            packages += road.getOrders().size();
        }

        double maxPackageIndex = Collections.max(packageIndex);

        // Calculate capacity and round up
        this.capacity = (int) Math.ceil(packages * maxPackageIndex) * 365;
    }

    public double calculateCosts() {
        double locationCostTotal = locationCost;
        double capacityCostTotal = capacity * capacityCost;
        double roadOrderCostTotal = 0;
        double deliveryDriverCostTotal = 0;

        // Calculate the total cost for road orders
        for (Road road : roads) {
            ArrayList<ArrayList<Order>> orderdays = road.getOrders();
            for (ArrayList<Order> orderday : orderdays) {
                for(Order order : orderday) {
                    if (order.isForDelivery()) {
                        roadOrderCostTotal += order.getWalkingDistanceServiceLocation() / 1000 * distanceCost;
                    }
                }
            }
        }

        // Calculate the total cost for delivery drivers
        for (DeliveryDriver deliveryDriver : deliveryDrivers) {
            if (deliveryDriver.isContract()) {
                deliveryDriverCostTotal += (150 * deliveryDriver.getNumberHours());
            } else {
                deliveryDriverCostTotal += (250 * deliveryDriver.getNumberHours());
            }
        }

        // Calculate total cost
        double totalCost = locationCostTotal + capacityCostTotal + roadOrderCostTotal + deliveryDriverCostTotal;


        // Assign total cost to the class variable
        this.totalCost = totalCost;
        return totalCost;
    }

    public void removePickupOrders() {
        for (Road road : roads) {
            for (ArrayList<Order> dayOrders : road.getOrders()) {
                for (Order order : dayOrders) {
                    if (!order.isForDelivery()) {
                        dayOrders.remove(order);
                    }
                }
            }
        }

    }

}
