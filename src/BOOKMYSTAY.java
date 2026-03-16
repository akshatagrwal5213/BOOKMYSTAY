
import java.util.*;

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

class BookingHistory {
    private List<Reservation> confirmedBookings;

    public BookingHistory() {
        confirmedBookings = new ArrayList<>();
    }

    public void addBooking(Reservation reservation) {
        confirmedBookings.add(reservation);
        System.out.println("Booking recorded for " + reservation.getGuestName() +
                " (" + reservation.getRoomType() + ")");
    }

    public List<Reservation> getAllBookings() {
        return confirmedBookings;
    }
}

class BookingReportService {
    private BookingHistory history;

    public BookingReportService(BookingHistory history) {
        this.history = history;
    }

    public void displayAllBookings() {
        System.out.println("\n=== Booking History Report ===");
        List<Reservation> bookings = history.getAllBookings();
        if (bookings.isEmpty()) {
            System.out.println("No bookings recorded.");
        } else {
            for (Reservation r : bookings) {
                r.displayReservation();
            }
        }
        System.out.println("==============================");
    }

    public void displaySummaryByRoomType() {
        System.out.println("\n=== Booking Summary by Room Type ===");
        Map<String, Integer> summary = new HashMap<>();
        for (Reservation r : history.getAllBookings()) {
            summary.put(r.getRoomType(), summary.getOrDefault(r.getRoomType(), 0) + 1);
        }
        for (Map.Entry<String, Integer> entry : summary.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue() + " bookings");
        }
        System.out.println("====================================");
    }
}

public class BOOKMYSTAY {
    public static void main(String[] args) {
        BookingHistory history = new BookingHistory();

        Reservation r1 = new Reservation("Alice", "Single Room");
        Reservation r2 = new Reservation("Bob", "Suite Room");
        Reservation r3 = new Reservation("Charlie", "Double Room");
        Reservation r4 = new Reservation("Diana", "Single Room");

        history.addBooking(r1);
        history.addBooking(r2);
        history.addBooking(r3);
        history.addBooking(r4);

        BookingReportService reportService = new BookingReportService(history);

        reportService.displayAllBookings();
        reportService.displaySummaryByRoomType();

        System.out.println("\n=== End of Program ===");
    }
}
