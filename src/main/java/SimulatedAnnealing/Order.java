package SimulatedAnnealing;
import java.util.Random;

public class Order {
    private int orderID;
    private int x;
    private int y;
    private int closestNode;
    //Variable for defining if an order is bounced or not
    private boolean toServiceLocation;
    // Variable for defining if order should be delivered
    private boolean forDelivery;
    // Variable for distance to serviceLocation
    private double walkingDistanceServiceLocation;
    private double kValue;

    private Random random;

    public Order(int orderID, int x, int y, int closestNode, double kValue) {
        this.orderID = orderID;
        this.x = x;
        this.y = y;
        this.toServiceLocation = false;
        this.forDelivery = true;
        this.walkingDistanceServiceLocation = 100000000000.0;
        this.closestNode = closestNode;
        this.random =new Random();
        this.kValue = kValue;
    }

    public void setToServiceLocation(boolean isToServiceLocation) {
        this.toServiceLocation = isToServiceLocation;
    }

    public boolean isToServiceLocation() {
        return toServiceLocation;
    }



    public void setForDelivery() {
        double P0 = 0.8;
        double d0 = 1100;
        double k = kValue;
        double probability = P0 * (1 - 1 / (1 + Math.exp(-k * (walkingDistanceServiceLocation - d0))));
        if (probability > random.nextDouble()) {
            this.forDelivery = false;
        } else {
            this.forDelivery = true;
        }
    }

    public boolean isForDelivery() {
        return forDelivery;
    }

    public void setWalkingDistanceServiceLocation(double[][] distances, int ServiceLocationNode) {
        this.walkingDistanceServiceLocation = distances[ServiceLocationNode][closestNode];
        setForDelivery();
    }

    public double getWalkingDistanceServiceLocation() {
        return walkingDistanceServiceLocation;
    }
}
