package com.example.f1driverscatalogue.dto;

import com.example.f1driverscatalogue.domain.Address;
import com.example.f1driverscatalogue.domain.Status;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public record ContactResponseDTO(String id, String name, String email, String title, String phone, Address address, Status status, String photoUrl) {
}
