package com.company;

import java.util.ArrayList;
import java.util.Objects;

class AnalisadorSintatico {

    private static TabelaDeSimbolos tabela;

    private static ArrayList<String> pilha = new ArrayList<String>();

    static void setTabelaSimbolos(TabelaDeSimbolos tab) {
        tabela = tab;
    }

    private static void casaToken(int token) {
        RegistroTabelaSimbolos registroLido = tabela.pesquisar(AnalisadorLexico.lexema);
        if (registroLido == null) {
            System.out.println(AnalisadorLexico.linha + ": Fim de arquivo não esperado");
            System.exit(0);
        } else if (registroLido.getToken() == token) {
            AnalisadorLexico.nextLexema();
        } else {
            System.out.println(AnalisadorLexico.linha + ": token não esperado [" + AnalisadorLexico.lexema + "]");
            System.exit(0);
        }
    }

    public static void gramatica() {
        declaracoes();
        bloco();
    }

    public static void declaracoes() {
        do {
            declaracao();
        } while (notDeclaracao());
    }

    public static boolean notDeclaracao() {
        if (tabela.pesquisar(AnalisadorLexico.getLexema()) != null) {
            return tabela.pesquisar(AnalisadorLexico.getLexema()).getToken() == TabelaDeSimbolos.INT
                    || tabela.pesquisar(AnalisadorLexico.getLexema()).getToken() == TabelaDeSimbolos.BYTE
                    || tabela.pesquisar(AnalisadorLexico.getLexema()).getToken() == TabelaDeSimbolos.BOOLEAN
                    || tabela.pesquisar(AnalisadorLexico.getLexema()).getToken() == TabelaDeSimbolos.STRING
                    || tabela.pesquisar(AnalisadorLexico.getLexema()).getToken() == TabelaDeSimbolos.IDENTIFICADORES
                    || tabela.pesquisar(AnalisadorLexico.getLexema()).getToken() == TabelaDeSimbolos.FINAL;
        }
        return false;
    }

    public static int getToken() {
        if (tabela.pesquisar(AnalisadorLexico.getLexema()) != null) {
            return tabela.pesquisar(AnalisadorLexico.getLexema()).getToken();
        }
        return -1;
    }

    public static void declaracao() {
        if (getToken() == TabelaDeSimbolos.CHAVE_ABRE) {
            return;
        } else if (getToken() == TabelaDeSimbolos.INT
                || getToken() == TabelaDeSimbolos.BYTE
                || getToken() == TabelaDeSimbolos.BOOLEAN
                || getToken() == TabelaDeSimbolos.STRING) {

            String tipo = tipo();

            RegistroTabelaSimbolos registro = tabela.pesquisar(AnalisadorLexico.getLexema());

            casaToken(TabelaDeSimbolos.IDENTIFICADORES);

            if (getToken() == TabelaDeSimbolos.IGUAL) {
                casaToken(TabelaDeSimbolos.IGUAL);
                expressao("");
            }

            while (getToken() == TabelaDeSimbolos.VIRGULA) {
                casaToken(TabelaDeSimbolos.VIRGULA);

                RegistroTabelaSimbolos registro1 = tabela.pesquisar(AnalisadorLexico.lexema);
                casaToken(TabelaDeSimbolos.IDENTIFICADORES);

                if (getToken() == TabelaDeSimbolos.IGUAL) {
                    casaToken(TabelaDeSimbolos.IGUAL);
                    expressao("");
                }
            }
        } else {
            casaToken(TabelaDeSimbolos.FINAL);

            RegistroTabelaSimbolos registro = tabela.pesquisar(AnalisadorLexico.lexema);
            casaToken(TabelaDeSimbolos.IDENTIFICADORES);

            casaToken(TabelaDeSimbolos.IGUAL);

            String tipo = expressao("");
            registro.setTipo(tipo);
        }

        casaToken(TabelaDeSimbolos.PONTO_E_VIRGULA);
    }

    public static String tipo() {
        if (getToken() == TabelaDeSimbolos.INT) {
            casaToken(TabelaDeSimbolos.INT);
            return "int";
        } else if (getToken() == TabelaDeSimbolos.BYTE) {
            casaToken(TabelaDeSimbolos.BYTE);
            return "byte";
        } else if (getToken() == TabelaDeSimbolos.BOOLEAN) {
            casaToken(TabelaDeSimbolos.BOOLEAN);
            return "bool";
        } else {
            casaToken(TabelaDeSimbolos.STRING);
            return "string";
        }
    }

