package br.com.corretorProvas.controller;

import br.com.corretorProvas.service.ProvaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.ui.Model;
import java.io.*;
import java.nio.file.Files;

@Controller
public class ResultadosController {

    @Autowired
    private ProvaService provaService;

    // Exibe o formulário para envio de arquivo
    @GetMapping("/")
    public String index() {
        return "index";
    }

    // Processa o arquivo enviado e exibe os resultados
    @PostMapping("/corrigir")
    public String corrigirProva(@RequestParam("file") MultipartFile file, Model model) {
        try {
            // Lê o conteúdo do arquivo de respostas
            String respostas = new String(file.getBytes());

            // Corrige a prova
            String resultado = provaService.corrigirProva(respostas);

            // Adiciona o resultado ao modelo para exibição
            model.addAttribute("resultado", resultado);

        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("erro", "Erro ao processar o arquivo.");
        }

        return "resultado";
    }
}
