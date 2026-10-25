package com.hms.hms_test_2.ai;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/**
 * Controller for the Gemma 4 Clinical & Pharmacy AI Co-Pilot dialog.
 * Connects UI inputs to GemmaAIService and provides clinical decision support.
 */
public class GemmaAICoPilotController implements Initializable {

    @FXML private Label modelBadge;
    @FXML private Button closeButton;
    @FXML private TabPane tabPane;

    // Diagnostic Tab
    @FXML private TextArea symptomsInput;
    @FXML private TextArea patientContextInput;
    @FXML private Button analyzeSymptomsButton;
    @FXML private Button applyDiagnosisButton;
    @FXML private ProgressIndicator diagnosticProgress;
    @FXML private TitledPane thinkingAccordion;
    @FXML private TextArea thinkingOutput;
    @FXML private TextArea diagnosticOutput;

    // Safety Tab
    @FXML private TextArea prescribedDrugsInput;
    @FXML private TextArea safetyAllergiesInput;
    @FXML private Button checkSafetyButton;
    @FXML private ProgressIndicator safetyProgress;
    @FXML private TextArea safetyOutput;

    // Pharmacy Tab
    @FXML private TextField pharmacyDrugInput;
    @FXML private Button findSubstituteButton;
    @FXML private Button counselingButton;
    @FXML private ProgressIndicator pharmacyProgress;
    @FXML private TextArea pharmacyOutput;

    private final GemmaAIService gemmaService = GemmaAIService.getInstance();
    private Consumer<String> diagnosisApplyCallback;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (modelBadge != null) {
            modelBadge.setText("Gemma 4 (" + gemmaService.getModelName() + ")");
        }

        if (closeButton != null) {
            closeButton.setOnAction(e -> closeWindow());
        }

        if (analyzeSymptomsButton != null) {
            analyzeSymptomsButton.setOnAction(e -> handleAnalyzeSymptoms());
        }

        if (applyDiagnosisButton != null) {
            applyDiagnosisButton.setOnAction(e -> handleApplyDiagnosis());
        }

        if (checkSafetyButton != null) {
            checkSafetyButton.setOnAction(e -> handleCheckSafety());
        }

        if (findSubstituteButton != null) {
            findSubstituteButton.setOnAction(e -> handleFindSubstitute());
        }

