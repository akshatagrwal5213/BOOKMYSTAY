
import java.util.*;

class Reservation {
    private String guestName;
    private String roomType;
    private String reservationId; // unique ID

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
        this.reservationId = UUID.randomUUID().toString();
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public String getReservationId() {
        return reservationId;
    }

    public void displayReservation() {
        System.out.println("Reservation ID: " + reservationId +
                " | Guest: " + guestName +
                " | Room Type: " + roomType);
    }
}

class AddOnService {
    private String serviceName;
    private double cost;

    public AddOnService(String serviceName, double cost) {
        this.serviceName = serviceName;
        this.cost = cost;
    }

    public String getServiceName() {
        return serviceName;
    }

    public double getCost() {
        return cost;
    }

    @Override
    public String toString() {
        return serviceName + " ($" + cost + ")";
    }
}

class AddOnServiceManager {
    private Map<String, List<AddOnService>> reservationServices;

    public AddOnServiceManager() {
        reservationServices = new HashMap<>();
    }

    public void addServicesToReservation(Reservation reservation, List<AddOnService> services) {
        reservationServices.putIfAbsent(reservation.getReservationId(), new ArrayList<>());
        reservationServices.get(reservation.getReservationId()).addAll(services);
        System.out.println("Services added for Reservation ID: " + reservation.getReservationId());
    }

    public double calculateAdditionalCost(String reservationId) {
        List<AddOnService> services = reservationServices.getOrDefault(reservationId, new ArrayList<>());
        double total = 0;
        for (AddOnService service : services) {
            total += service.getCost();
        }
        return total;
    }

    public void displayServices(String reservationId) {
        List<AddOnService> services = reservationServices.getOrDefault(reservationId, new ArrayList<>());
        if (services.isEmpty()) {
            System.out.println("No services selected for Reservation ID: " + reservationId);
        } else {
            System.out.println("Services for Reservation ID: " + reservationId);
            for (AddOnService service : services) {
                System.out.println(" - " + service);
            }
            System.out.println("Total Additional Cost: $" + calculateAdditionalCost(reservationId));
        }
    }
}

public class BOOKMYSTAY {
    public static void main(String[] args) {
        // Create reservations
        Reservation r1 = new Reservation("Alice", "Single Room");
        Reservation r2 = new Reservation("Bob", "Suite Room");

        r1.displayReservation();
        r2.displayReservation();

        AddOnService breakfast = new AddOnService("Breakfast", 15.0);
        AddOnService spa = new AddOnService("Spa Access", 50.0);
        AddOnService airportPickup = new AddOnService("Airport Pickup", 30.0);

        AddOnServiceManager serviceManager = new AddOnServiceManager();

        serviceManager.addServicesToReservation(r1, Arrays.asList(breakfast, airportPickup));
        serviceManager.addServicesToReservation(r2, Arrays.asList(spa));

        System.out.println();
        serviceManager.displayServices(r1.getReservationId());
        System.out.println();
        serviceManager.displayServices(r2.getReservationId());

        System.out.println("\n=== End of Program ===");
    }
}
