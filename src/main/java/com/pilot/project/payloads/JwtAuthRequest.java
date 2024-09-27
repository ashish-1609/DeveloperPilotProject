package com.pilot.project.payloads;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtAuthRequest {
    private String username;
    private String password;
}
