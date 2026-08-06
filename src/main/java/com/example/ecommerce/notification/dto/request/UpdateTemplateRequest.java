package com.example.ecommerce.notification.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Payload for updating notification template contents.
 */
@Schema(description = "Payload for updating a notification template")
public class UpdateTemplateRequest {

    @Size(max = 200, message = "Subject must not exceed 200 characters")
    @Schema(description = "Updated subject line pattern", example = "Order {{orderNumber}} Confirmed - Thank You!")
    private String subject;

    @NotBlank(message = "Body template is required")
    @Schema(description = "Updated body template text", example = "Dear {{userName}}, thank you for your order {{orderNumber}}.")
    private String bodyTemplate;

    @Schema(description = "Active status flag", example = "true")
    private Boolean active;

    public UpdateTemplateRequest() {
    }

    public UpdateTemplateRequest(String subject, String bodyTemplate, Boolean active) {
        this.subject = subject;
        this.bodyTemplate = bodyTemplate;
        this.active = active;
    }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getBodyTemplate() { return bodyTemplate; }
    public void setBodyTemplate(String bodyTemplate) { this.bodyTemplate = bodyTemplate; }

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }

    public static UpdateTemplateRequestBuilder builder() { return new UpdateTemplateRequestBuilder(); }

    public static class UpdateTemplateRequestBuilder {
        private String subject;
        private String bodyTemplate;
        private Boolean active;

        UpdateTemplateRequestBuilder() {}

        public UpdateTemplateRequestBuilder subject(String subject) { this.subject = subject; return this; }
        public UpdateTemplateRequestBuilder bodyTemplate(String bodyTemplate) { this.bodyTemplate = bodyTemplate; return this; }
        public UpdateTemplateRequestBuilder active(Boolean active) { this.active = active; return this; }

        public UpdateTemplateRequest build() {
            return new UpdateTemplateRequest(subject, bodyTemplate, active);
        }
    }
}
