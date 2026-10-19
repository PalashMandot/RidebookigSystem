import javax.swing.*;
import java.awt.*;

// GUI Entry Point for the Ride Booking System with SQLite Database integration
public class Main {
    private JFrame frame;
    private JTextField txtUsername, txtPickup, txtDestination, txtDistance;
    private JPasswordField txtPassword;
    private JLabel lblLoginStatus, lblEstFare;
    private JRadioButton rbBike, rbAuto, rbCar;
    private JRadioButton rbEconomy, rbPremium;
    private JRadioButton rbCash, rbCard, rbUpi;
    private JLabel lblDetailRideId, lblDetailType, lblDetailDriver, lblDetailPayment, lblDetailFare, lblDetailStatus;
    private JTextArea txtNotifications;

    private RideBookingSystem system;
    private RideServiceProxy proxy;
    private NotificationService notificationService;
    private Ride currentRide;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Main().createAndShowGUI();
        });
    }

    public void createAndShowGUI() {
        system = RideBookingSystem.getInstance();
        proxy = new RideServiceProxy();
        notificationService = new NotificationService();

        frame = new JFrame("MINI RIDE BOOKING SYSTEM");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(520, 820);
        frame.setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        // Title
        JLabel titleLabel = new JLabel("MINI RIDE BOOKING SYSTEM", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createVerticalStrut(10));

        // Login Panel
        JPanel loginPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        loginPanel.setBorder(BorderFactory.createTitledBorder("User Login"));
        txtUsername = new JTextField(6);
        txtPassword = new JPasswordField(6);
        JButton btnLogin = new JButton("LOGIN");
        lblLoginStatus = new JLabel("Status: Not Logged In");
        lblLoginStatus.setForeground(Color.RED);

        loginPanel.add(new JLabel("Username:"));
        loginPanel.add(txtUsername);
        loginPanel.add(new JLabel("Password:"));
        loginPanel.add(txtPassword);
        loginPanel.add(btnLogin);
        loginPanel.add(lblLoginStatus);
        mainPanel.add(loginPanel);
        mainPanel.add(Box.createVerticalStrut(5));

        // Trip Inputs Panel
        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Trip Details"));
        txtPickup = new JTextField();
        txtDestination = new JTextField();
        txtDistance = new JTextField();

        inputPanel.add(new JLabel("Pickup Location:"));
        inputPanel.add(txtPickup);
        inputPanel.add(new JLabel("Destination:"));
        inputPanel.add(txtDestination);
        inputPanel.add(new JLabel("Distance (km):"));
        inputPanel.add(txtDistance);
        mainPanel.add(inputPanel);
        mainPanel.add(Box.createVerticalStrut(5));

        // Ride Type Panel
        JPanel typePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        typePanel.setBorder(BorderFactory.createTitledBorder("Ride Type"));
        rbBike = new JRadioButton("Bike (₹10/km)");
        rbAuto = new JRadioButton("Auto (₹15/km)");
        rbCar = new JRadioButton("Car (₹20/km)", true);
        ButtonGroup bgType = new ButtonGroup();
        bgType.add(rbBike); bgType.add(rbAuto); bgType.add(rbCar);
        typePanel.add(rbBike); typePanel.add(rbAuto); typePanel.add(rbCar);
        mainPanel.add(typePanel);

        // Category Panel
        JPanel catPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        catPanel.setBorder(BorderFactory.createTitledBorder("Category"));
        rbEconomy = new JRadioButton("Economy", true);
        rbPremium = new JRadioButton("Premium (+₹50)");
        ButtonGroup bgCat = new ButtonGroup();
        bgCat.add(rbEconomy); bgCat.add(rbPremium);
        catPanel.add(rbEconomy); catPanel.add(rbPremium);
        mainPanel.add(catPanel);

        // Payment Panel
        JPanel payPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        payPanel.setBorder(BorderFactory.createTitledBorder("Payment Method"));
        rbCash = new JRadioButton("Cash", true);
        rbCard = new JRadioButton("Card");
        rbUpi = new JRadioButton("UPI");
        ButtonGroup bgPay = new ButtonGroup();
        bgPay.add(rbCash); bgPay.add(rbCard); bgPay.add(rbUpi);
        payPanel.add(rbCash); payPanel.add(rbCard); payPanel.add(rbUpi);
        mainPanel.add(payPanel);

        // Fare Label & Actions
        lblEstFare = new JLabel("Estimated Fare: ₹0.0", SwingConstants.CENTER);
        lblEstFare.setFont(new Font("Arial", Font.BOLD, 14));
        lblEstFare.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(Box.createVerticalStrut(5));
        mainPanel.add(lblEstFare);
        mainPanel.add(Box.createVerticalStrut(5));

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnCalculate = new JButton("CALCULATE FARE");
        JButton btnBook = new JButton("BOOK RIDE");
        actionPanel.add(btnCalculate);
        actionPanel.add(btnBook);
        mainPanel.add(actionPanel);

        // Ride Details Display
        JPanel detailsPanel = new JPanel(new GridLayout(6, 1));
        detailsPanel.setBorder(BorderFactory.createTitledBorder("RIDE DETAILS"));
        lblDetailRideId = new JLabel("Ride ID: -");
        lblDetailType = new JLabel("Ride Type: -");
        lblDetailDriver = new JLabel("Driver: -");
        lblDetailPayment = new JLabel("Payment: -");
        lblDetailFare = new JLabel("Fare: -");
        lblDetailStatus = new JLabel("Status: -");
        detailsPanel.add(lblDetailRideId);
        detailsPanel.add(lblDetailType);
        detailsPanel.add(lblDetailDriver);
        detailsPanel.add(lblDetailPayment);
        detailsPanel.add(lblDetailFare);
        detailsPanel.add(lblDetailStatus);
        mainPanel.add(detailsPanel);
        mainPanel.add(Box.createVerticalStrut(5));

        // Notification Log Display
        JPanel notifyPanel = new JPanel(new BorderLayout());
        notifyPanel.setBorder(BorderFactory.createTitledBorder("NOTIFICATION"));
        txtNotifications = new JTextArea(5, 30);
        txtNotifications.setEditable(false);
        notifyPanel.add(new JScrollPane(txtNotifications), BorderLayout.CENTER);
        mainPanel.add(notifyPanel);
        mainPanel.add(Box.createVerticalStrut(5));

        // Register Observer after notification component initialization
        system.addObserver(notificationService);
        notificationService.setNotificationListener(msg -> {
            txtNotifications.append(msg + "\n");
        });

        // Display Database status
        int savedCount = system.getBookedRides().size();
        txtNotifications.append("DATABASE: Connected to rides.db (Loaded " + savedCount + " saved rides)\n");

        // Bottom Control Buttons
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnPatterns = new JButton("DESIGN PATTERNS");
        JButton btnClear = new JButton("CLEAR");
        JButton btnExit = new JButton("EXIT");
        bottomPanel.add(btnPatterns);
        bottomPanel.add(btnClear);
        bottomPanel.add(btnExit);
        mainPanel.add(bottomPanel);

        // Listeners
        btnLogin.addActionListener(e -> {
            String u = txtUsername.getText().trim();
            String p = new String(txtPassword.getPassword());
            if (system.login(u, p)) {
                lblLoginStatus.setText("Logged in as: " + u);
                lblLoginStatus.setForeground(new Color(0, 128, 0));
                txtNotifications.append("LOGIN: Successful as " + u + "\n");
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid Login! (Use admin / 1234)", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnCalculate.addActionListener(e -> calculateFare());
        btnBook.addActionListener(e -> bookRide());
        btnPatterns.addActionListener(e -> showDesignPatterns());
        btnClear.addActionListener(e -> clearFields());
        btnExit.addActionListener(e -> System.exit(0));

        frame.add(new JScrollPane(mainPanel));
        frame.setVisible(true);
    }

    private void calculateFare() {
        try {
            double distance = Double.parseDouble(txtDistance.getText().trim());
            String type = rbBike.isSelected() ? "BIKE" : (rbAuto.isSelected() ? "AUTO" : "CAR");
            String cat = rbPremium.isSelected() ? "PREMIUM" : "ECONOMY";

            Ride ride = RideFactory.createRide(type, txtPickup.getText(), txtDestination.getText(), distance);
            double totalFare = RidePlatformFactory.calculateTotalFare(ride.getFare(), cat);
            ride.setFare(totalFare);
            ride.setCategory(cat);
            this.currentRide = ride;

            lblEstFare.setText("Estimated Fare: ₹" + totalFare);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Please enter a valid distance in km!", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void bookRide() {
        if (!proxy.checkLogin()) {
            txtNotifications.append("PROXY: Access Denied! Please login first.\n");
            JOptionPane.showMessageDialog(frame, "Please login first!", "Proxy Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (currentRide == null) {
            calculateFare();
            if (currentRide == null) return;
        }

        String payStr = rbCash.isSelected() ? "CASH" : (rbCard.isSelected() ? "CARD" : "UPI");
        PaymentMethod paymentMethod = PaymentMethod.getPaymentMethod(payStr);

        RideBooking booking = new RideBooking(currentRide, paymentMethod);
        String result = proxy.bookRide(booking);

        if ("SUCCESS".equals(result)) {
            lblDetailRideId.setText("Ride ID: " + currentRide.getRideId());
            lblDetailType.setText("Ride Type: " + currentRide.getType() + " (" + currentRide.getCategory() + ")");
            lblDetailDriver.setText("Driver: " + currentRide.getDriver());
            lblDetailPayment.setText("Payment: " + currentRide.getPaymentMethodName());
            lblDetailFare.setText("Fare: ₹" + currentRide.getFare());
            lblDetailStatus.setText("Status: " + currentRide.getStatus());

            currentRide = null; // Clear active calculation for next booking
        }
    }

    private void showDesignPatterns() {
        String info = "DESIGN PATTERNS USED\n\n" +
                "1. Singleton\n" +
                "   RideBookingSystem\n" +
                "   One system instance + SQLite DB persistence (rides.db)\n\n" +
                "2. Factory Method\n" +
                "   RideFactory\n" +
                "   Creates Bike/Auto/Car rides\n\n" +
                "3. Proxy\n" +
                "   RideServiceProxy\n" +
                "   Checks login before booking\n\n" +
                "4. Abstract Factory\n" +
                "   RidePlatformFactory\n" +
                "   Economy/Premium ride category\n\n" +
                "5. Bridge\n" +
                "   RideBooking + PaymentMethod\n" +
                "   Separates booking and payment\n\n" +
                "6. Observer\n" +
                "   RideObserver + NotificationService\n" +
                "   Sends ride status notifications";

        JOptionPane.showMessageDialog(frame, info, "Design Patterns Information", JOptionPane.INFORMATION_MESSAGE);
    }

    private void clearFields() {
        txtPickup.setText("");
        txtDestination.setText("");
        txtDistance.setText("");
        lblEstFare.setText("Estimated Fare: ₹0.0");
        lblDetailRideId.setText("Ride ID: -");
        lblDetailType.setText("Ride Type: -");
        lblDetailDriver.setText("Driver: -");
        lblDetailPayment.setText("Payment: -");
        lblDetailFare.setText("Fare: -");
        lblDetailStatus.setText("Status: -");
        txtNotifications.setText("DB Connected to rides.db\n");
        currentRide = null;
    }
}
