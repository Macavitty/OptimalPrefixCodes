package primat;

import primat.TextEntropy.CharacterAttribute;

import java.io.PrintStream;
import java.util.List;

public class TextEntropyUtils {

    public static void printFormattedTable(PrintStream printStream, List<CharacterAttribute> characterAttributes) {
        final int columnWidth[] = {10, 12, 8};
        final String formatString = "%" + columnWidth[0] + "s" + "%" + columnWidth[1] + "s" + "%" + columnWidth[2] + "s\n";
        printStream.printf(formatString, "Character", "Possibility", "Entropy");
        characterAttributes.forEach(characterAttribute -> printStream.printf(formatString,
                characterAttribute.getCharacter(),
                characterAttribute.getPossibility(),
                characterAttribute.getEntropy()));
    }

}
