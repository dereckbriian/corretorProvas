package br.com.corretorProvas.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

@Controller
public class ClienteController {

    private static final String SERVER_ADDRESS = "127.0.0.1";
    private static final int SERVER_PORT = 12345;

    @GetMapping("/")
    public String exibirFormulario() {
        return "formulario"; // Renderiza o arquivo HTML
    }

    @PostMapping("/enviar-respostas")
    public String enviarRespostas(@RequestParam("arquivo") MultipartFile arquivo, Model model) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            // Verificar se o arquivo foi recebido
            if (arquivo.isEmpty()) {
                model.addAttribute("erro", "Nenhum arquivo foi enviado.");
                return "resultado";
            }

            // Ler o arquivo .txt
            BufferedReader reader = new BufferedReader(new InputStreamReader(arquivo.getInputStream()));
            String linha;
            while ((linha = reader.readLine()) != null) {
                out.println(linha);  // Aqui vai enviar cada linha para o servidor
            }
            out.println();  // Enviar uma linha vazia
            socket.shutdownOutput();  // fecha o socket indicando que não vai ter mais envio

            // Receber o resultado do servidor
            String resultado = in.readLine();
            model.addAttribute("resultado", resultado);

        } catch (IOException e) {
            model.addAttribute("erro", "Erro ao conectar ao servidor: " + e.getMessage());
        }
        return "resultado";
    }
}
