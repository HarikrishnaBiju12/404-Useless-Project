package ui;

import utils.Config;
import javax.swing.*;
import java.awt.*;

public class AlarmScheduler {

    public static void promptForAlarmTime(Runnable onAlarmTriggered) {
        JFrame setupFrame = new JFrame("Set Your Doom");
        setupFrame.setSize(450, 250);
        setupFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setupFrame.setLocationRelativeTo(null);
        setupFrame.getContentPane().setBackground(Config.BG_NORMAL);
        setupFrame.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;

        JLabel titleLabel = new JLabel("CONFIGURING INESCAPABLE ALARM");
        titleLabel.setForeground(Config.BG_PANIC);
        titleLabel.setFont(new Font("Monospaced", Font.BOLD, 16));

        JLabel promptLabel = new JLabel("Delay alarm by (minutes):");
        promptLabel.setForeground(Config.TEXT_COLOR);
        promptLabel.setFont(Config.INPUT_FONT);

        JTextField minuteField = new JTextField("1", 10); // Default to 1 minute for demo
        minuteField.setFont(Config.INPUT_FONT);
        minuteField.setBackground(Color.DARK_GRAY);
        minuteField.setForeground(Config.TEXT_COLOR);

        JButton setButton = new JButton("Arm Alarm");
        setButton.setBackground(Config.BG_PANIC);
        setButton.setForeground(Config.TEXT_COLOR);

        setButton.addActionListener(e -> {
            try {
                int minutes = Integer.parseInt(minuteField.getText());
                setupFrame.dispose(); // Close setup window

                // Start a background timer to trigger the alarm later
                startCountdown(minutes, onAlarmTriggered);

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(setupFrame, "Please enter a valid number of minutes!");
            }
        });

        gbc.gridy = 0; setupFrame.add(titleLabel, gbc);
        gbc.gridy = 1; setupFrame.add(promptLabel, gbc);
        gbc.gridy = 2; setupFrame.add(minuteField, gbc);
        gbc.gridy = 3; setupFrame.add(setButton, gbc);

        setupFrame.setVisible(true);
    }

    private static void startCountdown(int minutes, Runnable onAlarmTriggered) {
        long delayMillis = minutes * 60L * 1000L;

        System.out.println("Alarm armed! Triggering in " + minutes + " minute(s)...");

        new Thread(() -> {
            try {
                Thread.sleep(delayMillis);
                // Time's up! Execute the lockdown callback
                SwingUtilities.invokeLater(onAlarmTriggered);
            } catch (InterruptedException e) {
                System.out.println("Countdown interrupted.");
            }
        }).start();
    }
}