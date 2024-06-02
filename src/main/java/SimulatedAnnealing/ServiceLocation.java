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

    public double getBounceRate() {
        calculateBounceRate();
        return bounceRate;
    }

    public double calculateBounceRate() {

        int totalOrders = 0;
        for (Road road : roads) {
            totalOrders += road.getOrders().size();
        }

        if (totalOrders > this.capacity) {

            this.bounceRate = (double) (totalOrders - this.capacity) / totalOrders;
        } else {
            this.bounceRate = 0.0;
        }

        return this.bounceRate;
    }

    public void clearProperties() {
        this.deliveryDrivers.clear();
        this.roads.clear();
        this.bounceRate = 0;
        this.totalCost = 0;
    }

    public void setCapacity() {
        int packages = 0;
        for (Road road : roads) {
            packages += (int) (road.getOrders().size() * 1.1);
        }



        // Calculate capacity and round up
        this.capacity = packages;
    }

    public double calculateCosts() {
        calculateBounceRate();
        this.totalCost = 0.0;
        double locationCostTotal = locationCost / 365;
        double capacityCostTotal = capacity * capacityCost;
        double roadOrderCostTotal = 0;
        double deliveryDriverCostTotal = 0;
        double penaltyCost = 0;

        // Calculate the total cost for road orders
        for (Road road : roads) {

            ArrayList<Order> orders = road.getOrders();
            for (Order order : orders) {
                if (order.isForDelivery()) {
                    roadOrderCostTotal += order.getWalkingDistanceServiceLocation() / 1000 * distanceCost * 2;
                }
            }
        }



        if(getBounceRate() > 0.02) {
            penaltyCost += 10000;
        }

        //System.out.println("Location = " + locationCostTotal + " Capacity = " + capacityCostTotal + " roadordercost + " + roadOrderCostTotal + "deliverycost = " + deliveryDriverCostTotal + " penaltycost = " + penaltyCost);
        // Calculate total cost
        double totalCost = locationCostTotal + capacityCostTotal + roadOrderCostTotal + deliveryDriverCostTotal + penaltyCost;
    // Assign total cost to the class variable
        this.totalCost = totalCost;
        return totalCost;
    }

    public int getNrPackages() {
        int nrPackages = 0;
        for (Road road : roads) {
            nrPackages += road.getOrders().size();
        }

        return nrPackages;
    }

}
