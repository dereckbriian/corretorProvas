package br.com.corretorProvas;

import java.io.*;
import java.net.Socket;

public class Cliente {
    private static final String SERVER_ADDRESS = "127.0.0.1";
    private static final int SERVER_PORT = 12345;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            // Enviar o arquivo com respostas
            try (BufferedReader fileReader = new BufferedReader(new FileReader("src/main/java/br/com/corretorProvas/Respostas.txt"))) {
                String linha;
                while ((linha = fileReader.readLine()) != null) {
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