package com.company;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        String file_name = "";
        if(args.length > 0){
            file_name = args[0];
        }else{
            System.out.println("Informe o nome do arquivo a ser compilado");
            System.exit(0);
        }

        AnalisadorLexico.setCodigoFonte(file_name);
        AnalisadorLexico.i = 0;
        AnalisadorLexico.nextLexema();

        TabelaDeSimbolos tabela = new TabelaDeSimbolos();
        AnalisadorSintatico.setTabelaSimbolos(tabela);
        AnalisadorSintatico.gramatica();


        while (AnalisadorLexico.getLexema() != ""){
            System.out.println(AnalisadorLexico.getLexema());
            AnalisadorLexico.nextLexema();
        }

    }
}
