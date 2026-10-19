// Factory Method Pattern
// Creates different Ride objects (Bike, Auto, Car) and calculates basic fare
public class RideFactory {

    public static Ride createRide(String typeStr, String pickup, String destination, double distance) {
        Ride.RideType type;
        double ratePerKm;

        switch (typeStr.toUpperCase()) {
            case "BIKE":
                type = Ride.RideType.BIKE;
                ratePerKm = 10.0; // ₹10/km
                break;
            case "AUTO":
                type = Ride.RideType.AUTO;
                ratePerKm = 15.0; // ₹15/km
                break;
            case "CAR":
            default:
                type = Ride.RideType.CAR;
                ratePerKm = 20.0; // ₹20/km
                break;
        }

        double baseFare = distance * ratePerKm;
        return new Ride(pickup, destination, distance, type, baseFare);
    }
}
