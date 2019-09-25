package primat;

import java.io.IOException;
import java.util.List;

public interface OptimalPrefixCode {

    String PUNCTUATION_CHARACTER = ".";

    List<SymbolAttribute> getAllSymbolsFromFile() throws IOException;

    double getFullEntropy();
}
