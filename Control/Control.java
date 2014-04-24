package Control;
import java.util.*;
import Token.*;
import Build.*;
import LexToInt.*;
public class Control{
    public String name;
    public int index;
    public String refer;

    public Control(String a){
        this.name = a;
        this.index = 0;
        this.refer = null;
    }

    public Control(String a, int b, String c){
        this.name = a;
        this.index = b;
        this.refer = c;
    }

    public static void pre_lam_traversal(NSTree a, ArrayList<ArrayList<Control>> b, int i){
      if(a != null){
        if(a.retname() != "lambda" && a.retname() != "->"){
          b.get(i).add(new Control(a.retname()));
          if(a.retchild() != null)
            pre_lam_traversal(a.retchild(), b, i);
          if(a.retsibling() != null)
            pre_lam_traversal(a.retsibling(), b, i);
        }
        else if(a.retname() == "lambda"){
          if(a.retchild().retname() != ",")
            b.get(i).add(new Control("lambda", b.size(), a.retchild().retname()));
          else{
            String str = a.retchild().retchild().retname();
            NSTree temptree = a.retchild().retchild();
            while(temptree.retsibling() != null){
              str = str + "," + temptree.retsibling().retname();
              temptree = temptree.retsibling();
            }
            b.get(i).add(new Control("lambda", b.size(), str));
          }
          b.add(new ArrayList<Control>());
          /* store current index */
          int temp = b.size()-1;
          if(a.retsibling() != null)
            pre_lam_traversal(a.retsibling(), b, i);
          pre_lam_traversal(a.retchild().retsibling(), b, temp);
        }
        /* handle "->" */
        else{
          int size = b.size();
          b.add(new ArrayList<Control>());
          b.add(new ArrayList<Control>());
          b.get(size-1).add(new Control("delta", size, null));
          b.get(size-1).add(new Control("delta", size+1, null));
          b.get(size-1).add(new Control("beta"));
          pre_lam_traversal(a.retchild().retsibling().retsibling(), b, size+1);
          a.retchild().retsibling().setsibling(null);
          pre_lam_traversal(a.retchild().retsibling(), b, size);
          a.retchild().setsibling(null);
          pre_lam_traversal(a.retchild(), b, size - 1);
        }
      }
    }

}
