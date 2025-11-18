package com.example.AminalReport.entities.formularios;

import com.example.AminalReport.entities.usuarios.Usuario;
import com.example.AminalReport.entities.enums.EnumTipoAnimal;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("ADOCAO")
public class Adocao extends Formulario{

    @Column
    private String nomeAnimal;

    @Column
    private Integer idadeEstimada;

    //Construtores
    public Adocao () {}

    public Adocao(byte[] foto, EnumTipoAnimal tipoAnimal, String descricao, Usuario usuarioCriador, String cep, String rua, String bairro, String municipio, String estado, String complemento, String nomeAnimal, Integer idadeEstimada) {
        super(foto, tipoAnimal, descricao, usuarioCriador, cep, rua, bairro, municipio, estado, complemento);
        this.nomeAnimal = nomeAnimal;
        this.idadeEstimada = idadeEstimada;
    }

    //Getters e Setters

    public String getNomeAnimal() {
        return nomeAnimal;
    }

    public void setNomeAnimal(String nomeAnimal) {
        this.nomeAnimal = nomeAnimal;
    }

    public Integer getIdadeEstimada() {
        return idadeEstimada;
    }

    public void setIdadeEstimada(Integer idadeEstimada) {
        this.idadeEstimada = idadeEstimada;
    }
}
