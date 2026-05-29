package com.example.AminalReport.service;

import com.example.AminalReport.entities.usuarios.Usuario;
import com.example.AminalReport.repository.usuarios.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final UploadService uploadService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder,  UploadService uploadService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.uploadService = uploadService;
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

    public void alterarUsuario( Long id, MultipartFile foto, String nome, String email, String telefone, String senha) throws IOException {

        Usuario usuarioAlterado = userRepository.findById(id).orElseThrow(null);

        if(nome != null && !nome.isBlank()) {
            usuarioAlterado.setNome(nome);
        }
        if(senha != null && !senha.isBlank()) {
            usuarioAlterado.setSenha(passwordEncoder.encode(senha));
        }
        if(email != null && !email.isBlank()) {
            usuarioAlterado.setEmail(email);
        }
        if(telefone != null && !telefone.isBlank()) {
            usuarioAlterado.setTelefone(telefone);
        }
        if (foto != null && !foto.isEmpty()) {
            if (usuarioAlterado.getFoto()  != null) {
                uploadService.deletarImagem(usuarioAlterado.getFoto());
            }

            String caminhoFoto = uploadService.salvarImagem(foto, "usuario");

            usuarioAlterado.setFoto(caminhoFoto);
        }

        userRepository.save(usuarioAlterado);
    }
}
