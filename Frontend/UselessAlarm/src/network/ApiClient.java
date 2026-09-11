package network;

import utils.Config;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class ApiClient {
    // 1. Fetch the puzzle image URL and trigger the Python audio
    public static String getPuzzle() {
        try {
            URL url = new URL(Config.BASE_URL + "/get-puzzle");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            Scanner scanner = new Scanner(conn.getInputStream());
            String response = scanner.useDelimiter("\\A").next();
            scanner.close();
            // Hacky JSON parse to grab the image URL
            return response.split("\"")[3];
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    // 2. Verify the answer
    public static String verifyAnswer(String answer) {
        try {
            URL url = new URL(Config.BASE_URL + "/verify");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            String jsonInput = "{\"answer\": \"" + answer.replace("\"", "\\\"") + "\"}";
            try (OutputStream os = conn.getOutputStream()) {
                os.write(jsonInput.getBytes("utf-8"));
            }

            Scanner scanner = new Scanner(conn.getInputStream());
            String response = scanner.useDelimiter("\\A").next();
            scanner.close();
            return response; // Returns the raw JSON string
        } catch (Exception e) {
            return "{\"status\": \"failed\"}";
        }
    }
    // 3. The Secret Escape Hatch
    public static void triggerEscape() {
        try {
            URL url = new URL(Config.BASE_URL + "/escape");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.getResponseCode(); // Execute request
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}