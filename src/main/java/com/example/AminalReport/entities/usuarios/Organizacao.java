package com.example.AminalReport.entities.usuarios;

import com.example.AminalReport.entities.enums.EnumStatusUsuario;
import com.example.AminalReport.entities.enums.EnumTipoOrg;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@DiscriminatorValue("ORGANIZAÇÃO")
public class Organizacao extends Usuario{

    @Column(nullable = false, length = 14)
    private String cnpj;

    @Column(nullable = false, length = 14)
    private String inscricaoEstadual;

    @Column
    @Enumerated(EnumType.STRING)
    private EnumTipoOrg tipoOrg;

    //Construtores

    public Organizacao () {}

    public Organizacao(String nome, String email, String telefone, String senha, LocalDateTime dataCadastro, EnumStatusUsuario statusUsuario, byte[] foto, String cnpj, String inscricaoEstadual, EnumTipoOrg tipoOrg) {
        super(nome, email, telefone, senha, dataCadastro, statusUsuario, foto);
        this.cnpj = cnpj;
        this.inscricaoEstadual = inscricaoEstadual;
        this.tipoOrg = tipoOrg;
    }

    //Getters e Setters


    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getInscricaoEstadual() {
        return inscricaoEstadual;
    }

    public void setInscricaoEstadual(String inscricaoEstadual) {
        this.inscricaoEstadual = inscricaoEstadual;
    }

    public EnumTipoOrg getTipoOrg() {
        return tipoOrg;
    }

    public void setTipoOrg(EnumTipoOrg tipoOrg) {
        this.tipoOrg = tipoOrg;
    }
}
