package br.com.corretorProvas.service;

import br.com.corretorProvas.model.Resultado;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CorrecaoService {

    private final Map<Integer, String> gabarito = new HashMap<>();

    public CorrecaoService() {
        gabarito.put(1, "VFVVF");
        gabarito.put(2, "VVFFF");
        gabarito.put(3, "FFFVV");
    }

    public Resultado processarArquivo(String conteudo) {
        int acertos = 0, erros = 0;

        String[] linhas = conteudo.split("\n");
        for (String linha : linhas) {
            String[] partes = linha.split("-");
            int numeroQuestao = Integer.parseInt(partes[0].trim());
            String respostas = partes[1].trim();

            String gabaritoQuestao = gabarito.get(numeroQuestao);
            if (gabaritoQuestao != null) {
                for (int i = 0; i < respostas.length(); i++) {
                    if (respostas.charAt(i) == gabaritoQuestao.charAt(i)) {
                        acertos++;
                    } else {
                        erros++;
                    }
                }
            }
        }
        return new Resultado(acertos, erros);
    }
}
