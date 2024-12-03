package br.com.corretorProvas.service;

import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ProvaService {

    private GabaritoService gabaritoService = new GabaritoService();

    public String corrigirProva(String respostas) {
        // Carregar o gabarito do arquivo
        Map<Integer, String> gabarito = gabaritoService.loadGabaritoFromFile("src/main/resources/gabarito.txt");

        // Inicializa os contadores de acertos e erros
        int acertos = 0;
        int erros = 0;

        // Itera sobre as respostas recebidas e compara com o gabarito
        String[] respostasAluno = respostas.split("\n");  // Supõe-se que as respostas do aluno estejam em várias linhas, uma por questão

        for (String resposta : respostasAluno) {
            String[] partes = resposta.split("-");
            int numeroQuestao = Integer.parseInt(partes[0]);
            String respostasAlunoQuestao = partes[1];

            // Compara a resposta do aluno com o gabarito para a questão correspondente
            if (gabarito.containsKey(numeroQuestao) && gabarito.get(numeroQuestao).equals(respostasAlunoQuestao)) {
                acertos++;
            } else {
                erros++;
            }
        }

        return acertos + " acertos e " + erros + " erros.";
    }
}
