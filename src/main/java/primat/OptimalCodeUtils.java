package primat;

import java.io.*;
import java.util.List;
import java.util.Scanner;

import static java.lang.Math.log;

public class TextEntropyUtils {

    public static void printFormattedTable(PrintStream printStream, List<SymbolAttribute> symbolAttributes) {
        final int[] columnWidth = {15, 12, 8};
        final String formatString = "%" + columnWidth[0] + "s%" + columnWidth[1] + ".4f%" + columnWidth[2] + ".4f\n";
        printStream.printf("%" + columnWidth[0] + "s%" + columnWidth[1] + "s%" + columnWidth[2] + "s\n", "Character", "Possibility", "Entropy");
        symbolAttributes.forEach(symbolAttribute -> {
            String outputString = symbolAttribute.getString();
            outputString = outputString.replace(" ", "<space>");
            outputString = outputString.replace(TextEntropy.PUNCTUATION_CHARACTER, "<punct>");
            printStream.printf(formatString,
                    outputString,
                    symbolAttribute.getPossibility(),
                    symbolAttribute.getEntropy());
        });
    }

    public static void printHeader(PrintStream printStream) {
        printStream.println("Программа для нахождения энтропии английского текста.");
        printStream.println("Допустимые символы: символы английского алфавита, пробелы и знаки пунктуации.");
        printStream.println("Для начала работы");
    }

    public static TextEntropy getTextEntropyFromInputWithOutput(PrintStream outputStream, InputStream inputStream) {
        Scanner in = new Scanner(inputStream);
        while (true) {
            try {
                outputStream.print("Введите 0 чтобы ввести путь к файлу для нахождения энтропии без частот пар," +
                        "\n 1 чтобы ввести путь к файлу для нахождения энтропии с частототами пар," +
                        "\n q или 2 для выхода из программы: ");
                String token = in.next();
                switch (token) {
                    case "0":
                        return new SimpleTextEntropy(new FileInputStream(new File(readFilename(outputStream, inputStream))));
                    case "1":
                        return new PairTextEntropy(new FileInputStream(new File(readFilename(outputStream, inputStream))));
                    case "2":
                    case "q":
                        System.exit(0);
                    default:
                        throw new IllegalArgumentException();
                }
            } catch (FileNotFoundException e) {
                outputStream.println("Файл не найден, повторите ввод.\n");
            }  catch (Exception e) {
                outputStream.println("Произошла ошибка ввода, повторите ввод.\n");
                in.nextLine();
            }
        }
    }

    private static String readFilename(PrintStream outputStream, InputStream inputStream) {
        Scanner in = new Scanner(inputStream);
        while (true) {
            try {
                outputStream.print("Введите имя файла: ");
                String filename = in.nextLine();
                outputStream.println("Имя файла введено.");
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
            TextEntropy textEntropy = getTextEntropyFromInputWithOutput(System.out, System.in);
            try {
                printFormattedTable(System.out, textEntropy.readTextAndGetAllCharacterAttributes());
                System.out.printf("Общая энтропия = %.4f\n", textEntropy.getFullEntropy());
                System.out.println();
            } catch (IOException e) {
                System.out.println("Введено неверное имя файла, пожалуйста, следуйте инструкциям.");
            }
        }
    }

    public static double log2(double arg) {
        return log(arg)/log(2);
    }

}
