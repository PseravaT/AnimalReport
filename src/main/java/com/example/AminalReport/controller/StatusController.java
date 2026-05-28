package com.example.AminalReport.controller;


import com.example.AminalReport.entities.enums.EnumNivelUrgencia;
import com.example.AminalReport.entities.formularios.Denuncia;
import com.example.AminalReport.entities.usuarios.Usuario;
import com.example.AminalReport.repository.formularios.DenunciaRepository;
import com.example.AminalReport.repository.usuarios.UserRepository; // Confirme se o pacote do repository é este mesmo ou .repositories
import com.example.AminalReport.service.DenunciaService;
import com.example.AminalReport.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.Arrays;
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

    @Autowired
    private UploadService uploadService;




    @GetMapping("/status")
    public String status(Model model, Principal principal) {


        if (principal != null) {
            String email = principal.getName();
            Usuario usuario = userRepository.findByEmail(email).orElse(null);

            String tipoUsuario = usuario.getTipoUsuario();

            if (tipoUsuario.equals("Comum")){

                List<Denuncia> listaDenuncias = denunciaRepository.findByUsuarioCriador(usuario);
                model.addAttribute("denuncias", listaDenuncias);

            }

            if (tipoUsuario.equals("Organizacao")){
                List<Denuncia> listaDenuncias = denunciaRepository.findByOrganizacaoResponsavel(usuario);
                model.addAttribute("denuncias", listaDenuncias);
            }
        }

        return "status";
    }


    @GetMapping("/status/detalhe/{id}")
    @Transactional(readOnly = true)
    public String verDetalhes(@PathVariable("id") Long id, Model model, Principal principal) {

        Optional<Denuncia> denunciaOpt = denunciaService.buscarPorId(id);

        if (denunciaOpt.isPresent()) {
            Denuncia denuncia = denunciaOpt.get();
            model.addAttribute("denuncia", denuncia);


            model.addAttribute("voltarPara", "/status");


            if (denuncia.getFoto() != null && !denuncia.getFoto().isBlank()) {

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
        return "redirect:/status";
    }

    @GetMapping("/status/editar/{id}")
    public String editarDenuncia(@PathVariable("id") Long id, Model model) {
        Optional<Denuncia> denunciaOpt = denunciaService.buscarPorId(id);
        if (denunciaOpt.isPresent()) {
            Denuncia denuncia = denunciaOpt.get();
            model.addAttribute("denuncia", denuncia);

            List<EnumNivelUrgencia> nivelUrgencias = Arrays.asList(EnumNivelUrgencia.values());
                model.addAttribute("nivelUrgencias", nivelUrgencias);

            if (denuncia.getFoto() != null && !denuncia.getFoto().isBlank()) {

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
            return "alterarDenuncia";

        }
        else return "redirect:/status";
    }

    @PostMapping("/status/editar/{id}")
    public String atualizarDenuncia(@PathVariable("id") Long id,
                                    @RequestParam(value = "descricao", required = false) String descricao,
                                    @RequestParam(value = "nivelUrgencia", required = false) EnumNivelUrgencia urgencia,
                                    @RequestParam(value = "pontoRef", required = false) String pontoRef,
                                    @RequestParam(value = "foto", required = false) MultipartFile foto) {
        Denuncia denuncia = denunciaService.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Denúncia não encontrada: " + id));

        if (descricao != null && !descricao.isBlank()) {
            denuncia.setDescricao(descricao);
        }

        if (urgencia != null) {
            denuncia.setUrgencia(urgencia);
        }

        if (pontoRef != null && !pontoRef.isBlank()) {
            denuncia.setPontoRef(pontoRef);
        }

        if (foto != null && !foto.isEmpty()) {

            try {


                String caminhoFoto = uploadService.salvarImagem(foto, "denuncia");

                denuncia.setFoto(caminhoFoto);

            } catch (java.io.IOException e) {

                throw new IllegalStateException(
                        "Erro ao salvar imagem da denúncia",
                        e
                );
            }
        }

        denunciaService.editarDenuncia(denuncia);
        return "redirect:/status";
    }
}