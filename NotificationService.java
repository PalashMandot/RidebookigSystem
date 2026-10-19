import java.util.ArrayList;
import java.util.List;

// Observer Pattern - Concrete Observer
public class NotificationService implements RideObserver {
    private List<String> notificationLog = new ArrayList<>();
    private java.util.function.Consumer<String> listener;

    public void setNotificationListener(java.util.function.Consumer<String> listener) {
        this.listener = listener;
    }

    @Override
    public void onStatusUpdate(String message) {
        notificationLog.add(message);
        if (listener != null) {
            listener.accept(message);
        }
    }

    public List<String> getNotificationLog() {
        return notificationLog;
    }

    public void clearLog() {
        notificationLog.clear();
    }
}
