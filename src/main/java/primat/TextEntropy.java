package primat;

import java.io.*;
import java.util.*;

import static java.lang.Math.*;

public class TextEntropy {

    public static void main(String... args) {
        try {
            TextEntropy textEntropy = new TextEntropy(new FileInputStream(new File("data.txt")));
            List<CharacterAttribute> characterAttributes = textEntropy.readTextAndGetAllCharacterAttributes();
            //characterAttributes.forEach(System.out::println);
            TextEntropyUtils.printFormattedTable(System.out, characterAttributes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static class CharacterAttribute {
        private char character;
        private int count;
        private double possibility;
        private double entropy;

        public CharacterAttribute(char character, int count) {
            this.character = character;
            this.count = count;
        }

        public char getCharacter() {
            return character;
        }

        public void setCharacter(char character) {
            this.character = character;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public double getPossibility() {
            return possibility;
        }

        public void setPossibility(double possibility) {
            this.possibility = possibility;
        }

        public double getEntropy() {
            return entropy;
        }

        public void setEntropy(double entropy) {
            this.entropy = entropy;
        }

        @Override
        public String toString() {
            return "CharacterAttribute{" +
                    "character=" + character +
                    ", count=" + count +
                    ", possibility=" + possibility +
                    ", entropy=" + entropy +
                    '}';
        }
    }

    private static final char PUNCTUATION_CHARACTER = '.';

    private InputStream input;
    private Map<Character, CharacterAttribute> mapCharacterAttributes = new HashMap<>();
    private int fileLength = 0;

    public TextEntropy(InputStream input) {
        this.input = input;
    }

    private void readTextAndFillCounts() throws IOException {
        String inputStream = new String(input.readAllBytes());
        fileLength = inputStream.length();
        inputStream.chars()
                .mapToObj(character -> (char) character)
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

    public static char getPunctuationCharacter() {
        return PUNCTUATION_CHARACTER;
    }
}
