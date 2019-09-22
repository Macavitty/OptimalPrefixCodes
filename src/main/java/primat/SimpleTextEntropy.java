package primat;

import java.io.*;
import java.util.*;

import static java.lang.Math.*;

public class SimpleTextEntropy implements TextEntropy {

    private InputStream input;
    private Map<Character, CharacterAttribute> mapCharacterAttributes = new TreeMap<>();
    private int fileLength = 0;

    public SimpleTextEntropy(InputStream input) {
        this.input = input;
    }

    private void readTextAndFillCounts() throws IOException {
        String inputStream = new String(input.readAllBytes());
        fileLength = inputStream.length();
        inputStream.chars()
                .mapToObj(character -> (char) character)
                .filter(character -> character != '\n')
                .map(character -> Character.isLetter(character) || Character.isWhitespace(character) ? character : PUNCTUATION_CHARACTER)
                .map(Character::toLowerCase)
                .forEach(character -> {
                    if (!mapCharacterAttributes.containsKey(character))
                        mapCharacterAttributes.put(character, new CharacterAttribute(character, 1));
                    else {
                        CharacterAttribute characterAttribute = mapCharacterAttributes.get(character);
                        characterAttribute.setCount(characterAttribute.getCount() + 1);
                    }
                });
    }

    private void findEntropy() {
        mapCharacterAttributes.values()
                .forEach(characterAttribute -> {
                    characterAttribute.setPossibility(((double) characterAttribute.getCount()) / fileLength);
                    characterAttribute.setEntropy(-(characterAttribute.getPossibility() * log(characterAttribute.getPossibility())));
                });
    }

    public List<CharacterAttribute> readTextAndGetAllCharacterAttributes() throws IOException {
        readTextAndFillCounts();
        findEntropy();
        return new ArrayList<>(mapCharacterAttributes.values());
    }

    public double getFullEntropy() {
        return mapCharacterAttributes.values().stream()
                .mapToDouble(CharacterAttribute::getEntropy)
                .sum();
    }
}
