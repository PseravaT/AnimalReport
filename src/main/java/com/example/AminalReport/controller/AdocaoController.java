package com.example.AminalReport.controller;

import com.example.AminalReport.entities.enums.EnumAndamentoAdocao;
import com.example.AminalReport.entities.enums.EnumAndamentoDenuncia;
import com.example.AminalReport.entities.enums.EnumTipoAnimal;
import com.example.AminalReport.entities.formularios.Adocao;
import com.example.AminalReport.entities.formularios.Denuncia;
import com.example.AminalReport.entities.usuarios.Usuario;
import com.example.AminalReport.repository.formularios.AdocaoRepository;
import com.example.AminalReport.repository.usuarios.UserRepository;
import com.example.AminalReport.service.AdocaoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping("/adocao")
public class AdocaoController {

    private final AdocaoRepository adocaoRepository;

    private final AdocaoService adocaoService;

    private final UserRepository userRepository;

    public AdocaoController(AdocaoRepository adocaoRepository,  AdocaoService adocaoService,  UserRepository userRepository) {
        this.adocaoRepository = adocaoRepository;
        this.adocaoService = adocaoService;
        this.userRepository = userRepository;
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
                                  @RequestParam String contato,
                                  Principal principal){

        try {
            Adocao adocao = new Adocao();

            Integer idadeEstimadaInt = Integer.parseInt(idadeEstimada);

            if (principal != null) {
                Usuario usuario = userRepository.findByEmail(principal.getName()).orElse(null);
                adocao.setUsuarioCriador(usuario);
            }


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

            adocaoService.cadastrarAdocao(adocao);
        } catch (Exception e) {
            throw new IllegalArgumentException("Erro ao cadastrar Adoção: " + e.getMessage());
        }

        return "redirect:/adocao/minhasDoacoes";
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

    @GetMapping("/minhasDoacoes")
    public String minhasDoacoes(Model model, Principal principal){
        if (principal != null) {
            String email = principal.getName();
            Usuario usuario = userRepository.findByEmail(email).orElse(null);

            List<Adocao> listaDoacoes = adocaoRepository.findByUsuarioCriador(usuario);
            model.addAttribute("minhasAdocoes", listaDoacoes);
        }
        return "adocaoMinhas";
    }

    @GetMapping("/alterar/{id}")
    public String alterar(@PathVariable("id") Long id, Model model){
        Adocao adocao = adocaoRepository.findById(id).orElse(null);
        model.addAttribute("adocao", adocao);

        List<EnumAndamentoAdocao> statusExistentes = Arrays.asList(EnumAndamentoAdocao.values());
        model.addAttribute("statusExistentes", statusExistentes);

        return "adocaoAlterar";
    }

    @PostMapping("/alterar/{id}")
    public String alterarAdocao(@PathVariable("id") Long id,
                                @RequestParam(value = "foto", required = false) MultipartFile foto,
                                @RequestParam(required = false) String nomeAnimal,
                                @RequestParam(required = false) Integer idadeEstimada,
                                @RequestParam(required = false) String descricao,
                                @RequestParam(required = false) String contato,
                                @RequestParam(required = false) EnumAndamentoAdocao statusAdocao){
        try{
            adocaoService.alterarAdocao(id, foto, nomeAnimal, idadeEstimada, descricao, contato, statusAdocao);
        } catch (Exception e){
            throw new  IllegalArgumentException("Erro ao alterar Adocao: " + e.getMessage());
        }

        return "redirect:/adocao/home/detalhe/{id}";
    }
}
