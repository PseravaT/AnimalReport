package com.example.AminalReport.service;

import com.example.AminalReport.entities.usuarios.Usuario;
import com.example.AminalReport.repository.usuarios.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    // Dependência: Interface para operações de banco de dados
    private final UserRepository userRepository;
    // Dependência: Componente para criptografar e verificar senhas.
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // salvar um novo usuário no banco de dados.
    public void saveUser(Usuario usuario) {
        // 1. Criptografa a senha antes de salvar.
        String senhaCriptografada = passwordEncoder.encode(usuario.getSenha());
        // 2. Atualiza o objeto Usuario com a senha criptografada.
        usuario.setSenha(senhaCriptografada);
        // 3. Salva a Entidade no banco de dados
        userRepository.save(usuario);
    }

    // buscar um usuário pelo email (lógica de login ou verificação)
    public Usuario loadUserByEmail(String email) {
        // Chama o metodo customizado que definimos no UserRepository - query de pegar todos os dados pelo email;
        return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Usuario não encontrado"));
    }

    // Verificar se a senha digitada bate senha criptografada.
    public boolean verifyPassword(String senhaDigitada, String senhaCriptografada) {
        return passwordEncoder.matches(senhaDigitada, senhaCriptografada);
    }
}
