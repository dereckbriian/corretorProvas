package br.com.corretorProvas.model;

public class Resultado {
    private int acertos;
    private int erros;

    public Resultado(int acertos, int erros) {
        this.acertos = acertos;
        this.erros = erros;
    }

    public int getAcertos() {
        return acertos;
    }

    public int getErros() {
        return erros;
    }
}
