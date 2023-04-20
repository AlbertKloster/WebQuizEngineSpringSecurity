package engine.service;

import engine.dto.UserEmailPasswordDTO;
import engine.entity.User;
import engine.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean isUserExist(String email) {
        return userRepository.existsByEmail(email.toLowerCase());
    }

    public ResponseEntity<Void> save(UserEmailPasswordDTO userEmailPasswordDTO) {
        if (isUserExist(userEmailPasswordDTO.email().toLowerCase())) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        User user = new User();
        user.setEmail(userEmailPasswordDTO.email().toLowerCase());
        user.setPassword(passwordEncoder.encode(userEmailPasswordDTO.password()));
        userRepository.save(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
