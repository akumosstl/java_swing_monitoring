package io.github.akumosstl.http.json;

import io.github.akumosstl.model.Action;

import java.io.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class JsonEventParser {

    public static Map<String, Action> parse(String json) {
        Map<String, Action> result = new LinkedHashMap<>();

        json = json.trim();
        if (json.startsWith("{")) json = json.substring(1);
        if (json.endsWith("}")) json = json.substring(0, json.length() - 1);

        String[] entries = json.split("\"\\d+\":\\s*\\{");

        int index = 0;
        for (String entry : entries) {
            entry = entry.trim();
            if (entry.isEmpty()) continue;

            // Clean entry
            if (!entry.startsWith("{")) entry = "{" + entry;
            if (entry.endsWith(",")) entry = entry.substring(0, entry.length() - 1);
            if (entry.endsWith("}")) entry = entry.substring(0, entry.lastIndexOf("}") + 1);

            Action action = parseEventRecord(entry);
            result.put(String.valueOf(index++), action);
        }

        return result;
    }

    public static Map<String, Action> parse(File file) throws IOException {
        Map<String, Action> result = new LinkedHashMap<>();
        BufferedReader reader = new BufferedReader(new FileReader(file));
        StringBuilder jsonBuilder = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            jsonBuilder.append(line.trim());
        }

        reader.close();
        String json = jsonBuilder.toString();
        json = json.substring(1, json.length() - 1); // remove outer braces

        String[] entries = json.split("\"\\d+\":\\s*\\{");
        int index = 0;

        for (String entry : entries) {
            if (entry.trim().isEmpty()) continue;

            entry = entry.trim();
            if (entry.endsWith(",")) entry = entry.substring(0, entry.length() - 1);
            if (!entry.startsWith("{")) entry = "{" + entry;

            Action record = parseEventRecord(entry);
            result.put(String.valueOf(index++), record);
        }

        return result;
    }

    public static Action parseEventRecord(String json) {
        Action record = new Action();
        json = json.substring(1, json.length() - 1); // remove inner braces

        String[] fields = json.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

        for (String field : fields) {
            String[] parts = field.split(":", 2);
            String key = parts[0].trim().replace("\"", "");
            String value = parts[1].trim().replace("\"", "");

            switch (key) {
                case "componentType":
                    record.setComponentType( value);
                    break;
                case "componentId":
                    record.setComponentId(value);
                    break;
                case "inputValue":
                    record.setInputValue(value);
                    break;
                case "timeToNext":
                    record.setTimeToNext((long) Integer.parseInt(value));
                    break;
                case "eventType":
                    record.setEventType(value);
                    break;

            }
        }

        return record;
    }

    public static void writeToJsonFile(Map<String, Action> map, File outputFile) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
        writer.write("{\n");

        int count = 0;
        for (Map.Entry<String, Action> entry : map.entrySet()) {
            String key = entry.getKey();
            Action record = entry.getValue();

            writer.write("  \"" + key + "\": {\n");
            writer.write("    \"componentType\": \"" + record.getComponentType() + "\",\n");
            writer.write("    \"componentId\": \"" + record.getComponentId() + "\",\n");
            writer.write("    \"inputValue\": \"" + escapeJson(record.getInputValue()) + "\",\n");
            writer.write("    \"timeToNext\": " + record.getTimeToNext() + ",\n");
            writer.write("    \"eventType\": \"" + record.getEventType() + "\"\n");
            writer.write("  }");

            count++;
            if (count < map.size()) {
                writer.write(",");
            }
            writer.write("\n");
        }

        writer.write("}\n");
        writer.close();
    }

    private static String escapeJson(String input) {
        if (input == null) return "";
        return input.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}

