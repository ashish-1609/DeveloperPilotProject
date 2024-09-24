package com.pilot.project.payloads;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JwtAuthRequest {
    private String username;
    private String password;
}
