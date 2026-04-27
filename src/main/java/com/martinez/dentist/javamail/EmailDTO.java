package com.martinez.dentist.javamail;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EmailDTO {
    private String recipient;
    private String subject;
    private String body;

    public EmailDTO(String recipient, String subject, String body) {
        this.recipient = recipient;
        this.subject = subject;
        this.body = body;
    }

    public EmailDTO() {}

    @Override
    public String toString() {
        return "MailDTO{" +
                "destinatario='" + recipient + '\'' +
                '}';
    }
}

