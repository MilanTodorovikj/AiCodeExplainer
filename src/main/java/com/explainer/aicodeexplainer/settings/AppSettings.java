package com.explainer.aicodeexplainer.settings;

import com.intellij.credentialStore.CredentialAttributes;
import com.intellij.credentialStore.CredentialAttributesKt;
import com.intellij.ide.passwordSafe.PasswordSafe;

public class AppSettings {

    private static final String SERVICE_NAME = "AICodeExplainer";
    private static final String API_KEY_ATTR = "geminiApiKey";

    private AppSettings() {}

    public static String getApiKey() {
        CredentialAttributes attrs = createCredentialAttributes();
        String password = PasswordSafe.getInstance().getPassword(attrs);
        return password != null ? password : "";
    }

    public static void setApiKey(String apiKey) {
        CredentialAttributes attrs = createCredentialAttributes();
        PasswordSafe.getInstance().setPassword(attrs, apiKey);
    }

    private static CredentialAttributes createCredentialAttributes() {
        return new CredentialAttributes(
                CredentialAttributesKt.generateServiceName(SERVICE_NAME, API_KEY_ATTR)
        );
    }
}
