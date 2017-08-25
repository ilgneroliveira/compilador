/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.company;

public class RegistroTabelaSimbolos {
    private int token;
    private String lexema;
    private String classe;
    private String tipo;
    private String endereco;
    private RegistroTabelaSimbolos proximo; 

    public RegistroTabelaSimbolos getProximo() {
        return proximo;
    }

    public void setProximo(RegistroTabelaSimbolos proximo) {
        this.proximo = proximo;
    }

    public RegistroTabelaSimbolos(int token, String lexema){
        this.token = token;
        this.lexema = lexema;
        this.tipo = "";
        this.classe = "";
        this.endereco = "";
    }

    public int getToken() {
        return token;
    }

    public void setToken(int token) {
        this.token = token;
    }

    public String getLexema() {
        return lexema;
    }

    public void setClasse(String classe) {
        this.classe = classe;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }


    public void setLexema(String lexema) {
        this.lexema = lexema;
    }

    public String getClasse() {
        return classe;
    }
}