package com.david.writing.client;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.json.JSONObject;

import com.david.writing.config.ApiConfig;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DeepseekApiClient {
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private final OkHttpClient client;
    private final ApiConfig apiConfig;
    private JSONObject lastUsage;

    public DeepseekApiClient(ApiConfig apiConfig) {
        this.apiConfig = apiConfig;
        this.client = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build();
    }

    public String sendRequest(String prompt) throws IOException {
        JSONObject requestBody = new JSONObject()
            .put("model", apiConfig.getModel())
            .put("messages", new JSONObject[]{ new JSONObject()
                .put("role", "user")
                .put("content", prompt)
            })
            .put("temperature", apiConfig.getTemperature())
            .put("max_tokens", apiConfig.getMaxTokens());

        Request request = new Request.Builder()
            .url(apiConfig.getApiUrl())
            .addHeader("Authorization", "Bearer " + apiConfig.getApiKey())
            .post(RequestBody.create(requestBody.toString(), JSON))
            .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                String errorBody = response.body() != null ? response.body().string() : "No error details";
                throw new IOException("API request failed with code " + response.code() + ": " + errorBody);
            }

            String responseBody = response.body().string();
            JSONObject jsonResponse = new JSONObject(responseBody);
            
            if (jsonResponse.has("usage")) {
                this.lastUsage = jsonResponse.getJSONObject("usage");
            }

            return jsonResponse.getJSONArray("choices")
                .getJSONObject(0)
                .getJSONObject("message")
                .getString("content");
        }
    }

    public JSONObject getLastUsage() {
        return lastUsage;
    }
} 