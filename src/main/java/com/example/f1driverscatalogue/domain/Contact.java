package com.example.f1driverscatalogue.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Table(name = "contacts")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Contact {

    @Id
    @UuidGenerator
    @Column(name = "id", unique = true, updatable = false)
    private String id;
    @NotBlank(message = "The name must not be blank")
    private String name;
    private String email;
    private String title;
    private String phone;
    @Embedded
    private Address address;
    private Status status;
    private String photoUrl;
}
