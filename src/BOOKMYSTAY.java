
import java.util.*;
class Reservation {
    private String guestName;
    private String roomType;
    private String reservationId;
    private String roomId;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
        this.reservationId = UUID.randomUUID().toString();
        this.roomId = null;
    }

    public String getGuestName() { return guestName; }
    public String getRoomType() { return roomType; }
    public String getReservationId() { return reservationId; }
    public String getRoomId() { return roomId; }
    public void assignRoomId(String roomId) { this.roomId = roomId; }

    public void displayReservation() {
        System.out.println("Reservation ID: " + reservationId +
                " | Guest: " + guestName +
                " | Room Type: " + roomType +
                " | Room ID: " + (roomId == null ? "Pending" : roomId));
    }
}


class RoomInventory {
    private Map<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
    }

    public synchronized void addRoomType(String roomType, int availability) {
        inventory.put(roomType, availability);
    }

    public synchronized int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    public synchronized boolean allocateRoom(String roomType) {
        int current = getAvailability(roomType);
        if (current > 0) {
            inventory.put(roomType, current - 1);
            return true;
        }
        return false;
    }

    public synchronized void displayInventory() {
        System.out.println("=== Current Room Inventory ===");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " -> Available: " + entry.getValue());
        }
        System.out.println("==============================");
    }
}


class BookingProcessor implements Runnable {
    private Reservation reservation;
    private RoomInventory inventory;

    public BookingProcessor(Reservation reservation, RoomInventory inventory) {
        this.reservation = reservation;
        this.inventory = inventory;
    }

    @Override
    public void run() {
        synchronized (inventory) {
            if (inventory.allocateRoom(reservation.getRoomType())) {
                String roomId = UUID.randomUUID().toString();
                reservation.assignRoomId(roomId);
                System.out.println("Booking Confirmed: " + reservation.getGuestName() +
                        " -> " + reservation.getRoomType() +
                        " | Room ID: " + roomId);
            } else {
                System.out.println("Booking Failed: No availability for " +
                        reservation.getGuestName() + " (" + reservation.getRoomType() + ")");
            }
        }
    }
}


public class BOOKMYSTAY {
    public static void main(String[] args) {

        RoomInventory inventory = new RoomInventory();
        inventory.addRoomType("Single Room", 2);
        inventory.addRoomType("Suite Room", 1);


        Reservation r1 = new Reservation("Alice", "Single Room");
        Reservation r2 = new Reservation("Bob", "Single Room");
        Reservation r3 = new Reservation("Charlie", "Single Room");
        Reservation r4 = new Reservation("Diana", "Suite Room");
        Reservation r5 = new Reservation("Eve", "Suite Room");


        Thread t1 = new Thread(new BookingProcessor(r1, inventory));
        Thread t2 = new Thread(new BookingProcessor(r2, inventory));
        Thread t3 = new Thread(new BookingProcessor(r3, inventory));
        Thread t4 = new Thread(new BookingProcessor(r4, inventory));
        Thread t5 = new Thread(new BookingProcessor(r5, inventory));


        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();


        try {
            t1.join();
            t2.join();
            t3.join();
            t4.join();
            t5.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        inventory.displayInventory();

        System.out.println("=== End of Program ===");
    }
}
