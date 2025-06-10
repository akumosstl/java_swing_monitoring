package io.github.akumosstl.http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import io.github.akumosstl.automation.execute.ExecutorQueue;
import io.github.akumosstl.automation.record.Recorder;
import io.github.akumosstl.automation.record.RecorderQueue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class SimpleHttpServer {

    private HttpServer server;
    private Thread recordThread;

    public SimpleHttpServer() {
    }

    public void start(int port) throws IOException {
        server = HttpServer.create(new InetSocketAddress(port), port);

        server.createContext("/start-record", this::handleStartRecord);
        server.createContext("/stop-record", this::handleStopRecord);
        server.createContext("/automation", this::handleAutomation);

        server.setExecutor(null);
        server.start();

        System.out.println("HTTP Server started on port " + port);
    }

    public void stop(int port) {
        server.stop(port);
    }

    private void handleStartRecord(HttpExchange exchange) throws IOException {
        if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {

            recordThread = new Thread(new RecorderQueue());
            recordThread.setDaemon(true);
            recordThread.start();

            sendResponse(exchange, "Recording started.");
        } else {
            sendMethodNotAllowed(exchange);
        }
    }

    private void handleStopRecord(HttpExchange exchange) throws IOException {
        if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            Recorder.stopRecording();
            recordThread.stop();
            sendResponse(exchange, "Recording stopped and saved.");
        } else {
            sendMethodNotAllowed(exchange);
        }
    }

    private void handleAutomation(HttpExchange exchange) throws IOException {
        if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8);
            BufferedReader br = new BufferedReader(isr);

            int b;
            StringBuilder buf = new StringBuilder(512);
            while ((b = br.read()) != -1) {
                buf.append((char) b);
            }
            br.close();
            isr.close();

            String json = buf.toString();

            System.out.println("[SimpleHttpServer]  Handling automation, json : " + json);
            Thread executor = new Thread(new ExecutorQueue(json));
            executor.setDaemon(true);
            executor.start();

            sendResponse(exchange, "Automation started.");
        } else {
            sendMethodNotAllowed(exchange);
        }
    }

    private void sendResponse(HttpExchange exchange, String responseText) throws IOException {
        byte[] bytes = responseText.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(200, bytes.length);
        OutputStream os = exchange.getResponseBody();
        os.write(bytes);
        os.close();
    }

    private void sendMethodNotAllowed(HttpExchange exchange) throws IOException {
        String response = "Method Not Allowed";
        exchange.sendResponseHeaders(405, response.length());
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes(StandardCharsets.UTF_8));
        os.close();
    }
}
