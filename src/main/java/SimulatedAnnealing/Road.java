package SimulatedAnnealing;

import java.util.ArrayList;

public class Road {
    private int V1;
    private int V2;
    private double dist;
    private double x1;
    private double y1;
    private double x2;
    private double y2;
    private String type;
    private int maxSpeed;
    private double timeToDrive;
    private int population;
    private int serviceLocation;
    private double orderOdds;
    private ArrayList<Order> orders;
    private double distanceServiceLocation;

    public Road(int V1, int V2, double dist, double x1, double y1, double x2, double y2, String type, int maxSpeed, int population, double orderOdds) {
        this.V1 = V1;
        this.V2 = V2;
        this.dist = dist;
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.type = type;
        this.maxSpeed = maxSpeed;
        this.timeToDrive = timeToDrive();
        this.population = population;
        this.serviceLocation = -1;
        this.orderOdds = orderOdds;
        this.orders = new ArrayList<>();
        this.distanceServiceLocation = -1;
    }

    public int getV1() {
        return V1;
    }

    public int getV2() {
        return V2;
    }

    public double getX1() {
        return x1;
    }

    public double getY1() {
        return y1;
    }

    public double getX2() {
        return x2;
    }

    public double getY2() {
        return y2;
    }

    public String getType() {
        return type;
    }

    public int getMaxSpeed() {
        return maxSpeed;
    }

    public int getPopulation() {
        return population;
    }

    public double getOrderOdds() {
        return orderOdds;
    }

    public ArrayList<Order> getOrders() {
        return this.orders;
    }

    public void addOrder(Order order) {
        this.orders.add(order);
    }

    public double getDist() {
        return dist;
    }

    public double timeToDrive() {
        return dist / (maxSpeed / 3.6);
    }

    public void setServiceLocation(ServiceLocation serviceLocation, double[][] distances) {
        this.serviceLocation = serviceLocation.getClosestNodeId();
        int V1 = this.V1;
        int V2 = this.V2;

        double distance1 = DistanceCalc.calculateDist(distances, V1, this.serviceLocation);
        double distance2 = DistanceCalc.calculateDist(distances, V2, this.serviceLocation);

        this.distanceServiceLocation = Math.min(distance1, distance2);
    }
}

