package com.demo.web.service.admin;

import com.demo.web.dto.admin.UserProfileResponse;
import com.demo.web.model.User;
import com.demo.web.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AdminProfileService {

    private final UserRepository userRepository;

    public AdminProfileService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public UserProfileResponse getProfile(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("User id is required");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        UserProfileResponse response = new UserProfileResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setFullName(user.getEmployee() != null ? user.getEmployee().getFullName() : user.getUsername());
        response.setEmail(user.getEmail());
        response.setPrimaryRole(user.getPrimaryRole().name());
        response.setStatus(user.getStatus().name());

        List<String> roles = new java.util.ArrayList<>();
        if (user.getPrimaryRole() != null)
            roles.add(user.getPrimaryRole().name());
        response.setRoles(roles);

        response.setPermissions(new java.util.HashSet<>());
        response.setCreatedAt(user.getCreatedAt());
        response.setUpdatedAt(user.getUpdatedAt());
        return response;
    }
}
