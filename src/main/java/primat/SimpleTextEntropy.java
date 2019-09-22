package primat;

import java.io.*;
import java.util.*;

import static java.lang.Math.*;

public class SimpleTextEntropy implements TextEntropy {

    private InputStream input;
    private Map<String, StringAttribute> mapCharacterAttributes = new TreeMap<>();
    private int fileLength = 0;

    public SimpleTextEntropy(InputStream input) {
        this.input = input;
    }

    private void readTextAndFillCounts() throws IOException {
        String inputStream = new String(input.readAllBytes());
        fileLength = inputStream.length();
        inputStream.chars()
                .mapToObj(character -> String.valueOf((char) character))
                .filter(string -> !string.equals("\n"))
                .map(string -> Character.isLetter(string.charAt(0)) || Character.isWhitespace(string.charAt(0)) ? string : PUNCTUATION_CHARACTER)
                .map(String::toLowerCase)
                .forEach(string -> {
                    if (!mapCharacterAttributes.containsKey(string))
                        mapCharacterAttributes.put(string, new StringAttribute(string, 1));
                    else {
                        StringAttribute stringAttribute = mapCharacterAttributes.get(string);
                        stringAttribute.setCount(stringAttribute.getCount() + 1);
                    }
                });
    }

    private void findEntropy() {
        mapCharacterAttributes.values()
                .forEach(stringAttribute -> {
                    stringAttribute.setPossibility(((double) stringAttribute.getCount()) / fileLength);
                    stringAttribute.setEntropy(-(stringAttribute.getPossibility() * log(stringAttribute.getPossibility())));
                });
    }

    @Override
    public List<StringAttribute> readTextAndGetAllCharacterAttributes() throws IOException {
        readTextAndFillCounts();
        findEntropy();
        return new ArrayList<>(mapCharacterAttributes.values());
    }

    @Override
    public double getFullEntropy() {
        return mapCharacterAttributes.values().stream()
                .mapToDouble(StringAttribute::getEntropy)
                .sum();
    }

}
