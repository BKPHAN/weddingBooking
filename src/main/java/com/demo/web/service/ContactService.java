package com.demo.web.service;

import com.demo.web.model.Contact;
import com.demo.web.model.User;
import com.demo.web.model.enums.UserRole;
import com.demo.web.repository.ContactRepository;
import com.demo.web.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Set;

@Service
public class ContactService {

    private static final Set<String> URGENT_KEYWORDS = Set.of("gấp", "khẩn", "urgent", "asap", "nhanh");

    private final ContactRepository contactRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    public ContactService(ContactRepository contactRepository,
                          UserRepository userRepository,
                          NotificationService notificationService) {
        this.contactRepository = contactRepository;
        this.userRepository = userRepository;
        this.notificationService = notificationService;
    }

    @Transactional
    public Contact saveContact(Contact contact) {
        contact.setFlag(determineFlag(contact));
        contact.setResolvedAt(null);

        if (contact.getAssignedTo() == null) {
            resolveAssignee().ifPresent(contact::setAssignedTo);
        }

        Contact saved = contactRepository.save(contact);
        notificationService.notifyContactReceived(saved);
        return saved;
    }

    @Transactional
    public Contact assignContact(Long contactId, Long userId) {
        Contact contact = contactRepository.findById(contactId)
                .orElseThrow(() -> new EntityNotFoundException("Contact not found: " + contactId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + userId));
        contact.setAssignedTo(user);
        contact.setFlag("ASSIGNED");
        return contactRepository.save(contact);
    }

    @Transactional
    public Contact markResolved(Long contactId) {
        Contact contact = contactRepository.findById(contactId)
                .orElseThrow(() -> new EntityNotFoundException("Contact not found: " + contactId));
        contact.setResolvedAt(LocalDateTime.now());
        contact.setFlag("RESOLVED");
        return contactRepository.save(contact);
    }

    @Transactional(readOnly = true)
    public List<Contact> getLatestContacts() {
        return contactRepository.findTop10ByOrderByCreatedAtDesc();
    }

    private String determineFlag(Contact contact) {
        String message = contact.getMessage() != null
                ? contact.getMessage().toLowerCase(Locale.ROOT)
                : "";

        if (URGENT_KEYWORDS.stream().anyMatch(message::contains)) {
            return "URGENT";
        }

        return contactRepository.findTopByEmailIgnoreCaseOrderByCreatedAtDesc(contact.getEmail())
                .map(last -> {
                    Duration between = Duration.between(last.getCreatedAt(), LocalDateTime.now());
                    if (between.toDays() < 7) {
                        return "FOLLOW_UP";
                    }
                    return "NEW";
                })
                .orElse("NEW");
    }

    private java.util.Optional<User> resolveAssignee() {
        return userRepository.findFirstByPrimaryRoleOrderByIdAsc(UserRole.ADMIN);
    }
}
