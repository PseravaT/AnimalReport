package com.example.AminalReport.controller;

import com.example.AminalReport.entities.enums.EnumNivelUrgencia;
import com.example.AminalReport.entities.enums.EnumTipoAnimal;
import com.example.AminalReport.entities.formularios.Denuncia;
import com.example.AminalReport.service.DenunciaService;
import org.springframework.transaction.annotation.Transactional; // <--- USE ESTE IMPORT
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.Optional;

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

    @GetMapping("/denuncias/detalhes/{id}")
    @Transactional(readOnly = true) // <--- Agora podemos usar readOnly=true
    public String verDetalhes(@PathVariable("id") Long id, Model model) {

        Optional<Denuncia> denunciaOpt = denunciaService.buscarPorId(id);

        if (denunciaOpt.isPresent()) {
            Denuncia denuncia = denunciaOpt.get();
            model.addAttribute("denuncia", denuncia);

            if (denuncia.getFoto() != null && denuncia.getFoto().length > 0) {
                String imagemBase64 = Base64.getEncoder().encodeToString(denuncia.getFoto());
                model.addAttribute("imagemDetalhe", "data:image/jpeg;base64," + imagemBase64);
            } else {
                model.addAttribute("imagemDetalhe", "/images/sem-foto.jpg");
            }
            return "detalhe";
        }
        return "redirect:/";
    }

    @GetMapping("/denuncia/urgente")
    public String denunciaUrgente() { return "denunciaUrgente"; }

    @PostMapping("/denuncia/urgente")
    public String denunciaUrgente(
            @RequestParam MultipartFile foto, // <--- CORRIGIDO: Usando MultipartFile
            @RequestParam EnumTipoAnimal tipoAnimal,
            @RequestParam String descricao,
            @RequestParam String contato) throws IOException { // <--- Exception adicionada para o getBytes()

        Denuncia denuncia = new Denuncia();

        // Lógica de conversão da foto
        if (!foto.isEmpty()) {
            denuncia.setFoto(foto.getBytes());
        }

        denuncia.setTipoAnimal(tipoAnimal);
        denuncia.setDescricao(descricao);

        // Valores padrão para passar na validação do banco (Not Null)
        denuncia.setBairro("URGÊNCIA");
        denuncia.setEstado("URGÊNCIA");
        denuncia.setMunicipio("URGÊNCIA");

        denuncia.setUrgencia(EnumNivelUrgencia.EMERGENCIA); // Ajuste se necessário (EMERGENCIA ou URGENTE)
        denuncia.setContato(contato);

        denunciaService.saveDenuncia(denuncia);

        return "redirect:/";
    }
}