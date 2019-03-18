package Model;

public class Derivation {
    private String left;
    private String[] right;

    public Derivation(String left, String[] right) {
        this.left = left;
        this.right = right;
    }

    public String[] getRight() {
        return right;
    }

    public String print() {
        String str = left + " -> ";
        if (right != null)
            for (String s : right) str += (s + " ");
        else str += "ε";
        return str;
    }
}
