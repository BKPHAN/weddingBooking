package com.demo.web.service;

import com.demo.web.model.Contact;
import com.demo.web.model.User;
import com.demo.web.model.enums.UserRole;
import com.demo.web.repository.ContactRepository;
import com.demo.web.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContactServiceTest {

    @Mock
    private ContactRepository contactRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private ContactService contactService;

    private Contact contact;

    @BeforeEach
    void setup() {
        contact = new Contact();
        contact.setFullName("Trần Văn B");
        contact.setEmail("tvb@example.com");
        contact.setPhone("0912345678");
        contact.setMessage("Tôi cần tư vấn gấp cho sự kiện trong tháng tới.");
        contact.setSubject("Tư vấn gấp");
    }

    @Test
    void saveContact_shouldMarkUrgent_whenMessageContainsKeyword() {
        when(contactRepository.save(any(Contact.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Contact saved = contactService.saveContact(contact);

        assertThat(saved.getFlag()).isEqualTo("URGENT");
        verify(notificationService).notifyContactReceived(saved);
    }

    @Test
    void saveContact_shouldMarkFollowUp_whenRecentContactExists() {
        Contact lastContact = new Contact();
        lastContact.setEmail("tvb@example.com");
        ReflectionTestUtils.setField(lastContact, "createdAt", LocalDateTime.now().minusDays(2));

        when(contactRepository.findTopByEmailIgnoreCaseOrderByCreatedAtDesc(eq("tvb@example.com")))
                .thenReturn(Optional.of(lastContact));
        when(contactRepository.save(any(Contact.class))).thenAnswer(invocation -> invocation.getArgument(0));

        contact.setMessage("Xin cập nhật báo giá");

        Contact saved = contactService.saveContact(contact);

        assertThat(saved.getFlag()).isEqualTo("FOLLOW_UP");
        verify(notificationService).notifyContactReceived(saved);
    }

    @Test
    void saveContact_shouldAssignAdminAutomatically() {
        when(contactRepository.save(any(Contact.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User staff = new User();
        staff.setFullName("Nguyễn Nhân Viên");
        when(userRepository.findFirstByPrimaryRoleOrderByIdAsc(UserRole.ADMIN))
                .thenReturn(Optional.of(staff));

        Contact saved = contactService.saveContact(contact);

        assertThat(saved.getAssignedTo()).isNotNull();
        assertThat(saved.getAssignedTo().getFullName()).isEqualTo("Nguyễn Nhân Viên");
        verify(notificationService).notifyContactReceived(saved);
    }
}
