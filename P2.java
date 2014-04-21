import java.io.*;
import Token.*;
import AST.*;
import java.util.*;
import Build.*;
import LexToInt.*;
public class P2 {

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
            AST.TokenList(list);
            System.out.print("\n");
        }
        Token.removeDelete(list);
        AST.E(list, tree);
        if(printAST == 1)
            tree.peek().PrintTree(0);
        tree.peek().ST();
        if(printST == 1)
            tree.peek().PrintTree(0);
    }
}
