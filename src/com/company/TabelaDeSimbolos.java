/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.company;

import com.sun.org.apache.regexp.internal.RE;
import javafx.scene.control.Tab;

import java.util.ArrayList;
import java.util.List;

public class TabelaDeSimbolos {
    public static int BYTE = 0;
    public static int INT = 1;
    public static int BOOLEAN = 2;
    public static int STRING = 3;
    public static int SUBLINHADO = 4;
    public static int PONTO = 5;
    public static int VIRGULA = 6;
    public static int PONTO_E_VIRGULA = 7;
    public static int DOIS_PONTOS = 8;
    public static int COLCHETES = 9;
    public static int CHAVES = 10;
    public static int MAIS = 11;
    public static int MENOS = 12;
    public static int ASPAS_DUPLAS = 13;
    public static int APOSTOFRO = 14;
    public static int BARRA = 15;
    public static int BARRA_INVERTIDA = 16;
    public static int BARRA_EM_PE = 17;
    public static int EXCLAMACAO = 18;
    public static int INTERROGACAO = 19;
    public static int MAIOR = 20;
    public static int MENOR = 21;
    public static int IGUAL = 22;
    public static int CONSTANTES = 23;
    public static int IDENTIFICADORES = 24;
    public static int FINAL = 25;
    public static int WHILE = 26;
    public static int IF = 27;
    public static int ELSE = 28;
    public static int AND = 29;
    public static int OR = 30;
    public static int NOT = 31;
    public static int IGUAL_IGUAL = 32;
    public static int PARENTESES = 33;
    public static int DIFERENTE = 34;
    public static int MAIOR_IGUAL = 35;
    public static int MENOR_IGUAL = 36;
    public static int MULTIPLICACAO = 37;
    public static int CHAVE_ABRE = 38;
    public static int CHAVE_FECHA = 39;
    public static int READLN = 40;
    public static int WRITE = 41;
    public static int WRITELN = 42;
    public static int TRUE = 43;
    public static int FALSE = 44;

    public RegistroTabelaSimbolos primeiro;
    public RegistroTabelaSimbolos ultimo;

    public int endSegmentoDados = 16384;
    public int endTemp = 0;

    public TabelaDeSimbolos() {
        inserir(BYTE, "byte");
        inserir(INT, "int");
        inserir(BOOLEAN, "boolean");
        inserir(STRING, "string");
        inserir(SUBLINHADO, "_");
        inserir(PONTO, ".");
        inserir(VIRGULA, ",");
        inserir(PONTO_E_VIRGULA, ";");
        inserir(DOIS_PONTOS, ":");
        inserir(VIRGULA, ",");
        inserir(COLCHETES, "[");
        inserir(COLCHETES, "]");
        inserir(CHAVE_ABRE, "{");
        inserir(CHAVE_FECHA, "}");
        inserir(MAIS, "+");
        inserir(MENOS, "-");
        inserir(ASPAS_DUPLAS, "\"");
        inserir(APOSTOFRO, "'");
        inserir(BARRA, "/");
        inserir(BARRA_INVERTIDA, "\\");
        inserir(BARRA_EM_PE, "|");
        inserir(EXCLAMACAO, "!");
        inserir(INTERROGACAO, "?");
        inserir(MAIOR, ">");
        inserir(MENOR, "<");
        inserir(IGUAL, "=");
        inserir(FINAL, "final");
        inserir(WHILE, "while");
        inserir(IF, "if");
        inserir(ELSE, "else");
        inserir(AND, "and");
        inserir(OR, "or");
        inserir(NOT, "not");
        inserir(IGUAL_IGUAL, "==");
        inserir(PARENTESES, "(");
        inserir(PARENTESES, ")");
        inserir(DIFERENTE, "!=");
        inserir(MAIOR_IGUAL, ">=");
        inserir(MENOR_IGUAL, "<=");
        inserir(MULTIPLICACAO, "*");
        inserir(READLN, "readln");
        inserir(WRITE, "write");
        inserir(WRITELN, "writeln");
        inserir(TRUE, "true");
        inserir(FALSE, "false");
    }

    public RegistroTabelaSimbolos inserir(int token, String lexema) {
        RegistroTabelaSimbolos registro = new RegistroTabelaSimbolos(token, lexema);
        if (primeiro == null && ultimo == null) {
            primeiro = registro;
            ultimo = registro;
        } else {
            ultimo.setProximo(registro);
            ultimo = registro;
        }

        return registro;
    }

    public RegistroTabelaSimbolos pesquisar(String lexema) {
        boolean continua_procurando = true;

        RegistroTabelaSimbolos atual = primeiro;
        while (continua_procurando) {
            if (atual != null) {
                if (atual.getLexema().equals(lexema)) {
                    return atual;
                } else {
                    atual = atual.getProximo();
                }
            } else {
                continua_procurando = false;
            }
        }

        RegistroTabelaSimbolos reg;
        if (lexema.matches("[a-zA-Z_][a-zA-Z_0-9]*")) {
            reg = inserir(TabelaDeSimbolos.IDENTIFICADORES, lexema);
            return reg;
        } else if (lexema.matches("(\").*(\")") // string entre aspas
                || lexema.matches("[0-9]+")
                || lexema.matches("[0-9]+[.][0-9]+")) { // qualquer valor numerico int
            reg = inserir(TabelaDeSimbolos.CONSTANTES, lexema);
            return reg;
        }
        return null;
    }
}

