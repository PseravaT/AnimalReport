package com.example.AminalReport.controller;

import com.example.AminalReport.entities.enums.EnumStatusUsuario;
import com.example.AminalReport.entities.enums.EnumTipoOrg;
import com.example.AminalReport.entities.usuarios.Comum;
import com.example.AminalReport.entities.usuarios.Organizacao;
import com.example.AminalReport.entities.usuarios.Usuario;
import com.example.AminalReport.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;

import java.util.List;

import static java.util.regex.Pattern.matches;

@Controller
public class LoginController {

    private final UserService userservice;

    private final SecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();


    public LoginController(UserService userservice) {
        this.userservice = userservice;
    }


    @GetMapping("/entrar")
    public String entrar() {
        return "login";
    }


    @PostMapping("/entrar")
    public String login(@RequestParam String email,
                        @RequestParam String senha,
                        HttpServletRequest request,
                        HttpServletResponse response) {


        Usuario usuario = userservice.loadUserByEmail(email);


        if (usuario == null || !userservice.verifyPassword(senha, usuario.getSenha())) {

            return "redirect:/entrar?error=true";

        }


        String perfilDoUsuario = usuario.getClass().getSimpleName();
        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(perfilDoUsuario));


        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(usuario, null, authorities);


        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);

        System.out.println("Usuário autenticado com sucesso: " + SecurityContextHolder.getContext().getAuthentication().getName());


        securityContextRepository.saveContext(context, request, response);


        if ("ROLE_ADMIN".equals(perfilDoUsuario)) {
            return "redirect:/admin/dashboard";
        }

        else if ("ORGANIZAÇÃO".equals(perfilDoUsuario))
            return "redirect:/homeOrg";

        else

            return "redirect:/home";
    }


    @GetMapping("/registrar")
    public String registrar( ) {
        return "registrar";
    }


    @PostMapping("/registrar")
    public String registrar(@RequestParam String nome,
                            @RequestParam String email,
                            @RequestParam String telefone,
                            @RequestParam String cpf,
                            @RequestParam String senha,
                            @RequestParam String confirmar_senha) {


        Comum usuario = new Comum();
        usuario.setNome(nome);
        usuario.setEmail(email);
        usuario.setTelefone(telefone);
        usuario.setCpf(cpf.replaceAll("[^0-9]", ""));
        usuario.setSenha(senha);
        usuario.setDataCadastro(java.time.LocalDateTime.now());
        usuario.setStatusUsuario(EnumStatusUsuario.ATIVO);
        try {
            java.io.InputStream is = getClass().getResourceAsStream("/static/images/perfilPadrao.jpg");

            if (is != null){
                usuario.setFoto(is.readAllBytes());
                is.close();
            }
        } catch (java.io.IOException e){
            e.printStackTrace();
        }

//        if (senha.length() < 8 || senha.matches(".[A-Z].") ||!senha.equals(confirmar_senha)) {

//            return "redirect:/registrar?error=senha_diferente";
//        }


        userservice.saveUser(usuario);


        return "redirect:/entrar";
    }


    @GetMapping("/registrarOrg")
    public String registrarOrg(HttpServletRequest request, Model model) {
        model.addAttribute("requestURI", request.getRequestURI());
        return "registrarOrg";
    }

    @PostMapping("/registrarOrg")
    public String registrarOrg(@RequestParam String nome,
                               @RequestParam String email,
                               @RequestParam String telefone,
                               @RequestParam String cnpj,
                               @RequestParam String inscricaoEstadual,
                               @RequestParam EnumTipoOrg tipoOrg,
                               @RequestParam String senha,
                               @RequestParam String confirmar_senha
                               ) {
        Organizacao usuario = new Organizacao();
        usuario.setNome(nome);
        usuario.setEmail(email);
        usuario.setTelefone(telefone);
        usuario.setCnpj(cnpj.replaceAll("[^0-9]", ""));
        usuario.setInscricaoEstadual(inscricaoEstadual.replaceAll("[^0-9]", ""));
        usuario.setTipoOrg(tipoOrg);
        usuario.setSenha(senha);
        usuario.setDataCadastro(java.time.LocalDateTime.now());
        usuario.setStatusUsuario(EnumStatusUsuario.ATIVO);
        try {
            java.io.InputStream is = getClass().getResourceAsStream("/static/images/perfilPadrao.jpg");

            if (is != null){
                usuario.setFoto(is.readAllBytes());
                is.close();
            }
        } catch (java.io.IOException e){
            e.printStackTrace();
        }

//        if (senha.length() < 8 || senha.matches(".[A-Z].") ||!senha.equals(confirmar_senha)) {
//            // Redireciona de volta para o registro com um parâmetro de erro
//            return "redirect:/registrar?error=senha_diferente";
//        }



        userservice.saveUser(usuario);


        return "redirect:/entrar";
    }
}