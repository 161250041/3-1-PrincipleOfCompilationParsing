package SyntacticAnalysis;

import LexicalAnalysis.LexicalAnalysis;
import Model.Derivation;
import Model.PPT;
import Model.Token;

import java.io.*;
import java.util.*;

public class Main {

    private static PPT ppt = new PPT();
    private static List<String> result;

    public static void main(String[] args) {
        String inputFile = "project1";
        result = new ArrayList<>();
        LexicalAnalysis lexicalAnalysis = new LexicalAnalysis();
        List<Token> tokens = lexicalAnalysis.getTokens(inputFile);

        //初始化栈
        Stack<String> stack = new Stack<>();
        stack.push("$");
        stack.push("S");

        while (tokens.size() > 0 && stack.size() > 0) {
            String tokenStr;
            Token token = tokens.get(0);

            if (token.getAttributeValue() == 54) {
                tokenStr = "num";
            } else if (token.getAttributeValue() == 55) {
                tokenStr = "word";
            } else {
                tokenStr = token.getTokenName();
            }
            String left = stack.pop();

            //栈顶与目前token相同
            if (tokenStr.equals(left)) {
                tokens.remove(0);                //后移
            } else if (left.equals("$")) { // 栈顶与目前token不相同   //如果栈顶为终结符，error
                result.add("ERROR:存在无效输入");
                break;
            } else {
                Derivation derivation = ppt.search(left,tokenStr);
                if (derivation == null) {                           //无对应的式子
                    result.add("ERROR:无效的token \"" + tokenStr + "\"");
                    break;
                } else {                                            //有对应的式子
                    result.add(derivation.print());
                    String[] strings = derivation.getRight();
                    if (strings != null) {
                        for (int i=strings.length-1;i>=0;i--) {
                            stack.push(strings[i]);
                        }
                    }
                }
            }

        }


        //输出
        File file=new File(inputFile+"_syn.txt");
        try {
            if(!file.exists())
                file.createNewFile();

            BufferedWriter br=new BufferedWriter(new FileWriter(file));
            int i=0;
            for (String res : result) {
                br.append(i+") "+res+'\n');
                i++;
            }
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("语法分析完成！！");
        System.out.println("结果输出在"+inputFile+"_syn.txt");

    }


}

