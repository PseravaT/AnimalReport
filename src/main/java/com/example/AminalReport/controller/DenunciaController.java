package com.example.AminalReport.controller;

import com.example.AminalReport.entities.enums.EnumNivelUrgencia;
import com.example.AminalReport.entities.enums.EnumTipoAnimal;
import com.example.AminalReport.entities.formularios.Denuncia;
import com.example.AminalReport.service.DenunciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping
public class DenunciaController {

    @Autowired
    DenunciaService denunciaService;


    @GetMapping("/denuncia")
    public String denuncia(){
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
            @RequestParam String pontoRef) throws IOException {

        Denuncia denuncia = new Denuncia();

        byte[] fotoBytes = null;

        if (!foto.isEmpty()) {
            fotoBytes = foto.getBytes();
        }

        denuncia.setFoto(fotoBytes);
        denuncia.setTipoAnimal(tipoAnimal);
        denuncia.setDescricao(descricao);
        denuncia.setUrgencia(nivelUrgencia);
        denuncia.setCep(cep);
        denuncia.setEstado(estado);
        denuncia.setMunicipio(municipio);
        denuncia.setBairro(bairro);
        denuncia.setRua(rua);
        denuncia.setComplemento(pontoRef);

        denunciaService.saveDenuncia(denuncia);

        return "redirect:/";
    }

    @GetMapping("/denuncia/urgente")
    public String denunciaUrgente() {return "denunciaUrgente";}

    @PostMapping("/denuncia/urgente")
    public String denunciaUrgente(
    @RequestParam byte[] foto,
    @RequestParam EnumTipoAnimal tipoAnimal,
    @RequestParam String descricao,
    @RequestParam String contato) {

        Denuncia denuncia = new Denuncia();

        denuncia.setFoto(foto);
        denuncia.setTipoAnimal(tipoAnimal);
        denuncia.setDescricao(descricao) ;
        denuncia.setBairro("URGÊNCIA");
        denuncia.setEstado("URGÊNCIA");
        denuncia.setMunicipio("URGÊNCIA");
        denuncia.setUrgencia(EnumNivelUrgencia.URGENTE);
        denuncia.setContato(contato);

        denunciaService.saveDenuncia(denuncia);

        return "redirect:/";
    }
}
