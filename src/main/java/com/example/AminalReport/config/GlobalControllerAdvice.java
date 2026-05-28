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

        if (principal == null) {
            return;
        }

        String email = principal.getName();
        Usuario usuario = userRepository.findByEmail(email).orElse(null);

        if (usuario != null) {
            model.addAttribute("usuarioLogado", usuario);

            if (usuario.getFoto() != null && !usuario.getFoto().isBlank()) {

                model.addAttribute(
                        "fotoPerfil",
                        "/uploads/" + usuario.getFoto()
                );

            } else {

                model.addAttribute(
                        "fotoPerfil",
                        "/images/perfilPadrao.jpg"
                );
            }
        }
    }
}
