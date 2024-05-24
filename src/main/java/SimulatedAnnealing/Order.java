package SimulatedAnnealing;

public class Order {
    private int orderID;
    private int customerID;
    private boolean delivery; // Make this non-static
    private double distanceServiceLocation;

    public Order(int orderID, boolean delivery) {
        this.orderID = orderID;
        this.delivery = delivery;
        this.distanceServiceLocation = -1.00;
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

    public void setDistanceServiceLocation(double distanceServiceLocation) {
        this.distanceServiceLocation = distanceServiceLocation;
    }

    public void setDelivery(boolean delivery) {
        this.delivery = delivery;
    }
}
