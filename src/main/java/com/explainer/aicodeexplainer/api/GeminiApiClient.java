package com.explainer.aicodeexplainer.api;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class GeminiApiClient {

    private static final String BASE_URL =
            "https://generativelanguage.googleapis.com/v1beta/models/gemma-4-31b-it:generateContent";

    private final HttpClient httpClient;
    private final Gson gson;

    public GeminiApiClient() {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(15))
                .build();
        this.gson = new Gson();
    }

    public String explainCode(String code, String apiKey) throws IOException, InterruptedException {
        String prompt = "Explain what this code does in one short paragraph of plain English. " +
                "Output only the explanation, nothing else:\n\n" + code;

        JsonObject textPart = new JsonObject();
        textPart.addProperty("text", prompt);

        JsonArray parts = new JsonArray();
        parts.add(textPart);

        JsonObject content = new JsonObject();
        content.add("parts", parts);

        JsonArray contents = new JsonArray();
        contents.add(content);

        JsonObject body = new JsonObject();
        body.add("contents", contents);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "?key=" + apiKey))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(body)))
                .timeout(Duration.ofSeconds(30))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new IOException("API error " + response.statusCode() + ": " + response.body());
        }

        return parseResponse(response.body());
    }

    private String parseResponse(String responseBody) throws IOException {
        JsonObject root = gson.fromJson(responseBody, JsonObject.class);

        try {
            com.google.gson.JsonArray parts = root
                    .getAsJsonArray("candidates")
                    .get(0).getAsJsonObject()
                    .getAsJsonObject("content")
                    .getAsJsonArray("parts");


            String lastText = null;
            for (int i = 0; i < parts.size(); i++) {
                JsonObject part = parts.get(i).getAsJsonObject();
                if (part.has("thought") && part.get("thought").getAsBoolean()) {
                    continue;
                }
                if (part.has("text")) {
                    lastText = part.get("text").getAsString().trim();
                }
            }

            if (lastText != null && !lastText.isEmpty()) {
                return lastText;
            }

            throw new IOException("No valid response part found");

        } catch (IOException e) {
            throw e;
        } catch (Exception e) {
            throw new IOException("Failed to parse Gemini response: " + responseBody);
        }
    }
}
