package SimulatedAnnealing;

public class Order {
    private int orderID;
    private int customerID;
    private boolean delivery; // Make this non-static
    private double distanceServiceLocation;

    public Order(int orderID, int customerID, boolean delivery, double distanceServiceLocation) {
        this.orderID = orderID;
        this.customerID = customerID;
        this.delivery = delivery; // Correctly set the delivery field
        this.distanceServiceLocation = distanceServiceLocation;
    }

    public int getCustomerID() {
        return this.customerID;
    }

    public boolean isDelivery() {
        return this.delivery;
    }

    public double getDistanceServiceLocation() {
        return this.distanceServiceLocation;
    }
}