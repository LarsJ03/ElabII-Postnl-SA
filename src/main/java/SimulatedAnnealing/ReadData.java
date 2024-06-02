package SimulatedAnnealing;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ReadData {
    public static ArrayList<Road> readRoadsFromFile(String filename, double dayIndex) throws IOException {
        ArrayList<Road> roads = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line;
        boolean firstLine = true; // to skip header

        int orderID = 1; // Initialize the order ID counter

        while ((line = reader.readLine()) != null) {
            if (firstLine) {
                firstLine = false; // Skip the header row
                continue;
            }
            String[] data = line.split(",");

            int v1 = Integer.parseInt(data[0]);
            int v2 = Integer.parseInt(data[1]);
            String type = data[7];
            int x1 = (int) Double.parseDouble(data[3]);
            int y1 = (int) Double.parseDouble(data[4]);
            int x2 = (int) Double.parseDouble(data[5]);
            int y2 = (int) Double.parseDouble(data[6]);
            int population = (int) Double.parseDouble(data[13]);
            int ordersCount = Integer.parseInt(data[38]) / 365;

            ArrayList<Order> orders = new ArrayList<>();

            // Create Order objects and add to the orders list
            for (int i = 0; i < (int) (ordersCount * dayIndex); i++) {
                Order order = new Order(orderID++, x1, y1, v1);
                orders.add(order);
            }

            Road road = new Road(x1, y1, x2, y2, v1, v2, population, orders);
            roads.add(road);

        }

        reader.close();
        return roads;
    }

    public static ArrayList<Double> readPackageIndex(String filename) throws IOException {
        ArrayList<Double> packageIndex = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line;
        boolean firstLine = true; // to skip header

        while ((line = reader.readLine()) != null) {
            if (firstLine) {
                firstLine = false; // Skip the header row
                continue;
            }
            String[] data = line.split(",");

            double index = Double.parseDouble(data[1]);
            packageIndex.add(index);
        }




        reader.close();
        return packageIndex;
    }

    public static ArrayList<Node> readNodes(String filename) throws IOException {
        ArrayList<Node> nodes = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line;
        boolean firstLine = true; // to skip header

        while ((line = reader.readLine()) != null) {
            if (firstLine) {
                firstLine = false; // Skip the header row
                continue;
            }
            String[] data = line.split(",");

            int nodeID = Integer.parseInt(data[0]);
            nodes.add(new Node(nodeID));
        }




        reader.close();
        return nodes;
    }


    public static ArrayList<ServiceLocation> readServiceLocationsFromFile(String filename) throws IOException {
        ArrayList<ServiceLocation> locations = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line;

        while ((line = reader.readLine()) != null) {
            String[] data = line.split(";");

            int nodeId = Integer.parseInt(data[0].replace(",", "."));
            ServiceLocation servicelocation = new ServiceLocation(nodeId);
            locations.add(servicelocation);

        }
        return locations;
    }

    public static double[][] loadDistances(String filePath) throws IOException {
        double[][] distances = new double[9396][9396];
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;
        int i = 0;
        // Assuming this size is correct
        while ((line = reader.readLine()) != null && i < distances.length) {
            String[] values = line.split(";");
            for (int j = 0; j < distances[i].length && j < values.length; j++) {
                String value = values[j].replace(",", "."); // Replace commas with dots
                distances[i][j] = value.equals("INF") ? Double.MAX_VALUE : Double.parseDouble(value);
            }
            i++;
        }
        reader.close();
        return distances;
    }
}