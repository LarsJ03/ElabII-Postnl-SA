package SimulatedAnnealing;

import java.util.ArrayList;
import java.util.Random;

public class ServiceLocationConfig {
    private ArrayList<ServiceLocation> servicelocations;
    private double totalCost;
    private ArrayList<Road> roads;
    private double[][] distances;
    private double globalBounceRate;
    private boolean checkDay;

    public ServiceLocationConfig(ArrayList<ServiceLocation> servicelocations, ArrayList<Road> roads, double[][] distances, boolean checkDay) {
        this.servicelocations = Utils.deepCopy(servicelocations);
        this.roads = Utils.deepCopy(roads);
        this.distances = distances;
        this.totalCost = 0.0;
        this.globalBounceRate = 0.0;
        this.checkDay = checkDay;
        assignRoads(distances);
        updateDistances();
        setCapacity(checkDay);
        calculateTotalCost();
    }

    public void setGlobalBounceRate() {
        double bounceRate = 0.0;
        for (ServiceLocation serviceLocation : servicelocations) {
            bounceRate += serviceLocation.getBounceRate();
        }



        this.globalBounceRate = bounceRate / servicelocations.size();
    }

    public double getGlobalBounceRate() {
        setGlobalBounceRate();
        return this.globalBounceRate;
    }

    public ArrayList<ServiceLocation> getServicelocations() {
        return this.servicelocations;
    }

    public ArrayList<Road> getRoads() {
        return this.roads;
    }

    public double[][] getDistances() {
        return this.distances;
    }

    public double getTotalCost() {
        calculateTotalCost();
        return this.totalCost;
    }

    private void calculateTotalCost() {
        totalCost = 0.0;
        for (ServiceLocation servicelocation : servicelocations) {
            totalCost += servicelocation.calculateCosts();
        }
    }

    public void addServiceLocation(ServiceLocation servicelocation) {
        this.servicelocations.add(servicelocation);
    }

    public void assignRoads(double[][] distances) {
        for (ServiceLocation serviceLocation : servicelocations) {
            serviceLocation.clearProperties();
        }

        for (Road road : roads) {
            double minDistance = Double.MAX_VALUE;
            ServiceLocation closestServiceLocation = null;
            for (ServiceLocation serviceLocation : servicelocations) {
                double distance = distances[road.getV1()][serviceLocation.getNodeID()];
                if (distance < minDistance) {
                    minDistance = distance;
                    closestServiceLocation = serviceLocation;
                }
            }
            if (closestServiceLocation != null) {
                closestServiceLocation.addRoad(road);
            }
        }
    }

    public void updateDistances() {
        for (ServiceLocation serviceLocation : servicelocations) {
            int serviceNode = serviceLocation.getNodeID();
            for (Road road : serviceLocation.getRoads()) {
                for (Order order : road.getOrders()) {
                    order.setWalkingDistanceServiceLocation(distances, serviceNode);
                }
            }
        }
    }

    public void setCapacity(boolean checkDay) {
        if (!checkDay) {
            for (ServiceLocation serviceLocation : servicelocations) {
                serviceLocation.setCapacity();
            }
        }
    }

    public void removeRandomServiceLocation() {
        if (!servicelocations.isEmpty()) {
            Random rand = new Random();
            int randomIndex = rand.nextInt(servicelocations.size());
            servicelocations.remove(randomIndex);
            clearAllParameters();
            assignRoads(distances);
            updateDistances();
            setCapacity(false);
            calculateTotalCost();
        } else {
            System.out.println("No service locations to remove.");
        }
    }

    public void addRandomServiceLocation(int nodeID) {
        ServiceLocation newServiceLocation = new ServiceLocation(nodeID);
        servicelocations.add(newServiceLocation);
        clearAllParameters();
        assignRoads(distances);
        updateDistances();
        setCapacity(false);
        calculateTotalCost();
    }

    private void clearAllParameters() {
        for (ServiceLocation serviceLocation : servicelocations) {
            serviceLocation.clearProperties();
        }
    }
}
