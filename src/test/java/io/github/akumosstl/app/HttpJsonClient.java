package io.github.akumosstl.app;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class HttpJsonClient {

    /**
     * Sends a POST request with JSON content read from a file.
     *
     * @param endpointUrl  URL to send the POST request to
     * @param jsonFilePath Path to the JSON file to send
     * @return Server response as a String
     * @throws IOException if any IO error occurs
     */
    public static String postJson(String endpointUrl, Path jsonFilePath) throws IOException {
        byte[] bytes = Files.readAllBytes(jsonFilePath);
        String json = new String(bytes, StandardCharsets.UTF_8);

        URL url = new URL(endpointUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json; utf-8");
        connection.setDoOutput(true);

        // Write JSON to output stream
        try (OutputStream os = connection.getOutputStream()) {
            os.write(json.getBytes());
            os.flush();
        }

        // Read response
        int status = connection.getResponseCode();
        InputStream is = (status >= 200 && status < 400)
                ? connection.getInputStream()
                : connection.getErrorStream();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null)
                response.append(line).append("\n");
            return response.toString().trim();
        } finally {
            connection.disconnect();
        }
    }
}

