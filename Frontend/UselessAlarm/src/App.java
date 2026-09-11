import ui.AlarmScheduler;
import ui.AlarmLockdown;
import javax.swing.SwingUtilities;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Open the setup window, and pass the lockdown method to run when time expires
            AlarmScheduler.promptForAlarmTime(() -> {
                AlarmLockdown lockdown = new AlarmLockdown();
                lockdown.showGUI();
            });
        });
    }
}