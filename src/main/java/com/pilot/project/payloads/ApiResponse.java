package com.pilot.project.payloads;

import lombok.*;

@Data
@EqualsAndHashCode
public class ApiResponse {
    private String message;

    public ApiResponse(String message) {
        this.message = message;
    }

}
