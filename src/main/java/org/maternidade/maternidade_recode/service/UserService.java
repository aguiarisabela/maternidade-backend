package org.maternidade.maternidade_recode.service;

import org.maternidade.maternidade_recode.model.User;
import org.maternidade.maternidade_recode.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public Optional<User> buscarPorId(Long id) {
        return userRepository.findById(id);
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow();
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    // Novo método para buscar por username
    public Optional<User> findByEmail(String email) {
    return userRepository.findByEmail(email); // Adicione isso no UserRepository
}

    public Optional<User> findByUsername(String identifier) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}