    public static void bloco() {
        casaToken(TabelaDeSimbolos.CHAVE_ABRE);
        pilha.add("{");
        comandos();
        casaToken(TabelaDeSimbolos.CHAVE_FECHA);
        pilha.add("}");
    }

    public static void comandos() {
        while (getToken() == TabelaDeSimbolos.IDENTIFICADORES
                || getToken() == TabelaDeSimbolos.WHILE
                || getToken() == TabelaDeSimbolos.IF
                || getToken() == TabelaDeSimbolos.READLN
                || getToken() == TabelaDeSimbolos.WRITE
                || getToken() == TabelaDeSimbolos.WRITELN) {
            comando();
        }
    }

    public static void comando() {
        if (getToken() == TabelaDeSimbolos.IDENTIFICADORES) {
            RegistroTabelaSimbolos registro = tabela.pesquisar(AnalisadorLexico.getLexema());
            casaToken(TabelaDeSimbolos.IDENTIFICADORES);
            pilha.add(registro.getLexema());
            casaToken(TabelaDeSimbolos.IGUAL);
            pilha.add("=");
            String tipo = expressao("");
            casaToken(TabelaDeSimbolos.PONTO_E_VIRGULA);
            pilha.add(";");
        } else if (getToken() == TabelaDeSimbolos.WHILE) {
            casaToken(TabelaDeSimbolos.WHILE);
            pilha.add("while");
            String tipo = expressao("");
            if (getToken() == TabelaDeSimbolos.CHAVE_ABRE) {
                bloco();
            } else {
                comando();
            }
        } else if (getToken() == TabelaDeSimbolos.IF) {
            casaToken(TabelaDeSimbolos.IF);
            pilha.add("if");
            String tipo = expressao("");
            if (getToken() == TabelaDeSimbolos.CHAVE_ABRE) {
                bloco();
            } else {
                comando();
            }
            if (getToken() == TabelaDeSimbolos.ELSE) {
                casaToken(TabelaDeSimbolos.ELSE);
                pilha.add("else");
                if (getToken() == TabelaDeSimbolos.CHAVE_ABRE) {
                    bloco();
                } else {
                    comando();
                }
            }
        } else if (getToken() == TabelaDeSimbolos.READLN) {
            casaToken(TabelaDeSimbolos.READLN);
            pilha.add("readln");
            casaToken(TabelaDeSimbolos.VIRGULA);
            pilha.add(",");
            casaToken(TabelaDeSimbolos.IDENTIFICADORES);
            pilha.add(tabela.pesquisar(AnalisadorLexico.lexema).getLexema());
            casaToken(TabelaDeSimbolos.PONTO_E_VIRGULA);
            pilha.add(";");
        } else if (getToken() == TabelaDeSimbolos.WRITE) {
            casaToken(TabelaDeSimbolos.WRITE);
            pilha.add("write");
            casaToken(TabelaDeSimbolos.VIRGULA);
            pilha.add(",");
            expressoes();
            casaToken(TabelaDeSimbolos.PONTO_E_VIRGULA);
            pilha.add(";");
        } else if (getToken() == TabelaDeSimbolos.WRITELN) {
            casaToken(TabelaDeSimbolos.WRITELN);
            pilha.add("writeln");
            casaToken(TabelaDeSimbolos.VIRGULA);
            expressoes();
            casaToken(TabelaDeSimbolos.PONTO_E_VIRGULA);
            pilha.add(";");
        } else {
            casaToken(TabelaDeSimbolos.PONTO_E_VIRGULA);
            pilha.add(";");
        }
    }

    public static void expressoes() {
        expressao("");
        while (getToken() == TabelaDeSimbolos.VIRGULA) {
            casaToken(TabelaDeSimbolos.VIRGULA);
            pilha.add(",");
            expressao("");
        }
    }

    public static String expressao(String tipo1) {
        tipo1 = expressao_simples(tipo1);
        while (getToken() == TabelaDeSimbolos.IGUAL_IGUAL
                || getToken() == TabelaDeSimbolos.DIFERENTE
                || getToken() == TabelaDeSimbolos.MAIOR
                || getToken() == TabelaDeSimbolos.MENOR
                || getToken() == TabelaDeSimbolos.MAIOR_IGUAL
                || getToken() == TabelaDeSimbolos.MENOR_IGUAL) {
            if (getToken() == TabelaDeSimbolos.IGUAL_IGUAL) {
                casaToken(TabelaDeSimbolos.IGUAL_IGUAL);
                pilha.add("==");
            } else if (getToken() == TabelaDeSimbolos.DIFERENTE) {
                casaToken(TabelaDeSimbolos.DIFERENTE);
                pilha.add("!=");
            } else if (getToken() == TabelaDeSimbolos.MAIOR) {
                casaToken(TabelaDeSimbolos.MAIOR);
                pilha.add(">");
            } else if (getToken() == TabelaDeSimbolos.MENOR) {
                casaToken(TabelaDeSimbolos.MENOR);
                pilha.add("<");
            } else if (getToken() == TabelaDeSimbolos.MAIOR_IGUAL) {
                casaToken(TabelaDeSimbolos.MAIOR_IGUAL);
                pilha.add(">=");
            } else {
                casaToken(TabelaDeSimbolos.MENOR_IGUAL);
                pilha.add("<=");
            }
            String tipo2 = expressao_simples("");
        }
        return tipo1;
    }

