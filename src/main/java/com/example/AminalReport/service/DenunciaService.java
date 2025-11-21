package com.example.AminalReport.service;

import com.example.AminalReport.entities.formularios.Denuncia;
import com.example.AminalReport.entities.usuarios.Usuario;
import com.example.AminalReport.repository.formularios.DenunciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DenunciaService {

    @Autowired
    private DenunciaRepository denunciaRepository;

    public void saveDenuncia(Denuncia denuncia){
        denunciaRepository.save(denuncia);
    }

    public Denuncia loadDenunciaByUser (Usuario user){
        return denunciaRepository.findByUsuarioCriador(user);
    }
}
