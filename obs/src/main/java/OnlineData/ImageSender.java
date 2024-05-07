package OnlineData;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

import static Main.Game.reward;
import static Main.Game.state;

@Component
public class ImageSender {
    public static void sendImage(BufferedImage image) {
        try {
            String targetUrl = "http://127.0.0.1:5000";
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "png", baos);
            byte[] imageData = baos.toByteArray();

            URL url = new URL(targetUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "image/png");
            connection.setDoOutput(true);

            try (OutputStream os = connection.getOutputStream()) {
                os.write(imageData);
            }

            int responseCode = connection.getResponseCode();
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void sendReward() {
        try {
            String targetUrl = "http://127.0.0.1:5000/reward";
            URL url = new URL(targetUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            ObjectMapper mapper = new ObjectMapper();
            String rewardJson = mapper.writeValueAsString(reward);

            try (OutputStream os = connection.getOutputStream()) {
                os.write(rewardJson.getBytes());
            }

            int responseCode = connection.getResponseCode();

            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void sendGameState() {
        try {
            String targetUrl = "http://127.0.0.1:5000/state";
            URL url = new URL(targetUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            ObjectMapper mapper = new ObjectMapper();
            String rewardJson = mapper.writeValueAsString(state);

            try (OutputStream os = connection.getOutputStream()) {
                os.write(rewardJson.getBytes());
            }

            int responseCode = connection.getResponseCode();

            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}