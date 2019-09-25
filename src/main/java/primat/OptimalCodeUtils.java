package primat;

import java.io.*;
import java.util.List;
import java.util.Scanner;

import static java.lang.Math.log;

public class OptimalCodeUtils {

    public static void printFormattedTable(PrintStream printStream, List<SymbolAttribute> symbolAttributes) {
        final int[] columnWidth = {10, 15, 15, 15};
        final String formatString = "%" + columnWidth[0] + "s %" + columnWidth[1] + ".4f  %" + columnWidth[2] + "d   %s\n";
        printStream.printf("%" + columnWidth[0] + "s%" + columnWidth[1] + "s %" + columnWidth[2] + "s  %s \n", "Symbol", "Possibility", "Code length", "Code");
        symbolAttributes.forEach(symbolAttribute -> {
            String outputString = symbolAttribute.getString();
            outputString = outputString.replace(" ", "<space>");
            outputString = outputString.replace(OptimalPrefixCode.PUNCTUATION_CHARACTER, "<punct>");
            printStream.printf(formatString,
                    outputString,
                    symbolAttribute.getProbability(),
                    symbolAttribute.getCode().length(),
                    symbolAttribute.getCode());
        });
    }

    public static void printFormattedTable(PrintStream printStream, List<SymbolAttribute> symbolAttributes, boolean texTableFormat) {
        final int[] columnWidth = {10, 15, 15, 15};
        final String formatString = "%" + columnWidth[0] + "s & %" + columnWidth[1] + ".4f & %" + columnWidth[2] + "d  &  %s \\\\\n";
        printStream.printf("%" + columnWidth[0] + "s & %" + columnWidth[1] + "s & %" + columnWidth[2] + "s  &  %s \\\\\n", "Symbol", "Possibility", "Code length", "Code");
        symbolAttributes.forEach(symbolAttribute -> {
            String outputString = symbolAttribute.getString();
            outputString = outputString.replace(" ", "<space>");
            outputString = outputString.replace(OptimalPrefixCode.PUNCTUATION_CHARACTER, "<punct>");
            printStream.printf(formatString,
                    outputString,
                    symbolAttribute.getProbability(),
                    symbolAttribute.getCode().length(),
                    symbolAttribute.getCode());
            printStream.println("\\hline");
        });
    }

    public static void printHeader(PrintStream printStream) {
        printStream.println("Данная программа строит оптимальный код для текста на английском языке.");
        printStream.println("Выберите метод кодирования:");
    }

    public static OptimalPrefixCode getOptimalCodeFromInputWithOutput(PrintStream outputStream, InputStream inputStream) {
        Scanner in = new Scanner(inputStream);
        while (true) {
            try {
                outputStream.print("Введите номер действия" +
                        "\n 0 - построить код Хафмана," +
                        "\n 1 - построить код Шеннона-Фано," +
                        "\n 2 или q - выйти из программы: ");
                String token = in.next();
                switch (token) {
                    case "0":
                        return new HuffmanCode(new FileInputStream(new File(readFilename(outputStream, inputStream))));
                    case "1":
                        return new ShannonFanoCode(new FileInputStream(new File(readFilename(outputStream, inputStream))));
                    case "2":
                    case "q":
                        System.exit(0);
                    default:
                        throw new IllegalArgumentException();
                }
            } catch (FileNotFoundException e) {
                outputStream.println("Файл не найден или содержит недопустимые символы, повторите ввод.\n");
            }  catch (Exception e) {
                outputStream.println("Ошибка ввода, попробуйте ещё раз.\n");
                in.nextLine();
            }
        }
    }

    private static String readFilename(PrintStream outputStream, InputStream inputStream) {
        Scanner in = new Scanner(inputStream);
        while (true) {
            try {
                outputStream.print("Введите путь к файлу: ");
                String filename = in.nextLine();
                outputStream.println("ОК");
                return filename;
            } catch (Exception e) {
                outputStream.println("Ошибка ввода, повторите ввод.");
                in.nextLine();
            }
        }
    }

    public static void main(String... args) {
        printHeader(System.out);
        while (true) {
            OptimalPrefixCode optimalPrefixCode = getOptimalCodeFromInputWithOutput(System.out, System.in);
            try {
                printFormattedTable(System.out, optimalPrefixCode.getAllSymbolsFromFile());
                System.out.printf("Общая энтропия = %.4f\n", optimalPrefixCode.getFullEntropy());
                System.out.println();
            } catch (IOException e) {
                System.out.println("Такого файла не существует, пожалуйста, следуйте инструкциям.");
            }
        }
    }
}
