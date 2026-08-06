package com.example.ecommerce.order.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Payload for appending an administrative note to an order.
 */
@Schema(description = "Payload for adding an order note")
public class AddOrderNoteRequest {

    @NotBlank(message = "Order note is required")
    @Size(max = 500, message = "Order note must not exceed 500 characters")
    @Schema(description = "Administrative note content", example = "Customer called to verify delivery window.")
    private String note;

    public AddOrderNoteRequest() {
    }

    public AddOrderNoteRequest(String note) {
        this.note = note;
    }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public static AddOrderNoteRequestBuilder builder() { return new AddOrderNoteRequestBuilder(); }

    public static class AddOrderNoteRequestBuilder {
        private String note;

        AddOrderNoteRequestBuilder() {}

        public AddOrderNoteRequestBuilder note(String note) { this.note = note; return this; }

        public AddOrderNoteRequest build() {
            return new AddOrderNoteRequest(note);
        }
    }
}
