package com.example.AminalReport.entities.formularios;

import com.example.AminalReport.entities.enums.EnumAndamentoDenuncia;
import com.example.AminalReport.entities.enums.EnumNivelUrgencia;
import com.example.AminalReport.entities.enums.EnumTipoAnimal;
import com.example.AminalReport.entities.usuarios.Organizacao;
import com.example.AminalReport.entities.usuarios.Usuario;
import jakarta.persistence.*;

@Entity
@DiscriminatorValue("DENUNCIA")
public class Denuncia extends Formulario {

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EnumNivelUrgencia urgencia;

    @JoinColumn(nullable = true)
    @ManyToOne
    private Organizacao organizacaoResponsavel;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EnumAndamentoDenuncia andamentoDenuncia;

    //Construtores
    public Denuncia () {}

    public Denuncia(byte[] foto, EnumTipoAnimal tipoAnimal, String pontoRef, String descricao, Usuario usuarioCriador, String cep, String rua, String bairro, String municipio, String estado, String contato, EnumNivelUrgencia urgencia, Organizacao organizacaoResponsavel, EnumAndamentoDenuncia andamentoDenuncia) {
        super(foto, tipoAnimal, pontoRef, descricao, usuarioCriador, cep, rua, bairro, municipio, estado, contato);
        this.urgencia = urgencia;
        this.organizacaoResponsavel = organizacaoResponsavel;
        this.andamentoDenuncia = andamentoDenuncia;
    }

//Getters e Setters

    public EnumNivelUrgencia getUrgencia() {
        return urgencia;
    }

    public void setUrgencia(EnumNivelUrgencia urgencia) {
        this.urgencia = urgencia;
    }

    public Organizacao getOrganizacaoResponsavel() {
        return organizacaoResponsavel;
    }

    public void setOrganizacaoResponsavel(Organizacao organizacaoResponsavel) {
        this.organizacaoResponsavel = organizacaoResponsavel;
    }

    public EnumAndamentoDenuncia getAndamentoDenuncia() {
        return andamentoDenuncia;
    }

    public void setAndamentoDenuncia(EnumAndamentoDenuncia andamentoDenuncia) {
        this.andamentoDenuncia = andamentoDenuncia;
    }
}
