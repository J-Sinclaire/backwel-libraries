package com.backwell.mail;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.Map;

public class EmailRequestDTO implements Serializable {
    @NotBlank
    private String to;

    @NotBlank
    private String subject;

    @NotBlank
    private String templateName;

    @NotNull
    private Map<String, Object> variables;

    public EmailRequestDTO() {}

    public EmailRequestDTO(String to, String subject, String templateName, Map<String, Object> variables) {
        this.to = to;
        this.subject = subject;
        this.templateName = templateName;
        this.variables = variables;
    }

    public String getTo() {
        return to;
    }

    public String getSubject() {
        return subject;
    }

    public String getTemplateName() {
        return templateName;
    }

    public Map<String, Object> getVariables() {
        return variables;
    }
}
