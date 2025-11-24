package com.example.AminalReport.controller;


import com.example.AminalReport.entities.formularios.Denuncia;
import com.example.AminalReport.entities.usuarios.Usuario;
import com.example.AminalReport.repository.formularios.DenunciaRepository;
import com.example.AminalReport.repository.usuarios.UserRepository; // Confirme se o pacote do repository é este mesmo ou .repositories
import com.example.AminalReport.service.DenunciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.Base64;
import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping
public class StatusController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DenunciaRepository denunciaRepository;

    @Autowired
    private DenunciaService denunciaService;

    @GetMapping("/status")
    public String status(Model model, Principal principal){

        // 1. Carregar dados do usuário logado (se houver)
        if (principal != null) {
            String email = principal.getName();
            Usuario usuario = userRepository.findByEmail(email).orElse(null);

            List<Denuncia> listaDenuncias = denunciaRepository.findByUsuarioCriador(usuario);
            model.addAttribute("denuncias", listaDenuncias);
        }
        return "status";
    }

    // --- PÁGINA DE DETALHES (GET) ---
    @GetMapping("/status/detalhe/{id}")
    @Transactional(readOnly = true)
    public String verDetalhes(@PathVariable("id") Long id, Model model) {

        Optional<Denuncia> denunciaOpt = denunciaService.buscarPorId(id);

        if (denunciaOpt.isPresent()) {
            Denuncia denuncia = denunciaOpt.get();
            model.addAttribute("denuncia", denuncia);

            // --- AQUI ESTÁ A MÁGICA ---
            model.addAttribute("voltarPara", "/status"); // Define que volta para Status
            // ---------------------------

            if (denuncia.getFoto() != null && denuncia.getFoto().length > 0) {
                String imagemBase64 = Base64.getEncoder().encodeToString(denuncia.getFoto());
                model.addAttribute("imagemDetalhe", "data:image/jpeg;base64," + imagemBase64);
            } else {
                model.addAttribute("imagemDetalhe", "/images/sem-foto.jpg");
            }
            return "detalhe";
        }
        return "redirect:/status";
    }
}