package com.hms.hms_test_2.ai;

import com.hms.hms_test_2.SystemConfiguration;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * GemmaAIService integrates Google's open Gemma 4 models (e.g. gemma-4-26b-a4b-it,
 * gemma-4-31b-it) into HealthPlus using the Gemini API.
 *
 * Provides Clinical Decision Support:
 * - Symptom Analysis & Differential Diagnosis (with multi-step Thinking reasoning)
 * - Drug-Drug Interaction & Allergy Cross-Reactivity Safety Checks
 * - Generic Drug Substitution & Patient Counseling for Pharmacists
 * - Clinical Visit Summarization
 *
 * Built for the MLH Hacktoberfest "Build with Gemma" track.
 */
public class GemmaAIService {

    private static GemmaAIService instance;

    private String apiKey;
    private String modelName;
    private String thinkingLevel;
    private boolean enabled;

    private static final String DEFAULT_MODEL = "gemma-4-26b-a4b-it";
    private static final String BASE_URL = "https://generativelanguage.googleapis.com/v1beta/models/";

    private final HttpClient httpClient;

    private GemmaAIService() {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(15))
                .build();
        reloadConfiguration();
    }

    public static synchronized GemmaAIService getInstance() {
        if (instance == null) {
            instance = new GemmaAIService();
        }
        return instance;
    }

    public synchronized void reloadConfiguration() {
        SystemConfiguration config = SystemConfiguration.getInstance();
        this.apiKey = config.getConfig("gemma.api.key", "");
        if (this.apiKey.isEmpty()) {
            String envKey = System.getenv("GEMINI_API_KEY");
            if (envKey == null || envKey.isEmpty()) {
                envKey = System.getenv("GEMMA_API_KEY");
            }
            if (envKey != null) {
                this.apiKey = envKey;
            }
        }
        this.modelName = config.getConfig("gemma.model", DEFAULT_MODEL);
        this.thinkingLevel = config.getConfig("gemma.thinking.level", "high");
        String enabledStr = config.getConfig("gemma.enabled", "true");
        this.enabled = "true".equalsIgnoreCase(enabledStr);
    }

    public boolean isConfigured() {
        return enabled && apiKey != null && !apiKey.trim().isEmpty();
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getThinkingLevel() {
        return thinkingLevel;
    }

    public void setThinkingLevel(String thinkingLevel) {
        this.thinkingLevel = thinkingLevel;
    }

    /**
     * Data class holding Gemma 4's response, including reasoning thoughts and final content.
     */
    public static class GemmaResponse {
        private final boolean success;
        private final String content;
        private final String thought;
        private final String model;
        private final String errorMessage;

        public GemmaResponse(boolean success, String content, String thought, String model, String errorMessage) {
            this.success = success;
            this.content = content != null ? content : "";
            this.thought = thought != null ? thought : "";
            this.model = model != null ? model : "";
            this.errorMessage = errorMessage != null ? errorMessage : "";
        }

        public boolean isSuccess() {
            return success;
        }

        public String getContent() {
            return content;
        }

        public String getThought() {
            return thought;
        }

        public String getModel() {
            return model;
        }

        public String getErrorMessage() {
            return errorMessage;
        }

        @Override
        public String toString() {
            if (!success) {
                return "Error: " + errorMessage;
            }
            return content;
        }
    }

    /**
     * Core method to generate content using Gemma 4 with thinking configuration.
     *
     * @param prompt The user prompt
     * @param systemInstruction Optional system instruction
     * @return GemmaResponse containing generated content, thinking process, and metadata
     */
    public GemmaResponse generateContent(String prompt, String systemInstruction) {
        if (!isConfigured()) {
            return new GemmaResponse(false, "", "", modelName,
                    "Gemma AI is not configured. Please supply a valid API key in config.properties or GEMINI_API_KEY.");
        }

        String endpoint = BASE_URL + modelName + ":generateContent?key=" + apiKey;

        String requestBody = buildRequestBody(prompt, systemInstruction, thinkingLevel);

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(endpoint))
                    .header("Content-Type", "application/json")
                    .timeout(Duration.ofSeconds(30))
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                return parseSuccessResponse(response.body(), modelName);
            } else {
                String errorMsg = extractErrorMessage(response.body());
                if (errorMsg.isEmpty()) {
                    errorMsg = "HTTP error " + response.statusCode();
                }
                return new GemmaResponse(false, "", "", modelName, errorMsg);
            }

        } catch (IOException | InterruptedException e) {
            return new GemmaResponse(false, "", "", modelName,
                    "Connection error to Gemma API: " + e.getMessage());
        }
    }

    /**
     * Analyzes patient symptoms and suggests differential diagnosis, recommended lab tests,
     * and critical warnings using Gemma 4's multi-step thinking reasoning.
     */
    public GemmaResponse analyzeSymptoms(String symptoms, String patientAgeGender, String medicalHistory) {
        String systemInstruction = "You are a clinical decision support AI assistant powered by Gemma 4 for HealthPlus Hospital System. "
                + "Provide evidence-based clinical reasoning. Structure your response into: "
                + "1) Primary Differential Diagnoses (ranked with likelihood), "
                + "2) Recommended Diagnostic Tests (reference tests available in HealthPlus: Complete Blood Count, Urine Full Report, Lipid Test, Liver Function Test, Renal Function Test, Blood Grouping, CPK), "
                + "3) Red-Flag / Critical Warnings, "
                + "4) Suggested Initial Management. "
                + "Always clarify that recommendations are clinical decision support for certified medical practitioners.";

        StringBuilder prompt = new StringBuilder();
        prompt.append("Patient Assessment:\n");
        if (patientAgeGender != null && !patientAgeGender.trim().isEmpty()) {
            prompt.append("- Demographics: ").append(patientAgeGender.trim()).append("\n");
        }
        if (medicalHistory != null && !medicalHistory.trim().isEmpty()) {
            prompt.append("- Known Medical History / Allergies: ").append(medicalHistory.trim()).append("\n");
        }
        prompt.append("- Presenting Symptoms: ").append(symptoms != null ? symptoms.trim() : "None stated").append("\n\n");
        prompt.append("Please perform deep clinical reasoning to analyze this case and provide diagnostic guidance.");

        return generateContent(prompt.toString(), systemInstruction);
    }

    /**
     * Verifies prescription safety: checks for drug-drug interactions, allergy contraindications,
     * and dosage warnings before saving the prescription.
     */
    public GemmaResponse checkPrescriptionSafety(List<String> prescribedDrugs, String patientAllergies, String currentDiagnosis) {
        String systemInstruction = "You are a clinical pharmacologist AI assistant powered by Gemma 4 for HealthPlus Hospital System. "
                + "Analyze prescribed medications for potential safety hazards. Evaluate: "
                + "1) Allergy Cross-Reactivity (CRITICAL: flag if patient is allergic to penicillin, sulfonamides, NSAIDs, etc. and a related drug is prescribed), "
                + "2) Drug-Drug Interactions (major, moderate, minor), "
                + "3) Contraindications against Current Diagnosis, "
                + "4) Safety Recommendation (PASS, CAUTION, or ALERT - DO NOT ADMINISTER).";

        StringBuilder prompt = new StringBuilder();
        prompt.append("Prescription Safety Evaluation Request:\n");
        if (currentDiagnosis != null && !currentDiagnosis.trim().isEmpty()) {
            prompt.append("- Current Diagnosis: ").append(currentDiagnosis.trim()).append("\n");
        }
        if (patientAllergies != null && !patientAllergies.trim().isEmpty()) {
            prompt.append("- Documented Patient Allergies / Medical History: ").append(patientAllergies.trim()).append("\n");
        } else {
            prompt.append("- Documented Patient Allergies: None recorded\n");
        }
        prompt.append("- Prescribed Medications:\n");
        if (prescribedDrugs != null && !prescribedDrugs.isEmpty()) {
            for (String drug : prescribedDrugs) {
                prompt.append("  * ").append(drug).append("\n");
            }
        } else {
            prompt.append("  * None\n");
        }
        prompt.append("\nEvaluate potential adverse interactions or allergy hazards immediately.");

        return generateContent(prompt.toString(), systemInstruction);
    }

    /**
     * Suggests generic bioequivalent alternatives and therapeutic substitutes when a prescribed
     * drug is out of stock in the pharmacy.
     */
    public GemmaResponse suggestGenericSubstitutes(String unavailableDrug, String therapeuticCategory) {
        String systemInstruction = "You are an expert Hospital Pharmacist AI assistant powered by Gemma 4. "
                + "Provide clinically sound generic substitutes and therapeutic alternatives when a drug is out of stock. "
                + "Include generic active ingredients, standard dosing equivalents, and precautions when switching.";

        StringBuilder prompt = new StringBuilder();
        prompt.append("Pharmacy Stock Out - Substitution Request:\n");
        prompt.append("- Out of Stock Drug: ").append(unavailableDrug != null ? unavailableDrug : "Unknown").append("\n");
        if (therapeuticCategory != null && !therapeuticCategory.trim().isEmpty()) {
            prompt.append("- Clinical Category / Indication: ").append(therapeuticCategory.trim()).append("\n");
        }
        prompt.append("\nPlease recommend 2-3 suitable bioequivalent or therapeutic alternatives with dosing guidelines.");

        return generateContent(prompt.toString(), systemInstruction);
    }

    /**
     * Generates clear patient medication counseling instructions for pharmacy dispensing.
     */
    public GemmaResponse generatePatientCounseling(List<String> drugs) {
        String systemInstruction = "You are a hospital pharmacy patient counselor AI assistant powered by Gemma 4. "
                + "Explain to the patient how to safely take their medications in clear, empathetic, non-technical language. "
                + "Include timing (with food vs empty stomach), common side effects, and foods/substances to avoid.";

        StringBuilder prompt = new StringBuilder();
        prompt.append("Patient Medication Counseling Guide Request:\nMedications:\n");
        if (drugs != null) {
            for (String d : drugs) {
                prompt.append("- ").append(d).append("\n");
            }
        }
        prompt.append("\nGenerate simple, numbered patient instructions for everyday use.");

        return generateContent(prompt.toString(), systemInstruction);
    }

    /**
     * Summarizes the entire clinical consultation into a structured clinical note.
     */
    public GemmaResponse summarizeClinicalVisit(String patientId, String diagnosis, List<String> drugs, List<String> tests) {
        String systemInstruction = "You are a Medical Documentation AI assistant powered by Gemma 4 for HealthPlus. "
                + "Generate a standard SOAP (Subjective, Objective, Assessment, Plan) clinical visit summary.";

        StringBuilder prompt = new StringBuilder();
        prompt.append("Patient ID: ").append(patientId != null ? patientId : "N/A").append("\n");
        prompt.append("Diagnosis: ").append(diagnosis != null ? diagnosis : "N/A").append("\n");
        prompt.append("Prescribed Medications: ").append(drugs != null ? String.join(", ", drugs) : "None").append("\n");
        prompt.append("Ordered Lab Tests: ").append(tests != null ? String.join(", ", tests) : "None").append("\n");
        prompt.append("\nGenerate a concise professional clinical chart summary.");

        return generateContent(prompt.toString(), systemInstruction);
    }

    // JSON Helper Methods (Zero External Dependency)

    public static String buildRequestBody(String prompt, String systemInstruction, String thinkingLevel) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");

        if (systemInstruction != null && !systemInstruction.trim().isEmpty()) {
            sb.append("\"system_instruction\": {");
            sb.append("\"parts\": [{\"text\": \"").append(escapeJson(systemInstruction)).append("\"}]");
            sb.append("},");
        }

        sb.append("\"contents\": [{");
        sb.append("\"parts\": [{\"text\": \"").append(escapeJson(prompt)).append("\"}]");
        sb.append("}],");

        sb.append("\"generationConfig\": {");
        sb.append("\"temperature\": 0.4,");
        sb.append("\"thinking_config\": {");
        String level = ("minimal".equalsIgnoreCase(thinkingLevel)) ? "minimal" : "high";
        sb.append("\"thinking_level\": \"").append(level).append("\"");
        sb.append("}");
        sb.append("}");

        sb.append("}");
        return sb.toString();
    }

    public static GemmaResponse parseSuccessResponse(String json, String model) {
        if (json == null || json.isEmpty()) {
            return new GemmaResponse(false, "", "", model, "Empty response from Gemma API");
        }

        String thoughtText = "";
        String contentText = "";

        // Extract candidates parts
        // Look for parts with "thought": true
        Pattern thoughtPattern = Pattern.compile(
                "\\{\\s*\"text\"\\s*:\\s*\"(.*?)\"\\s*,\\s*\"thought\"\\s*:\\s*true\\s*\\}",
                Pattern.DOTALL
        );
        Matcher thoughtMatcher = thoughtPattern.matcher(json);
        if (thoughtMatcher.find()) {
            thoughtText = unescapeJson(thoughtMatcher.group(1));
        }

        // Look for main text parts
        Pattern textPattern = Pattern.compile(
                "\"parts\"\\s*:\\s*\\[(.*?)\\]",
                Pattern.DOTALL
        );
        Matcher textMatcher = textPattern.matcher(json);
        while (textMatcher.find()) {
            String partsBlock = textMatcher.group(1);
            Pattern partTextPattern = Pattern.compile(
                    "\\{\\s*\"text\"\\s*:\\s*\"((?:[^\"\\\\]|\\\\.)*)\"(?!.*\"thought\"\\s*:\\s*true)",
                    Pattern.DOTALL
            );
            Matcher partTextMatcher = partTextPattern.matcher(partsBlock);
            while (partTextMatcher.find()) {
                String candidateText = unescapeJson(partTextMatcher.group(1));
                if (!candidateText.equals(thoughtText)) {
                    if (!contentText.isEmpty()) {
                        contentText += "\n";
                    }
                    contentText += candidateText;
                }
            }
        }

        if (contentText.isEmpty()) {
            // Fallback general text search
            Pattern generalText = Pattern.compile("\"text\"\\s*:\\s*\"((?:[^\"\\\\]|\\\\.)*)\"");
            Matcher m = generalText.matcher(json);
            while (m.find()) {
                String extracted = unescapeJson(m.group(1));
                if (!extracted.equals(thoughtText)) {
                    contentText = extracted;
                    break;
                }
            }
        }

        if (contentText.isEmpty() && thoughtText.isEmpty()) {
            return new GemmaResponse(false, "", "", model, "No text candidates generated");
        }

        return new GemmaResponse(true, contentText, thoughtText, model, "");
    }

    private static String extractErrorMessage(String json) {
        if (json == null) return "";
        Pattern p = Pattern.compile("\"message\"\\s*:\\s*\"((?:[^\"\\\\]|\\\\.)*)\"");
        Matcher m = p.matcher(json);
        if (m.find()) {
            return unescapeJson(m.group(1));
        }
        return "";
    }

    public static String escapeJson(String raw) {
        if (raw == null) return "";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < raw.length(); i++) {
            char c = raw.charAt(i);
            switch (c) {
                case '"':
                    sb.append("\\\"");
                    break;
                case '\\':
                    sb.append("\\\\");
                    break;
                case '\b':
                    sb.append("\\b");
                    break;
                case '\f':
                    sb.append("\\f");
                    break;
                case '\n':
                    sb.append("\\n");
                    break;
                case '\r':
                    sb.append("\\r");
                    break;
                case '\t':
                    sb.append("\\t");
                    break;
                default:
                    if (c < ' ') {
                        String t = "000" + Integer.toHexString(c);
                        sb.append("\\u").append(t.substring(t.length() - 4));
                    } else {
                        sb.append(c);
                    }
            }
        }
        return sb.toString();
    }

    public static String unescapeJson(String input) {
        if (input == null) return "";
        StringBuilder sb = new StringBuilder();
        int len = input.length();
        for (int i = 0; i < len; i++) {
            char c = input.charAt(i);
            if (c == '\\' && i + 1 < len) {
                char next = input.charAt(i + 1);
                switch (next) {
                    case '"':
                        sb.append('"');
                        i++;
                        break;
                    case '\\':
                        sb.append('\\');
                        i++;
                        break;
                    case 'n':
                        sb.append('\n');
                        i++;
                        break;
                    case 'r':
                        sb.append('\r');
                        i++;
                        break;
                    case 't':
                        sb.append('\t');
                        i++;
                        break;
                    case 'b':
                        sb.append('\b');
                        i++;
                        break;
                    case 'f':
                        sb.append('\f');
                        i++;
                        break;
                    case 'u':
                        if (i + 5 < len) {
                            String hex = input.substring(i + 2, i + 6);
                            try {
                                sb.append((char) Integer.parseInt(hex, 16));
                                i += 5;
                            } catch (NumberFormatException e) {
                                sb.append(c);
                            }
                        } else {
                            sb.append(c);
                        }
                        break;
                    default:
                        sb.append(next);
                        i++;
                        break;
                }
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}
