package io.github.alexfx1.domain.dto.user;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserSearchDTO {
    private String login;
    private String email;
}
