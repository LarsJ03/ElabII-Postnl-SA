package SimulatedAnnealing;

import java.util.ArrayList;
import java.util.Random;

public class OrderConfig {

    private ArrayList<Road> roads;
    public Random random;

    public OrderConfig(ArrayList<Road> roads) {
        this.roads = roads;
        this.random = new Random();
        generateOrders();
    }

    public ArrayList<Road> getRoads() {
        return this.roads;
    }

    public ArrayList<Order> generateOrders() {
        ArrayList<Order> orders = new ArrayList<>();
        int orderID = 1;

        for (Road road : roads) {
            int baseOrderCount = (int) Math.round(road.getPopulation() * road.getOrderOdds());
            int orderCount = (int) Math.round(baseOrderCount + random.nextGaussian() * baseOrderCount * 0.1);  // Adding some variability

            for (int i = 0; i < orderCount; i++) {
                Order order = new Order(orderID++, true);
                orders.add(order);
                road.addOrder(order);
            }
        }

        return orders;
    }

}

