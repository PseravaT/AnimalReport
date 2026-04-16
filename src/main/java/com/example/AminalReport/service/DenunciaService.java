package com.example.AminalReport.service;

import com.example.AminalReport.entities.formularios.Denuncia;
import com.example.AminalReport.entities.usuarios.Usuario;
import com.example.AminalReport.repository.formularios.DenunciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional; // <--- Importante adicionar este import

@Service
public class DenunciaService {

    @Autowired
    private DenunciaRepository denunciaRepository;

    // Salvar denúncia
    public void saveDenuncia(Denuncia denuncia){
        denunciaRepository.save(denuncia);
    }

    public List<Denuncia> loadDenunciaByUser (Usuario user){
        return denunciaRepository.findByUsuarioCriador(user);
    }


    public Optional<Denuncia> buscarPorId(Long id) {
        return denunciaRepository.findById(id);
    }

    public void editarDenuncia(Denuncia denuncia){
        Optional<Denuncia> denunciaOpt = denunciaRepository.findById(denuncia.getId());
        if (denunciaOpt.isPresent()) {
            Denuncia denunciaExistente = denunciaOpt.get();
            if (denuncia.getDescricao() != null){
                denunciaExistente.setDescricao(denuncia.getDescricao());
            }
            denunciaExistente.setUrgencia(denuncia.getUrgencia());
            if (denuncia.getPontoRef() != null) {
                denunciaExistente.setPontoRef(denuncia.getPontoRef());
            }
            if (denuncia.getFoto() != null){
                denunciaExistente.setFoto(denuncia.getFoto());
            }
        }
        denunciaRepository.save(denuncia);
    }
}
