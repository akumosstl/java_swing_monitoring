package io.github.akumosstl.app;

import io.github.akumosstl.automation.record.Recorder;

import javax.swing.*;

public class SwingApp {

    private static JFrame frame;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SwingApp::createMainFrame);
    }

    public static void createMainFrame() {
        Recorder.setupGlobalListeners();

        frame = new JFrame("Example App");
        frame.setName("MainFrame");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);

        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("File");
        JMenuItem openFormItem = new JMenuItem("Open Form");
        openFormItem.addActionListener(e -> openFormFrame());

        menu.add(openFormItem);
        menuBar.add(menu);
        frame.setJMenuBar(menuBar);

        frame.setVisible(true);

    }

    private static void openFormFrame() {
        JFrame formFrame = new JFrame("Form Window");
        formFrame.setName("FormFrame");
        formFrame.setSize(400, 300);
        formFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();

        JLabel label = new JLabel("Name:");
        JTextField textField = new JTextField(20);
        textField.setName("nameInput");

        String[] items = {"Item 1", "Item 2", "Item 3"};
        JComboBox<String> comboBox = new JComboBox<>(items);
        comboBox.setName("comboBox");

        JButton submitButton = new JButton("Submit");
        submitButton.setName("submitButton");
        submitButton.addActionListener(e -> {
            String name = textField.getText();
            String item = (String) comboBox.getSelectedItem();
            JOptionPane.showMessageDialog(formFrame,
                    "Name: " + name + "\nItem: " + item);
        });

        panel.add(label);
        panel.add(textField);
        panel.add(comboBox);
        panel.add(submitButton);

        formFrame.add(panel);
        formFrame.setVisible(true);
    }

    public static void stop() {
        frame.dispose();

    }
}
