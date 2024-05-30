package SimulatedAnnealing;

import java.util.ArrayList;

public class DeliveryDriverConfig {
    private double[][] deliveryDistances;
    private double totalDeliveryCost;
    ArrayList<ServiceLocation> serviceLocations;
    ArrayList<DeliveryDriver> deliveryDrivers;

    public DeliveryDriverConfig(double[][] deliveryDistances, ArrayList<ServiceLocation> serviceLocations) {
        this.deliveryDistances = deliveryDistances;
        this.serviceLocations = serviceLocations;
        this.totalDeliveryCost = 0.0;
        this.deliveryDrivers = new ArrayList<>();
        updateOrders();
        createOrderAssignment();
    }

    public void updateOrders() {
        // Method to remove all orders that are picked up
        for (ServiceLocation serviceLocation : serviceLocations) {
            serviceLocation.removePickupOrders();

        }
    }

    public void createOrderAssignment() {
        ArrayList<ServiceLocation> assignedServiceLocations = new ArrayList<>();
        for (ServiceLocation serviceLocation : serviceLocations) {
            for (int i = 1; i < 10; i++) {
                deliveryDrivers.add(new DeliveryDriver(true));
            }
        }
    }

    public double calculateTotalCost() {
        totalDeliveryCost = 0.0;
        for (DeliveryDriver deliveryDriver : deliveryDrivers) {
            totalDeliveryCost += deliveryDriver.totalCosts();
        }

        return this.totalDeliveryCost;
    }
}

