package ui;

import javax.imageio.ImageIO;
import java.net.URL;
import network.ApiClient;
import utils.Config;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AlarmLockdown {
    private int failureCount = 0;
    private void loadNewCaptcha(JLabel imageLabel) {
        String imageUrl = ApiClient.getPuzzle();
        if (imageUrl != null) {
            try {
                Image image = ImageIO.read(new URL(imageUrl));
                imageLabel.setIcon(new ImageIcon(image));
                imageLabel.setText(""); // Clear the loading text
            } catch (Exception e) {
                imageLabel.setText("Failed to load image.");
            }
        }
    }
    public void showGUI() {
        JFrame frame = new JFrame("Wake Up!");
        JLabel puzzleImageLabel = new JLabel("Loading CAPTCHA...");
        frame.setUndecorated(true);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setAlwaysOnTop(true);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        // Aggressively pull focus back if they try to click away
        frame.addWindowFocusListener(new WindowAdapter() {
            @Override
            public void windowLostFocus(WindowEvent e) {
                frame.requestFocusInWindow();
                frame.toFront();
            }
        });

        frame.getContentPane().setBackground(Config.BG_NORMAL);
        frame.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;

        JLabel warningLabel = new JLabel("ALARM ACTIVE. SOLVE TO DISABLE.");
        warningLabel.setForeground(Config.BG_PANIC);
        warningLabel.setFont(Config.MAIN_FONT);

        JLabel puzzleLabel = new JLabel("Awaiting puzzle from Python backend...");
        puzzleLabel.setForeground(Config.TEXT_COLOR);
        puzzleLabel.setFont(Config.INPUT_FONT);

        // Live Failure Counter Label
        JLabel attemptsLabel = new JLabel("Attempts Failed: 0");
        attemptsLabel.setForeground(Color.YELLOW);
        attemptsLabel.setFont(Config.INPUT_FONT);

        JTextField inputField = new JTextField(40);
        inputField.setFont(Config.INPUT_FONT);
        inputField.setBackground(Color.DARK_GRAY);
        inputField.setForeground(Config.TEXT_COLOR);
        inputField.setCaretColor(Config.BG_PANIC);

        JButton submitButton = new JButton("Attempt Disable");
        submitButton.setBackground(Config.BG_PANIC);
        submitButton.setForeground(Config.TEXT_COLOR);
        submitButton.setFocusPainted(false);

        // The Logic Hookup
        // Right before making the frame visible, load the first CAPTCHA
        loadNewCaptcha(puzzleImageLabel);

        submitButton.addActionListener(e -> {
            String answer = inputField.getText();
            String response = ApiClient.verifyAnswer(answer);

            if (response.contains("success_but_endless")) {
                // THEY GOT IT RIGHT. DO NOT EXIT.
                // Clear the field, reset failure count, and load a new CAPTCHA immediately.
                inputField.setText("");
                failureCount = 0;
                attemptsLabel.setText("Attempts Failed: " + failureCount);
                loadNewCaptcha(puzzleImageLabel);
            } else {
                // THEY FAILED. Trigger panic and let Python blast a meme.
                failureCount++;
                attemptsLabel.setText("Attempts Failed: " + failureCount);
                UIAnimations.triggerPanic(frame, inputField);

                if (failureCount == 3) warningLabel.setText("WARNING: BRAIN CELLS EVAPORATING.");
                else if (failureCount == 6) warningLabel.setText("EVEN A RANDOM NUMBER GENERATOR IS SMARTER.");
                else if (failureCount >= 10) warningLabel.setText("JUST GIVE UP. ABANDON DEGREE.");
            }
        });

        // Developer Escape Hatch (Remove before Hackathon demo!)
        // Stage Demo Safe Word: Press ESC or (Ctrl + Shift + X) to kill app during demo bugs
        inputField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                boolean isSafeShortcut = (e.isControlDown() && e.isShiftDown() && e.getKeyCode() == KeyEvent.VK_X);
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE || isSafeShortcut) {
                    ApiClient.triggerEscape(); // Tell Python to kill the audio loop!
                    System.exit(0);
                }
            }
        });

        gbc.gridy = 0; frame.add(warningLabel, gbc);
        gbc.gridy = 1; frame.add(puzzleLabel, gbc);
        gbc.gridy = 2; frame.add(inputField, gbc);
        gbc.gridy = 3; frame.add(submitButton, gbc);

        frame.setVisible(true);
        inputField.requestFocus();
    }
}