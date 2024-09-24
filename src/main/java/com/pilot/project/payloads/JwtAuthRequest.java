package com.pilot.project.payloads;

import lombok.*;
import org.hibernate.query.sql.internal.ParameterRecognizerImpl;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JwtAuthRequest {
    private String username;
    private String password;
}