    public static String expressao_simples(String tipo1) {
        String sinal1 = "";
        if (getToken() == TabelaDeSimbolos.MAIS) {
            sinal1 = "+";
            casaToken(TabelaDeSimbolos.MAIS);
            String empilha = "add ax, bx";
        } else if (getToken() == TabelaDeSimbolos.MENOS) {
            sinal1 = "-";
            casaToken(TabelaDeSimbolos.MENOS);
            pilha.add("-");
        }
        tipo1 = t(tipo1);
        while (getToken() == TabelaDeSimbolos.MAIS
                || getToken() == TabelaDeSimbolos.MENOS
                || getToken() == TabelaDeSimbolos.OR) {
            String sinal2;
            if (getToken() == TabelaDeSimbolos.MAIS) {
                sinal2 = "+";
                casaToken(TabelaDeSimbolos.MAIS);
                pilha.add("+");
            } else if (getToken() == TabelaDeSimbolos.MENOS) {
                sinal2 = "-";
                casaToken(TabelaDeSimbolos.MENOS);
                pilha.add("-");
            } else {
                sinal2 = "or";
                casaToken(TabelaDeSimbolos.OR);
                pilha.add("or");
            }
            String tipo2 = t("");
        }
        return tipo1;
    }

    public static String t(String tipo1) {
        tipo1 = f(tipo1);
        while (getToken() == TabelaDeSimbolos.MULTIPLICACAO
                || getToken() == TabelaDeSimbolos.BARRA
                || getToken() == TabelaDeSimbolos.AND) {
            String sinal = "";
            if (getToken() == TabelaDeSimbolos.MULTIPLICACAO) {
                sinal = "*";
                casaToken(TabelaDeSimbolos.MULTIPLICACAO);
                pilha.add("imul bx");
            } else if (getToken() == TabelaDeSimbolos.BARRA) {
                sinal = "/";
                casaToken(TabelaDeSimbolos.BARRA);
                pilha.add("idiv bx");
            } else {
                sinal = "and";
                casaToken(TabelaDeSimbolos.AND);
                pilha.add("and");
            }
            String tipo2 = f("");
            String empilha = "mov ax, " + pilha.get(0) + " \n";
            pilha.remove(0);
            empilha += "mov bx, " + pilha.get(0) + " \n";
            pilha.remove(0);
            empilha += pilha.get(0) + " \n";
            pilha.remove(0);
            pilha.add(empilha);
        }
        return tipo1;
    }

    public static String f(String tipo) {
        RegistroTabelaSimbolos registro = tabela.pesquisar(AnalisadorLexico.getLexema());
        if (getToken() == TabelaDeSimbolos.CONSTANTES) {
            casaToken(TabelaDeSimbolos.CONSTANTES);
            pilha.add(registro.getLexema());
        } else if (getToken() == TabelaDeSimbolos.IDENTIFICADORES) {
            casaToken(TabelaDeSimbolos.IDENTIFICADORES);
            pilha.add("DS:[" + registro.getEndereco() + "]");
        } else if (getToken() == TabelaDeSimbolos.NOT) {
            casaToken(TabelaDeSimbolos.NOT);
            pilha.add("not");
            tipo = f(tipo);
        } else if (getToken() == TabelaDeSimbolos.PARENTESES) {
            casaToken(TabelaDeSimbolos.PARENTESES);
            pilha.add("(");
            tipo = expressao(tipo);
            pilha.add(")");
            casaToken(TabelaDeSimbolos.PARENTESES);
        } else if (getToken() == TabelaDeSimbolos.TRUE) {
            casaToken(TabelaDeSimbolos.TRUE);
            pilha.add("FFh");
        } else {
            casaToken(TabelaDeSimbolos.FALSE);
            pilha.add("0h");
        }
        return tipo;
    }
}
