// Abstract Factory Pattern
// Provides platform category charges (Economy vs Premium)
public class RidePlatformFactory {

    public static double getCategoryCharge(String category) {
        if ("PREMIUM".equalsIgnoreCase(category)) {
            return 50.0; // Premium charge ₹50
        }
        return 0.0; // Economy normal fare
    }

    public static double calculateTotalFare(double baseFare, String category) {
        return baseFare + getCategoryCharge(category);
    }
}
