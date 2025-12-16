package com.demo.web.mapper;

import com.demo.web.dto.ContactRequest;
import com.demo.web.dto.ContactResponse;
import com.demo.web.model.Contact;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class ContactMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "assignedTo", ignore = true)
    @Mapping(target = "resolvedAt", ignore = true)
    @Mapping(target = "flag", ignore = true)
    public abstract Contact toEntity(ContactRequest request);

    @Mapping(target = "assignedTo", expression = "java(mapAssignedToName(contact))")
    @Mapping(target = "resolvedAt", source = "resolvedAt")
    public abstract ContactResponse toResponse(Contact contact);

    public abstract List<ContactResponse> toResponseList(List<Contact> contacts);

    protected String mapAssignedToName(Contact contact) {
        if (contact.getAssignedTo() == null) {
            return null;
        }
        if (contact.getAssignedTo().getEmployee() != null) {
            return contact.getAssignedTo().getEmployee().getFullName();
        }
        return contact.getAssignedTo().getUsername();
    }
}
