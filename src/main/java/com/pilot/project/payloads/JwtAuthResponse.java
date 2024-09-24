package com.pilot.project.payloads;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JwtAuthResponse {
    private String token;
}
