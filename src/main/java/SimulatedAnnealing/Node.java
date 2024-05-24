package SimulatedAnnealing;

public class Node {
    private int nodeId;
    private double x;
    private double y;
    private String square;
    private int assignedFacility; // New field to store the facility assigned to the intersection

    public Node(int nodeId, double x, double y, String square) {
        this.nodeId = nodeId;
        this.x = x;
        this.y = y;
        this.square = square;
        this.assignedFacility = -1; // Initialize with a default value indicating no facility assigned
    }

    // Getters and setters for all fields
    public int getNodeId() {
        return nodeId;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public String getSquare() {
        return square;
    }

    public int getAssignedFacility() {
        return assignedFacility;
    }

    public void setAssignedFacility(int assignedFacility) {
        this.assignedFacility = assignedFacility;
    }

    @Override
    public String toString() {
        return String.format("Intersection[nodeId=%s, x=%.2f, y=%.2f, square=%s, assignedFacility=%d]", nodeId, x, y, square, assignedFacility);
    }
}
