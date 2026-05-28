package com.example.AminalReport.controller;

import com.example.AminalReport.entities.enums.EnumAndamentoDenuncia;
import com.example.AminalReport.entities.enums.EnumNivelUrgencia;
import com.example.AminalReport.entities.enums.EnumTipoAnimal;
import com.example.AminalReport.entities.formularios.Denuncia;
import com.example.AminalReport.entities.usuarios.Usuario;
import com.example.AminalReport.repository.usuarios.UserRepository;
import com.example.AminalReport.service.DenunciaService;
import com.example.AminalReport.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.Optional;

@Controller
@RequestMapping
public class DenunciaController {

    @Autowired
    private DenunciaService denunciaService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UploadService uploadService;



    @GetMapping("/denuncia")
    public String denuncia() {
        return "denuncia";
    }


    @PostMapping("/denuncia")
    public String denuncia(
            @RequestParam MultipartFile foto,
            @RequestParam EnumTipoAnimal tipoAnimal,
            @RequestParam String descricao,
            @RequestParam EnumNivelUrgencia nivelUrgencia,
            @RequestParam String cep,
            @RequestParam String estado,
            @RequestParam String municipio,
            @RequestParam String bairro,
            @RequestParam String rua,
            @RequestParam String pontoRef,
            Principal principal) throws IOException {

        Denuncia denuncia = new Denuncia();

        if (principal != null) {
            Usuario usuario = userRepository.findByEmail(principal.getName()).orElse(null);
            denuncia.setUsuarioCriador(usuario);
        }

        String caminhoFoto = uploadService.salvarImagem(foto, "denuncia");

        denuncia.setFoto(caminhoFoto);
        denuncia.setTipoAnimal(tipoAnimal);
        denuncia.setDescricao(descricao);
        denuncia.setUrgencia(nivelUrgencia);
        denuncia.setCep(cep);
        denuncia.setEstado(estado);
        denuncia.setMunicipio(municipio);
        denuncia.setBairro(bairro);
        denuncia.setRua(rua);
        denuncia.setPontoRef(pontoRef);
        denuncia.setAndamentoDenuncia(EnumAndamentoDenuncia.AGUARDANDO);

        denunciaService.saveDenuncia(denuncia);

        return "redirect:/";
    }

    @GetMapping("/denuncias/detalhe/{id}")
    @Transactional(readOnly = true)
    public String verDetalhes(@PathVariable("id") Long id, Model model) {

        Optional<Denuncia> denunciaOpt = denunciaService.buscarPorId(id);

        if (denunciaOpt.isPresent()) {
            Denuncia denuncia = denunciaOpt.get();
            model.addAttribute("denuncia", denuncia);


            model.addAttribute("voltarPara", "/home");


            if (denuncia.getFoto() != null) {
                model.addAttribute(
                        "imagemDetalhe",
                        "/uploads/" + denuncia.getFoto()
                );
            } else {
                model.addAttribute(
                        "imagemDetalhe",
                        "/images/sem-foto.jpg"
                );
            }
            return "detalhe";
        }
        return "redirect:/";
    }


    @GetMapping("/urgente")
    public String denunciaUrgente() {
        return "urgente";
    }


    @PostMapping("/urgente")
    public String denunciaUrgente(
            @RequestParam MultipartFile foto,
            @RequestParam EnumTipoAnimal tipoAnimal,
            @RequestParam String descricao,
            @RequestParam String contato,
            Principal principal) throws IOException {

        Denuncia denuncia = new Denuncia();

        if (principal != null) {
            Usuario usuario = userRepository.findByEmail(principal.getName()).orElse(null);
            denuncia.setUsuarioCriador(usuario);
        }

        String caminhoFoto = uploadService.salvarImagem(foto, "denuncia");

        denuncia.setFoto(caminhoFoto);

        denuncia.setTipoAnimal(tipoAnimal);
        denuncia.setDescricao(descricao);


        denuncia.setBairro("URGÊNCIA");
        denuncia.setEstado("URGÊNCIA");
        denuncia.setMunicipio("URGÊNCIA");
        denuncia.setCep("98998-989");

        denuncia.setUrgencia(EnumNivelUrgencia.EMERGENCIA);
        denuncia.setContato(contato);
        denuncia.setAndamentoDenuncia(EnumAndamentoDenuncia.AGUARDANDO);

        denunciaService.saveDenuncia(denuncia);

        return "redirect:/";
    }

}