package com.example.AminalReport.service;

import com.example.AminalReport.entities.enums.EnumAndamentoAdocao;
import com.example.AminalReport.entities.formularios.Adocao;
import com.example.AminalReport.repository.formularios.AdocaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
            adocao.setStatusAdocao(EnumAndamentoAdocao.AGUARDANDO);
            adocaoRepository.save(adocao);
        }
    }

    public void alterarAdocao(Long id, MultipartFile foto, String nomeAnimal, Integer idadeEstimada, String descricao, String contato, EnumAndamentoAdocao status) throws IOException {
        Optional<Adocao> adocao = adocaoRepository.findById(id);
        if (adocao.isPresent()){
            if (!foto.isEmpty()){
                adocao.get().setFoto(foto.getBytes());
            }
            if(!nomeAnimal.isBlank()){
                adocao.get().setNomeAnimal(nomeAnimal);
            }
            if(idadeEstimada != null && idadeEstimada > 0) {
                adocao.get().setIdadeEstimada(idadeEstimada);
            }
            if(!descricao.isBlank()){
                adocao.get().setDescricao(descricao);
            }
            if(!contato.isBlank()){
                adocao.get().setContato(contato);
            }
            if(status != null){
                adocao.get().setStatusAdocao(status);
            }
            adocaoRepository.save(adocao.get());
        }

    }
}
