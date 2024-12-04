package br.com.corretorProvas;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Servidor {
    private static final int PORT = 12345;
    private static Map<Integer, String> gabarito = new HashMap<>();

    public static void main(String[] args) {
        carregarGabarito();

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Servidor aguardando conexões na porta " + PORT);

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Cliente conectado: " + socket.getInetAddress().getHostAddress());
                new Thread(new ClienteHandler(socket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void carregarGabarito() {
        try (BufferedReader br = new BufferedReader(new FileReader("src/main/java/br/com/corretorProvas/Gabarito.txt"))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] partes = linha.split("-");
                int numeroQuestao = Integer.parseInt(partes[0]);
                String resposta = partes[1];
                gabarito.put(numeroQuestao, resposta);
            }
            System.out.println("Gabarito carregado: " + gabarito);
        } catch (IOException e) {
            System.err.println("Erro ao carregar o gabarito: " + e.getMessage());
        }
    }


    private static class ClienteHandler implements Runnable {
        private Socket socket;

        public ClienteHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) { // Auto-flush habilitado

                String linha;
                int acertos = 0, erros = 0;

                System.out.println("Iniciando processamento do cliente...");

                // Processar respostas do cliente
                while ((linha = in.readLine()) != null && !linha.isEmpty()) { // Verifica fim da entrada
                    System.out.println("Linha recebida do cliente: " + linha);
                    try {
                        String[] partes = linha.split("-");
                        int numeroQuestao = Integer.parseInt(partes[0]);
                        String respostaCliente = partes[1];

                        String respostaCorreta = gabarito.get(numeroQuestao);
                        if (respostaCorreta != null && respostaCorreta.equals(respostaCliente)) {
                            acertos++;
                        } else {
                            erros++;
                        }
                    } catch (Exception e) {
                        System.err.println("Erro ao processar linha: " + linha + " - " + e.getMessage());
                    }
                }

                // Enviar o resultado de volta ao cliente
                String resultado = acertos + "-" + erros;
                out.println(resultado); // Envia o resultado
                System.out.println("Resultado enviado ao cliente: " + resultado);

            } catch (IOException e) {
                System.err.println("Erro ao processar cliente: " + e.getMessage());
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


    }
}