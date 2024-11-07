package br.com.corretorProvas.controller;

import br.com.corretorProvas.service.ReceberArquivo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.stereotype.Controller;

@Controller
public class ArquivoController {

    @Autowired
    private ReceberArquivo receberArquivo;

    @GetMapping("/redirect")
    public String redirectToExternalPage() {
        return "redirect:" + receberArquivo.redirecionar();
    }

    @GetMapping("/")
    public String inserirArquivo() {
        return "arquivos";
    }
}
