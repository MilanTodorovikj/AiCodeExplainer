# AI Code Explainer — IntelliJ Plugin

An IntelliJ IDEA plugin that explains selected code using Google's Gemini AI.
Select any code snippet, right-click, and get a plain-English explanation in a side panel.

## What it does

- Adds an "Explain with AI" option to the editor right-click context menu
- Sends the selected code to the Gemini API
- Displays a clean, plain-English explanation in a tool window panel on the right side
- Stores your API key securely using IntelliJ's built-in PasswordSafe

## Tech stack

- Java 17+
- IntelliJ Platform SDK (Plugin DevKit)
- Gradle build system
- Google Gemini API (gemma-4-31b-it model)
- Gson for JSON parsing

## Project structure

```
ai-code-explainer/
├── build.gradle.kts                             # Build configuration
├── gradle.properties                            # Plugin metadata and platform version
├── settings.gradle.kts                          # Gradle settings
└── src/main/
    ├── java/com/explainer/aicodeexplainer/
    │   ├── actions/
    │   │   └── ExplainCodeAction.java           # Right-click action handler
    │   ├── api/
    │   │   └── GeminiApiClient.java             # Gemini API HTTP client
    │   ├── settings/
    │   │   ├── AppSettings.java                 # Secure API key storage
    │   │   └── AppSettingsConfigurable.java     # Settings UI page
    │   └── toolwindow/
    │       ├── ExplainerToolWindowFactory.java  # Tool window registration
    │       └── ExplainerToolWindowPanel.java    # Side panel UI
    └── resources/META-INF/
        └── plugin.xml                           # Plugin manifest
```

## Getting started

### Prerequisites

- IntelliJ IDEA (Ultimate or Community) with the **Plugin DevKit** plugin installed
- JDK 17 or higher
- A free Google Gemini API key from [aistudio.google.com](https://aistudio.google.com)

### 1. Clone or open the project

Open the project in IntelliJ IDEA. Gradle will sync automatically.

### 2. Run the plugin

In the Gradle panel on the right side:

```
Tasks -> intellij -> runIde
```

This opens a new IntelliJ window with the plugin loaded.

### 3. Set your API key

In the new window go to:

```
File -> Settings -> Tools -> AI Code Explainer
```

Paste your Gemini API key and click **OK**.

### 4. Use the plugin

1. Open any code file
2. Select a block of code
3. Right-click -> **"Explain with AI"**
4. The explanation appears in the **AI Explainer** panel on the right

You can also use the keyboard shortcut **Ctrl+Alt+E** instead of right-clicking.

## Getting a free API key

1. Go to [aistudio.google.com](https://aistudio.google.com)
2. Sign in with your Google account
3. Click **"Get API key"** in the left sidebar
4. Click **"Create API key"**
5. Copy the key and paste it in the plugin settings

The free tier provides 1,500 requests per day which is more than enough for development and testing.

## Notes

- The plugin only shows the "Explain with AI" option when code is selected
- API calls run on a background thread so the IDE never freezes
- The API key is stored securely using IntelliJ's PasswordSafe, not in plain text