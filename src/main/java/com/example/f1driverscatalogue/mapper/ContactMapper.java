package com.example.f1driverscatalogue.mapper;

import com.example.f1driverscatalogue.domain.Contact;
import com.example.f1driverscatalogue.dto.ContactRequestDTO;
import com.example.f1driverscatalogue.dto.ContactResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface ContactMapper {

    @Mapping(target = "name", source = "name")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "title", source = "title")
    @Mapping(target = "phone", source = "phone")
    @Mapping(target = "address", source = "address")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "photoUrl", source = "photoUrl")
    Contact contactRequestDTOtoContact(ContactRequestDTO contactRequestDTO);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "title", source = "title")
    @Mapping(target = "phone", source = "phone")
    @Mapping(target = "address", source = "address")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "photoUrl", source = "photoUrl")
    ContactResponseDTO contactToContactResponseDTO(Contact contact);
}
