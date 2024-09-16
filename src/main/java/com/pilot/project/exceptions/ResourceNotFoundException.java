package com.pilot.project.exceptions;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ResourceNotFoundException extends RuntimeException{
    private String resourceName;
    private String fieldName;
    private String field;

    public ResourceNotFoundException(String resourceName, String fieldName, String field){
        super(String.format("%s not found with %s : %s", resourceName, fieldName, field));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.field = field;
    }

}
