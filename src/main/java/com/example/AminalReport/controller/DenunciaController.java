package com.example.AminalReport.controller;

import com.example.AminalReport.entities.enums.EnumNivelUrgencia;
import com.example.AminalReport.entities.enums.EnumTipoAnimal;
import com.example.AminalReport.entities.formularios.Denuncia;
import com.example.AminalReport.entities.usuarios.Usuario;
import com.example.AminalReport.repository.usuarios.UserRepository; // Confirme se o pacote do repository é este mesmo ou .repositories
import com.example.AminalReport.service.DenunciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.Base64;
import java.util.Optional;

@Controller
@RequestMapping
public class DenunciaController {

    @Autowired
    private DenunciaService denunciaService;

    @Autowired
    private UserRepository userRepository;

    // --- MÉTODO AUXILIAR PARA CARREGAR O USUÁRIO NA NAVBAR ---
    private void adicionarUsuarioAoModel(Model model, Principal principal) {
        if (principal != null) {
            String email = principal.getName();
            // Busca o usuário e evita NullPointerException com orElse(null)
            Usuario usuario = userRepository.findByEmail(email).orElse(null);

            if (usuario != null) {
                model.addAttribute("usuarioLogado", usuario);

                // Lógica da foto do perfil da navbar
                if (usuario.getFoto() != null && usuario.getFoto().length > 0) {
                    String imagem = Base64.getEncoder().encodeToString(usuario.getFoto());
                    model.addAttribute("fotoPerfil", "data:image/jpeg;base64," + imagem);
                } else {
                    model.addAttribute("fotoPerfil", "/images/perfilPadrao.jpg");
                }
            }
        }
    }

    // --- PÁGINA DE DENÚNCIA PADRÃO (GET) ---
    @GetMapping("/denuncia")
    public String denuncia(Model model, Principal principal) {
        // Adiciona o usuário para a Navbar não quebrar
        adicionarUsuarioAoModel(model, principal);
        return "denuncia";
    }

    // --- SALVAR DENÚNCIA PADRÃO (POST) ---
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
            Principal principal) throws IOException { // Adicionei Principal aqui caso queira vincular o autor

        Denuncia denuncia = new Denuncia();

        // Se estiver logado, vincula o usuário à denúncia
        if (principal != null) {
            Usuario usuario = userRepository.findByEmail(principal.getName()).orElse(null);
            denuncia.setUsuarioCriador(usuario);
        }

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

    // --- PÁGINA DE DETALHES (GET) ---
    @GetMapping("/denuncias/detalhe/{id}")
    @Transactional(readOnly = true)
    public String verDetalhes(@PathVariable("id") Long id, Model model, Principal principal) {

        // Adiciona o usuário para a Navbar não quebrar
        adicionarUsuarioAoModel(model, principal);

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
            return "detalhe"; // ATENÇÃO: O nome do arquivo HTML é "detalhes.html"
        }
        return "redirect:/";
    }

    // --- PÁGINA DE DENÚNCIA URGENTE (GET) ---
    @GetMapping("/urgente")
    public String denunciaUrgente(Model model, Principal principal) {
        // Adiciona o usuário para a Navbar não quebrar
        adicionarUsuarioAoModel(model, principal);
        return "urgente";
    }

    // --- SALVAR DENÚNCIA URGENTE (POST) ---
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

        if (!foto.isEmpty()) {
            denuncia.setFoto(foto.getBytes());
        }

        denuncia.setTipoAnimal(tipoAnimal);
        denuncia.setDescricao(descricao);

        // Valores padrão para validação
        denuncia.setBairro("URGÊNCIA");
        denuncia.setEstado("URGÊNCIA.");
        denuncia.setMunicipio("URGÊNCIA");
        denuncia.setCep("98998-989");

        denuncia.setUrgencia(EnumNivelUrgencia.EMERGENCIA);
        denuncia.setContato(contato);

        denunciaService.saveDenuncia(denuncia);

        return "redirect:/";
    }
}