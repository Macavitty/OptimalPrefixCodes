package primat;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static primat.TextEntropyUtils.log2;

public class PairTextEntropy implements TextEntropy {

    private InputStream input;
    private Map<String, StringAttribute> mapStringAttributes = new TreeMap<>();
    private Map<String, StringAttribute> mapCharacterAttributes = new TreeMap<>();
    private int fileLength = 0;

    public PairTextEntropy(InputStream input) {
        this.input = input;
    }

    private void readTextAndFillCounts() throws IOException {
        final String[] previousCharacter = {null};
        String inputStream = new String(input.readAllBytes());
        inputStream.chars()
                .mapToObj(character -> String.valueOf((char) character))
                .filter(string -> !string.equals("\n"))
                .map(string -> Character.isLetter(string.charAt(0)) || Character.isWhitespace(string.charAt(0)) ? string : PUNCTUATION_CHARACTER)
                .map(String::toLowerCase)
                .forEach(string -> {

                    ++fileLength;

                    if (!mapCharacterAttributes.containsKey(string))
                        mapCharacterAttributes.put(string, new StringAttribute(string, 1));
                    else {
                        StringAttribute stringAttribute = mapCharacterAttributes.get(string);
                        stringAttribute.setCount(stringAttribute.getCount() + 1);
                    }

                    if (previousCharacter[0] != null) {
                        string = previousCharacter[0] + string;
                        previousCharacter[0] = String.valueOf(string.charAt(1));
                        if (!mapStringAttributes.containsKey(string))
                            mapStringAttributes.put(string, new StringAttribute(string, 1));
                        else {
                            StringAttribute stringAttribute = mapStringAttributes.get(string);
                            stringAttribute.setCount(stringAttribute.getCount() + 1);
                        }
                    } else
                        previousCharacter[0] = string;

                });
    }

    private void findEntropy() {
        mapStringAttributes.values()
                .forEach(stringAttribute -> {
                    stringAttribute.setPossibility(((double) stringAttribute.getCount()) / (fileLength - 1));
                    stringAttribute.setEntropy(-(stringAttribute.getPossibility() * log2(stringAttribute.getPossibility())));
                });
        mapCharacterAttributes.values()
                .forEach(characterAttribute -> {
                    characterAttribute.setPossibility(((double) characterAttribute.getCount()) / fileLength);
                    characterAttribute.setEntropy(-(characterAttribute.getPossibility() * log2(characterAttribute.getPossibility())));
                });
    }

    @Override
    public List<StringAttribute> readTextAndGetAllCharacterAttributes() throws IOException {
        readTextAndFillCounts();
        findEntropy();
        return new ArrayList<>(mapStringAttributes.values());
    }

    @Override
    public double getFullEntropy() {
        return mapStringAttributes.values().stream()
                .mapToDouble(stringAttribute -> -(stringAttribute.getPossibility()
                        * mapCharacterAttributes.get(String.valueOf(stringAttribute.getString().charAt(1))).getPossibility()
                        * log2(stringAttribute.getPossibility())))
                .sum();
    }
}
