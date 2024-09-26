package com.pilot.project.payloads;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String id;
    @NotBlank(message = "User's name cannot be empty.")
    @Size(min = 2, max = 20, message = "User's name should be in range of 2-20 characters.")
    private String name;
    @NotBlank(message = "User's EmailId cannot be empty.")
    @Email(message = "Please provide a valid mail. for example, example@gmail.com")
    private String email;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank(message = "Password Cannot be empty.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$", message = "Password Should Contain minimum eight characters, at least one letter, one number and one special character")
    private String password;
    @Size(min = 0, max = 100, message = "About user must be in range of 0-100 characters.")
    private String about;
}
