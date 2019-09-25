package primat;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class HuffmanCode implements OptimalPrefixCode {
    private InputStream input;
    private Map<String, SymbolAttribute> mapSymbolAttributes = new TreeMap<>();
    private int fileLength = 0;

    public HuffmanCode(InputStream input) {
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
                        SymbolAttribute stringAttribute = mapSymbolAttributes.get(string);
                        stringAttribute.setCount(stringAttribute.getCount() + 1);
                    }
                });
        mapSymbolAttributes.values()
                .forEach(characterAttribute -> {
                    characterAttribute.setProbability(((double) characterAttribute.getCount()) / fileLength);
                });
    }

    private void generateCodes(Node<SymbolAttribute> root) {
        dfs(root, "");
    }

    private void dfs(Node<SymbolAttribute> node, String code){
        if (node.getChildren().isEmpty()){
            node.getNode().setCode(code);
            return;
        }
        dfs(node.getChildren().get(0), code + "0");
        dfs(node.getChildren().get(1), code + "1");
    }

    private Node<SymbolAttribute> generateTree() {
        // sort by probabilities
        Set<Map.Entry<Double, Node<SymbolAttribute>>> tmpNodes = new TreeSet<>(
                (e1, e2) -> {
                    if (e1.getKey() < e2.getKey()) return -1;
                    else if (e1.getKey() > e2.getKey()) return 1;
                    return e1.getValue().hashCode() - e2.getValue().hashCode();
                });
        mapSymbolAttributes.forEach((k, attr) -> {
            tmpNodes.add(new AbstractMap.SimpleEntry<>(attr.getProbability(), new Node<>(attr, attr.getProbability())));
        });

        while (tmpNodes.size() != 1) {
            Node<SymbolAttribute> n1 = tmpNodes.stream().findFirst().get().getValue();
            tmpNodes.remove(tmpNodes.stream().findFirst().get());
            Node<SymbolAttribute> n2 = tmpNodes.stream().findFirst().get().getValue();
            tmpNodes.remove(tmpNodes.stream().findFirst().get());

            Node<SymbolAttribute> newNode = new Node<>();
            newNode.addChild(n1);
            newNode.addChild(n2);
            n1.setParent(newNode);
            n2.setParent(newNode);
            newNode.setProbability(n1.getProbability() + n2.getProbability());

            tmpNodes.add(new AbstractMap.SimpleEntry<>(newNode.getProbability(), newNode));
        }
        return tmpNodes.stream().findFirst().get().getValue();
    }



    @Override
    public List<SymbolAttribute> getAllSymbolsFromFile() throws IOException {
        readFileAndCountProbability();
        generateCodes(generateTree());
        return new ArrayList<>(mapSymbolAttributes.values());
    }

    @Override
    public double getFullEntropy() {
        return mapSymbolAttributes.values().stream()
                .mapToDouble(e -> e.getCode().length() * e.getCount())
                .sum() / fileLength;
    }
}

