package com.example.AminalReport.entities.formularios;

import com.example.AminalReport.entities.enums.EnumTipoAnimal;
import com.example.AminalReport.entities.usuarios.Usuario;
import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "tipo", discriminatorType = DiscriminatorType.STRING)
public class Formulario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "BYTEA")
    @Lob
    private byte[] foto;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EnumTipoAnimal tipoAnimal;

    @Column
    @Lob
    private String descricao;

    @Column(name = "usuario_criaddor_id", nullable = false)
    @ManyToOne
    private Usuario usuarioCriador;

    @Column(nullable = false)
    private String cep;

    @Column
    private String rua;

    @Column
    private String bairro;

    @Column
    private String municipio;

    @Column
    private String estado;

    @Column
    private String complemento;

    //Construtores
    public Formulario () {}

    public Formulario (byte[] foto, EnumTipoAnimal tipoAnimal, String descricao, Usuario usuarioCriador, String cep, String rua, String bairro, String municipio, String estado, String complemento) {
        this.foto = foto;
        this.tipoAnimal = tipoAnimal;
        this.descricao = descricao;
        this.usuarioCriador = usuarioCriador;
        this.cep = cep;
        this.rua = rua;
        this.bairro = bairro;
        this.municipio = municipio;
        this.estado = estado;
        this.complemento = complemento;
    }


    //Getters e Setters

    public Long getId() {
        return id;
    }

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }

    public EnumTipoAnimal getTipoAnimal() {
        return tipoAnimal;
    }

    public void setTipoAnimal(EnumTipoAnimal tipoAnimal) {
        this.tipoAnimal = tipoAnimal;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Usuario getUsuarioCriador() {
        return usuarioCriador;
    }

    public void setUsuarioCriador(Usuario usuarioCriador) {
        this.usuarioCriador = usuarioCriador;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }
}
