package com.example.AminalReport.repository;

import com.example.AminalReport.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;


// Repositório para a entidade Usuario
public interface UserRepository extends JpaRepository <Usuario,Long>{
    // O Spring gera o SQL: SELECT * FROM Usuario WHERE email = ?
    Usuario findByEmail(String email);
}
