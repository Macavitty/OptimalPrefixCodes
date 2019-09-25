package primat;

public class SymbolAttribute {

    private String string;
    private int count;
    private double probability;
    private String code = "";
    private int idx;

    public SymbolAttribute(String string, int count, int idx) {
        this.string = string;
        this.count = count;
        this.idx = idx;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public double getProbability() {
        return probability;
    }

    public void setProbability(double probability) {
        this.probability = probability;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean addDigitToCode(String digit) {
        if (digit.equals("0") || digit.equals("1")){
            this.code = code + digit;
            return true;
        }
        return false;
    }

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }
}