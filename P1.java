import java.io.*;
import Token.*;
import java.util.*;
import Build.*;
import LexToInt.*;
public class P1 {

    public static void main(String[] args) {
        String input;
        String s;
        ArrayList<Token> list = new ArrayList<Token>();
        Stack<NSTree> tree = new Stack<NSTree>();
        int location = 0;
        int i = 0;
        int k = 0;
        int line = 0;
        int printToken = 0;
        int printAST = 0;
        int printST = 0;
        int noout = 0;
        while (k < args.length){
            if(args[k].equals("-ast"))
                printAST = 1;
            else if(args[k].equals("-st"))
                printST = 1;
            else if(args[k].equals("-noout"))
                noout = 1;
            else if(args[k].equals("-l"))
                printToken = 1;
            k++;
        }
        try {
            FileReader fr = new FileReader(args[args.length-1]);
            BufferedReader br = new BufferedReader(fr);
            while (br.ready()) {
                input = br.readLine();
                line++;
                while(input != null){
                	location = 0;
                    list.add(new Token(0,null));
                    //System.out.println(input);
                	if (Token.makespace(input, list.get(i)) != -1 ) {
                		location = Token.makespace(input, list.get(i));
                    }
                	else if (Token.makeIdentifier(input, list.get(i)) != -1) {
						location = Token.makeIdentifier(input, list.get(i));
                    }
                    else if (Token.makestring(input, list.get(i)) != -1) {
                        location = Token.makestring(input, list.get(i));
                    }
                    else if (Token.makepunc(input, list.get(i)) != -1) {
                        location = Token.makepunc(input, list.get(i));
                    }
                    else if (Token.makecomment(input, list.get(i)) != -1) {
                        location = Token.makecomment(input, list.get(i));
                    }
                    else if (Token.makeoperator(input, list.get(i)) != -1) {
                        location = Token.makeoperator(input, list.get(i));
                    }
                    else if (Token.makeint(input, list.get(i)) != -1) {
                        location = Token.makeint(input, list.get(i));
                    }
                    else{
                        System.out.println("Your input is error");
                        System.out.println("Line:"+line+",   "+input);
                    }
                	input = location==input.length() ? null : input.substring(location,input.length());
                    i++;
                    }
                    list.add(new Token(0,null));
                    Token.setToken(list.get(i), 0, "\n", LexToInt.delete);
                    i++;
                }
            br.close();
            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        list.remove(list.size()-1);
        if(printToken == 1){
            TokenList(list);
            System.out.print("\n");
        }
        Token.removeDelete(list);
        E(list, tree);
        if(printAST == 1)
            tree.peek().PrintTree(0);
        tree.peek().ST();
        if(printST == 1)
            tree.peek().PrintTree(0);

    }

    public static void TokenList(ArrayList<Token> a){
        for(int i = 0; i < a.size(); i++)
            a.get(i).printname();
    }

    public static void BuildTree(String a, int d ,int b, Stack<NSTree> c){
        NSTree new_node = new NSTree(d,a);
        while (b!=0){
            new_node.setchild(c.pop());
            b--;
        }
        c.push(new_node);
    }

    public static void PushStack(ArrayList<Token> a, Stack<NSTree> b){
        NSTree temp = new NSTree(0,null);
        temp.setname(a.get(0).retname());
        temp.settype(a.get(0).retype());
        b.push(temp);
        a.remove(0);
    }

    public static void PushStackEmpty(Stack<NSTree> b){
        NSTree temp = new NSTree(0,"()");
        b.push(temp);
    }

    public static void read(ArrayList<Token> a){
        a.remove(0);
    }

    public static int next(ArrayList<Token> a){
        if(a.isEmpty())
            return 0;
        else
            return a.get(0).retype();
    }

   public static void E(ArrayList<Token> a, Stack<NSTree> b){
        int n = 1;
        switch(next(a)){
            case LexToInt.let: {
                read(a);
                D(a,b);
                //System.out.println(a.get(0).retname());
                //System.out.println(next(a));
                if (next(a) != LexToInt.in) System.out.println("Need in here!!");
                read(a);
                //System.out.println(a.get(0).retname());
                E(a,b);
                BuildTree("let",LexToInt.let, 2 ,b);
                break;
            }
            case LexToInt.fn: {
                read(a);
                while(next(a) != LexToInt.dot){
                    n++;
                    Vb(a,b);
                }
                read(a);
                E(a,b);
                BuildTree("lambda",LexToInt.lambda, n, b);
                break;
            }
            default: Ew(a,b);
        }
    }

    public static void Ew(ArrayList<Token> a, Stack<NSTree> b){
        T(a,b);
        if (next(a) == LexToInt.where) {
            read(a);
            Dr(a,b);
            BuildTree("where",LexToInt.where, 2, b);
        }
    }

    public static void T(ArrayList<Token> a, Stack<NSTree> b){
        int n = 1;
        Ta(a,b);
        if (next(a) == LexToInt.comma) {
            while(next(a) == LexToInt.comma) {
                read(a);
                Ta(a,b);
                n++;
            }
            BuildTree("tau",LexToInt.tau, n, b);
        }
    }

    public static void Ta(ArrayList<Token> a, Stack<NSTree> b){
        Tc(a,b);
        while(next(a) == LexToInt.aug) {
            read(a);
            Tc(a,b);
            BuildTree("aug",LexToInt.aug, 2, b);
        }
    }

    public static void Tc(ArrayList<Token> a, Stack<NSTree> b){
        B(a,b);
        if (next(a) == LexToInt.conditional) {
            read(a);
            Tc(a,b);
            if (next(a) != LexToInt.or) {
                System.out.println("PassError in '|' ");
                System.exit(1);
            }
            read(a);
            Tc(a,b);
            BuildTree("->",LexToInt.conditional, 3, b);
        }
    }

    public static void B(ArrayList<Token> a, Stack<NSTree> b){
        Bt(a,b);
        while(next(a) == LexToInt.or_bit){
            read(a);
            Bt(a,b);
            BuildTree("or",LexToInt.or_bit, 2 ,b);
        }
    }

    public static void Bt(ArrayList<Token> a, Stack<NSTree> b){
        Bs(a,b);
        while(next(a) == LexToInt.and_bit){
            read(a);
            Bs(a,b);
            BuildTree("&",LexToInt.and_bit, 2 ,b);
        }
    }

    public static void Bs(ArrayList<Token> a, Stack<NSTree> b){
        if (next(a) == LexToInt.not) {
            read(a);
            Bp(a,b);
            BuildTree("not",LexToInt.not, 1 ,b);
        }
        else
            Bp(a,b);
    }

    public static void Bp(ArrayList<Token> a, Stack<NSTree> b){
        A(a,b);
        switch(next(a)){
            case LexToInt.gr : read(a); A(a,b); BuildTree("gr",LexToInt.gr, 2,b); break;
            case LexToInt.ge : read(a); A(a,b); BuildTree("ge",LexToInt.ge, 2,b); break;
            case LexToInt.ls : read(a); A(a,b); BuildTree("ls",LexToInt.ls, 2,b); break;
            case LexToInt.le : read(a); A(a,b); BuildTree("le",LexToInt.le, 2,b); break;
            case LexToInt.eq : read(a); A(a,b); BuildTree("eq",LexToInt.eq, 2,b); break;
            case LexToInt.ne : read(a); A(a,b); BuildTree("ne",LexToInt.ne, 2,b); break;
            default: break;
        }
    }

    public static void A(ArrayList<Token> a, Stack<NSTree> b){
        if (next(a) == LexToInt.plus) {
            read(a);
            At(a,b);
        }
        else if (next(a) == LexToInt.minus) {
            read(a);
            At(a,b);
            BuildTree("neg",LexToInt.neg, 1,b);
        }
        else
            At(a,b);
        while(next(a) == LexToInt.plus || next(a) == LexToInt.minus){
            switch(next(a)){
                case LexToInt.plus: read(a); At(a,b); BuildTree("+",LexToInt.plus, 2,b); break;
                case LexToInt.minus: read(a); At(a,b); BuildTree("-",LexToInt.minus, 2,b); break;
            }
        }
    }

    public static void At(ArrayList<Token> a, Stack<NSTree> b){
        Af(a,b);
        while(next(a) == LexToInt.times || next(a) == LexToInt.divide){
            switch(next(a)){
                case LexToInt.times: read(a); Af(a,b); BuildTree("*",LexToInt.times, 2,b); break;
                case LexToInt.divide: read(a); Af(a,b); BuildTree("/",LexToInt.divide, 2,b); break;
            }
        }
    }

    public static void Af(ArrayList<Token> a, Stack<NSTree> b){
        Ap(a,b);
        if (next(a) == LexToInt.exp) {
            read(a);
            Af(a,b);
            BuildTree("**",LexToInt.exp, 2,b);
        }
    }

    public static void Ap(ArrayList<Token> a, Stack<NSTree> b){
        R(a,b);
        while (next(a) == LexToInt.at) {
            read(a);
            if (next(a) != LexToInt.identifier) {
                System.out.println("ParseError after @, need an ID");
                System.exit(1);
            }
            PushStack(a,b);
            R(a,b);
            BuildTree("@",LexToInt.at, 3,b);
        }
    }

    public static void R(ArrayList<Token> a, Stack<NSTree> b){
        if (Rn(a,b)) {
            while(Rn(a,b)){
                BuildTree("gamma",LexToInt.gamma, 2,b);
                //System.out.println("BT!!!!");
            }
        }
    }

    public static boolean Rn(ArrayList<Token> a, Stack<NSTree> b){
        switch(next(a)){
            case LexToInt.identifier: case LexToInt.integer: case LexToInt.fal:
            case LexToInt.string: case LexToInt.tr: case LexToInt.dummy:
            case LexToInt.nil: PushStack(a,b); return true;
            case LexToInt.l_punction: read(a); E(a,b); if(next(a) != LexToInt.r_punction) {System.out.println("ParseError, need )"); System.exit(1);} read(a); return true;
            default: return false;
        }
    }

    public static void D(ArrayList<Token> a, Stack<NSTree> b){
        Da(a,b);
        if (next(a) == LexToInt.within) {
            read(a);
            D(a,b);
            BuildTree("within",LexToInt.within, 2,b);
        }
    }

    public static void Da(ArrayList<Token> a, Stack<NSTree> b){
        Dr(a,b);
        //System.out.println(a.get(0).retname());
        int i = 1;
        while(next(a) == LexToInt.and){
            read(a);
            Dr(a,b);
            i++;
        }
        if(i > 1) BuildTree("and",LexToInt.and, i, b);
    }

    public static void Dr(ArrayList<Token> a, Stack<NSTree> b){
        if (next(a) == LexToInt.rec) {
            read(a);
            Db(a,b);
            BuildTree("rec",LexToInt.rec, 1,b);
        }
        else
            Db(a,b);
    }

    public static void Db(ArrayList<Token> a, Stack<NSTree> b){
        int i = 1;
        if (next(a) == LexToInt.l_punction) {
            read(a);
            D(a,b);
            if(next(a) != LexToInt.r_punction) {
                System.out.println("ParseError, need )");
                System.exit(1);
            }
            read(a);
            return;
        }
        else if(Vl(a,b)){
            //System.out.println(a.get(0).retname());
            if(next(a) == LexToInt.equal){
                read(a);
                E(a,b);
                BuildTree("=",LexToInt.equal, 2,b);
            }
            else{
                //System.out.println(a.get(0).retname());
                while(Vb(a,b))
                    i++;
                if (next(a) != LexToInt.equal){
                    System.out.println("ParseError, need =");
                    System.exit(1);
                }
                //System.out.println(a.get(0).retname());
                read(a);
                //System.out.println(a.get(0).retname());
                E(a,b);
                //System.out.println(i);
                //System.out.println(a.get(0).retname());
                //System.out.println(b.peek().retname());
                //System.out.println(b.size());
                BuildTree("function_form",LexToInt.func_form, i+1,b);
                //System.out.println("fuck");
            }
        }
    }

    public static boolean Vb(ArrayList<Token> a, Stack<NSTree> b){
        switch(next(a)){
            case LexToInt.identifier: PushStack(a,b);return true;
            case LexToInt.l_punction: {
                read(a);
                if (Vl(a,b)) {
                    if (next(a) != LexToInt.r_punction) {
                        System.out.println("ParseError! Need ) after Vl");
                        System.exit(1);
                    }
                    read(a);
                    return true;
                }
                else if (next(a) != LexToInt.r_punction){
                    System.out.println("ParseError! Need ) after Vl");
                    System.exit(1);
                }
                read(a);
                PushStackEmpty(b);
                return true;
            }
            default: return false;
        }
    }

    public static boolean Vl(ArrayList<Token> a, Stack<NSTree> b){
        int n = 1;
        if (next(a) == LexToInt.identifier) {
            PushStack(a,b);
            while(next(a) == LexToInt.comma){
                read(a);
                if (next(a) != LexToInt.identifier){System.out.println("ParseError! Need an ID after comma!"); System.exit(1);}
                PushStack(a,b);
                n++;
            }
            if (n > 1) {
                BuildTree(",",LexToInt.comma, n,b);
            }
            return true;
        }
        return false;
    }

}
