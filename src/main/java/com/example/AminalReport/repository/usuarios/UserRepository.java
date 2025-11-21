package com.example.AminalReport.repository.usuarios;

import com.example.AminalReport.entities.usuarios.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


// Repositório para a entidade Usuario
public interface UserRepository extends JpaRepository <Usuario,Long>{
    // O Spring gera o SQL: SELECT * FROM Usuario WHERE email = ?
    Optional<Usuario> findByEmail(String email);

}
