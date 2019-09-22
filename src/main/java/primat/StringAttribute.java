package primat;

public class StringAttribute {

    private String string;
    private int count;
    private double possibility;
    private double entropy;

    public StringAttribute(String string, int count) {
        this.string = string;
        this.count = count;
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

}