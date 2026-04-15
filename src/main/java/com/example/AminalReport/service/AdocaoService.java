package com.example.AminalReport.service;

import com.example.AminalReport.entities.formularios.Adocao;
import com.example.AminalReport.repository.formularios.AdocaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdocaoService {

    @Autowired
    private AdocaoRepository adocaoRepository;

    public void cadastrarAdocao(Adocao adocao){
        Optional<Adocao> adocaoExistente = adocaoRepository.findByNomeAnimalAndUsuarioCriador(adocao.getNomeAnimal(), adocao.getUsuarioCriador());

        if (adocaoExistente.isPresent()){
            throw new IllegalArgumentException("Registro de adoção já cadastrado.");
        }
        else if(adocao.getIdadeEstimada() < 0){
            throw new IllegalArgumentException("Idade não pode ser negativa.");
        }
        else{
            adocaoRepository.save(adocao);
        }
    }
}
