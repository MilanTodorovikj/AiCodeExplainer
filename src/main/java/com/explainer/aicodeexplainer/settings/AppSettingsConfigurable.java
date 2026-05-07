package com.explainer.aicodeexplainer.settings;

import com.intellij.openapi.options.Configurable;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class AppSettingsConfigurable implements Configurable {

    private JPasswordField apiKeyField;

    @Nls
    @Override
    public String getDisplayName() {
        return "AI Code Explainer";
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(12, 12, 12, 12));

        GridBagConstraints labelConstraints = new GridBagConstraints();
        labelConstraints.gridx = 0;
        labelConstraints.gridy = 0;
        labelConstraints.anchor = GridBagConstraints.WEST;
        labelConstraints.insets = new Insets(0, 0, 0, 8);

        GridBagConstraints fieldConstraints = new GridBagConstraints();
        fieldConstraints.gridx = 1;
        fieldConstraints.gridy = 0;
        fieldConstraints.fill = GridBagConstraints.HORIZONTAL;
        fieldConstraints.weightx = 1.0;

        panel.add(new JLabel("Gemini API Key:"), labelConstraints);

        apiKeyField = new JPasswordField(40);
        apiKeyField.setText(AppSettings.getApiKey());
        panel.add(apiKeyField, fieldConstraints);

        GridBagConstraints fillerConstraints = new GridBagConstraints();
        fillerConstraints.gridx = 0;
        fillerConstraints.gridy = 1;
        fillerConstraints.gridwidth = 2;
        fillerConstraints.weighty = 1.0;
        fillerConstraints.fill = GridBagConstraints.VERTICAL;
        panel.add(new JPanel(), fillerConstraints);

        return panel;
    }

    @Override
    public boolean isModified() {
        return !new String(apiKeyField.getPassword()).equals(AppSettings.getApiKey());
    }

    @Override
    public void apply() {
        AppSettings.setApiKey(new String(apiKeyField.getPassword()));
    }

    @Override
    public void reset() {
        apiKeyField.setText(AppSettings.getApiKey());
    }
}