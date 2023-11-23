package com.example.demoOTP.Service;

import java.util.Map;

public class EmailTemplate {

    private String template;

    // Constructor for providing template content directly
    public EmailTemplate(String templateContent) {
        this.template = templateContent;
    }

    // ... (các phương thức khác)

    public String getTemplate(Map<String, String> replacements) {
        String cTemplate = this.template;
        // Replace the String
        for (Map.Entry<String, String> entry : replacements.entrySet()) {
            cTemplate = cTemplate.replace("{{" + entry.getKey() + "}}", entry.getValue());
        }
        return cTemplate;
    }
}

