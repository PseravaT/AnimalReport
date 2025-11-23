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
    // Componente para persistir a sessão de segurança
    private final SecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();

    // Injeção de Dependência via construtor
    public LoginController(UserService userservice) {
        this.userservice = userservice;
    }

    // Exibir página de login
    @GetMapping("/entrar")
    public String entrar() {
        return "login";
    }

    // Processar submissão de login
    @PostMapping("/entrar")
    public String login(@RequestParam String email,
                        @RequestParam String senha,
                        HttpServletRequest request,
                        HttpServletResponse response) {

        // Busca o usuário no DB
        Usuario usuario = userservice.loadUserByEmail(email);

        // 1. Validação: Se usuário não existe ou senha incorreta
        if (usuario == null || !userservice.verifyPassword(senha, usuario.getSenha())) {
            // Redireciona para a página de login com erro
            return "redirect:/entrar?error=true";

        }

        // 2. Autenticação Manual no Spring Security
        // Pega o perfil (Ex: "ROLE_USER" ou "ROLE_ADMIN")
        String perfilDoUsuario = usuario.getClass().getSimpleName();
        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(perfilDoUsuario));

        // Cria o token de autenticação
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(usuario, null, authorities);

        // Define o contexto de segurança
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);

        System.out.println("Usuário autenticado com sucesso: " + SecurityContextHolder.getContext().getAuthentication().getName());

        // 3. Persistência da Sessão
        // Salva o contexto na sessão HTTP
        securityContextRepository.saveContext(context, request, response);

        // Redirecionamento condicional
        if ("ROLE_ADMIN".equals(perfilDoUsuario)) {
            return "redirect:/admin/dashboard";
        }

        else if ("ORGANIZAÇÃO".equals(perfilDoUsuario))
            return "redirect:/homeOrg";

        else
            // Redireciona para a home page, mapeada pelo HomeController
            return "redirect:/home";
    }

    // Exibe a página de registro.
    @GetMapping("/registrar")
    public String registrar( ) {
        return "registrar";
    }

    // Processar submissão de registro (capturando todos os campos)
    @PostMapping("/registrar")
    public String registrar(@RequestParam String nome,
                            @RequestParam String email,
                            @RequestParam String telefone,
                            @RequestParam String cpf,
                            @RequestParam String senha,
                            @RequestParam String confirmar_senha) {

        // Cria e preenche o objeto Usuario
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
//            // Redireciona de volta para o registro com um parâmetro de erro
//            return "redirect:/registrar?error=senha_diferente";
//        }

        // Salva (e criptografa a senha)
        userservice.saveUser(usuario);

        // Redireciona para a página de login para que o novo usuário possa acessar
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
// Verifica se a senha tem mais de 8 caracteres e possui uma letra maiúscula
//        if (senha.length() < 8 || senha.matches(".[A-Z].") ||!senha.equals(confirmar_senha)) {
//            // Redireciona de volta para o registro com um parâmetro de erro
//            return "redirect:/registrar?error=senha_diferente";
//        }


        // Salva (e criptografa a senha)
        userservice.saveUser(usuario);


        return "redirect:/entrar";
    }
}