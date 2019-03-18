package LexicalAnalysis;

import Model.Token;

import java.io.*;
import java.util.ArrayList;

public class LexicalAnalysis {

    public static String[] ReservedWords = new String[]{
            "public", "protected", "private", "class", "static", "void", "char", "String", "int", "float", "double", "if", "else", "switch", "case", "for", "while", "do", "try", "catch"
    };


    private static char chars[] = new char[400];
    private static int ind = 0;
    private static int commentsRow = -1;
    private static int row = 1;
    private static ArrayList<Token> tokens = new ArrayList<>();

    public static void lex(String path) {
        //input
        File file=new File(path+".txt");
        if(!file.exists()||file.isDirectory())
            try {
                throw new FileNotFoundException();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        BufferedReader br= null;
        try {
            br = new BufferedReader(new FileReader(file));
            String str="";

            while((str=br.readLine())!=null){
                char[] temp = str.toCharArray();
                for (char c : temp) {
                    chars[ind++] = c;
                }
                chars[ind++] = '\n';
            }
            br.close();
            chars[ind] = '~';
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }



        //从头开始
        ind = 0;
        while (chars[ind] != '~') {
            while (row <= commentsRow) {
                while (chars[ind] != '\n') {
                    ind++;
                }
                row++;
            }
            printToken();
        }

        //输出
        File file1=new File(path+"_lex.txt");
        try {
            if(!file1.exists())
                file1.createNewFile();
            BufferedWriter bw=new BufferedWriter(new FileWriter(file1));
            for (Token token : tokens) {
                bw.append(token.print()+'\n');
            }
            bw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("词法分析完成！！");
        System.out.println("结果输出在"+path+"_lex.txt");
        tokens.add(new Token(-1, "$"));
    }

    private static void printToken() {
        char c = chars[ind];
        String tokenName = "";//token-name
        int attributeValue = 0;//attribute-Value

        if (isLetter(c)) {  //首char是字母  可能为字符串或保留字
            while (isInteger(c) || isLetter(c)) {
                tokenName += c;
                c = chars[++ind];
            }

            if ((attributeValue = isReservedWords(tokenName)) > 0) {//保留字 <tokenName，attributeValue>
                Token token = new Token(attributeValue, tokenName);
                tokens.add(token);
//                token.print();
                return;
            }
            //否则不是保留字 是字符串
            attributeValue = 55;

        } else if (isInteger(c)) { //首字母是数字
            int tempInt = 0;//该数字的大小
            while (isInteger(c)) {
                tempInt = tempInt * 10 + c - '0';
                if (tempInt < 0) {//数字越界
                    System.out.println("ERROR:数字越界 " + "row:" + row);
                    while (isInteger(c)) {
                        c = chars[ind];
                        ind++;
                    }
                    return;
                }
                c = chars[++ind];
            }
//            if (c!='+'&&c!='-'&&c!='*'&&c!='/'&&c!=';'&&c!='\t'&&c!='\n') {
//                System.out.println("ERROR:存在非法类型(命名须以字母开头，由数字和字母构成）"+"row:"+row);
//            } else {
//                attributeValue = 54;
//                tokenName = String.valueOf(tempInt);
//            }
            if (c == '.') {
                tokenName = "" + tempInt;
                tokenName += c;
                c = chars[++ind];
                while (isInteger(c)) {
                    tokenName += c;
                    c = chars[++ind];
                }
            } else if (isLetter(c)) {
                System.out.println("ERROR:存在非法类型(命名须以字母开头，由数字和字母构成）" + "row:" + row);
                return;
            } else {
                tokenName = String.valueOf(tempInt);
            }
            attributeValue = 54;

        } else {
            switch (c) {
                case ' ':
                    break;

                case '\n':
                    row++;
                    break;

                case '=':
                    c = chars[++ind];
                    if (c == '=') {
                        attributeValue = 21;
                        tokenName = "==";
                    } else {
                        attributeValue = 20;
                        tokenName = "=";
                        ind--;
                    }
                    break;

                case '<':
                    c = chars[++ind];
                    if (c == '=') {
                        attributeValue = 23;
                        tokenName = "<=";
                    } else { // <
                        attributeValue = 22;
                        tokenName = "<";
                        ind--;
                    }
                    break;

                case '>':
                    c = chars[++ind];
                    if (c == '=') {
                        attributeValue = 25;
                        tokenName = ">=";
                    } else { //>
                        attributeValue = 24;
                        tokenName = ">";
                        ind--;
                    }
                    break;

                case '+':
                    c = chars[++ind];
                    if (c == '=') {
                        attributeValue = 27;
                        tokenName = "+=";
                    } else {
                        attributeValue = 26;
                        tokenName = "+";
                        ind--;
                    }
                    break;

                case '-':
                    c = chars[++ind];
                    if (c == '=') {
                        attributeValue = 29;
                        tokenName = "-=";
                    } else if (isInteger(c)) { //负数
                        int tempInt = 0;//该数字的大小
                        while (isInteger(c)) {
                            tempInt = tempInt * 10 + c - '0';
                            if (tempInt < 0) {//数字越界
                                System.out.println("ERROR:数字越界" + "row:" + row);
                                while (isInteger(c)) {
                                    c = chars[ind];
                                    ind++;
                                }
                                return;
                            }
                            c = chars[++ind];
                        }
                        if (c == '.') {
                            tokenName = "-" + tempInt;
                            tokenName += c;
                            c = chars[++ind];
                            while (isInteger(c)) {
                                tokenName += c;
                                c = chars[++ind];
                            }
                        } else if (isLetter(c)) {
                            System.out.println("ERROR:存在非法类型(命名须以字母开头，由数字和字母构成）" + "row:" + row);
                            return;
                        } else {
                            tokenName = String.valueOf(-tempInt);
                        }
                        ind--;
                        attributeValue = 54;
                    } else {
                        attributeValue = 28;
                        tokenName = "-";
                        ind--;
                    }
                    break;

                case '*':
                    c = chars[++ind];
                    if (c == '/') {        //   */注释
                        attributeValue = 52;
                        tokenName = "*/";
                    } else if (c == '=') {
                        attributeValue = 31;
                        tokenName = "*=";
                    } else {
                        attributeValue = 30;
                        tokenName = "*";
                        ind--;
                    }
                    break;

                case '/':
                    c = chars[++ind];
                    if (c == '/') {        //   //注释
                        attributeValue = 53;
                        tokenName = "//";
                        commentsRow = row;
                    } else if (c == '*') {    //   /*注释
                        attributeValue = 51;
                        tokenName = "/*";
                    } else if (c == '=') {
                        attributeValue = 33;
                        tokenName = "/=";
                    } else {
                        attributeValue = 32;
                        tokenName = "/";
                        ind--;
                    }
                    break;

                case '&':
                    c = chars[++ind];
                    if (c == '&') {
                        attributeValue = 35;
                        tokenName = "&&";
                    } else { // &
                        attributeValue = 34;
                        tokenName = "&";
                        ind--;
                    }
                    break;

                case '|':
                    c = chars[++ind];
                    if (c == '|') {
                        attributeValue = 37;
                        tokenName = "||";
                    } else {
                        attributeValue = 36;
                        tokenName = "|";
                        ind--;
                    }
                    break;

                case '!':
                    c = chars[++ind];
                    if (c == '=') {
                        attributeValue = 39;
                        tokenName = "!=";
                    } else { // !
                        attributeValue = 38;
                        tokenName = "!";
                        ind--;
                    }
                    break;

                case ',':
                    tokenName += c;
                    attributeValue = 40;
                    break;

                case ':':
                    tokenName += c;
                    attributeValue = 41;
                    break;

                case ';':
                    tokenName += c;
                    attributeValue = 42;
                    break;

                case '\'':
                    tokenName += c;
                    attributeValue = 43;
                    break;

                case '\"':
                    tokenName += c;
                    attributeValue = 44;
                    break;

                case '(':
                    tokenName += c;
                    attributeValue = 45;
                    break;

                case ')':
                    tokenName += c;
                    attributeValue = 46;
                    break;

                case '{':
                    tokenName += c;
                    attributeValue = 47;
                    break;

                case '}':
                    tokenName += c;
                    attributeValue = 48;
                    break;

                case '[':
                    tokenName += c;
                    attributeValue = 49;
                    break;

                case ']':
                    tokenName += c;
                    attributeValue = 50;
                    break;
                case '\\':
                    c = chars[++ind];
                    if (c == 't') {
                        attributeValue = 56;
                        tokenName = "\\t";
                    } else if (c == 'n') {
                        attributeValue = 57;
                        tokenName = "\\n";
                    }
                default:
                    System.out.println("ERROR:含有未定义字符" + "row:" + row);
                    break;

            }
            ind++;
        }
        if (attributeValue > 0) {
            Token token = new Token(attributeValue, tokenName);
            tokens.add(token);
//            token.print();
        }


    }

    private static boolean isInteger(char c) {
        return (c >= '0' && c <= '9');

    }

    private static boolean isLetter(char c) {
        return ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z'));
    }

    private static int isReservedWords(String str) {
        for (int t = 0; t < ReservedWords.length; t++) {
            if (str.equals(ReservedWords[t])) {
                return t;
            }
        }
        return -1;
    }

    public ArrayList<Token> getTokens(String path){
        lex(path);
        return tokens;
    }

}
