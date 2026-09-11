package ui;

import utils.Config;
import javax.swing.*;
import java.awt.*;

public class UIAnimations {
    public static void triggerPanic(JFrame frame, JTextField inputField) {
        // 1. Flash the background red
        Container cp = frame.getContentPane();
        Color originalColor = cp.getBackground();
        cp.setBackground(Config.BG_PANIC);

        Timer colorTimer = new Timer(150, e -> cp.setBackground(originalColor));
        colorTimer.setRepeats(false);
        colorTimer.start();

        // 2. Shake the entire application window
        Point originalLocation = frame.getLocation();
        Timer shakeTimer = new Timer(20, null);
        shakeTimer.addActionListener(new java.awt.event.ActionListener() {
            int count = 0;
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                if (count > 15) {
                    frame.setLocation(originalLocation);
                    shakeTimer.stop();
                } else {
                    int dx = (count % 2 == 0) ? 20 : -20;
                    frame.setLocation(originalLocation.x + dx, originalLocation.y);
                    count++;
                }
            }
        });
        shakeTimer.start();

        // 3. Clear the input to frustrate the user
        inputField.setText("");
    }
}