package com.demo.web.service;

import com.demo.web.model.User;
import com.demo.web.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }


    public User registerUser(String name, String email, String password) {
        if (userRepository.findByEmail(email) != null) {
            throw new RuntimeException("Email already exists");
        }

        // Mã hóa mật khẩu
        String encodedPassword = passwordEncoder.encode(password);

        // Tạo và lưu người dùng
        User newUser = new User();
        newUser.setUsername(name);
        newUser.setEmail(email);
        newUser.setPassword(encodedPassword);
        return userRepository.save(newUser);
    }

    // Tìm người dùng theo email
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);  // Trả về người dùng nếu tồn tại, null nếu không
    }
}
