package io.github.akumosstl.service;

import io.github.akumosstl.AgentMain;
import io.github.akumosstl.model.ComponentJson;
import io.github.akumosstl.utils.LoggerUtil;

import javax.swing.*;
import java.awt.*;
import java.util.logging.Logger;

public class ExtractService {

    private static final Logger logger = LoggerUtil.getLogger(ExtractService.class);

    public static void extractMetadata(Object frame) {
        ComponentJson componentJson = new ComponentJson();
        if (frame instanceof JFrame) {
            componentJson = new ComponentJson((JFrame) frame);
            addComponentsPoll(((JFrame) frame).getComponents());
        } else if (frame instanceof JDialog) {
            componentJson = new ComponentJson((JDialog) frame);
            addComponentsPoll(((JDialog) frame).getComponents());
        }
        AgentMain.componentsPool.put(componentJson.getId(), componentJson);
    }

    private static void addComponentsPoll(Component[] comps) {
        for (Component c : comps) {
            if (c instanceof Container) {
                addComponentsPoll(((Container) c).getComponents());//call recursively
            }
            try {
                if (c instanceof JMenuItem) {
                    addJMenu((JMenu) c);
                } else {
                    ComponentJson componentJson = new ComponentJson(c);
                    if (!AgentMain.componentsPool.containsKey(componentJson.getId())) {
                        AgentMain.componentsPool.put(componentJson.getId(), componentJson);
                    }
                }
            } catch (Exception e) {
                logger.severe(e.getMessage());
            }
        }
    }

    private static void addJMenu(JMenu component) {
        for (int j = 0; j < component.getMenuComponentCount(); j++) {
            Component comp = component.getMenuComponent(j);
            ComponentJson componentJson = new ComponentJson(comp);
            if (!AgentMain.componentsPool.containsKey(componentJson.getId())) {
                AgentMain.componentsPool.put(componentJson.getId(), componentJson);
            }
        }
    }
}

