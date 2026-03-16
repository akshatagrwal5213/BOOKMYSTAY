
import java.io.*;
import java.util.*;

class Reservation implements Serializable {
    private static final long serialVersionUID = 1L;

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


class RoomInventory implements Serializable {
    private static final long serialVersionUID = 1L;
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

    public boolean allocateRoom(String roomType) {
        int current = getAvailability(roomType);
        if (current > 0) {
            inventory.put(roomType, current - 1);
            return true;
        }
        return false;
    }

    public void incrementAvailability(String roomType) {
        inventory.put(roomType, getAvailability(roomType) + 1);
    }

    public void displayInventory() {
        System.out.println("=== Current Room Inventory ===");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " -> Available: " + entry.getValue());
        }
        System.out.println("==============================");
    }
}


class BookingHistory implements Serializable {
    private static final long serialVersionUID = 1L;
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

    public void displayBookings() {
        System.out.println("=== Booking History ===");
        for (Reservation r : confirmedBookings) {
            r.displayReservation();
        }
        System.out.println("=======================");
    }
}


class PersistenceService {
    private static final String FILE_NAME = "system_state.ser";

    public static void saveState(RoomInventory inventory, BookingHistory history) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(inventory);
            oos.writeObject(history);
            System.out.println("System state saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving system state: " + e.getMessage());
        }
    }

    public static Object[] loadState() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            RoomInventory inventory = (RoomInventory) ois.readObject();
            BookingHistory history = (BookingHistory) ois.readObject();
            System.out.println("System state restored successfully.");
            return new Object[]{inventory, history};
        } catch (FileNotFoundException e) {
            System.out.println("No saved state found. Starting fresh.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error restoring system state: " + e.getMessage());
        }
        return new Object[]{new RoomInventory(), new BookingHistory()};
    }
}


public class BOOKMYSTAY {
    public static void main(String[] args) {

        Object[] state = PersistenceService.loadState();
        RoomInventory inventory = (RoomInventory) state[0];
        BookingHistory history = (BookingHistory) state[1];


        if (inventory.getAvailability("Single Room") == 0 &&
                inventory.getAvailability("Suite Room") == 0) {
            inventory.addRoomType("Single Room", 2);
            inventory.addRoomType("Suite Room", 1);
        }


        Reservation r1 = new Reservation("Alice", "Single Room", UUID.randomUUID().toString());
        if (inventory.allocateRoom(r1.getRoomType())) {
            history.addBooking(r1);
        }

        Reservation r2 = new Reservation("Bob", "Suite Room", UUID.randomUUID().toString());
        if (inventory.allocateRoom(r2.getRoomType())) {
            history.addBooking(r2);
        }


        inventory.displayInventory();
        history.displayBookings();


        PersistenceService.saveState(inventory, history);

        System.out.println("=== End of Program ===");
    }
}
