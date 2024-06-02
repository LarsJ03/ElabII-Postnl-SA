package SimulatedAnnealing;

import java.util.ArrayList;

public class DeliveryDriver {
    private boolean isContract;
    private double numberHours;
    private ArrayList<Order> orders;

    public DeliveryDriver(boolean isContract) {
        this.isContract = isContract;
        this.numberHours = 0.0;
        this.orders = new ArrayList<>();
    }

    public void setContract(boolean isContract) {
        this.isContract = isContract;
    }

    public boolean isContract() {
        return this.isContract;
    }

    public void setNumberHours(double numberHours) {
        this.numberHours = numberHours;
    }

    public double getNumberHours() {
        return this.numberHours;
    }

    public void addOrder(Order order) {
        this.orders.add(order);
    }

    public void removeOrder(Order order) {
        this.orders.remove(order);
    }
}
