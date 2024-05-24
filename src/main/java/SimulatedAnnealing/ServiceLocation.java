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

    public ServiceLocation(double x, double y, String square, int closestNodeId) {
        this.x = x;
        this.y = y;
        this.square = square;
        this.totalDeliveries = 0;
        this.totalPickups = 0;
        this.closestNodeId = closestNodeId;
        this.capacity = 10;
        this.orders = new ArrayList<>();
        this.assignedNodes = new ArrayList<>();
    }

    public double getX() { return x; }
    public double getY() { return y; }
    public String getSquare() { return square; }
    public int getTotalDeliveries() { return totalDeliveries; }
    public int getTotalPickups() { return totalPickups; }
    public int getClosestFacilityId() { return closestFacilityId; }
    public void setClosestFacilityId(int facilityId) { this.closestFacilityId = facilityId; }
    public int getClosestNodeId() { return closestNodeId; }
    public int getCapacity() { return capacity; }
    public ArrayList<Order> getOrders() { return orders; }
    public int nrOfOrders() { return orders.size(); }
    public ArrayList<Node> getAssignedNodes() { return assignedNodes; }

    public void setCapacity(int capacity) { this.capacity = capacity; }

    public void increaseCapacity() { this.capacity += 1; }

    public void decreaseCapacity() { this.capacity -= 1; }

    public void addNode(Node node) { this.assignedNodes.add(node); }

    public void addOrdersFromRoad(ArrayList<Order> roadOrders) {
        this.orders.addAll(roadOrders);
    }
}
