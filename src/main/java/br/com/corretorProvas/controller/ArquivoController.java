package br.com.corretorProvas.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.stereotype.Controller;

@Controller
public class ArquivoController {

    @GetMapping("/")
    public String inserirArquivo() {
        return "arquivos";
    }
}
