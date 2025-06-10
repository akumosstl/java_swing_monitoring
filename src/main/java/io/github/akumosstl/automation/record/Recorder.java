package io.github.akumosstl.automation.record;

import io.github.akumosstl.model.Action;
import io.github.akumosstl.model.ID;
import io.github.akumosstl.persistence.JsonPersistence;
import io.github.akumosstl.service.ExtractService;
import io.github.akumosstl.utils.LoggerUtil;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

public final class Recorder {

    public static final Map<String, Action> queue = new LinkedHashMap<>();

    public static AtomicBoolean recording = new AtomicBoolean(false);
    public static int actionCounter = 0;
    public static long lastTimestamp = System.currentTimeMillis();

    private static final Logger logger = LoggerUtil.getLogger(Recorder.class);

    public static void startRecording() {
        if (recording.compareAndSet(false, true)) {
            LoggerUtil.logInfo(logger, "[Recorder] Recording started.");
            queue.clear();
            lastTimestamp = System.currentTimeMillis();
        }
    }

    public static void stopRecording() {
        LoggerUtil.logInfo(logger, "[Recorder] Recording stopped.");
        JsonPersistence.saveQueue(queue);
    }

    public static void setupGlobalListeners() {
        Toolkit.getDefaultToolkit().addAWTEventListener(event -> {
            if (event.getID() == WindowEvent.WINDOW_OPENED) {
                ExtractService.extractMetadata(event.getSource());
            }
            if (Recorder.recording.get()) {
                if (event instanceof MouseEvent) {
                    MouseEvent me = (MouseEvent) event;
                    if (me.getID() == MouseEvent.MOUSE_CLICKED || me.getID() == MouseEvent.MOUSE_PRESSED) {
                        performRecordAutomation(me.getComponent(), MouseEvent.MOUSE_CLICKED);
                    }
                } else if (event instanceof FocusEvent) {
                    FocusEvent fe = (FocusEvent) event;
                    if (fe.getID() == FocusEvent.FOCUS_GAINED) {
                        performRecordAutomation(fe.getComponent(), FocusEvent.FOCUS_GAINED);
                    } else if (fe.getID() == FocusEvent.FOCUS_LOST) {
                        performRecordAutomation(fe.getComponent(), FocusEvent.FOCUS_LOST);
                    }
                } else if (event instanceof KeyEvent) {
                    KeyEvent ke = (KeyEvent) event;
                    if (ke.getID() == KeyEvent.KEY_TYPED) {
                        performRecordAutomation(ke.getComponent(), KeyEvent.KEY_TYPED);
                    }
                }
            }

        }, AWTEvent.MOUSE_EVENT_MASK
                | AWTEvent.WINDOW_EVENT_MASK
                | AWTEvent.FOCUS_EVENT_MASK
                | AWTEvent.KEY_EVENT_MASK
                | AWTEvent.INPUT_METHOD_EVENT_MASK);
    }

    public static void performRecordAutomation(Object component, int eventId) {
        if (component instanceof JTextComponent || component instanceof JComboBox ||
                component instanceof JTable || component instanceof JMenuBar || component instanceof AbstractButton) {
            Action action = new Action();
            action.setEventType(String.valueOf(eventId));
            action.setComponentType(component.getClass().getName());
            action.setComponentId(ID.of((Component) component));
            action.setInputValue(getInputValue((Component) component));

            long now = System.currentTimeMillis();
            action.setTimeToNext(now - Recorder.lastTimestamp);
            lastTimestamp = now;

            queue.put(String.valueOf(Recorder.actionCounter++), action);
        }
    }

    public static String getInputValue(Component comp) {
        if (comp instanceof JTextComponent) {
            return ((JTextComponent) comp).getText();
        } else if (comp instanceof AbstractButton) {
            return ((AbstractButton) comp).getText();
        } else if (comp instanceof JLabel) {
            return ((JLabel) comp).getText();
        } else if (comp instanceof JComboBox) {
            Object selected = ((JComboBox<?>) comp).getSelectedItem();
            return selected != null ? selected.toString() : "";
        } else if (comp instanceof JTable) {
            JTable table = (JTable) comp;
            int row = table.getSelectedRow();
            int col = table.getSelectedColumn();
            if (row >= 0 && col >= 0) {
                Object value = table.getValueAt(row, col);
                return "Row: " + row + ", Col: " + col + ", Value: " + (value != null ? value.toString() : "null");
            } else {
                return "No row selected";
            }
        } else if (comp instanceof JList) {
            JList<?> list = (JList<?>) comp;
            Object selected = list.getSelectedValue();
            return selected != null ? selected.toString() : "No selection";
        }
        return "";
    }
}
