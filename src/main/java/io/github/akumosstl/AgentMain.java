package io.github.akumosstl;

import io.github.akumosstl.app.SwingApp;
import io.github.akumosstl.automation.record.Recorder;
import io.github.akumosstl.config.Config;
import io.github.akumosstl.http.SimpleHttpServer;
import io.github.akumosstl.model.ComponentJson;
import io.github.akumosstl.utils.LoggerUtil;

import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;
import java.lang.instrument.Instrumentation;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Properties;
import java.util.logging.Logger;

public class AgentMain {

    private static final Logger logger = LoggerUtil.getLogger(AgentMain.class);
    public static LinkedHashMap<String, ComponentJson> componentsPool = new LinkedHashMap<>();

    public static void main(String[] args) {
        try {
            init();
            SwingApp.main(null);
        } catch (Exception e) {
            LoggerUtil.logError(logger, e.getMessage(), new RuntimeException(e.getMessage()));
        }
    }

    public static void premain(String agentArgs, Instrumentation inst) {
        try {
            init();
        } catch (Exception e) {
            LoggerUtil.logError(logger, e.getMessage(), new RuntimeException(e.getMessage()));
        }
    }

    private static void init() {
        try {
            LoggerUtil.configure(Paths.get("logging.properties"));

            initConfig();

            SimpleHttpServer server = new SimpleHttpServer();
            server.start(8080);

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                System.out.println("[Agent] Shutting down.");
                server.stop(8080);
                System.exit(0);
            }));

            Recorder.setupGlobalListeners();

            LoggerUtil.logInfo(logger, "[Agent] Started successfully.");
        } catch (Exception e) {
            LoggerUtil.logError(logger, e.getMessage(), new RuntimeException(e.getMessage()));
        }
    }

    private static void initConfig() {
        String configPropertiesFilePath = System.getProperty("config.file");
        if (configPropertiesFilePath != null) {
            Properties properties = new Properties();
            try (InputStream is = Files.newInputStream(Paths.get(configPropertiesFilePath))) {
                properties.load(is);
                Config.setProperties(properties);
            } catch (IOException io) {
                JOptionPane.showConfirmDialog(null, String.format("PreMain error: %s", io.getMessage()));
            }
        }
    }
}