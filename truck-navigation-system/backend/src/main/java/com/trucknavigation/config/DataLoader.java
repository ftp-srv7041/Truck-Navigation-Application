package com.trucknavigation.config;

import com.trucknavigation.model.User;
import com.trucknavigation.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Create test users if they don't exist
        if (userRepository.count() == 0) {
            createTestUser("testuser", "test@example.com", "password123", "Test User", "9876543210");
            createTestUser("admin", "admin@trucknavigation.com", "admin123", "Admin User", "9876543211");
            createTestUser("driver1", "driver1@example.com", "driver123", "John Driver", "9876543212");
            
            System.out.println("✅ Test users created successfully!");
            System.out.println("📧 Login credentials:");
            System.out.println("   Email: test@example.com, Password: password123");
            System.out.println("   Email: admin@trucknavigation.com, Password: admin123");
            System.out.println("   Email: driver1@example.com, Password: driver123");
        }
    }

    private void createTestUser(String username, String email, String password, String fullName, String phoneNumber) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setFullName(fullName);
        user.setPhoneNumber(phoneNumber);
        user.setRole(username.equals("admin") ? User.Role.ADMIN : User.Role.DRIVER);
        user.setEnabled(true);
        
        userRepository.save(user);
    }
}