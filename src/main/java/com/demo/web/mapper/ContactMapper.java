package com.demo.web.mapper;

import com.demo.web.dto.ContactRequest;
import com.demo.web.dto.ContactResponse;
import com.demo.web.model.Contact;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ContactMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "assignedTo", ignore = true)
    @Mapping(target = "resolvedAt", ignore = true)
    @Mapping(target = "flag", ignore = true)
    Contact toEntity(ContactRequest request);

    @Mapping(target = "assignedTo", expression = "java(contact.getAssignedTo() != null ? contact.getAssignedTo().getFullName() : null)")
    @Mapping(target = "resolvedAt", source = "resolvedAt")
    ContactResponse toResponse(Contact contact);

    List<ContactResponse> toResponseList(List<Contact> contacts);
}
