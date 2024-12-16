package br.com.corretorProvas;

import java.io.*;
import java.net.Socket;

public class Cliente {
    //Endereço e porta do servidor
    private static final String SERVER_ADDRESS = "127.0.0.1";
    private static final int SERVER_PORT = 12345;

    public static void main(String[] args) {
        // Bloco try-with-resources pra fechamento automatico
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT); // Cria uma conexão de socket com o servidor
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true); // Fluxo de saída para enviar dados ao servidor
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {// Fluxo de entrada para ler dados do servidor

            // Enviar o arquivo com respostas
            try (BufferedReader fileReader = new BufferedReader(new FileReader("src/main/java/br/com/corretorProvas/Respostas.txt"))) {
                //abre o arquivo com as respostas
                String linha;
                while ((linha = fileReader.readLine()) != null) {
                    //envia a linha pro servidor
                    out.println(linha);
                }
                System.out.println("Arquivo de respostas carregado e enviado ao servidor.");
            }
            // Sinalizar que o cliente terminou de enviar dados
            out.println(); // Enviar uma linha vazia
            socket.shutdownOutput(); // Indicar que não haverá mais envio

            // Receber o resultado do servidor
            String resultado = in.readLine();
            if (resultado != null) {
                System.out.println("Resultado: " + resultado);
            } else {
                System.out.println("Nenhum resultado recebido do servidor.");
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}