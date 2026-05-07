package com.explainer.aicodeexplainer.toolwindow;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ExplainerToolWindowPanel extends JPanel {

    private final JTextArea outputArea;
    private final JLabel statusLabel;

    public ExplainerToolWindowPanel() {
        setLayout(new BorderLayout());

        statusLabel = new JLabel("Select code and right-click -> \"Explain with AI\"");
        statusLabel.setBorder(new EmptyBorder(8, 10, 8, 10));
        statusLabel.setFont(statusLabel.getFont().deriveFont(Font.ITALIC));

        outputArea = new JTextArea();
        outputArea.setLineWrap(true);
        outputArea.setWrapStyleWord(true);
        outputArea.setEditable(false);
        outputArea.setFont(new Font("JetBrains Mono", Font.PLAIN, 13));
        outputArea.setBorder(new EmptyBorder(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setBorder(null);

        add(statusLabel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    public void setLoading() {
        statusLabel.setText("Asking Gemini...");
        outputArea.setText("");
    }

    public void setExplanation(String explanation) {
        statusLabel.setText("Done");
        outputArea.setText(explanation);
        outputArea.setCaretPosition(0);
    }

    public void setError(String message) {
        statusLabel.setText("Error");
        outputArea.setText("Error: " + message);
    }
}
