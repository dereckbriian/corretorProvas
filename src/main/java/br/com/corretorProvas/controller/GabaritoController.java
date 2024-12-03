package br.com.corretorProvas.controller;

import br.com.corretorProvas.service.GabaritoService;
import br.com.corretorProvas.service.ProvaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/gabarito")
public class GabaritoController {

    @Autowired
    private GabaritoService gabaritoService;

    @Autowired
    private ProvaService provaService;

    // Endpoint para o upload do arquivo de gabarito
    @PostMapping("/upload")
    public ResponseEntity<String> uploadGabarito(@RequestParam("file") MultipartFile file) {
        try {
            // Salva o arquivo temporariamente no servidor para processá-lo
            String tempFilePath = "caminho/desejado/" + file.getOriginalFilename();
            File tempFile = new File(tempFilePath);
            file.transferTo(tempFile);

            // Carrega o gabarito a partir do arquivo
            Map<Integer, String> gabarito = gabaritoService.loadGabaritoFromFile(tempFilePath);

            // Aqui você pode armazenar o gabarito para correção posterior
            // Por exemplo, salvar no contexto da aplicação ou em um banco de dados

            return ResponseEntity.ok("Gabarito carregado com sucesso!");
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Erro ao processar o arquivo: " + e.getMessage());
        }
    }

    // Endpoint para corrigir a prova do aluno
    @PostMapping("/corrigir")
    public ResponseEntity<String> corrigirProva(@RequestParam("respostas") String respostas) {
        String resultado = provaService.corrigirProva(respostas);
        return ResponseEntity.ok(resultado);
    }
}
