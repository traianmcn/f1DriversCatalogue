package com.example.f1driverscatalogue.service;

import com.example.f1driverscatalogue.domain.Address;
import com.example.f1driverscatalogue.domain.Contact;
import com.example.f1driverscatalogue.domain.Status;
import com.example.f1driverscatalogue.dto.ContactRequestDTO;
import com.example.f1driverscatalogue.dto.ContactResponseDTO;
import com.example.f1driverscatalogue.exception.ContactNotFoundException;
import com.example.f1driverscatalogue.mapper.ContactMapper;
import com.example.f1driverscatalogue.repository.ContactRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContactServiceImplTest {

    @Mock
    private ContactRepository contactRepository;
    @Mock
    private ContactMapper mapper;
    @InjectMocks
    private ContactServiceImpl underTest;

    @Test
    void getAllContacts() {
        ContactResponseDTO contactResponseDTO = new ContactResponseDTO("1", "name", "email", "title", "phoneNumber", new Address(), Status.ACTIVE, "photoUrl");
        Contact contact = new Contact();
        Page<Contact> contactPage = new PageImpl<>(Arrays.asList(contact));

        when(contactRepository.findAll(PageRequest.of(0, 10, Sort.by("name")))).thenReturn(contactPage);
        when(mapper.contactToContactResponseDTO(contact)).thenReturn(contactResponseDTO);

        var response = underTest.getAllContacts(0, 10);

        assertThat(response.getSize(), is(1));
        assertThat(response.getContent().get(0), is(contactResponseDTO));
    }

    @Test
    void getContactByIdWhenExist() {
        ContactResponseDTO contactResponseDTO = new ContactResponseDTO("1", "name", "email", "title", "phoneNumber", new Address(), Status.ACTIVE, "photoUrl");
        Contact contact = new Contact();
        contact.setId("id");

        when(contactRepository.findById("id")).thenReturn(Optional.of(contact));
        when(mapper.contactToContactResponseDTO(contact)).thenReturn(contactResponseDTO);

        var response = underTest.getContactById("id");

        assertThat(response, is(contactResponseDTO));
    }

    @Test
    void getContactByIdWhenDoesNotExist() {
        when(contactRepository.findById("id")).thenReturn(Optional.empty());

        var response = assertThrows(ContactNotFoundException.class, () -> underTest.getContactById("id"));

        assertThat(response.getMessage(), is(String.format("Contact not found")));
    }

    @Test
    void createContact() {
        ContactResponseDTO contactResponseDTO = new ContactResponseDTO("1", "name", "email", "title", "phoneNumber", new Address(), Status.ACTIVE, "photoUrl");
        ContactRequestDTO contactRequestDTO = new ContactRequestDTO("email", "name", "title", "phoneNumber", new Address(), Status.ACTIVE, "photoUrl");
        Contact contact = new Contact();

        when(mapper.contactRequestDTOtoContact(contactRequestDTO)).thenReturn(contact);
        when(mapper.contactToContactResponseDTO(contact)).thenReturn(contactResponseDTO);

        ArgumentCaptor<Contact> contactArgumentCaptor = ArgumentCaptor.forClass(Contact.class);

        var response = underTest.createContact(contactRequestDTO);

        verify(contactRepository).save(contactArgumentCaptor.capture());
        assertThat(response, is(contactResponseDTO));
    }

    @Test
    void deleteWhenContactExist() {
        Contact contact = new Contact();
        contact.setId("id");

        when(contactRepository.findById("id")).thenReturn(Optional.of(contact));

        underTest.deleteContact("id");

        verify(contactRepository, atLeastOnce()).deleteById("id");
    }

    @Test
    void deleteContactWhenDoesNotExist() {
        when(contactRepository.findById("id")).thenReturn(Optional.empty());

        var response = assertThrows(ContactNotFoundException.class, () -> underTest.deleteContact("id"));

        assertThat(response.getMessage(), is(String.format("Contact with id %s does not exist", "id")));
    }

    @Test
    void updateContactWhenExist() {
        Contact contact = new Contact();
        Contact newContact = new Contact();
        contact.setId("id");
        ContactRequestDTO contactRequestDTO = new ContactRequestDTO("email", "name", "title", "phoneNumber", new Address(), Status.ACTIVE, "photoUrl");
        ContactResponseDTO contactResponseDTO = new ContactResponseDTO("1", "name", "email", "title", "phoneNumber", new Address(), Status.ACTIVE, "photoUrl");

        when(contactRepository.findById("id")).thenReturn(Optional.of(contact));
        when(mapper.contactRequestDTOtoContact(contactRequestDTO)).thenReturn(newContact);
        when(mapper.contactToContactResponseDTO(newContact)).thenReturn(contactResponseDTO);

        ArgumentCaptor<Contact> contactArgumentCaptor = ArgumentCaptor.forClass(Contact.class);
        var response = underTest.updateContact("id", contactRequestDTO);

        verify(contactRepository, atLeastOnce()).save(contactArgumentCaptor.capture());
        assertThat(contactArgumentCaptor.getValue().getId(), is("id"));
        assertThat(response, is(contactResponseDTO));
    }

    @Test
    void updateContactWhenDoesNotExist() {
        ContactRequestDTO contactRequestDTO = new ContactRequestDTO("email", "name", "title", "phoneNumber", new Address(), Status.ACTIVE, "photoUrl");

        when(contactRepository.findById("id")).thenReturn(Optional.empty());

        var response = assertThrows(ContactNotFoundException.class, () -> underTest.updateContact("id", contactRequestDTO));

        assertThat(response.getMessage(), is(String.format("Contact with id %s does not exist", "id")));
    }

}