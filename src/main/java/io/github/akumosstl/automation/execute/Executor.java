package io.github.akumosstl.automation.execute;

import io.github.akumosstl.AgentMain;
import io.github.akumosstl.http.json.JsonEventParser;
import io.github.akumosstl.model.Action;
import io.github.akumosstl.utils.LoggerUtil;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.util.Map;
import java.util.logging.Logger;

public class Executor {
    private static final Logger logger = LoggerUtil.getLogger(Executor.class);

    public void execute(String json) {
        Map<String, Action> queue = JsonEventParser.parse(json);
        queue.forEach((key, action) -> {
            try {
                Object component = AgentMain.componentsPool.get(action.getComponentId()).getComponent();

                performAction(component, action);
            } catch (Exception e) {
                logger.severe(e.getMessage());
            }
        });
    }

    private void performAction(Object component, Action action) {
        if (component == null) return;

        if (component instanceof AbstractButton) {
            if (component instanceof JMenuItem) {
                JMenuItem jMenuItem = (JMenuItem) component;
                jMenuItem.setArmed(true);
                jMenuItem.doClick();
            } else {
                clickButton((AbstractButton) component);

            }
        } else if (component instanceof JTextComponent) {
            setText((JTextComponent) component, action.getInputValue());
        } else if (component instanceof JComboBox) {
            selectComboBoxItem((JComboBox<?>) component, action.getInputValue());
        } else if (component instanceof JTable) {
            selectTableCell((JTable) component, action.getInputValue());
        } else if (component instanceof JList) {
            selectListItem((JList<?>) component, action.getInputValue());
        } else {
            logger.info("Unsupported component type: " + component.getClass().getSimpleName());
        }
    }

    private static void clickButton(AbstractButton button) {
        SwingUtilities.invokeLater(button::doClick);
    }

    private static void setText(JTextComponent textComponent, String text) {
        SwingUtilities.invokeLater(() -> textComponent.setText(text));
    }

    private static void selectComboBoxItem(JComboBox<?> comboBox, String item) {
        SwingUtilities.invokeLater(() -> comboBox.setSelectedItem(item));
    }

    private static void selectListItem(JList<?> list, String item) {
        SwingUtilities.invokeLater(() -> list.setSelectedValue(item, true));
    }

    private static void selectTableCell(JTable table, String inputValue) {
        try {
            // Expect input like "Row: 2, Col: 1"
            String[] parts = inputValue.split(",");
            int row = Integer.parseInt(parts[0].split(":")[1].trim());
            int col = Integer.parseInt(parts[1].split(":")[1].trim());

            SwingUtilities.invokeLater(() -> table.changeSelection(row, col, false, false));
        } catch (Exception e) {
            logger.severe("Invalid table input format: " + inputValue);
        }
    }

}

