package com.example.AminalReport.controller;

import com.example.AminalReport.entities.formularios.Adocao;
import com.example.AminalReport.entities.formularios.Denuncia;
import com.example.AminalReport.repository.formularios.AdocaoRepository;
import com.example.AminalReport.repository.formularios.DenunciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping
public class HomeController {

    @Autowired
    private DenunciaRepository denunciaRepository;

    @Autowired
    private AdocaoRepository adocaoRepository;

    @GetMapping({"/", "/home"})
    public String home(Model model) {

        List<Denuncia> listaDenuncias = denunciaRepository.findByOrganizacaoResponsavelIsNullOrderByIdDesc();
        model.addAttribute("denuncias", listaDenuncias);

        List<Adocao> listaAdocoes = adocaoRepository.findTop5ByOrderByIdDesc();
        model.addAttribute("adocoesDestaque", listaAdocoes);

        return "home";
    }
}