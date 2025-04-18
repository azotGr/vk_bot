package com.example.vk_bot;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Random;

@Component
public class CoreBot {

    private final String token;
    private final String version = "5.199";

    public CoreBot(@Value("${vk.bot.token}") String token) {
        this.token = token;
    }

    @Scheduled(fixedRate = 5000)
    public void newMessage() {
        try{
            String url = String.format(
                    "https://api.vk.com/method/messages.getConversations?" +
                            "access_token=%s&v=%s&count=10&offset=0",
                    token, version
            );

            HttpResponse<String> response = HttpClient.newHttpClient().send(
                    HttpRequest.newBuilder()
                            .uri(URI.create(url))
                            .timeout(Duration.ofSeconds(10))
                            .GET()
                            .build(),
                    HttpResponse.BodyHandlers.ofString()
            );

            JSONObject json = new JSONObject(response.body());
            JSONArray items = json.getJSONObject("response")
                    .getJSONArray("items");

            for (int i = 0; i < items.length(); i++) {
                JSONObject message = items.getJSONObject(i)
                        .getJSONObject("last_message");
                processMessage(message);
            }

        }catch (Exception e) {
            System.err.println("Ошибка: " + e.getMessage());
        }
    }

    private void processMessage(JSONObject message){
        try {
            int userId = message.getInt("from_id");
            String text = message.getString("text");
            System.out.println("Новое сообщение от " + userId + ": " + text);

            sendMessage(userId, "Вы написали: " + text);
        } catch (Exception e) {
            System.err.println("Ошибка обработки: " + e.getMessage());
        }
    }

    private void sendMessage(int userId, String text){
        try {
            String url = String.format(
                    "https://api.vk.com/method/messages.send?" +
                            "user_id=%d&message=%s&random_id=%d&access_token=%s&v=%s",
                    userId,
                    URLEncoder.encode(text, StandardCharsets.UTF_8),
                    new Random().nextInt(10000),
                    token,
                    version
            );

            HttpResponse<String> response = HttpClient.newHttpClient().send(
                    HttpRequest.newBuilder()
                            .uri(URI.create(url))
                            .GET()
                            .build(),
                    HttpResponse.BodyHandlers.ofString()
            );

            JSONObject json = new JSONObject(response.body());


        }catch (Exception e){
            System.err.println("Ошибка отправки: " + e.getMessage());
        }
    }
}