        if (counselingButton != null) {
            counselingButton.setOnAction(e -> handleGenerateCounseling());
        }
    }

    public void setDiagnosisApplyCallback(Consumer<String> callback) {
        this.diagnosisApplyCallback = callback;
    }

    public void prefillPatientData(String symptoms, String demographicsAndHistory, List<String> currentDrugs, String allergies) {
        if (symptomsInput != null && symptoms != null) {
            symptomsInput.setText(symptoms);
        }
        if (patientContextInput != null && demographicsAndHistory != null) {
            patientContextInput.setText(demographicsAndHistory);
        }
        if (prescribedDrugsInput != null && currentDrugs != null && !currentDrugs.isEmpty()) {
            prescribedDrugsInput.setText(String.join(", ", currentDrugs));
        }
        if (safetyAllergiesInput != null && allergies != null) {
            safetyAllergiesInput.setText(allergies);
        }
        if (pharmacyDrugInput != null && currentDrugs != null && !currentDrugs.isEmpty()) {
            pharmacyDrugInput.setText(currentDrugs.get(0));
        }
    }

    public void selectTab(int tabIndex) {
        if (tabPane != null && tabIndex >= 0 && tabIndex < tabPane.getTabs().size()) {
            tabPane.getSelectionModel().select(tabIndex);
        }
    }

    private void handleAnalyzeSymptoms() {
        String symptoms = symptomsInput != null ? symptomsInput.getText() : "";
        String context = patientContextInput != null ? patientContextInput.getText() : "";

        if (symptoms.trim().isEmpty()) {
            diagnosticOutput.setText("⚠️ Please enter presenting symptoms to analyze.");
            return;
        }

        setBusy(diagnosticProgress, analyzeSymptomsButton, true);
        diagnosticOutput.setText("Analyzing symptoms with Gemma 4 (Thinking mode active)...");

        CompletableFuture.supplyAsync(() -> gemmaService.analyzeSymptoms(symptoms, "", context))
                .thenAccept(response -> Platform.runLater(() -> {
                    setBusy(diagnosticProgress, analyzeSymptomsButton, false);
                    if (response.isSuccess()) {
                        diagnosticOutput.setText(response.getContent());
                        if (!response.getThought().isEmpty()) {
                            thinkingOutput.setText(response.getThought());
                            thinkingAccordion.setExpanded(true);
                        } else {
                            thinkingAccordion.setExpanded(false);
                        }
                    } else {
                        diagnosticOutput.setText("❌ Analysis failed: " + response.getErrorMessage());
                    }
                }))
                .exceptionally(ex -> {
                    Platform.runLater(() -> {
                        setBusy(diagnosticProgress, analyzeSymptomsButton, false);
                        diagnosticOutput.setText("❌ Error during analysis: " + ex.getMessage());
                    });
                    return null;
                });
    }

    private void handleApplyDiagnosis() {
        if (diagnosticOutput != null && !diagnosticOutput.getText().trim().isEmpty() && diagnosisApplyCallback != null) {
            diagnosisApplyCallback.accept(diagnosticOutput.getText().trim());
            closeWindow();
        }
    }

    private void handleCheckSafety() {
        String drugsRaw = prescribedDrugsInput != null ? prescribedDrugsInput.getText() : "";
        String allergies = safetyAllergiesInput != null ? safetyAllergiesInput.getText() : "";

        if (drugsRaw.trim().isEmpty()) {
            safetyOutput.setText("⚠️ Please enter medications to evaluate.");
            return;
        }

        List<String> drugList = Arrays.asList(drugsRaw.split("[,|\\n]+"));

        setBusy(safetyProgress, checkSafetyButton, true);
        safetyOutput.setText("Evaluating prescription safety, drug-drug interactions, and allergy contraindications with Gemma 4...");

        CompletableFuture.supplyAsync(() -> gemmaService.checkPrescriptionSafety(drugList, allergies, ""))
                .thenAccept(response -> Platform.runLater(() -> {
                    setBusy(safetyProgress, checkSafetyButton, false);
                    if (response.isSuccess()) {
                        safetyOutput.setText(response.getContent());
                    } else {
                        safetyOutput.setText("❌ Safety check failed: " + response.getErrorMessage());
                    }
                }))
                .exceptionally(ex -> {
                    Platform.runLater(() -> {
                        setBusy(safetyProgress, checkSafetyButton, false);
                        safetyOutput.setText("❌ Error during safety check: " + ex.getMessage());
                    });
                    return null;
                });
    }

    private void handleFindSubstitute() {
        String drug = pharmacyDrugInput != null ? pharmacyDrugInput.getText().trim() : "";
        if (drug.isEmpty()) {
            pharmacyOutput.setText("⚠️ Please specify an out-of-stock drug name.");
            return;
        }

        setBusy(pharmacyProgress, findSubstituteButton, true);
        pharmacyOutput.setText("Searching bioequivalent generic substitutes with Gemma 4...");

        CompletableFuture.supplyAsync(() -> gemmaService.suggestGenericSubstitutes(drug, ""))
                .thenAccept(response -> Platform.runLater(() -> {
                    setBusy(pharmacyProgress, findSubstituteButton, false);
                    if (response.isSuccess()) {
                        pharmacyOutput.setText(response.getContent());
                    } else {
                        pharmacyOutput.setText("❌ Substitution search failed: " + response.getErrorMessage());
                    }
                }))
                .exceptionally(ex -> {
                    Platform.runLater(() -> {
                        setBusy(pharmacyProgress, findSubstituteButton, false);
                        pharmacyOutput.setText("❌ Error: " + ex.getMessage());
                    });
                    return null;
                });
    }

    private void handleGenerateCounseling() {
        String drug = pharmacyDrugInput != null ? pharmacyDrugInput.getText().trim() : "";
        if (drug.isEmpty()) {
            pharmacyOutput.setText("⚠️ Please specify medications for patient counseling.");
            return;
        }

        List<String> drugs = new ArrayList<>();
        drugs.add(drug);

        setBusy(pharmacyProgress, counselingButton, true);
        pharmacyOutput.setText("Generating patient medication counseling guide with Gemma 4...");

        CompletableFuture.supplyAsync(() -> gemmaService.generatePatientCounseling(drugs))
                .thenAccept(response -> Platform.runLater(() -> {
                    setBusy(pharmacyProgress, counselingButton, false);
                    if (response.isSuccess()) {
                        pharmacyOutput.setText(response.getContent());
                    } else {
                        pharmacyOutput.setText("❌ Counseling generation failed: " + response.getErrorMessage());
                    }
                }))
                .exceptionally(ex -> {
                    Platform.runLater(() -> {
                        setBusy(pharmacyProgress, counselingButton, false);
                        pharmacyOutput.setText("❌ Error: " + ex.getMessage());
                    });
                    return null;
                });
    }

    private void setBusy(ProgressIndicator indicator, Button button, boolean busy) {
        if (indicator != null) indicator.setVisible(busy);
        if (button != null) button.setDisable(busy);
    }

    private void closeWindow() {
        if (closeButton != null && closeButton.getScene() != null && closeButton.getScene().getWindow() != null) {
            ((Stage) closeButton.getScene().getWindow()).close();
        }
    }
}
