
import java.util.*;

class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

class Reservation {
    private String guestName;
    private String roomType;
    private String reservationId;

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

class RoomInventory {
    private Map<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
    }

    public void addRoomType(String roomType, int availability) {
        inventory.put(roomType, availability);
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, -1); // -1 means invalid room type
    }

    public boolean decrementAvailability(String roomType) throws InvalidBookingException {
        int current = getAvailability(roomType);
        if (current == -1) {
            throw new InvalidBookingException("Invalid room type: " + roomType);
        }
        if (current <= 0) {
            throw new InvalidBookingException("No availability for room type: " + roomType);
        }
        inventory.put(roomType, current - 1);
        return true;
    }

    public void displayInventory() {
        System.out.println("=== Current Room Inventory ===");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " -> Available: " + entry.getValue());
        }
        System.out.println("==============================");
    }
}

class BookingService {
    private RoomInventory inventory;

    public BookingService(RoomInventory inventory) {
        this.inventory = inventory;
    }

    public void confirmBooking(Reservation reservation) {
        try {
            inventory.decrementAvailability(reservation.getRoomType());

            System.out.println("Booking Confirmed: " + reservation.getGuestName() +
                    " -> " + reservation.getRoomType() +
                    " | Reservation ID: " + reservation.getReservationId());

        } catch (InvalidBookingException e) {
            System.out.println("Booking Failed for " + reservation.getGuestName() +
                    ": " + e.getMessage());
        }
    }
}

public class BOOKMYSTAY {
    public static void main(String[] args) {
        RoomInventory inventory = new RoomInventory();
        inventory.addRoomType("Single Room", 1);
        inventory.addRoomType("Double Room", 0); // unavailable
        inventory.addRoomType("Suite Room", 2);

        BookingService bookingService = new BookingService(inventory);

        Reservation r1 = new Reservation("Alice", "Single Room");
        bookingService.confirmBooking(r1);

        Reservation r2 = new Reservation("Bob", "Penthouse");
        bookingService.confirmBooking(r2);

        Reservation r3 = new Reservation("Charlie", "Double Room");
        bookingService.confirmBooking(r3);

        Reservation r4 = new Reservation("Diana", "Suite Room");
        bookingService.confirmBooking(r4);

        inventory.displayInventory();

        System.out.println("=== End of Program ===");
    }
}
