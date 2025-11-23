package com.example.AminalReport.controller;

import com.example.AminalReport.entities.formularios.Denuncia;
import com.example.AminalReport.entities.usuarios.Usuario;
import com.example.AminalReport.repository.formularios.DenunciaRepository;
import com.example.AminalReport.repository.usuarios.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.Base64;
import java.util.List;

@Controller
@RequestMapping
public class HomeController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DenunciaRepository denunciaRepository;

    @GetMapping({"/", "/home"})
    public String home(Model model, Principal principal){


        if (principal != null){
            String email = principal.getName();
            Usuario usuario = userRepository.findByEmail(email).orElse(null);

            if (usuario != null){
                model.addAttribute("usuarioLogado", usuario);

                if (usuario.getFoto() != null && usuario.getFoto().length > 0){
                    String imagem = Base64.getEncoder().encodeToString(usuario.getFoto());
                    model.addAttribute("fotoPerfil", "data:image/jpeg;base64," + imagem);
                } else{
                    model.addAttribute("fotoPerfil", "/images/perfilPadrao.jpg");
                }
            }
        }

        List<Denuncia> listaDenuncias = denunciaRepository.findAllByOrderByIdDesc();

        // enviar para o HTML
        model.addAttribute("denuncias", listaDenuncias);

        return "home";
    }
}