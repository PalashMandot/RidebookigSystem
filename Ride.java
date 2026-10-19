// Model class representing a Ride
public class Ride {
    // Enum for Ride Types
    public enum RideType { BIKE, AUTO, CAR }

    // Enum for Ride Statuses (Observer Pattern updates)
    public enum RideStatus { BOOKED, DRIVER_ASSIGNED, RIDE_STARTED, COMPLETED }

    private String rideId;
    private String pickup;
    private String destination;
    private double distance;
    private RideType type;
    private String category;
    private double fare;
    private String driver;
    private String paymentMethodName;
    private RideStatus status;

    // Constructor for new ride creation
    public Ride(String pickup, String destination, double distance, RideType type, double baseFare) {
        this.rideId = "RIDE-" + (int)(Math.random() * 9000 + 1000);
        this.pickup = pickup;
        this.destination = destination;
        this.distance = distance;
        this.type = type;
        this.fare = baseFare;
        this.status = RideStatus.BOOKED;
        this.driver = "Not Assigned";
        this.paymentMethodName = "Pending";
        this.category = "Economy";
    }

    // Constructor for restoring ride from SQLite Database
    public Ride(String rideId, String pickup, String destination, double distance, RideType type, String category, String paymentMethodName, double fare, String driver, RideStatus status) {
        this.rideId = rideId;
        this.pickup = pickup;
        this.destination = destination;
        this.distance = distance;
        this.type = type;
        this.category = category;
        this.paymentMethodName = paymentMethodName;
        this.fare = fare;
        this.driver = driver;
        this.status = status;
    }

    public String getRideId() { return rideId; }
    public String getPickup() { return pickup; }
    public String getDestination() { return destination; }
    public double getDistance() { return distance; }
    public RideType getType() { return type; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public double getFare() { return fare; }
    public void setFare(double fare) { this.fare = fare; }
    public String getDriver() { return driver; }
    public void setDriver(String driver) { this.driver = driver; }
    public String getPaymentMethodName() { return paymentMethodName; }
    public void setPaymentMethodName(String paymentMethodName) { this.paymentMethodName = paymentMethodName; }
    public RideStatus getStatus() { return status; }
    public void setStatus(RideStatus status) { this.status = status; }
}
