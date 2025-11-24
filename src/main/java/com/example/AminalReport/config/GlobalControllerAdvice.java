package com.example.AminalReport.config;

import com.example.AminalReport.entities.usuarios.Usuario;
import com.example.AminalReport.repository.usuarios.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.security.Principal;
import java.util.Base64;

@ControllerAdvice
public class GlobalControllerAdvice {

    @Autowired
    private UserRepository userRepository;

    @ModelAttribute("requestURI")
    public String requestURI(HttpServletRequest request) {
        return request.getRequestURI();
    }

    @ModelAttribute
    public void adicionarUsuarioGlobal(Model model, Principal principal) {

        // Se não tiver ninguém logado, não faz nada (o th:if da navbar cuida do resto)
        if (principal == null) {
            return;
        }

        String email = principal.getName();
        Usuario usuario = userRepository.findByEmail(email).orElse(null);

        if (usuario != null) {
            // Adiciona o usuário no Model globalmente
            model.addAttribute("usuarioLogado", usuario);

            // Lógica da Foto
            if (usuario.getFoto() != null && usuario.getFoto().length > 0) {
                String imagem = Base64.getEncoder().encodeToString(usuario.getFoto());
                model.addAttribute("fotoPerfil", "data:image/jpeg;base64," + imagem);
            } else {
                model.addAttribute("fotoPerfil", "/images/perfilPadrao.jpg");
            }
        }
    }
}
