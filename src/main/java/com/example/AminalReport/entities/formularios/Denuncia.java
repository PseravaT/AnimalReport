package com.example.AminalReport.entities.formularios;

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

    //Construtores
    public Denuncia () {}

    public Denuncia(byte[] foto, EnumTipoAnimal tipoAnimal, String descricao, Usuario usuarioCriador, String cep, String rua, String bairro, String municipio, String estado, String complemento, EnumNivelUrgencia urgencia, Organizacao organizacaoResponsavel, String contato) {
        super(foto, tipoAnimal, descricao, usuarioCriador, cep, rua, bairro, municipio, estado, complemento, contato);
        this.urgencia = urgencia;
        this.organizacaoResponsavel = organizacaoResponsavel;
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
}
