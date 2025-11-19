package com.example.AminalReport.repository.usuarios;

import com.example.AminalReport.entities.usuarios.Organizacao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganizacaoRepository extends JpaRepository<Organizacao, Long> {

    Organizacao findByCnpj (String cnpj);

    Organizacao findByInscricaoEstadual (String inscricaoEstadual);
}
