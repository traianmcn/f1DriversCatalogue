package com.example.f1driverscatalogue.controller;

import com.example.f1driverscatalogue.constant.Constant;
import com.example.f1driverscatalogue.dto.ContactRequestDTO;
import com.example.f1driverscatalogue.dto.ContactResponseDTO;
import com.example.f1driverscatalogue.service.ContactService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/contacts")
public class ContactController {

    private final ContactService contactService;

    @GetMapping
    public ResponseEntity<Page<ContactResponseDTO>> getAllContacts() {
        var contacts = contactService.getAllContacts(0, 10);
        return new ResponseEntity<>(contacts, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContactResponseDTO> getContactById(@PathVariable(name = "id") String contactId) {
        var response = contactService.getContactById(contactId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ContactResponseDTO> create(@RequestBody @Valid ContactRequestDTO requestDTO) {
        var response = contactService.createContact(requestDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    public ResponseEntity<ContactResponseDTO> updateContact(@PathVariable(name = "id") String contactId, @RequestBody @Valid ContactRequestDTO requestDTO) {
        var response = contactService.updateContact(contactId, requestDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity deleteContact(@PathVariable(name = "id") String id) {
        contactService.deleteContact(id);
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }

    @PutMapping("/photo")
    public ResponseEntity<String> uploadPhoto(@RequestParam(name = "id") String id, @RequestParam(name = "file")MultipartFile file) {
        var response = contactService.uploadPhoto(id, file);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = "/image/{filename}", produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] getPhoto(@PathVariable(name = "filename") String filename) throws IOException {
        return Files. readAllBytes(Paths.get(Constant.PHOTO_DIRECTORY + filename));
    }
}
