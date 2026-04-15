package com.example.AminalReport.controller;

import com.example.AminalReport.entities.enums.EnumTipoAnimal;
import com.example.AminalReport.entities.formularios.Adocao;
import com.example.AminalReport.repository.formularios.AdocaoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.Base64;
import java.util.List;
import java.util.Optional;


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

    @GetMapping("/home")
    public String adocaoHome(Model model,@RequestParam(defaultValue = "0")int pagina){

        Pageable pageable = PageRequest.of(pagina, 6, Sort.by("id").descending());
        Page<Adocao> adocaoPage= adocaoRepository.findAll(pageable);

        model.addAttribute("listaAdocao", adocaoPage.getContent());
        model.addAttribute("currentPage", pagina);
        model.addAttribute("totalPages", adocaoPage.getTotalPages());

        return "adocaoHome";
    }

    @GetMapping("/home/detalhe/{id}")
    @Transactional(readOnly = true)
    public String verDetalhes(@PathVariable("id") Long id, Model model){

        Optional<Adocao> adocaoOpt = adocaoRepository.findById(id);

        if (adocaoOpt.isPresent()){
            Adocao adocao = adocaoOpt.get();
            model.addAttribute("adocao", adocao);

            model.addAttribute("voltarPara", "/adocao/home");

            if (adocao.getFoto() != null && adocao.getFoto().length > 0) {
                String imagemBase64 = Base64.getEncoder().encodeToString(adocao.getFoto());
                model.addAttribute("imagemDetalhe", "data:image/jpeg;base64," + imagemBase64);
            }
            else {
                model.addAttribute("imagemDetalhe", "/images/sem-foto.jpg");
            }
            return "adocaoDetalhe";
        }
        return "redirect:/adocao/home";

    }
}
