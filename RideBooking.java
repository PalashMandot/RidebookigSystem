// Bridge Pattern - Abstraction
// Separates Ride Booking logic from PaymentMethod implementation using composition
public class RideBooking {
    private Ride ride;
    private PaymentMethod paymentMethod;

    public RideBooking(Ride ride, PaymentMethod paymentMethod) {
        this.ride = ride;
        this.paymentMethod = paymentMethod;
        this.ride.setPaymentMethodName(paymentMethod.getMethodName());
    }

    public String processBooking() {
        return paymentMethod.processPayment(ride.getFare());
    }

    public Ride getRide() {
        return ride;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }
}
