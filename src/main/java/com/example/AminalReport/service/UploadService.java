package com.example.AminalReport.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class UploadService {

    public String salvarImagem(
            MultipartFile arquivo,
            String pasta
    ) throws IOException {

        if (arquivo.isEmpty()) {
            return null;
        }

        String nomeArquivo = UUID.randomUUID()
                + "_" + arquivo.getOriginalFilename();

        Path caminho = Paths.get(
                "uploads/" + pasta + "/" + nomeArquivo
        );

        Files.createDirectories(caminho.getParent());

        Files.write(caminho, arquivo.getBytes());

        return pasta + "/" + nomeArquivo;
    }

    public void deletarImagem(String fotoPath) throws IOException {

        if (fotoPath == null || fotoPath.isBlank()) {
            return;
        }

        Path caminho = Paths.get("uploads")
                .resolve(fotoPath);

        Files.deleteIfExists(caminho);
    }
}