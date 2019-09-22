package primat;

public class CharacterAttribute {

    private char character;
    private int count;
    private double possibility;
    private double entropy;

    public CharacterAttribute(char character, int count) {
        this.character = character;
        this.count = count;
    }

    public char getCharacter() {
        return character;
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

    @Override
    public String toString() {
        return "CharacterAttribute{" +
                "character=" + character +
                ", count=" + count +
                ", possibility=" + possibility +
                ", entropy=" + entropy +
                '}';
    }
}