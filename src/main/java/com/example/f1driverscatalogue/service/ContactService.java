package com.example.f1driverscatalogue.service;

import com.example.f1driverscatalogue.dto.ContactRequestDTO;
import com.example.f1driverscatalogue.dto.ContactResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

public interface ContactService {
    Page<ContactResponseDTO> getAllContacts(int page, int size);
    ContactResponseDTO getContactById(String id);
    ContactResponseDTO createContact(ContactRequestDTO contactRequestDTO);
    void deleteContact(String id);
    ContactResponseDTO updateContact(String id, ContactRequestDTO contactRequestDTO);
    String uploadPhoto(String id, MultipartFile file);
}
