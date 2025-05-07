package com.martinez.dentist.users.services;

import com.martinez.dentist.professionals.models.Professional;
import com.martinez.dentist.professionals.repositories.ProfessionalRepository;
import com.martinez.dentist.users.controllers.LoginDTO;
import com.martinez.dentist.users.controllers.LoginResponseDTO;
import com.martinez.dentist.users.controllers.UserRequestDTO;
import com.martinez.dentist.users.controllers.UserResponseDTO;
import com.martinez.dentist.users.models.User;
import com.martinez.dentist.users.models.UserRole;
import com.martinez.dentist.users.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProfessionalRepository professionalRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public String createUser(UserRequestDTO dto) {
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new RuntimeException("El nombre de usuario ya está en uso.");
        }

        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("El email ya está registrado.");
        }

        String encodedPassword = passwordEncoder.encode(dto.getPassword());

        User user = new User(
                dto.getUsername(),
                encodedPassword,
                dto.getEmail(),
                dto.getRole()
        );

        if (dto.getProfessionalId() != null) {
            if (!dto.getRole().equals(UserRole.USUARIO)) {
                throw new RuntimeException("Solo los usuarios con rol USUARIO pueden ser vinculados a un profesional.");
            }

            if (userRepository.existsByProfessionalId(dto.getProfessionalId())) {
                throw new RuntimeException("Este profesional ya tiene un usuario asignado.");
            }

            Professional prof = professionalRepository.findById(dto.getProfessionalId())
                    .orElseThrow(() -> new RuntimeException("Profesional no encontrado con ID: " + dto.getProfessionalId()));

            user.setProfessional(prof);
        }

        userRepository.save(user);
        return "Usuario creado con éxito.";
    }

    @Override
    public String updateUser(Long id, UserRequestDTO dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        user.updateData(dto.getUsername(), dto.getEmail(), dto.getRole());

        if (dto.getProfessionalId() != null) {
            if (!dto.getRole().equals(UserRole.USUARIO)) {
                throw new RuntimeException("Solo los usuarios con rol USUARIO pueden ser vinculados a un profesional.");
            }

            if (userRepository.existsByProfessionalId(dto.getProfessionalId()) &&
                    (user.getProfessional() == null || !user.getProfessional().getId().equals(dto.getProfessionalId()))) {
                throw new RuntimeException("Este profesional ya está vinculado a otro usuario.");
            }

            Professional prof = professionalRepository.findById(dto.getProfessionalId())
                    .orElseThrow(() -> new RuntimeException("Profesional no encontrado"));

            user.setProfessional(prof);
        }

        userRepository.save(user);
        return "Usuario actualizado correctamente.";
    }

    @Override
    public List<UserResponseDTO> getAllUsers() {
        return ((List<User>) userRepository.findAll()).stream().map(user -> {
            Professional prof = user.getProfessional();
            Long profId = (prof != null) ? prof.getId() : null;
            String profName = (prof != null) ? prof.getFullName() : null;

            return new UserResponseDTO(
                    user.getId(),
                    user.getUsername(),
                    user.getEmail(),
                    user.getRole().name(),
                    profId,
                    profName
            );
        }).toList();
    }

    @Override
    public UserResponseDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Professional prof = user.getProfessional();
        Long profId = (prof != null) ? prof.getId() : null;
        String profName = (prof != null) ? prof.getFullName() : null;

        return new UserResponseDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole().name(),
                profId,
                profName
        );
    }

    @Override
    public LoginResponseDTO loginUser(LoginDTO loginDTO) {
        User user = userRepository.findByUsername(loginDTO.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        Professional prof = user.getProfessional();
        Long profId = (prof != null) ? prof.getId() : null;
        String profName = (prof != null) ? prof.getFullName() : null;

        return new LoginResponseDTO(
                user.getUsername(),
                user.getEmail(),
                user.getRole().name(),
                profId,
                profName
        );
    }
}
