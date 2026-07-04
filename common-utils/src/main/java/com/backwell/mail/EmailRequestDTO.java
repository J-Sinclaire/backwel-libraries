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

    private EmailRequestDTO(Builder builder) {
        this.to = builder.to;
        this.subject = builder.subject;
        this.templateName = builder.templateName;
        this.variables = builder.variables;
    }

    public static Builder builder() {
        return new Builder();
    }

    /* Fluent API Builder*/
    public static class Builder {
        private String to;
        private String subject;
        private String templateName;
        private Map<String, Object> variables;

        public Builder() {}

        public Builder to(String to) {
            this.to = to;
            return this;
        }
        public Builder subject(String subject) {
            this.subject = subject;
            return this;
        }
        public Builder templateName(String templateName) {
            this.templateName = templateName;
            return this;
        }

        public Builder variables(Map<String, Object> variables) {
            this.variables = variables;
            return this;
        }
        public EmailRequestDTO build() {
            return new EmailRequestDTO(this);
        }

    }
}
