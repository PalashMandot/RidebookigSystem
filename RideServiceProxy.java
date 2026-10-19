// Proxy Pattern
// Controls access to booking service by checking user login state
public class RideServiceProxy {
    private RideBookingSystem system;

    public RideServiceProxy() {
        this.system = RideBookingSystem.getInstance();
    }

    public boolean checkLogin() {
        return system.isLoggedIn();
    }

    public String bookRide(RideBooking booking) {
        // Proxy Check: verify authentication before booking
        if (!checkLogin()) {
            return "Please login first!";
        }

        Ride ride = booking.getRide();

        // 1. Status: BOOKED
        ride.setStatus(Ride.RideStatus.BOOKED);
        system.notifyObservers("Ride booked successfully! (ID: " + ride.getRideId() + ")");

        // 2. Status: DRIVER_ASSIGNED
        String driver = system.assignRandomDriver();
        ride.setDriver(driver);
        ride.setStatus(Ride.RideStatus.DRIVER_ASSIGNED);
        system.notifyObservers("Driver assigned: " + driver + " for Ride ID: " + ride.getRideId());

        // 3. Status: RIDE_STARTED
        ride.setStatus(Ride.RideStatus.RIDE_STARTED);
        system.notifyObservers("Ride started for Ride ID: " + ride.getRideId());

        // Process Payment (Bridge Pattern)
        String paymentMsg = booking.processBooking();

        // 4. Status: COMPLETED
        ride.setStatus(Ride.RideStatus.COMPLETED);
        system.notifyObservers("Ride completed! " + paymentMsg);

        // Store booked ride in Singleton system
        system.addRide(ride);

        return "SUCCESS";
    }
}
