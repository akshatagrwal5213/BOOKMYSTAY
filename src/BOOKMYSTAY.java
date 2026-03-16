
import java.util.*;

class Reservation {
    private String guestName;
    private String roomType;
    private String reservationId;
    private String roomId;
    private boolean isCancelled;

    public Reservation(String guestName, String roomType, String roomId) {
        this.guestName = guestName;
        this.roomType = roomType;
        this.roomId = roomId;
        this.reservationId = UUID.randomUUID().toString();
        this.isCancelled = false;
    }

    public String getGuestName() { return guestName; }
    public String getRoomType() { return roomType; }
    public String getReservationId() { return reservationId; }
    public String getRoomId() { return roomId; }
    public boolean isCancelled() { return isCancelled; }
    public void cancel() { this.isCancelled = true; }

    public void displayReservation() {
        System.out.println("Reservation ID: " + reservationId +
                " | Guest: " + guestName +
                " | Room Type: " + roomType +
                " | Room ID: " + roomId +
                " | Status: " + (isCancelled ? "Cancelled" : "Confirmed"));
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
        return inventory.getOrDefault(roomType, 0);
    }

    public void incrementAvailability(String roomType) {
        inventory.put(roomType, getAvailability(roomType) + 1);
    }

    public void decrementAvailability(String roomType) {
        inventory.put(roomType, getAvailability(roomType) - 1);
    }

    public void displayInventory() {
        System.out.println("=== Current Room Inventory ===");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " -> Available: " + entry.getValue());
        }
        System.out.println("==============================");
    }
}

class BookingHistory {
    private List<Reservation> confirmedBookings;

    public BookingHistory() {
        confirmedBookings = new ArrayList<>();
    }

    public void addBooking(Reservation reservation) {
        confirmedBookings.add(reservation);
    }

    public List<Reservation> getAllBookings() {
        return confirmedBookings;
    }

    public Reservation findReservationById(String reservationId) {
        for (Reservation r : confirmedBookings) {
            if (r.getReservationId().equals(reservationId)) {
                return r;
            }
        }
        return null;
    }
}

class CancellationService {
    private RoomInventory inventory;
    private BookingHistory history;
    private Stack<String> rollbackStack; // stores released room IDs

    public CancellationService(RoomInventory inventory, BookingHistory history) {
        this.inventory = inventory;
        this.history = history;
        this.rollbackStack = new Stack<>();
    }

    public void cancelReservation(String reservationId) {
        Reservation reservation = history.findReservationById(reservationId);

        if (reservation == null) {
            System.out.println("Cancellation Failed: Reservation not found.");
            return;
        }

        if (reservation.isCancelled()) {
            System.out.println("Cancellation Failed: Reservation already cancelled.");
            return;
        }

        rollbackStack.push(reservation.getRoomId());
        inventory.incrementAvailability(reservation.getRoomType());
        reservation.cancel();

        System.out.println("Cancellation Successful: " + reservation.getGuestName() +
                " | Room Type: " + reservation.getRoomType() +
                " | Room ID released: " + reservation.getRoomId());
    }

    public void displayRollbackStack() {
        System.out.println("\n=== Rollback Stack (Released Room IDs) ===");
        for (String roomId : rollbackStack) {
            System.out.println("Released Room ID: " + roomId);
        }
        System.out.println("==========================================");
    }
}

public class BOOKMYSTAY {
    public static void main(String[] args) {
        RoomInventory inventory = new RoomInventory();
        inventory.addRoomType("Single Room", 1);
        inventory.addRoomType("Suite Room", 1);

        BookingHistory history = new BookingHistory();

        Reservation r1 = new Reservation("Alice", "Single Room", UUID.randomUUID().toString());
        Reservation r2 = new Reservation("Bob", "Suite Room", UUID.randomUUID().toString());

        history.addBooking(r1);
        history.addBooking(r2);

        System.out.println("=== Initial Bookings ===");
        r1.displayReservation();
        r2.displayReservation();
        inventory.displayInventory();

        CancellationService cancellationService = new CancellationService(inventory, history);

        cancellationService.cancelReservation(r1.getReservationId());
        cancellationService.cancelReservation(r1.getReservationId());
        cancellationService.cancelReservation("invalid-id");

        System.out.println("\n=== Final Bookings ===");
        for (Reservation r : history.getAllBookings()) {
            r.displayReservation();
        }

        inventory.displayInventory();
        cancellationService.displayRollbackStack();

        System.out.println("=== End of Program ===");
    }
}
