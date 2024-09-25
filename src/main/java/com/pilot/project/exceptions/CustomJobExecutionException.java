package com.pilot.project.exceptions;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class CustomJobExecutionException extends RuntimeException {
    public CustomJobExecutionException(String msg) {
        super(msg);
    }
}
