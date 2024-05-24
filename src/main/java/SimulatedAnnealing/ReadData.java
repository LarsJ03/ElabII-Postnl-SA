package SimulatedAnnealing;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReadData {
    public static ArrayList<Road> readRoadsFromFile(String filename) {
        ArrayList<Road> roads = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            boolean firstLine = true; // to skip header

            while ((line = reader.readLine()) != null) {
                if (firstLine) {
                    firstLine = false; // Skip the header row
                    continue;
                }
                String[] data = line.split(";");
                if (data.length == 16) {
                    int V1 = Integer.parseInt(data[1]);
                    int V2 = Integer.parseInt(data[2]);
                    String type = data[5];
                    int maxSpeed = Integer.parseInt(data[7]);
                    double dist = Double.parseDouble(data[3].replace(",", "."));
                    double x1 = Double.parseDouble(data[8].replace(",", "."));
                    double y1 = Double.parseDouble(data[9].replace(",", "."));
                    double x2 = Double.parseDouble(data[10].replace(",", "."));
                    double y2 = Double.parseDouble(data[11].replace(",", "."));
                    int population = Integer.parseInt(data[15].replace(",", "."));

                    Road road = new Road(V1, V2, dist, x1, y1, x2, y2, type, maxSpeed, population);
                    System.out.println(road);
                    roads.add(road);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return roads;
    }

    public static ArrayList<Node> readIntersectionsFromFile(String filename) {
        ArrayList<Node> intersections = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;

            while ((line = reader.readLine()) != null) {
                line = removeUtf8Bom(line); // Handle potential BOM on every line
                String[] data = line.split(";");
                if (data.length == 4) {
                    Node node = new Node(
                            Integer.parseInt(data[0]),
                            Double.parseDouble(data[1]),
                            Double.parseDouble(data[2]),
                            data[3]
                    );
                    System.out.println(node);
                    intersections.add(node);  // Adding to the list as well
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Loaded Intersections: " + intersections.size());  // Optional: Print the number of loaded intersections
        return intersections;  // Returning the list for use
    }

    private static String removeUtf8Bom(String s) {
        if (s.startsWith("\uFEFF")) {
            return s.substring(1);
        }
        return s;
    }

    public static ArrayList<ServiceLocation> readServiceLocationsFromFile(String filename) {
        ArrayList<ServiceLocation> locations = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] data = line.split(";");
                if (data.length == 8) {
                    ServiceLocation location = new ServiceLocation(
                            // Location ID, converted from String to int
                            Double.parseDouble(data[1]), // X
                            Double.parseDouble(data[2]), // Y
                            data[3], //
                            Integer.parseInt(data[7]));
                    // Closest Node ID, hardcoded as -1 if not available (adjust if data[7] is intended to be used)
                    locations.add(location);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return locations;
    }

    public static double[][] loadDistances(String filePath) {
        double[][] distances = new double[8486][8486];
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
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

            return distances;
        } catch (IOException e) {
            System.err.println("Error reading the distance file: " + e.getMessage());

        }

        return distances;
    }
}