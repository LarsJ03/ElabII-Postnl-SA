package SimulatedAnnealing;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ReadData {
    public static ArrayList<Road> readRoadsFromFile(String filename) throws IOException {
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
            String[] data = line.split(";");
            if (data.length == 17) {
                int v1 = Integer.parseInt(data[1]);
                int v2 = Integer.parseInt(data[2]);
                String type = data[5];
                int x1 = Integer.parseInt(data[8].replace(",", "."));
                int y1 = Integer.parseInt(data[9].replace(",", "."));
                int x2 = Integer.parseInt(data[10].replace(",", "."));
                int y2 = Integer.parseInt(data[11].replace(",", "."));
                int population = Integer.parseInt(data[15].replace(",", "."));
                double orderOdds = Double.parseDouble(data[16].replace(",", "."));
                int ordersCount = (int) (population * orderOdds);

                ArrayList<Order> orders = new ArrayList<>();

                // Create Order objects and add to the orders list
                for (int i = 0; i < ordersCount; i++) {
                    Order order = new Order(orderID++, x1, y1, v1);
                    orders.add(order);
                }

                Road road = new Road(x1, y1, x2, y2, v1, v2, population, orders);
                roads.add(road);
            }
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
            String[] data = line.split(";");

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
        double[][] distances = new double[8486][8486];
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