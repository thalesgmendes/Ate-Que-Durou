package br.ufma.ecp;

import java.io.*;
import java.util.*;

public class Parser {
    private BufferedReader reader;
    private String currentCommand;
    private List<String> tokens;

    public Parser(String filePath) throws IOException {
        reader = new BufferedReader(new FileReader(filePath));
        currentCommand = null;
        tokens = new ArrayList<>();
    }

    public boolean hasMoreCommands() throws IOException {
        while (reader.ready()) {
            currentCommand = reader.readLine().trim();
            if (!currentCommand.isEmpty() && !currentCommand.startsWith("//")) {
                return true;
            }
        }
        return false;
    }

    public void advance() {
        if (currentCommand != null) {
            tokens = Arrays.asList(currentCommand.split(" "));
        }
    }

    public CommandType commandType() {
        if (tokens.isEmpty()) return null;

        String firstToken = tokens.get(0);
        switch (firstToken) {
            case "push": return CommandType.PUSH;
            case "pop": return CommandType.POP;
            case "label": return CommandType.LABEL;
            case "goto": return CommandType.GOTO;
            case "if-goto": return CommandType.IF;
            case "function": return CommandType.FUNCTION;
            case "return": return CommandType.RETURN;
            case "call": return CommandType.CALL;
            default: return CommandType.ARITHMETIC;
        }
    }

    public String arg1() {
        if (commandType() == CommandType.RETURN) {
            throw new IllegalStateException("arg1() should not be called for RETURN commands");
        }
        return commandType() == CommandType.ARITHMETIC ? tokens.get(0) : tokens.get(1);
    }

    public int arg2() {
        CommandType type = commandType();
        if (type != CommandType.PUSH && type != CommandType.POP && type != CommandType.FUNCTION && type != CommandType.CALL) {
            throw new IllegalStateException("arg2() should only be called for PUSH, POP, FUNCTION, or CALL commands");
        }
        return Integer.parseInt(tokens.get(2));
    }

    public void close() throws IOException {
        reader.close();
    }

    public enum CommandType {
        ARITHMETIC, PUSH, POP, LABEL, GOTO, IF, FUNCTION, RETURN, CALL
    }
}