// Bridge Pattern - Implementor Interface & Concrete Implementations
public interface PaymentMethod {
    String getMethodName();
    String processPayment(double amount);

    public static PaymentMethod getPaymentMethod(String type) {
        switch (type.toUpperCase()) {
            case "CARD":
                return new CardPayment();
            case "UPI":
                return new UpiPayment();
            case "CASH":
            default:
                return new CashPayment();
        }
    }
}

class CashPayment implements PaymentMethod {
    @Override
    public String getMethodName() { return "CASH"; }

    @Override
    public String processPayment(double amount) {
        return "Paid ₹" + amount + " via Cash.";
    }
}

class CardPayment implements PaymentMethod {
    @Override
    public String getMethodName() { return "CARD"; }

    @Override
    public String processPayment(double amount) {
        return "Paid ₹" + amount + " via Credit/Debit Card.";
    }
}

class UpiPayment implements PaymentMethod {
    @Override
    public String getMethodName() { return "UPI"; }

    @Override
    public String processPayment(double amount) {
        return "Paid ₹" + amount + " via Instant UPI.";
    }
}
