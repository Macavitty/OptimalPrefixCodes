package primat;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class PairTextEntropy implements TextEntropy {

    private InputStream input;
    private Map<Character, StringAttribute> mapCharacterAttributes = new TreeMap<>();
    private int fileLength = 0;

    public PairTextEntropy(InputStream input) {
        this.input = input;
    }

    @Override
    public List<StringAttribute> readTextAndGetAllCharacterAttributes() throws IOException {
        return null;
    }

    @Override
    public double getFullEntropy() {
        return 0;
    }
}
