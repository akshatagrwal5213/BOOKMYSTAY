
import java.util.*;

class Reservation {
    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }
}

class BookingRequestQueue {
    private Queue<Reservation> requestQueue;

    public BookingRequestQueue() {
        requestQueue = new LinkedList<>();
    }

    public void addRequest(Reservation reservation) {
        requestQueue.add(reservation);
        System.out.println("Request added for " + reservation.getGuestName() + " (" + reservation.getRoomType() + ")");
    }

    public Reservation getNextRequest() {
        return requestQueue.poll(); // FIFO dequeue
    }

    public boolean hasRequests() {
        return !requestQueue.isEmpty();
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

    public boolean decrementAvailability(String roomType) {
        int current = getAvailability(roomType);
        if (current > 0) {
            inventory.put(roomType, current - 1);
            return true;
        }
        return false;
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
    private Map<String, Set<String>> allocatedRooms; // roomType -> set of room IDs

    public BookingService(RoomInventory inventory) {
        this.inventory = inventory;
        this.allocatedRooms = new HashMap<>();
    }

    public void processRequest(Reservation reservation) {
        String roomType = reservation.getRoomType();

        if (inventory.getAvailability(roomType) > 0) {
            String roomId = UUID.randomUUID().toString();

            allocatedRooms.putIfAbsent(roomType, new HashSet<>());
            if (!allocatedRooms.get(roomType).contains(roomId)) {
                allocatedRooms.get(roomType).add(roomId);

                inventory.decrementAvailability(roomType);

                System.out.println("Booking Confirmed: " + reservation.getGuestName() +
                        " -> " + roomType + " | Room ID: " + roomId);
            }
        } else {
            System.out.println("Booking Failed: No availability for " + reservation.getGuestName() +
                    " (" + roomType + ")");
        }
    }

    public void displayAllocations() {
        System.out.println("\n=== Allocated Rooms ===");
        for (Map.Entry<String, Set<String>> entry : allocatedRooms.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue().size() + " rooms allocated");
        }
        System.out.println("=======================");
    }
}

public class BOOKMYSTAY {
    public static void main(String[] args) {
        RoomInventory inventory = new RoomInventory();
        inventory.addRoomType("Single Room", 2);
        inventory.addRoomType("Double Room", 1);
        inventory.addRoomType("Suite Room", 1);

        BookingRequestQueue queue = new BookingRequestQueue();
        queue.addRequest(new Reservation("Alice", "Single Room"));
        queue.addRequest(new Reservation("Bob", "Suite Room"));
        queue.addRequest(new Reservation("Charlie", "Double Room"));
        queue.addRequest(new Reservation("Diana", "Single Room"));
        queue.addRequest(new Reservation("Eve", "Single Room")); // should fail

        BookingService bookingService = new BookingService(inventory);

        while (queue.hasRequests()) {
            Reservation r = queue.getNextRequest();
            bookingService.processRequest(r);
        }

        inventory.displayInventory();
        bookingService.displayAllocations();

        System.out.println("=== End of Program ===");
    }
}
