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
}
