package io.github.alexfx1.domain.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class EmailDTO {
    private String toEmail;
    private String fromEmail;
    private String password;
    private String subject;
    private String body;
    private boolean sent;
    private String msgError;
}
