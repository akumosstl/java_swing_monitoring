package io.github.akumosstl.persistence;


import io.github.akumosstl.model.Action;
import io.github.akumosstl.http.json.JsonEventParser;
import io.github.akumosstl.utils.LoggerUtil;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;

public class JsonPersistence {

    private static final Logger logger = LoggerUtil.getLogger(JsonPersistence.class);

    public static void saveQueue(Map<String, Action> queue) {
        try (FileWriter writer = new FileWriter("queue.json")) {
            JsonEventParser.writeToJsonFile(queue, new File("queue.json"));
            System.out.println("[FileManager] Saved to queue.json");
        } catch (IOException e) {
            logger.severe(e.getMessage());
        }
    }
}