package com.example.AminalReport.entities.logs;

import com.example.AminalReport.entities.usuarios.Usuario;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
public class Log {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private TipoAcao acao;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Usuario usuario;

    @Column(nullable = false)
    @CreationTimestamp
    private LocalDateTime data;

    @Column(nullable = false)
    private String entidadeAlterada;

    @Column
    private String antes;

    @Column
    private String depois;

    public enum TipoAcao {
        INSERT,
        UPDATE,
        DELETE
    }

    //Construtores
    public Log() {}

    public Log(TipoAcao acao, Usuario usuario, LocalDateTime data, String entidadeAlterada, String antes, String depois) {
        this.acao = acao;
        this.usuario = usuario;
        this.data = data;
        this.entidadeAlterada = entidadeAlterada;
        this.antes = antes;
        this.depois = depois;
    }

    //Getters e Setters

    public Long getId() {
        return id;
    }

    public TipoAcao getAcao() {
        return acao;
    }

    public void setAcao(TipoAcao acao) {
        this.acao = acao;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    public String getEntidadeAlterada() {
        return entidadeAlterada;
    }

    public void setEntidadeAlterada(String entidadeAlterada) {
        this.entidadeAlterada = entidadeAlterada;
    }

    public String getAntes() {
        return antes;
    }

    public void setAntes(String antes) {
        this.antes = antes;
    }

    public String getDepois() {
        return depois;
    }

    public void setDepois(String depois) {
        this.depois = depois;
    }
}
