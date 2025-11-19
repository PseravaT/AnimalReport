package com.example.AminalReport.repository.formularios;

import com.example.AminalReport.entities.formularios.Adocao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdocaoRepository extends JpaRepository<Adocao, Long> {
    Adocao findByNomeAnimal (String nome);
}
