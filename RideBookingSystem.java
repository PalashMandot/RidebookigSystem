import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// Singleton Pattern & Observer Pattern (Subject)
// Manages application state, observers, and SQLite Database persistence (rides.db)
public class RideBookingSystem {
    private static RideBookingSystem instance;
    private ArrayList<Ride> bookedRides;
    private List<RideObserver> observers;
    private boolean loggedIn;
    private String loggedInUser;
    private String[] drivers = {"Rahul", "Amit", "Rohit", "Vikas"};
    private Connection dbConnection;

    // Private constructor initializes SQLite Database and loads saved rides
    private RideBookingSystem() {
        bookedRides = new ArrayList<>();
        observers = new ArrayList<>();
        loggedIn = false;
        loggedInUser = "";
        initDatabase();
    }

    public static synchronized RideBookingSystem getInstance() {
        if (instance == null) {
            instance = new RideBookingSystem();
        }
        return instance;
    }

    private void initDatabase() {
        try {
            // Explicitly register SQLite JDBC driver
            Class.forName("org.sqlite.JDBC");

            // Connect to SQLite database file 'rides.db'
            String url = "jdbc:sqlite:rides.db";
            dbConnection = DriverManager.getConnection(url);

            // Create rides table if it doesn't exist
            String createTableSQL = "CREATE TABLE IF NOT EXISTS rides (" +
                    "ride_id TEXT PRIMARY KEY, " +
                    "pickup TEXT, " +
                    "destination TEXT, " +
                    "distance REAL, " +
                    "ride_type TEXT, " +
                    "category TEXT, " +
                    "payment_method TEXT, " +
                    "fare REAL, " +
                    "driver TEXT, " +
                    "status TEXT" +
                    ");";

            try (Statement stmt = dbConnection.createStatement()) {
                stmt.execute(createTableSQL);
            }

            // Load existing rides from database into ArrayList<Ride>
            loadRidesFromDatabase();

        } catch (ClassNotFoundException e) {
            System.err.println("SQLite Driver Error: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Database Initialization Error: " + e.getMessage());
        }
    }

    private void loadRidesFromDatabase() {
        String query = "SELECT * FROM rides;";
        try (Statement stmt = dbConnection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String rideId = rs.getString("ride_id");
                String pickup = rs.getString("pickup");
                String destination = rs.getString("destination");
                double distance = rs.getDouble("distance");
                Ride.RideType type = Ride.RideType.valueOf(rs.getString("ride_type"));
                String category = rs.getString("category");
                String paymentMethod = rs.getString("payment_method");
                double fare = rs.getDouble("fare");
                String driver = rs.getString("driver");
                Ride.RideStatus status = Ride.RideStatus.valueOf(rs.getString("status"));

                Ride ride = new Ride(rideId, pickup, destination, distance, type, category, paymentMethod, fare, driver, status);
                bookedRides.add(ride);
            }
        } catch (SQLException e) {
            System.err.println("Error loading rides from DB: " + e.getMessage());
        }
    }

    public void addRide(Ride ride) {
        bookedRides.add(ride);
        saveRideToDatabase(ride);
    }

    private void saveRideToDatabase(Ride ride) {
        if (dbConnection == null) return;

        String insertSQL = "INSERT INTO rides (ride_id, pickup, destination, distance, ride_type, category, payment_method, fare, driver, status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

        try (PreparedStatement pstmt = dbConnection.prepareStatement(insertSQL)) {
            pstmt.setString(1, ride.getRideId());
            pstmt.setString(2, ride.getPickup());
            pstmt.setString(3, ride.getDestination());
            pstmt.setDouble(4, ride.getDistance());
            pstmt.setString(5, ride.getType().name());
            pstmt.setString(6, ride.getCategory());
            pstmt.setString(7, ride.getPaymentMethodName());
            pstmt.setDouble(8, ride.getFare());
            pstmt.setString(9, ride.getDriver());
            pstmt.setString(10, ride.getStatus().name());

            pstmt.executeUpdate();
            notifyObservers("DB: Persisted Ride " + ride.getRideId() + " to SQLite database (rides.db).");
        } catch (SQLException e) {
            System.err.println("Error saving ride to DB: " + e.getMessage());
        }
    }

    public boolean login(String username, String password) {
        if ("admin".equals(username) && "1234".equals(password)) {
            this.loggedIn = true;
            this.loggedInUser = username;
            return true;
        }
        return false;
    }

    public void logout() {
        this.loggedIn = false;
        this.loggedInUser = "";
    }

    public boolean isLoggedIn() { return loggedIn; }
    public String getLoggedInUser() { return loggedInUser; }

    public void addObserver(RideObserver observer) {
        observers.add(observer);
    }

    public void notifyObservers(String message) {
        for (RideObserver observer : observers) {
            observer.onStatusUpdate(message);
        }
    }

    public ArrayList<Ride> getBookedRides() {
        return bookedRides;
    }

    public String assignRandomDriver() {
        Random rand = new Random();
        return drivers[rand.nextInt(drivers.length)];
    }
}
