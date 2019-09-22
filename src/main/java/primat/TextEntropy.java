package primat;

import java.io.*;
import java.util.*;

public interface TextEntropy {

    String PUNCTUATION_CHARACTER = ".";

    List<StringAttribute> readTextAndGetAllCharacterAttributes() throws IOException;

    double getFullEntropy();
}
