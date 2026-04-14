package com.example.AminalReport.controller;

import com.example.AminalReport.entities.enums.EnumTipoAnimal;
import com.example.AminalReport.entities.formularios.Adocao;
import com.example.AminalReport.repository.formularios.AdocaoRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;


@Controller
@RequestMapping("/adocao")
public class AdocaoController {

    private final AdocaoRepository adocaoRepository;

    public AdocaoController(AdocaoRepository adocaoRepository) {
        this.adocaoRepository = adocaoRepository;
    }

    @GetMapping("/cadastrar")
    public String cadastrarAdocao(){
        return "adocaoCadastro";
    }

    @PostMapping("/cadastrar")
    public String cadastrarAdocao(@RequestParam String nomeAnimal,
                                  @RequestParam String idadeEstimada,
                                  @RequestParam String descricao,
                                  @RequestParam MultipartFile foto,
                                  @RequestParam EnumTipoAnimal tipoAnimal,
                                  @RequestParam String contato ){

        try {
            Adocao adocao = new Adocao();

            Integer idadeEstimadaInt = Integer.parseInt(idadeEstimada);


            adocao.setNomeAnimal(nomeAnimal);
            adocao.setIdadeEstimada(idadeEstimadaInt);
            adocao.setDescricao(descricao);

            byte[] fotoBytes = null;
            if (!foto.isEmpty()) {
                fotoBytes = foto.getBytes();
            }
            adocao.setFoto(fotoBytes);
            adocao.setTipoAnimal(tipoAnimal);
            adocao.setContato(contato);

            adocaoRepository.save(adocao);
        } catch (Exception e) {
            throw new IllegalArgumentException("Erro ao cadastrar Adoção: " + e.getMessage());
        }

        return "redirect:/"; //alterar
    }
}
