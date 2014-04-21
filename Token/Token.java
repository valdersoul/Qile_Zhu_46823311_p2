package Token;
import java.util.*;
import LexToInt.*;
public class Token{
    private  int type;
    private  String name;
    private int length;

    public Token(int a, String c){
        this.type = a;
        this.length = a;
        this.name = c;
    }

    public void printtype(){
        System.out.println(this.type);
    }

    public void printname(){
        System.out.print(this.name);
    }

    public void printlength(){
        System.out.println(this.length);
    }

    public int retype(){
        return this.type;
    }

    public int relength(){
        return this.length;
    }

    public String retname(){
        return this.name;
    }

    public void settype(int a){
        this.type = a;
    }

    public void setname(String a){
        this.name = a;
    }

    public void setlength(int a){
        this.length = a;
    }

    public static void setToken(Token t, int length, String t_name, int type){
        t.setlength(length);
        t.setname(t_name);
        t.settype(type);
    }

    public static int makeIdentifier(String a, Token b){
        final char under = '_';
        char[] temp = new char[a.length()];
        temp = a.toCharArray();
        int i = 0;
        if (Character.isLetter( temp[i] )){
            while (Character.isLetter(temp[i]) || Character.isDigit(temp[i]) || temp[i] == under){
                i++;
                if (i == a.length()){
                    break;
                }
            }
            setToken(b, i, a.substring(0,i), LexToInt.identifier);
            detailID(b);
            return i;
        }
        return -1;
    }

    public static int makespace(String a, Token b){
        char[] temp = new char[a.length()];
        temp = a.toCharArray();
        int i = 0;
        if (a.isEmpty()) {
            setToken(b, a.length(), "\n", LexToInt.delete);
            return a.length();
        }
        if (Character.isWhitespace(temp[i])) {
            while (Character.isWhitespace(temp[i])) {
                i++;
                if (i == a.length()){
                    break;
                }
            }
            setToken(b, i, a.substring(0,i), LexToInt.delete);
            return i;
        }
        return -1;
    }

    public static int makestring(String a, Token b){
        char[] temp = new char[a.length()];
        temp = a.toCharArray();
        int i = 1;
        if (temp[0] == '\''){
             while (Character.isLetter(temp[i]) || Character.isDigit(temp[i]) || isoperator(temp[i])
                || ispunc(temp[i]) || Character.isWhitespace(temp[i])){
                i++;
                if (temp[i-1] == '\'') {
                    setToken(b, i-2, a.substring(1,i-1), LexToInt.string);
                    return i;
                }
                if (i == a.length()){
                    break;
                }
            }
            if(temp[i-1] == '\''){
                setToken(b, i-2, a.substring(1,i-1), LexToInt.string);
                return i;
            }
        }
        return -1;
    }

    public static int makeint(String a, Token b){
        char[] temp = new char[a.length()];
        temp = a.toCharArray();
        int i = 0;
        if(Character.isDigit(temp[i])){
            while(Character.isDigit(temp[i])){
                i++;
                if (i == a.length()){
                    break;
                }
            }
            setToken(b, i, a.substring(0,i), LexToInt.integer);
            return i;
        }
        return -1;
    }

    public static int makepunc(String a, Token b){
        char[] temp = new char[a.length()];
        temp = a.toCharArray();
        int i = 0;
        if(ispunc(temp[i])){
            switch(temp[i]){
                case '(' : setToken(b, 1, "(", LexToInt.l_punction); break;
                case ')' : setToken(b, 1, ")", LexToInt.r_punction); break;
                case ';' : setToken(b, 1, ";", LexToInt.semi); break;
                case ',' : setToken(b, 1, ",", LexToInt.comma); break;
            }
            return 1;
        }
        return -1;
    }

