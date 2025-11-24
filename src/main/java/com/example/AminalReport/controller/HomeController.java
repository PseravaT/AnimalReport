package com.example.AminalReport.controller;

import com.example.AminalReport.entities.formularios.Denuncia;
import com.example.AminalReport.repository.formularios.DenunciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping
public class HomeController {

    @Autowired
    private DenunciaRepository denunciaRepository;

    @GetMapping({"/", "/home"})
    public String home(Model model, Principal principal) {


        // 2. Carregar lista de denúncias ordenadas
        List<Denuncia> listaDenuncias = denunciaRepository.findAllByOrderByIdDesc();
        model.addAttribute("denuncias", listaDenuncias);

        return "home";
    }
}