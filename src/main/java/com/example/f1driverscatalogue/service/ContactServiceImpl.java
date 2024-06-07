package com.example.f1driverscatalogue.service;

import com.example.f1driverscatalogue.constant.Constant;
import com.example.f1driverscatalogue.dto.ContactRequestDTO;
import com.example.f1driverscatalogue.dto.ContactResponseDTO;
import com.example.f1driverscatalogue.exception.ContactNotFoundException;
import com.example.f1driverscatalogue.mapper.ContactMapper;
import com.example.f1driverscatalogue.repository.ContactRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@RequiredArgsConstructor
@Service
@Slf4j
public class ContactServiceImpl implements ContactService{
    private final ContactRepository contactRepository;
    private final ContactMapper mapper;

    @Override
    public Page<ContactResponseDTO> getAllContacts(int page, int size) {
        return contactRepository.findAll(PageRequest.of(page, size, Sort.by("name")))
                .map(mapper::contactToContactResponseDTO);
    }

    @Override
    public ContactResponseDTO getContactById(String id) {
        var contact =  contactRepository.findById(id)
                .orElseThrow(() -> new ContactNotFoundException("Contact not found"));
        return mapper.contactToContactResponseDTO(contact);
    }

    @Override
    public ContactResponseDTO createContact(ContactRequestDTO contactRequestDTO) {
        var contact = mapper.contactRequestDTOtoContact(contactRequestDTO);
        contactRepository.save(contact);
        return mapper.contactToContactResponseDTO(contact);
    }

    @Override
    public void deleteContact(String id) {
        var contact = contactRepository.findById(id)
                .orElseThrow(() -> new ContactNotFoundException(String.format("Contact with id %s does not exist", id)));
        contactRepository.deleteById(contact.getId());
    }

    @Override
    public ContactResponseDTO updateContact(String id, ContactRequestDTO contactRequestDTO) {
        contactRepository.findById(id)
                 .orElseThrow(() -> new ContactNotFoundException(String.format("Contact with id %s does not exist", id)));
        var contact = mapper.contactRequestDTOtoContact(contactRequestDTO);
        contact.setId(id);
        contactRepository.save(contact);
        return mapper.contactToContactResponseDTO(contact);
    }

    @Override
    public String uploadPhoto(String id, MultipartFile file) {
        log.info("Saving picture for user with ID {}", id);
        var contact = contactRepository.findById(id)
                .orElseThrow(() -> new ContactNotFoundException(String.format("Contact with id %s does not exist", id)));
        String photoUrl = photoFunction.apply(id, file);
        contact.setPhotoUrl(photoUrl);
        contactRepository.save(contact);
//        contactRepository.deleteById(id);
        return photoUrl;
    }

    private final Function<String, String> fileExtension =  filename -> Optional.of(filename).filter(name -> name.contains("."))
            .map(name -> "." + name.substring(filename.lastIndexOf(".") + 1)).orElse(".png");

    private final BiFunction<String, MultipartFile, String> photoFunction =  (id, image) -> {
        try {
            Path fileStorageLocation = Paths.get(Constant.PHOTO_DIRECTORY).toAbsolutePath().normalize();
            if (!Files.exists(fileStorageLocation)) {
                Files.createDirectories(fileStorageLocation);
            }
            Files.copy(image.getInputStream(), fileStorageLocation.resolve(id + fileExtension.apply(image.getOriginalFilename())), REPLACE_EXISTING);
            return ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/api/v1/contacts/image/" + id + fileExtension.apply(image.getOriginalFilename())).toUriString();
        } catch (Exception e) {
            throw new RuntimeException("Unable to save image");
        }
    };

}
