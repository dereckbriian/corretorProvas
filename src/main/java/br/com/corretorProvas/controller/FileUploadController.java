package br.com.corretorProvas.controller;

import br.com.corretorProvas.service.GabaritoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class FileUploadController {

    @Autowired
    private GabaritoService gabaritoService;

    // Alterando a rota para /upload
    @GetMapping("/upload")
    public String showUploadForm() {
        return "index";
    }

    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file, Model model) {
        try {
            String result = gabaritoService.processFiles(file);
            model.addAttribute("result", result);
            return "result";
        } catch (Exception e) {
            model.addAttribute("error", "Erro ao processar o arquivo: " + e.getMessage());
            return "result";
        }
    }
}

