package com.example.AminalReport.entities.formularios;

import com.example.AminalReport.entities.enums.EnumTipoAnimal;
import com.example.AminalReport.entities.usuarios.Usuario;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.util.Base64;


@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "tipo", discriminatorType = DiscriminatorType.STRING)
public class Formulario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "BYTEA")
    @Lob
    @JdbcTypeCode(SqlTypes.BINARY)
    private byte[] foto;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EnumTipoAnimal tipoAnimal;

    @Column (columnDefinition = "TEXT")
    private String descricao;

    @JoinColumn(name = "usuario_criaddor_id")
    @ManyToOne
    private Usuario usuarioCriador;

    @Column
    private String contato;

    //Construtores
    public Formulario () {}

    public Formulario(byte[] foto, EnumTipoAnimal tipoAnimal, String descricao, Usuario usuarioCriador, String contato) {
        this.foto = foto;
        this.tipoAnimal = tipoAnimal;
        this.descricao = descricao;
        this.usuarioCriador = usuarioCriador;
        this.contato = contato;
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

    public String getContato() { return contato; }

    public void setContato (String contato) {this.contato = contato; }

    public String getFotoBase64() {
        if (this.getFoto() != null && this.getFoto().length > 0) {
            return Base64.getEncoder().encodeToString(this.getFoto());
        }
        return null;
    }
}

