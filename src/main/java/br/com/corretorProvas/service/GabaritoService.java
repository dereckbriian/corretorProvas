package br.com.corretorProvas.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

@Service
public class GabaritoService {

    // Caminho onde o arquivo do gabarito está armazenado
    private static final String GABARITO_FILE_PATH = "src/main/resources/gabarito.txt";

    public String processFiles(MultipartFile file) throws IOException {
        // Carregar o gabarito
        List<String> gabarito = readGabarito();

        // Carregar as respostas enviadas pelo cliente
        List<String> respostas = readRespostas(file);

        int correctAnswers = 0;
        int wrongAnswers = 0;

        // Comparar as respostas com o gabarito
        for (int i = 0; i < respostas.size(); i++) {
            if (respostas.get(i).equals(gabarito.get(i))) {
                correctAnswers++;
            } else {
                wrongAnswers++;
            }
        }

        // Retornar o resultado
        return correctAnswers + "-" + wrongAnswers;
    }

    private List<String> readGabarito() throws IOException {
        // Lê o arquivo do gabarito
        return Files.readAllLines(Paths.get(GABARITO_FILE_PATH));
    }

    private List<String> readRespostas(MultipartFile file) throws IOException {
        // Lê as respostas enviadas pelo cliente
        return new BufferedReader(new InputStreamReader(file.getInputStream()))
                .lines()
                .collect(Collectors.toList());
    }
}
