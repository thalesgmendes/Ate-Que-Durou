package br.ufma.ecp;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Uso: java VMTranslator <arquivo.vm>");
            return;
        }

        String inputFilePath = args[0];
        String outputFilePath = inputFilePath.replace(".vm", ".asm");

        try {
            Parser parser = new Parser(inputFilePath);
            CodeWriter codeWriter = new CodeWriter(outputFilePath);

            while (parser.hasMoreCommands()) {
                parser.advance();
                Parser.CommandType commandType = parser.commandType();

                switch (commandType) {
                    case ARITHMETIC:
                        codeWriter.writeArithmetic(parser.arg1());
                        break;
                    case PUSH:
                        codeWriter.writePush(parser.arg1(), parser.arg2());
                        break;
                    case POP:
                        codeWriter.writePop(parser.arg1(), parser.arg2());
                        break;
                    // Outras implementações futuras
                }
            }

            parser.close();
            codeWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}