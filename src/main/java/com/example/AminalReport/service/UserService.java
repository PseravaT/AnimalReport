package com.example.AminalReport.service;

import com.example.AminalReport.entities.usuarios.Usuario;
import com.example.AminalReport.repository.usuarios.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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


    public void saveUser(Usuario usuario) {

        String senhaCriptografada = passwordEncoder.encode(usuario.getSenha());

        usuario.setSenha(senhaCriptografada);

        userRepository.save(usuario);
    }


    public Usuario loadUserByEmail(String email) {

        return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Usuario não encontrado"));
    }


    public boolean verifyPassword(String senhaDigitada, String senhaCriptografada) {
        return passwordEncoder.matches(senhaDigitada, senhaCriptografada);
    }
}
