package primat;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class ShannonFanoCode implements OptimalPrefixCode {
    private InputStream input;
    private Map<String, SymbolAttribute> mapSymbolAttributes = new TreeMap<>();
    private int fileLength = 0;

    public ShannonFanoCode(InputStream input) {
        this.input = input;
    }

    private void readFileAndCountProbability() throws IOException {
        String inputStream = new String(input.readAllBytes());
        inputStream.chars()
                .mapToObj(character -> String.valueOf((char) character))
                .filter(string -> !string.equals("\n"))
                .map(string -> Character.isLetter(string.charAt(0)) || Character.isWhitespace(string.charAt(0)) ? string : PUNCTUATION_CHARACTER)
                .forEach(string -> {

                    ++fileLength;

                    if (!mapSymbolAttributes.containsKey(string))
                        mapSymbolAttributes.put(string, new SymbolAttribute(string, 1, mapSymbolAttributes.size()));
                    else {
                        SymbolAttribute symbolAttribute = mapSymbolAttributes.get(string);
                        symbolAttribute.setCount(symbolAttribute.getCount() + 1);
                    }
                });
        mapSymbolAttributes.values()
                .forEach(characterAttribute -> {
                    characterAttribute.setProbability(((double) characterAttribute.getCount()) / fileLength);
                });
    }

    private void generateCodes() {
        // sort by probabilities
        Set<Map.Entry<String, SymbolAttribute>> setStringAttributes = new TreeSet<>(
                (e1, e2) -> { // TODO double comparator
                    if (e1.getValue().getProbability() < e2.getValue().getProbability()) return -1;
                    else if (e1.getValue().getProbability() > e2.getValue().getProbability()) return 1;
                    return e1.getValue().getString().compareTo(e2.getValue().getString());
                });
        setStringAttributes.addAll(mapSymbolAttributes.entrySet());

        // fill prefix array
        double[] prefixSums = new double[setStringAttributes.size()];
        final int[] i = {0};
        setStringAttributes.forEach(attr -> {
            prefixSums[i[0]] = attr.getValue().getProbability();
            if (i[0] > 0) prefixSums[i[0]] += prefixSums[i[0] - 1];
            attr.getValue().setIdx(i[0]);
            i[0]++;
        });

        shannonAlgo(0, mapSymbolAttributes.size() - 1, prefixSums);
    }

    private void shannonAlgo(int left, int right, double[] prefixSums) {
        if (right - left == 1) {
            findSymbolAttributeByIdx(left).addDigitToCode("0");
            findSymbolAttributeByIdx(right).addDigitToCode("1");
            return;
        }
        if (left == right) {
            return;
        }

        int middle = left;
        int localLeft = left - 1;
        while (middle < right ){
            double firstGroupSum, secondGroupSum;
            if (middle == localLeft){
                firstGroupSum = prefixSums[middle];
            }
            else firstGroupSum =  prefixSums[middle] - (localLeft < 0 ? 0 : prefixSums[localLeft]);
            secondGroupSum = prefixSums[right] - prefixSums[middle];
            if (firstGroupSum < secondGroupSum) {
                return;
            }
            middle++;
        }

//        while (middle < right && (middle == localLeft ? prefixSums[middle] : prefixSums[middle] - (localLeft < 0 ? 0 : prefixSums[localLeft])) < prefixSums[right] - prefixSums[middle]) {
//            middle++;
//        }

        for (int i = left; i <= middle; i++) {
            findSymbolAttributeByIdx(i).addDigitToCode("0");
        }
        for (int i = middle + 1; i <= right; i++) {
            findSymbolAttributeByIdx(i).addDigitToCode("1");
        }

        if (middle == right || middle == left)
            return;
        shannonAlgo(left, middle, prefixSums);
        shannonAlgo(middle + 1, right, prefixSums);
    }

    private SymbolAttribute findSymbolAttributeByIdx(int idx) {
        return mapSymbolAttributes.values().stream().filter(attr -> attr.getIdx() == idx)
                .findFirst().orElse(null);
    }

    @Override
    public List<SymbolAttribute> getAllSymbolsFromFile() throws IOException {
        readFileAndCountProbability();
        generateCodes();
        return new ArrayList<>(mapSymbolAttributes.values());
    }

    @Override
    public double getFullEntropy() {
        return mapSymbolAttributes.values().stream()
                .mapToDouble(e -> e.getCode().length() * e.getCount())
                .sum() / fileLength;
    }
}
