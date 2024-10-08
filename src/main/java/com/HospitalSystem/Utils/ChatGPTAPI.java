package com.HospitalSystem.Utils;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import java.nio.charset.StandardCharsets;
import org.json.*;

public class ChatGPTAPI {

    private static final String URL = "https://api.chatanywhere.tech/v1/chat/completions";
    private static final String API_KEY = "sk-0FipaHgKpAuhVS0YV45vLDV6fsTDJ5k6ABxbFu7LXKLlLJNm";

    public static String sendRequestToChatGPT(String message) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost post = new HttpPost(URL);
            post.setHeader("Content-Type", "application/json");
            post.setHeader("Authorization", "Bearer " + API_KEY);

            String jsonBody = String.format("{\"model\": \"gpt-4o-mini\", \"messages\": [{\"role\": \"system\", \"content\": \"现在你是一名医院中的导诊员，指导病人应该去看什么科室，以及提供一些医疗建议\"}, {\"role\": \"user\", \"content\": \"%s\"}]}",
                    message);
            post.setEntity(new StringEntity(jsonBody, StandardCharsets.UTF_8));

            String replay;
            try (CloseableHttpResponse response = httpClient.execute(post)) {
                JSONObject jsonObject = new JSONObject(EntityUtils.toString(response.getEntity()));
                replay = jsonObject.getJSONArray("choices")
                        .getJSONObject(0)
                        .getJSONObject("message")
                        .getString("content");
            }

            return replay;

        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
}
