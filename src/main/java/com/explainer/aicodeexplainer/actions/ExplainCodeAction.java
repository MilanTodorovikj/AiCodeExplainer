package com.explainer.aicodeexplainer.actions;

import com.explainer.aicodeexplainer.api.GeminiApiClient;
import com.explainer.aicodeexplainer.settings.AppSettings;
import com.explainer.aicodeexplainer.toolwindow.ExplainerToolWindowFactory;
import com.explainer.aicodeexplainer.toolwindow.ExplainerToolWindowPanel;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.content.Content;
import org.jetbrains.annotations.NotNull;

public class ExplainCodeAction extends AnAction {

    private final GeminiApiClient apiClient = new GeminiApiClient();

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        Project project = event.getProject();
        Editor editor = event.getData(CommonDataKeys.EDITOR);

        if (project == null || editor == null) return;

        SelectionModel selectionModel = editor.getSelectionModel();
        String selectedCode = selectionModel.getSelectedText();

        if (selectedCode == null || selectedCode.isBlank()) {
            Messages.showInfoMessage(project, "Please select some code.", "No code is selected");
            return;
        }

        String apiKey = AppSettings.getApiKey();
        if (apiKey.isBlank()) {
            Messages.showWarningDialog(
                    project,
                    "Please set your Gemini API key in File -> Settings -> Tools -> AI Code Explainer",
                    "API Key Missing"
            );
            return;
        }

        ToolWindow toolWindow = ToolWindowManager.getInstance(project)
                .getToolWindow(ExplainerToolWindowFactory.TOOL_WINDOW_ID);

        if (toolWindow == null) return;
        toolWindow.show();

        Content content = toolWindow.getContentManager().getContent(0);
        if (content == null) return;

        ExplainerToolWindowPanel panel = (ExplainerToolWindowPanel) content.getComponent();
        panel.setLoading();

        String codeToExplain = selectedCode;
        ApplicationManager.getApplication().executeOnPooledThread(() -> {
            try {
                String explanation = apiClient.explainCode(codeToExplain, apiKey);

                ApplicationManager.getApplication().invokeLater(() ->
                        panel.setExplanation(explanation)
                );
            } catch (Exception e) {
                ApplicationManager.getApplication().invokeLater(() ->
                        panel.setError(e.getMessage())
                );
            }
        });
    }

    @Override
    public void update(@NotNull AnActionEvent event) {
        Editor editor = event.getData(CommonDataKeys.EDITOR);
        boolean hasSelection = editor != null &&
                editor.getSelectionModel().hasSelection();
        event.getPresentation().setEnabledAndVisible(hasSelection);
    }
}
