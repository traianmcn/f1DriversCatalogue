package com.example.f1driverscatalogue.dto;

import com.example.f1driverscatalogue.domain.Address;
import com.example.f1driverscatalogue.domain.Status;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ContactRequestDTO(@JsonProperty("email") @JsonAlias("e-mail")String email, @NotBlank (message = "Enter a valid name") String name, String title, String phone, Address address, @NotNull(message = "Status is a mandatory field") Status status, String photoUrl) {
}
