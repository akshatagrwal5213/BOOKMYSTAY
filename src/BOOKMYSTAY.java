
import java.util.LinkedList;
import java.util.Queue;

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

    public void displayReservation() {
        System.out.println("Guest: " + guestName + " | Requested Room: " + roomType);
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

    public void displayQueue() {
        System.out.println("\n=== Current Booking Request Queue ===");
        if (requestQueue.isEmpty()) {
            System.out.println("No pending requests.");
        } else {
            for (Reservation r : requestQueue) {
                r.displayReservation();
            }
        }
        System.out.println("=====================================");
    }
}

public class BOOKMYSTAY {
    public static void main(String[] args) {
        BookingRequestQueue bookingQueue = new BookingRequestQueue();

        Reservation r1 = new Reservation("Alice", "Single Room");
        Reservation r2 = new Reservation("Bob", "Suite Room");
        Reservation r3 = new Reservation("Charlie", "Double Room");

        bookingQueue.addRequest(r1);
        bookingQueue.addRequest(r2);
        bookingQueue.addRequest(r3);

        bookingQueue.displayQueue();

        System.out.println("=== End of Program ===");
    }
}
