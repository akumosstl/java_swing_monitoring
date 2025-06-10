package io.github.akumosstl.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class LoggerUtil {

    private static boolean isConfigured = false;

    /**
     * Loads the logging configuration from the provided properties file path.
     *
     * @param configPath Path to the logging.properties file
     */
    public static void configure(Path configPath) {
        if (isConfigured) return;
        try (FileInputStream input = new FileInputStream(configPath.toFile())) {
            LogManager.getLogManager().readConfiguration(input);
            isConfigured = true;
        } catch (IOException e) {
            System.err.println("Failed to load logging configuration: " + e.getMessage());
        }
    }

    /**
     * Returns a logger for the provided class.
     *
     * @param clazz The class for which the logger is required
     * @return Logger instance
     */
    public static Logger getLogger(Class<?> clazz) {
        return Logger.getLogger(clazz.getName());
    }

    /**
     * Log a clean, formatted message.
     */
    public static void logInfo(Logger logger, String message) {
        logger.log(Level.INFO, clean(message));
    }

    public static void logWarning(Logger logger, String message) {
        logger.log(Level.WARNING, clean(message));
    }

    public static void logError(Logger logger, String message, Throwable t) {
        logger.log(Level.SEVERE, clean(message), t);
    }

    private static String clean(String message) {
        return message.replaceAll("\\s+", " ").trim();
    }
}