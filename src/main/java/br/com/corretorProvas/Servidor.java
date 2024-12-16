package br.com.corretorProvas;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap; // Importa a classe Hashmap
import java.util.Map;

public class Servidor {
    /*Porta do servidor*/
    private static final int PORT = 12345;
    /* Cria um mapa estático para armazenar o gabarito, a chave é o número da questão e o valor é a resposta correta*/
    private static Map<Integer, String> gabarito = new HashMap<>();

    public static void main(String[] args) {
        //Chama a função pra carregar o gabarito
        carregarGabarito();

        // Bloco try-with-resources para criar um ServerSocket que será fechado automaticamente
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            // Printa que o servidor esta pronto pra aceitar conexões
            System.out.println("Servidor aguardando conexões na porta " + PORT);
            // Loop infinito para aceitar múltiplas conexões dos clientes
            while (true) {
                //Aguarda pra aceitar uma nova conexão
                Socket socket = serverSocket.accept();
                //Exibe no console o IP do cliente conectado
                System.out.println("Cliente conectado: " + socket.getInetAddress().getHostAddress());
                // Cria uma nova thread para lidar com o cliente conectado, evitando bloquear o servidor
                /*Para cada cliente que se conecta, uma nova thread é criada*/
                new Thread(new ClienteHandler(socket)).start();
            }
        } catch (IOException e) {
            //Captura erros e exibe caso exista
            e.printStackTrace();
        }
    }

    private static void carregarGabarito() {
        // Bloco try-with-resources para garantir que o BufferedReader seja fechado automaticamente.
        // O BufferedReader é usado para ler o conteúdo de um arquivo linha por linha.
        try (BufferedReader br = new BufferedReader(new FileReader("src/main/java/br/com/corretorProvas/Gabarito.txt"))) {
            String linha;
            // Loop para ler cada linha do arquivo até que não tenha mais linhas
            while ((linha = br.readLine()) != null) {
                // Divide a linha lida em duas partes: número da questão e resposta
                String[] partes = linha.split("-");
                // Converte o numero da questão pra inteiro
                int numeroQuestao = Integer.parseInt(partes[0]);
                // Armazena a segunda parte da linha como a resposta correta da questão
                String resposta = partes[1];
                // Insere o número da questão e a resposta no mapa
                gabarito.put(numeroQuestao, resposta);
            }
            //Só de confirmação printa o gabarito que foi carregado quando terminar
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