package com.company;

import java.io.*;
import java.nio.charset.Charset;

public class AnalisadorLexico {

    public static String lexema = "";

    public static char[] codigoFonte;

    public static int linha = 1;

    public static int i = 0;

    public static void nextLexema() {
        String estado = "I";
        lexema = "";
        while (!estado.equals("F") && i < codigoFonte.length) {

            boolean devolve = false;

            String c = String.valueOf(codigoFonte[i]);
            if (c.equals("\n")) {
                linha++;
            }

            switch (estado) {

                case "I":
                    if (c.matches("[a-zA-Z_]+")) {
                        estado = "5";
                        lexema += c;
                    } else if (c.matches("[1-9]+")) {
                        estado = "6";
                        lexema += c;
                    } else if (c.equals("=") || c.equals(">") || c.equals("<")) {
                        estado = "1";
                        lexema += c;
                    } else if (c.equals("/")) {
                        estado = "2";
                        lexema += c;
                    } else if (c.equals("{")) {
                        estado = "10";
                    } else if (c.equals("\"")) {
                        estado = "11";
                        lexema += c;
                    } else if (c.equals("(") || c.equals(")") || c.equals(",") || c.equals("+")
                            || c.equals("-") || c.equals("*") || c.equals(";")) {
                        estado = "F";
                        lexema += c;
                    } else if (c.matches("[0]*")) {
                        estado = "7";
                        lexema += c;
                    } else {
                        if (!c.equals("\n") && !c.equals(" ") && !c.equals("\r") && !c.equals("\t")) {
                            System.out.println(linha + ": caractere inválido.");
                            System.exit(0);
                        }
                    }
                    break;
                case "1":
                    if (c.equals("=")) {
                        estado = "F";
                        lexema += c;
                    } else {
                        estado = "F";
                        devolve = true;
                    }
                    break;
                case "2":
                    switch (c) {
                        case "*":
                            estado = "3";

                            break;
                        default:
                            estado = "F";
                            devolve = true;

                            break;
                    }
                    break;
                case "3":
                    if (c.equals("*")) {
                        estado = "4";
                    } else {
                        estado = "3";
                    }
                    break;
                case "4":
                    switch (c) {
                        case "*":
                            estado = "4";

                            break;
                        case "/":
                            estado = "I";

                            break;
                        default:
                            estado = "3";
                            devolve = true;

                            break;
                    }
                    break;
                case "5":
                    if (c.matches("[a-zA-Z0-9_]+")) {
                        estado = "5";
                        lexema += c;
                    } else {
                        estado = "F";
                        devolve = true;
                    }
                    break;
                case "6":
                    if (c.matches("[0-9]+")) {
                        estado = "6";
                        lexema += c;
                    } else {
                        estado = "F";
                        devolve = true;
                    }
                    break;
                case "7":
                    if (c.matches("[0-9]+")) {
                        estado = "6";
                        lexema += c;
                    } else if (c.equals("x")) {
                        estado = "8";
                        lexema += c;
                    } else {
                        estado = "F";
                        devolve = true;
                    }
                    break;
                case "8":
                    if (c.matches("[a-zA-Z0-9_]+")) {
                        estado = "9";
                        lexema += c;
                    }
                    break;
                case "9":
                    if (c.matches("[a-zA-Z0-9_]+")) {
                        estado = "F";
                        lexema += c;
                    }
                    break;
                case "10":
                    if (c.equals("}")) {
                        estado = "I";
                    } else {
                        estado = "10";
                    }
                    break;
                case "11":
                    if (c.equals("\"")) {
                        estado = "I";
                    } else {
                        estado = "10";
                    }
                    break;
                default:
                    System.out.println(linha + ": estado desconhecido [" + estado + "]");
                    System.exit(0);
                    break;

            }

            if (!devolve) {
                i++;
            }
        }
    }

    public static String getLexema() {
        return lexema;
    }

    public static void setCodigoFonte(char[] codigo) {
        codigoFonte = codigo;
    }

    public static char[] setCodigoFonte(String filename) {
        Charset encoding = Charset.defaultCharset();
        File file = new File(filename);
        String codigoFonte = "";
        try {
            InputStream in = new FileInputStream(file);
            Reader reader = new InputStreamReader(in, encoding);
            Reader buffer = new BufferedReader(reader);
            int r;
            codigoFonte = "";
            while ((r = buffer.read()) != -1) {
                char ch = (char) r;
                codigoFonte += ch;
            }
        } catch (FileNotFoundException ex) {
            System.out.println("Arquivo fonte não encontrado: " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("Erro ao ler arquivo fonte: " + ex.getMessage());
        }

        AnalisadorLexico.setCodigoFonte(codigoFonte.toCharArray());
        return codigoFonte.toCharArray();
    }

}
