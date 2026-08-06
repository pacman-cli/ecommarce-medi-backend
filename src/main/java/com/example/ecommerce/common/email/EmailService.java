package com.example.ecommerce.common.email;

/**
 * Contract for sending application emails.
 */
public interface EmailService {

    /**
     * Sends a verification code to the given recipient.
     *
     * <p>Delivery is best-effort: when emailing is disabled (e.g. via the
     * {@code app.mail.enabled=false} flag in development) the code is written to
     * the application log instead, so the registration flows never depend on an
     * SMTP server being reachable.</p>
     *
     * @param to      the recipient address
     * @param code    the one-time code to deliver
     * @param purpose the reason for sending
     */
    void sendVerificationCode(String to, String code, VerificationPurpose purpose);
}