package com.example.AminalReport.controller;

import com.example.AminalReport.entities.usuarios.Comum;
import com.example.AminalReport.entities.usuarios.Usuario;
import com.example.AminalReport.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

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

        // Redireciona para a home page, mapeada pelo HomeController
        return "redirect:/home";
    }

    // Exibe a página de registro.
    @GetMapping("/registrar")
    public String registrar() {
        return "registrar";
    }

    // Processar submissão de registro (capturando todos os campos)
    @PostMapping("/registrar")
    public String registrar(@RequestParam String nome,
                            @RequestParam String email,
                            @RequestParam String telefone,
                            @RequestParam String cpf,
                            @RequestParam String senha) {

        // Cria e preenche o objeto Usuario
        Comum usuario = new Comum();
        usuario.setNome(nome);
        usuario.setEmail(email);
        usuario.setTelefone(telefone);
        usuario.setCpf(cpf);
        usuario.setSenha(senha);
        // Garante que novos usuários sejam criados com o perfil padrão, se não estiver definido na Entidade.
        // usuario.setPerfil("ROLE_USER");

        // Salva (e criptografa a senha)
        userservice.saveUser(usuario);

        // Redireciona para a página de login para que o novo usuário possa acessar
        return "redirect:/";
    }
}