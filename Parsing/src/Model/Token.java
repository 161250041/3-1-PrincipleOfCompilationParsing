package Model;

public class Token {
    private int attributeValue;
    private String tokenName;

    public int getAttributeValue() {
        return attributeValue;
    }

    public String getTokenName() {
        return tokenName;
    }

    public Token(int attributeValue, String tokenName) {
        this.attributeValue = attributeValue;
        this.tokenName = tokenName;
    }


    public String print() {
        return ("<" + this.tokenName + ", " + this.attributeValue + ">");
    }


}
