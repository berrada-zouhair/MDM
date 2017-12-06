package com.mdm.interfaces.notifcations;

import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

public class PushyAPI {
    public static ObjectMapper mapper = new ObjectMapper();
    
    // Insert your Secret API Key here
    public static final String SECRET_API_KEY = "1b8fc4d21f700b27c8a9ecf647c0302219653ec5bbf8e10a3a9c8d979c2d6deb";

    public static void sendPush(PushyPushRequest req) throws Exception {
        // Get custom HTTP client
		HttpClient client = new DefaultHttpClient();

        // Create POST request
        HttpPost request = new HttpPost("https://api.pushy.me/push?api_key=" + SECRET_API_KEY);

        // Set content type to JSON
        request.addHeader("Content-Type", "application/json");

        // Convert post data to JSON
        byte[] json = mapper.writeValueAsBytes(req);

        // Send post data as byte array
        request.setEntity(new ByteArrayEntity(json));

        // Execute the request
        HttpResponse response = client.execute(request, new BasicHttpContext());

        // Get response JSON as string
        String responseJSON = EntityUtils.toString(response.getEntity());

        // Convert JSON response into HashMap
        @SuppressWarnings("unchecked")
		Map<String, Object> map = mapper.readValue(responseJSON, Map.class);

        // Got an error?
        if (map.containsKey("error")) {
            // Throw it
            throw new Exception(map.get("error").toString());
        }
    }

    public static class PushyPushRequest {
        public Object to;
        public Object data;

        public PushyPushRequest(Object data, Object to) {
            this.to = to;
            this.data = data;
        }
    }
}
