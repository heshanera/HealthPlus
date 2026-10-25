package com.hms.hms_test_2;

import com.hms.hms_test_2.ai.GemmaAIService;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class GemmaAIServiceTest {

    private GemmaAIService service;

    @Before
    public void setUp() {
        service = GemmaAIService.getInstance();
    }

    @Test
    public void testServiceConfiguration() {
        assertNotNull("GemmaAIService instance should not be null", service);
        assertNotNull("Model name should not be null", service.getModelName());
        assertTrue("Model should default to a Gemma model", service.getModelName().contains("gemma"));
        assertEquals("Default thinking level should be high", "high", service.getThinkingLevel());
    }

    @Test
    public void testJsonEscapeAndUnescape() {
        String original = "Doctor's note: \"Patient states:\nFever & cough\\chills.\"";
        String escaped = GemmaAIService.escapeJson(original);
        assertFalse("Escaped string should not contain unescaped quotes", escaped.contains(" \""));
        assertTrue("Escaped string should escape newlines", escaped.contains("\\n"));
        assertTrue("Escaped string should escape backslashes", escaped.contains("\\\\"));

        String unescaped = GemmaAIService.unescapeJson(escaped);
        assertEquals("Unescaped string should match original text", original, unescaped);
    }

    @Test
    public void testRequestBodyConstruction() {
        String prompt = "Patient with high blood pressure and chest tightness";
        String systemInstruction = "You are an expert cardiologist.";
        String body = GemmaAIService.buildRequestBody(prompt, systemInstruction, "high");

        assertNotNull("Request body should not be null", body);
        assertTrue("Body should contain system_instruction", body.contains("\"system_instruction\""));
        assertTrue("Body should contain prompt text", body.contains("Patient with high blood pressure"));
        assertTrue("Body should configure thinking_config", body.contains("\"thinking_config\""));
        assertTrue("Body should specify thinking_level high", body.contains("\"thinking_level\": \"high\""));
    }

    @Test
    public void testParseResponseWithThinking() {
        String sampleJson = "{\n" +
                "  \"candidates\": [\n" +
                "    {\n" +
                "      \"content\": {\n" +
                "        \"parts\": [\n" +
                "          {\n" +
                "            \"text\": \"Step 1: Patient has crushing chest pain. Step 2: Consider myocardial infarction vs angina.\",\n" +
                "            \"thought\": true\n" +
                "          },\n" +
                "          {\n" +
                "            \"text\": \"Primary Differential: Acute Coronary Syndrome. Recommended tests: Complete Blood Count, CPK, Lipid Test.\"\n" +
                "          }\n" +
                "        ],\n" +
                "        \"role\": \"model\"\n" +
                "      }\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        GemmaAIService.GemmaResponse response = GemmaAIService.parseSuccessResponse(sampleJson, "gemma-4-26b-a4b-it");
        assertTrue("Response should be marked successful", response.isSuccess());
        assertTrue("Response should extract thought reasoning", response.getThought().contains("Consider myocardial infarction"));
        assertTrue("Response should extract clinical guidance content", response.getContent().contains("Acute Coronary Syndrome"));
        assertEquals("gemma-4-26b-a4b-it", response.getModel());
    }

    @Test
    public void testParseResponseWithoutThinking() {
        String sampleJson = "{\n" +
                "  \"candidates\": [\n" +
                "    {\n" +
                "      \"content\": {\n" +
                "        \"parts\": [\n" +
                "          {\n" +
                "            \"text\": \"Patient counseling: Take 1 tablet twice daily with meals. Avoid alcohol.\"\n" +
                "          }\n" +
                "        ],\n" +
                "        \"role\": \"model\"\n" +
                "      }\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        GemmaAIService.GemmaResponse response = GemmaAIService.parseSuccessResponse(sampleJson, "gemma-4-26b-a4b-it");
        assertTrue("Response should be marked successful", response.isSuccess());
        assertEquals("Thought should be empty if not returned", "", response.getThought());
        assertTrue("Content should contain counseling", response.getContent().contains("Take 1 tablet twice daily"));
    }

    @Test
    public void testClinicalSymptomAnalysisFallbackGraceful() {
        // Test that symptom analysis executes without throwing exceptions
        GemmaAIService.GemmaResponse res = service.analyzeSymptoms("Severe headache, stiff neck, photophobia", "30M", "No prior history");
        assertNotNull("Response object should not be null", res);
    }

    @Test
    public void testPrescriptionSafetyCheckFallbackGraceful() {
        List<String> drugs = Arrays.asList("Amoxicillin 500mg", "Ibuprofen 400mg");
        GemmaAIService.GemmaResponse res = service.checkPrescriptionSafety(drugs, "Allergic to Penicillin", "Bacterial Pharyngitis");
        assertNotNull("Safety check response should not be null", res);
    }

    @Test
    public void testGenericSubstitutionFallbackGraceful() {
        GemmaAIService.GemmaResponse res = service.suggestGenericSubstitutes("Augmentin 625mg", "Antibiotic");
        assertNotNull("Substitution response should not be null", res);
    }

    @Test
    public void testPatientCounselingFallbackGraceful() {
        List<String> drugs = Arrays.asList("Metformin 500mg", "Atorvastatin 20mg");
        GemmaAIService.GemmaResponse res = service.generatePatientCounseling(drugs);
        assertNotNull("Counseling response should not be null", res);
    }
}
