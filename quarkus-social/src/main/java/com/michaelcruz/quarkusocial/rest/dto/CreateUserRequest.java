package com.michaelcruz.quarkusocial.rest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class CreateUserRequest {

    @NotBlank(message = "Name Is Required")
    private String name;

    @NotNull(message = "Age Is Required")
    private Integer age;
}
