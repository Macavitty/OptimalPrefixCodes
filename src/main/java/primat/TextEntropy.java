package primat;

import java.io.*;
import java.util.*;

public interface TextEntropy {

    public static final char PUNCTUATION_CHARACTER = '.';

    public List<CharacterAttribute> readTextAndGetAllCharacterAttributes() throws IOException;

    public double getFullEntropy();
}