    public static int makeoperator(String a, Token b){ // huge work to do
         char[] temp = new char[a.length()];
         temp = a.toCharArray();
         if (isoperator(temp[0]) && temp[0] != '\'') {
            switch(temp[0]){
                case '<' : if (temp[1] == '=') {setToken(b, 2, "le", LexToInt.le); return(2);} break;
                case '>' : if (temp[1] == '=') {setToken(b, 2, "ge", LexToInt.ge); return(2);} break;
                case '-' : if (temp[1] == '>') {setToken(b, 2, "->", LexToInt.conditional); return(2);} break;
                case '*' : if (temp[1] == '*') {setToken(b, 2, "**", LexToInt.exp); return(2);} break;
                default: break;
            }
            setToken(b, 1, a.substring(0,1), LexToInt.operator);
            detailOpe(b);
            return 1;
         }
         return -1;
    }

    public static int makecomment(String a, Token b){
         char[] temp = new char[a.length()];
         temp = a.toCharArray();
         if(temp[0] == '/' && temp[1] == '/'){
            setToken(b, a.length()-2, a.substring(2,a.length()), LexToInt.delete);
            return a.length();
        }
        return -1;
    }

    public static void detailOpe(Token a){
        switch(a.retname()){
            case "<" : a.settype(LexToInt.ls); a.setname("ls"); break;
            case ">" : a.settype(LexToInt.gr); a.setname("gr"); break;
            case "&" : a.settype(LexToInt.and_bit); break;
            case "|" : a.settype(LexToInt.or); break;
            case "." : a.settype(LexToInt.dot); break;
            case "*" : a.settype(LexToInt.times); break;
            case "+" : a.settype(LexToInt.plus); break;
            case "-" : a.settype(LexToInt.minus); break;
            case "/" : a.settype(LexToInt.divide); break;
            case "@" : a.settype(LexToInt.at); break;
            case "=" : a.settype(LexToInt.equal); break;
            default: break;
        }
    }

    public static void detailID(Token a){
        if(a.relength() == 2){
            switch(a.retname()){
                case "in" : a.settype(LexToInt.in);break;
                case "gr" : a.settype(LexToInt.gr);break;
                case "ge" : a.settype(LexToInt.ge);break;
                case "ls" : a.settype(LexToInt.ls);break;
                case "le" : a.settype(LexToInt.le);break;
                case "eq" : a.settype(LexToInt.eq);break;
                case "ne" : a.settype(LexToInt.ne);break;
                case "or" : a.settype(LexToInt.or_bit);break;
                case "fn" : a.settype(LexToInt.fn);break;
                default: break;
            }
        }
        if(a.relength() == 3){
            switch(a.retname()){
                case "let" : a.settype(LexToInt.let);break;
                case "not" : a.settype(LexToInt.not);break;
                case "neg" : a.settype(LexToInt.neg);break;
                case "nil" : a.settype(LexToInt.nil);break;
                case "and" : a.settype(LexToInt.and);break;
                case "rec" : a.settype(LexToInt.rec);break;
                case "aug" : a.settype(LexToInt.aug);break;
                default: break;
            }
        }
        if (a.relength() == 4 && a.retname().equals("true"))
            a.settype(LexToInt.tr);

        if (a.relength() == 5) {
            switch(a.retname()){
                case "where" : a.settype(LexToInt.where);break;
                case "false" : a.settype(LexToInt.fal);break;
                case "dummy" : a.settype(LexToInt.dummy);break;
                default: break;
            }
        }
        if (a.relength() == 6 && a.retname().equals("within"))
            a.settype(LexToInt.within);
    }

    public static boolean isoperator(char a){
        String op = new String("+-*<>&\\.@/:=~|$!#%\t\b^-'_[]{}\"?`");
        if (op.indexOf(a) >= 0) {
            return true;
        }
        else
            return false;
    }

    public static boolean ispunc(char a){
        String punc = new String("();,");
        if (punc.indexOf(a) >= 0) {
            return true;
        }
        else
            return false;
    }

    public static void removeDelete(ArrayList<Token> a){
        Iterator<Token> iter = a.iterator();
        while(iter.hasNext()){
            if (iter.next().retype() == LexToInt.delete)
                iter.remove();
        }
    }
}
