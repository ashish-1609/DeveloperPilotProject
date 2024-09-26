package com.pilot.project.payloads;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class ApiResponse {
    private String message;

    public ApiResponse(String message) {
        this.message = message;
    }

}
