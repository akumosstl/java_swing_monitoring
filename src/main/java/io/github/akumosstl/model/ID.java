package io.github.akumosstl.model;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class ID {

    private ID() {

    }

    public static String of(Component component) {
        return getId(component);
    }

    private static String getId(Component component) {
        if (component instanceof JComponent) {
            StringBuilder id = getBuilder(component);

            return id.toString();
        } else {
            StringBuilder path = new StringBuilder();
            while (component != null) {
                java.awt.Container parent = component.getParent();
                int index = -1;
                if (parent != null) {
                    java.awt.Component[] siblings = parent.getComponents();
                    for (int i = 0; i < siblings.length; i++) {
                        if (siblings[i] == component) {
                            index = i;
                            break;
                        }
                    }
                }
                path.insert(0, "/" + component.getClass().getSimpleName() + "[" + index + "]");
                component = parent;
            }
            return Integer.toHexString(path.toString().hashCode());
        }

    }

    private static StringBuilder getBuilder(Component component) {
        StringBuilder id = new StringBuilder(component.getClass().getSimpleName());
        if (component instanceof AbstractButton) {
            String text = ((AbstractButton) component).getText();
            if (Objects.nonNull(text)) {
                text = text.trim().replaceAll("\\s", "");
            }
            id.append(text);
        }
        String name = component.getName() != null ? component.getName() : "@";
        if (Objects.nonNull(name)) {
            name = name.trim().replaceAll("\\s", "");
        }
        id.append(name);
        String parent = component.getParent() != null ? component.getParent().getName() : "@";
        if (Objects.nonNull(parent)) {
            parent = parent.trim().replaceAll("\\s", "");
        }
        id.append(parent);
        return id;
    }
}
