package SimulatedAnnealing;

import java.util.ArrayList;
import java.util.Random;

public class OrderConfig {

    private ArrayList<Road> roads;
    private Random random;

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
                boolean delivery = random.nextDouble() < probabilityOfPickup(road.getDist());

                Order order = new Order(orderID++, road.getV1(), delivery, road.getDist());
                orders.add(order);

                // Update the order count for the specific road
                road.addOrder(order);
            }
        }

        return orders;
    }

    private double probabilityOfPickup(double distance) {
        double P0 = 0.8;
        double d0 = 1100;
        double k = 0.005;
        return P0 * (1 - 1 / (1 + Math.exp(-k * (distance - d0))));
    }
}
