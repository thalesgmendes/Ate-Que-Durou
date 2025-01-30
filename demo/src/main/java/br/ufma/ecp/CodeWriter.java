package br.ufma.ecp;

import java.io.*;

public class CodeWriter {
    private BufferedWriter writer;
    private int labelCounter;

    public CodeWriter(String filePath) throws IOException {
        writer = new BufferedWriter(new FileWriter(filePath));
        labelCounter = 0;
    }

    public void writeArithmetic(String command) throws IOException {
        writer.write("// " + command + "\n");
        switch (command) {
            case "add":
                writer.write("@SP\nAM=M-1\nD=M\nA=A-1\nM=M+D\n");
                break;
            case "sub":
                writer.write("@SP\nAM=M-1\nD=M\nA=A-1\nM=M-D\n");
                break;
            case "neg":
                writer.write("@SP\nA=M-1\nM=-M\n");
                break;
            case "eq":
                writeComparison("JEQ");
                break;
            case "gt":
                writeComparison("JGT");
                break;
            case "lt":
                writeComparison("JLT");
                break;
            case "and":
                writer.write("@SP\nAM=M-1\nD=M\nA=A-1\nM=M&D\n");
                break;
            case "or":
                writer.write("@SP\nAM=M-1\nD=M\nA=A-1\nM=M|D\n");
                break;
            case "not":
                writer.write("@SP\nA=M-1\nM=!M\n");
                break;
        }
    }

    private void writeComparison(String jumpCommand) throws IOException {
        String labelTrue = "LABEL_TRUE_" + labelCounter;
        String labelEnd = "LABEL_END_" + labelCounter;
        labelCounter++;

        writer.write(
            "@SP\nAM=M-1\nD=M\nA=A-1\nD=M-D\n" +
            "@" + labelTrue + "\nD;" + jumpCommand + "\n" +
            "@SP\nA=M-1\nM=0\n" +
            "@" + labelEnd + "\n0;JMP\n" +
            "(" + labelTrue + ")\n@SP\nA=M-1\nM=-1\n" +
            "(" + labelEnd + ")\n"
        );
    }

    public void writePush(String segment, int index) throws IOException {
        writer.write("// push " + segment + " " + index + "\n");
        switch (segment) {
            case "constant":
                writer.write("@" + index + "\nD=A\n");
                break;
            case "local":
                writer.write("@LCL\nD=M\n@" + index + "\nA=D+A\nD=M\n");
                break;
            case "argument":
                writer.write("@ARG\nD=M\n@" + index + "\nA=D+A\nD=M\n");
                break;
            case "this":
                writer.write("@THIS\nD=M\n@" + index + "\nA=D+A\nD=M\n");
                break;
            case "that":
                writer.write("@THAT\nD=M\n@" + index + "\nA=D+A\nD=M\n");
                break;
            case "temp":
                writer.write("@R" + (5 + index) + "\nD=M\n");
                break;
            case "pointer":
                writer.write("@" + (3 + index) + "\nD=M\n");
                break;
            case "static":
                writer.write("@16\nD=A\n@" + index + "\nA=D+A\nD=M\n");
                break;
        }
        writer.write("@SP\nA=M\nM=D\n@SP\nM=M+1\n");
    }

    public void writePop(String segment, int index) throws IOException {
        writer.write("// pop " + segment + " " + index + "\n");
        switch (segment) {
            case "local":
                writer.write("@LCL\nD=M\n@" + index + "\nD=D+A\n@R13\nM=D\n");
                break;
            case "argument":
                writer.write("@ARG\nD=M\n@" + index + "\nD=D+A\n@R13\nM=D\n");
                break;
            case "this":
                writer.write("@THIS\nD=M\n@" + index + "\nD=D+A\n@R13\nM=D\n");
                break;
            case "that":
                writer.write("@THAT\nD=M\n@" + index + "\nD=D+A\n@R13\nM=D\n");
                break;
            case "temp":
                writer.write("@R" + (5 + index) + "\nD=A\n@R13\nM=D\n");
                break;
            case "pointer":
                writer.write("@" + (3 + index) + "\nD=A\n@R13\nM=D\n");
                break;
            case "static":
                writer.write("@16\nD=A\n@" + index + "\nD=D+A\n@R13\nM=D\n");
                break;
        }
        writer.write("@SP\nAM=M-1\nD=M\n@R13\nA=M\nM=D\n");
    }

    public void close() throws IOException {
        writer.close();
    }
}