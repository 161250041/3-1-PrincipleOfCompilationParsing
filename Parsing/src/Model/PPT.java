package Model;

import java.util.*;

public class PPT {
    private Map<String, Integer[]> PPT;
    private List<Derivation> derivations;
    public static String[] VtList = new String[]{
            "if", "while", "else", "(", ")", "{", "}", ";", "+", "-", "*", "/", "||", "&&",
            ">", ">=", "<", "<=", "==", "!=", "=", "num", "word", "$"
    };

    public Derivation search(String Vn,String Vt ) {   //输入非终结符和终结符 得到下一个式子
        Integer[] pptRow = PPT.get(Vn);
        int i=0;
        for(String str:VtList){
            if (str.equals(Vt)) {
                break;
            }
            i++;
        }

        if (pptRow!=null&&pptRow[i] > 0)
            return derivations.get(pptRow[i] - 1);
        else
            return null;

    }
    public PPT() {

        PPT = new HashMap<>();
        PPT.put("S", new Integer[]{2, 3, -1, -1, -1, -1, 4, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 1, 4});
        PPT.put("E", new Integer[]{-1, -1, -1, 5, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 5, 5, -1});
        PPT.put("E1", new Integer[]{-1, -1, -1, -1, -1, -1, -1, -1, 8, 9, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1});
        PPT.put("E2", new Integer[]{-1, -1, -1, -1, 7, -1, -1, 7, 6, 6, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1});
        PPT.put("T", new Integer[]{-1, -1, -1, 10, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 10, 10, -1});
        PPT.put("T1", new Integer[]{-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 13, 14, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1});
        PPT.put("T2", new Integer[]{-1, -1, -1, -1, 12, -1, -1, 12, 12, 12, 11, 11, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1});
        PPT.put("F", new Integer[]{-1, -1, -1, 17, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 15, 16, -1});
        PPT.put("C", new Integer[]{-1, -1, -1, 18, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 18, 18, -1});
        PPT.put("C'", new Integer[]{-1, -1, -1, -1, 20, -1, -1, -1, -1, -1, -1, -1, 19, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1});
        PPT.put("D", new Integer[]{-1, -1, -1, 21, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 21, 21, -1});
        PPT.put("D'", new Integer[]{-1, -1, -1, -1, 23, -1, -1, -1, -1, -1, -1, -1, 23, 22, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1});
        PPT.put("H", new Integer[]{-1, -1, -1, 24, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 25, 25, -1});
        PPT.put("K", new Integer[]{-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 26, 27, -1});
        PPT.put("COP", new Integer[]{-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 28, 29, 30, 31, 32, 33, -1, -1, -1, -1});


        derivations = new ArrayList<>();
        derivations.add(new Derivation("S", new String[]{"word", "=", "E", ";", "S"})); // S -> word=E;S
        derivations.add(new Derivation("S", new String[]{"if", "(", "C", ")", "{", "S", "}", "else", "{", "S", "}"})); // S -> if (C) {S} else {S}
        derivations.add(new Derivation("S", new String[]{"while", "(", "C", ")", "{", "S", "}", "S"})); // S -> while (C) {S} S
        derivations.add(new Derivation("S", null)); // S -> ε
        derivations.add(new Derivation("E", new String[]{"T", "E2"})); // E -> TE’’
        derivations.add(new Derivation("E2", new String[]{"E1", "E2"})); // E’’ -> E’E’’
        derivations.add(new Derivation("E2", null)); // E’’ -> ε
        derivations.add(new Derivation("E1", new String[]{"+", "T"})); // E’ -> +T
        derivations.add(new Derivation("E1", new String[]{"-", "T"})); // E’ -> -T
        derivations.add(new Derivation("T", new String[]{"F", "T2"})); // T -> FT’’
        derivations.add(new Derivation("T2", new String[]{"T1", "T2"})); // T’’ -> T’T’’
        derivations.add(new Derivation("T2", null)); // T’’ -> ε
        derivations.add(new Derivation("T1", new String[]{"*", "F"})); // T’ -> *F
        derivations.add(new Derivation("T1", new String[]{"/", "F"})); // T’ -> /F
        derivations.add(new Derivation("F", new String[]{"num"})); // F -> num
        derivations.add(new Derivation("F", new String[]{"word"})); // F -> word
        derivations.add(new Derivation("F", new String[]{"(", "E", ")"})); // F -> (E)
        derivations.add(new Derivation("C", new String[]{"D", "C'"})); // C -> DC’
        derivations.add(new Derivation("C'", new String[]{"||", "D", "C'"})); // C’ -> ||DC’
        derivations.add(new Derivation("C''", null)); // C’ -> ε
        derivations.add(new Derivation("D", new String[]{"H", "D'"})); // D -> HD’
        derivations.add(new Derivation("D'", new String[]{"&&", "H", "D'"})); // D’ -> &&HD’
        derivations.add(new Derivation("D'", null)); // D’ -> ε
        derivations.add(new Derivation("H", new String[]{"(", "C", ")"})); // H -> (C)
        derivations.add(new Derivation("H", new String[]{"K", "COP", "K"})); //  H -> K COP K
        derivations.add(new Derivation("K", new String[]{"num"})); // K -> num
        derivations.add(new Derivation("K", new String[]{"word"})); // K -> word
        derivations.add(new Derivation("COP", new String[]{">"})); // COP -> >
        derivations.add(new Derivation("COP", new String[]{">="})); // COP -> >=
        derivations.add(new Derivation("COP", new String[]{"<"})); // COP -> <
        derivations.add(new Derivation("COP", new String[]{"<="})); // COP -> <=
        derivations.add(new Derivation("COP", new String[]{"=="})); // COP -> ==
        derivations.add(new Derivation("COP", new String[]{"!="})); // COP -> !=
    }

}
