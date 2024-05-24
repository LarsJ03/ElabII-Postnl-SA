package SimulatedAnnealing;

import java.util.ArrayList;

public class ServiceLocation {
    private double x;
    private double y;
    private String square;
    private int totalDeliveries;
    private int totalPickups;
    private int closestFacilityId;
    private int closestNodeId;
    private int capacity;
    private ArrayList<Order> orders;
    private ArrayList<Node> assignedNodes;
    private double cost;

    public ServiceLocation(double x, double y, String square, int closestNodeId) {
        this.x = x;
        this.y = y;
        this.square = square;
        this.totalDeliveries = 0;
        this.totalPickups = 0;
        this.closestNodeId = closestNodeId;
        this.capacity = 0;
        this.orders = new ArrayList<>();
        this.assignedNodes = new ArrayList<>();
        this.cost = 75000;
    }

    public double getX() { return x; }
    public double getY() { return y; }
    public String getSquare() { return square; }

    public int getTotalPickups() { return totalPickups; }
    public int getClosestFacilityId() { return closestFacilityId; }
    public void setClosestFacilityId(int facilityId) { this.closestFacilityId = facilityId; }
    public int getClosestNodeId() { return closestNodeId; }
    public int getCapacity() { return capacity; }
    public ArrayList<Order> getOrders() { return orders; }
    public ArrayList<Node> getAssignedNodes() { return assignedNodes; }
    public double getCost() {return this.cost; }

    public void setCapacity(int capacity) { this.capacity = capacity; }

    public void clearOrders() {
        this.orders.clear();
    }

    public void addOrdersFromRoad(ArrayList<Order> roadOrders) {
        this.orders.addAll(roadOrders);
    }

    public void updateCost() {
        double orderCost = 0.0;
        double deliveryCost = 0.0;
        for (Order order : orders) {
            orderCost += 0.10;

            if (order.isDelivery()) {
                deliveryCost += 0.5 * order.getDistanceServiceLocation() / 1000 * 2;

            }
        }

        cost = cost + orderCost + deliveryCost;


    }
}
