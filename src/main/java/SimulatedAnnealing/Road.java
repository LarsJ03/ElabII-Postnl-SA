package SimulatedAnnealing;

import java.util.ArrayList;

public class Road {
    private int x1;
    private int y1;
    private int x2;
    private int y2;

    private int v1;
    private int v2;

    private int population;
    private ArrayList<Order> orders;

    public Road(int x1, int y1, int x2, int y2, int v1, int v2, int population, ArrayList<Order> orders) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.v1 = v1;
        this.v2 = v2;
        this.population = population;
        this.orders = orders;
    }

    public int getNrOrders() {return this.orders.size();}

    public int getV1() {
        return v1;
    }

    public int getV2() {
        return v2;
    }

    public ArrayList<Order> getOrders() {
        return orders;
    }

}
