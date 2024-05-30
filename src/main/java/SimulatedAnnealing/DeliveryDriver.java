package SimulatedAnnealing;
import java.util.ArrayList;

public class DeliveryDriver {
    private boolean isContract;
    private double numberHours;
    private double drivenKm;
    private double totalcost;
    private ArrayList<ArrayList<Order>> orders;
    private final double costPerKm = 0.21;

    public DeliveryDriver(boolean isContract) {
        this.isContract = isContract;
        this.numberHours = 0.0;
        this.drivenKm = 0.0;
        this.totalcost = 0.0;
        this.orders = new ArrayList<>();
    }

    public void setDrivenKm(double drivenKm) {
        this.drivenKm = drivenKm;
    }

    public double getDrivenKm() {
        return this.drivenKm;
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



    public double totalCosts() {
        totalcost = 0.0;
        if (isContract) {
            totalcost += 150 * 365;
        } else {
            totalcost += orders.size() * 250;
        }

        totalcost += costPerKm * drivenKm;


        return totalcost;
    }

    public void calculateDrivingTime() {

    }
}

