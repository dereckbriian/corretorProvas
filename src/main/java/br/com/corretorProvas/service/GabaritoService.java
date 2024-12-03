package br.com.corretorProvas.service;

import org.springframework.stereotype.Service;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

@Service
public class GabaritoService {


    public Map<Integer, String> loadGabaritoFromFile(String filePath) throws IOException {
        Map<Integer, String> gabarito = new HashMap<>();
        File file = new File(filePath);

        // Verifica se o arquivo existe
        if (!file.exists()) {
            throw new FileNotFoundException("Arquivo não encontrado: " + filePath);
        }

        // Lê o arquivo de gabarito
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String linha;

        while ((linha = reader.readLine()) != null) {
            // Divide a linha do arquivo em número da questão e respostas
            String[] partes = linha.split("-");
            if (partes.length == 2) {
                int numeroQuestao = Integer.parseInt(partes[0]);
                String respostas = partes[1];
                gabarito.put(numeroQuestao, respostas);
            }
        }

        reader.close();
        return gabarito;
    }
}
