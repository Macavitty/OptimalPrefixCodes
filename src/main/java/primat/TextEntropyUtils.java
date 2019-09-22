package primat;

import primat.TextEntropy.CharacterAttribute;

import java.io.PrintStream;
import java.util.List;

public class TextEntropyUtils {

    public static void printFormattedTable(PrintStream printStream, List<CharacterAttribute> characterAttributes) {
        final int columnWidth[] = {10, 12, 8};
        final String formatString = "%" + columnWidth[0] + "s" + "%" + columnWidth[1] + ".4f" + "%" + columnWidth[2] + ".4f\n";
        printStream.printf("%" + columnWidth[0] + "s" + "%" + columnWidth[1] + "s" + "%" + columnWidth[2] + "s\n", "Character", "Possibility", "Entropy");
        characterAttributes.forEach(characterAttribute -> {
            String outputCharacter = String.valueOf(characterAttribute.getCharacter());
            if (outputCharacter.equals(" "))
                outputCharacter = "space";
            else if (outputCharacter.equals(String.valueOf(TextEntropy.PUNCTUATION_CHARACTER)))
                outputCharacter = "punct";
            printStream.printf(formatString,
                    outputCharacter,
                    characterAttribute.getPossibility(),
                    characterAttribute.getEntropy());
        });
    }


}
