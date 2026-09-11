package utils;

import java.awt.Color;
import java.awt.Font;

public class Config {
    // Tell your teammate to run their Python FastAPI on port 500
    public static final String BASE_URL = "http://localhost:5000/verify";

    public static final Color BG_NORMAL = Color.BLACK;
    public static final Color BG_PANIC = Color.RED;
    public static final Color TEXT_COLOR = Color.WHITE;

    public static final Font MAIN_FONT = new Font("Monospaced", Font.BOLD, 24);
    public static final Font INPUT_FONT = new Font("Monospaced", Font.PLAIN, 18);
}