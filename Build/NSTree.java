package Build;
import LexToInt.*;
public class NSTree implements Cloneable {
	private String name;
	private int type;
	private int childnum;
	private NSTree child;
	private NSTree sibling;

	public NSTree(int a, String b){
		this.name = b;
		this.type = a;
		this.childnum = 0;
		this.child = null;
		this.sibling = null;
	}

	public Object clone(){
		NSTree o = null;
		try {
			o = (NSTree) super.clone();
			o.name = this.name;
			o.type = this.type;
			o.childnum = this.childnum;
			if(this.child != null)
				o.child = (NSTree) child.clone();
			else
				o.child = null;
			if(this.sibling != null)
				o.sibling = (NSTree) sibling.clone();
			else
				o.sibling = null;
		} catch (CloneNotSupportedException e){
			e.printStackTrace();
		}
		return o;
	}

	public void copy(NSTree a){
		this.name = a.retname();
		this.type = a.retype();
		this.childnum = a.retchildnum();
		if(a.child != null)
			this.child = (NSTree) a.child.clone();
		else
			this.child = null;
		if(a.sibling != null)
			this.sibling = (NSTree) a.sibling.clone();
		else
			this.sibling = null;
	}

	public void copynosib(NSTree a){
		this.name = a.retname();
		this.type = a.retype();
		this.childnum = a.retchildnum();
		if(a.child != null)
			this.child = (NSTree) a.child.clone();
		else
			this.child = null;
		this.sibling = null;
	}

	public void setname(String a){
		this.name = a;
	}

	public void settype(int a){
		this.type = a;
	}

	public int retchildnum(){
		return this.childnum;
	}

	public String retname(){
		return this.name;
	}

	public int retype(){
		return this.type;
	}

	public NSTree retchild(){
		return this.child;
	}

	public NSTree retsibling(){
		return this.sibling;
	}

	public void setchild(NSTree a){
		this.childnum++;
		if(a == null)
			this.child = null;
		else if(this.child == null)
			this.child = (NSTree) a.clone();
		else {
			NSTree temp = (NSTree) this.child.clone();
			this.child = (NSTree) a.clone();
			this.child.setsibling(temp);
		}
	}

	public void resetchild(){
		if(this.child != null){
			if(this.child.retsibling()!=null)
				this.child.setsibling(null);
			this.child = null;
			this.childnum=0;
		}
	}

	public void setsibling(NSTree a){
		if(a != null)
			this.sibling = (NSTree) a.clone();
		else
			this.sibling = null;
	}

	public void PrintTree(int i){
		for (int n = 0;n < i ;n++ ) {
			System.out.print('.');
		}
		if(this.type == LexToInt.identifier ){
			System.out.print("<ID:");
			System.out.print(this.name);
			System.out.println(">");
		}
		else if (this.type == LexToInt.string){
			System.out.print("<STR:");
			System.out.print("\'"+this.name+"\'");
			System.out.println(">");
		}
		else if (this.type == LexToInt.integer){
			System.out.print("<INT:");
			System.out.print(this.name);
			System.out.println(">");
		}
		else if (this.type == LexToInt.nil){
			System.out.println("<nil>");
		}
		else if (this.type == LexToInt.tr || this.type == LexToInt.fal || this.type == LexToInt.dummy || this.type == LexToInt.ystar){
			System.out.print("<");
			System.out.print(this.name);
			System.out.println(">");
		}
		else
			System.out.println(this.name);
    	if(this.child != null)
    		this.child.PrintTree(i+1);
    	if(this.sibling != null)
    		this.sibling.PrintTree(i);
    }

	public void reversechild(){
		NSTree temp = (NSTree)this.clone();
		NSTree a = temp.retchild();
		int i = temp.retchildnum();
		this.resetchild();
		while(i > 0){
			this.setchild(a);
			if(i == temp.retchildnum())
				this.child.setsibling(null);
			a = a.retsibling();
			i--;
		}
	}

	public void ST(){
		if(this != null){
			if(this.child != null)
				this.child.ST();
			if(this.retsibling() != null)
				this.retsibling().ST();
			this.ToSt();
		}
	}

	public void ToSt(){
  		switch(this.type){
  			case LexToInt.let:{
  				if(this.child.retype() != LexToInt.equal) break;
  				this.setname("gamma");
  				this.settype(LexToInt.gamma);
  				this.child.setname("lambda");
  				this.child.settype(LexToInt.lambda);
  				NSTree temp = (NSTree) this.child.retchild().retsibling().clone();
  				this.child.retchild().setsibling(this.child.retsibling());
  				this.child.setsibling(temp);
  				break;
  			}
  			case LexToInt.where:{
  				if(this.child.retsibling().retype() != LexToInt.equal) break;
  				this.setname("gamma");
  				this.settype(LexToInt.gamma);
  				NSTree temp = (NSTree) this.child.clone();
  				this.child.copy(this.child.retsibling());
   				this.child.setname("lambda");
  				this.child.settype(LexToInt.lambda);
  				temp.setsibling(null);
  				this.child.setsibling(this.child.retchild().retsibling());
  				this.child.retchild().setsibling(temp);
  				break;
  			}
  			case LexToInt.within:{
  				if(this.child.retsibling().retype() != LexToInt.equal || this.child.retype() != LexToInt.equal && this.retchildnum() == 2) break;
  				this.setname("=");
  				this.settype(LexToInt.equal);
  				NSTree temp1 = (NSTree) this.child.clone();
  				NSTree temp2 = (NSTree) this.child.retsibling().clone();
  				this.resetchild();
  				this.setchild(temp2);
  				this.child.setname("gamma");
  				this.child.settype(LexToInt.gamma);
  				this.child.resetchild();
  				this.child.setchild(temp1.retchild().retsibling());
  				this.child.setchild(temp1);
  				this.child.retchild().setname("lambda");
  				this.child.retchild().settype(LexToInt.lambda);
  				this.child.retchild().retchild().setsibling(temp2.retchild().retsibling());
  				this.setchild(temp2.retchild());
  				break;
  			}
			case LexToInt.at:{
				this.setname("gamma");
				this.settype(LexToInt.gamma);
				NSTree temp1 = new NSTree(0,null);
				temp1.copynosib(this.child);
				NSTree temp2 = new NSTree(0,null);
				temp2.copynosib(this.child.retsibling());
				this.child.setsibling(this.child.retsibling().retsibling());
				this.child.setname("gamma");
				this.child.settype(LexToInt.gamma);
				this.child.resetchild();
				this.child.setchild(temp1);
				this.child.setchild(temp2);
				break;
			}
			case LexToInt.and:{
				NSTree temp = this.child;
				int no = 0;
				if(temp == null)
					break;
				while(temp !=null){
					if(temp.retype() != LexToInt.equal){no = 1;break;}
					temp = temp.retsibling();
				}
				if(no == 1)
					break;
				else{
					NSTree temp1 = new NSTree(LexToInt.comma,",");
					NSTree temp2 = new NSTree(LexToInt.tau,"tau");
					this.setname("=");
					this.settype(LexToInt.equal);
					temp = this.child;
					while(temp != null){
						temp2.setchild(temp.retchild().retsibling());
						temp.retchild().setsibling(null);
						temp1.setchild(temp.retchild());
						temp = temp.retsibling();
					}
					this.child = null;
					temp2.reversechild();
					temp1.reversechild();
					this.setchild(temp2);
					this.setchild(temp1);
				}
				break;
			}
			case LexToInt.rec:{
				if(this.child.retype() != LexToInt.equal) break;
				this.setname("=");
				this.settype(LexToInt.equal);
				this.child.setname("gamma");
				this.child.settype(LexToInt.gamma);
				NSTree temp = (NSTree) this.child.retchild().clone(); // copy x
				NSTree temp1 = (NSTree) this.child.retchild().retsibling().clone(); // copy E
				temp.setsibling(null);
				this.setchild(temp);
				this.child.retsibling().retchild().resetchild();
				this.child.retsibling().retchild().setname("Y*");
				this.child.retsibling().retchild().settype(LexToInt.ystar);
				this.child.retsibling().retchild().retsibling().setname("lambda");
				this.child.retsibling().retchild().retsibling().settype(LexToInt.lambda);
				this.child.retsibling().retchild().retsibling().resetchild();
				this.child.retsibling().retchild().retsibling().setsibling(null);
				this.child.retsibling().retchild().retsibling().setchild(temp1);
				this.child.retsibling().retchild().retsibling().setchild(temp);
				break;
			}
			case LexToInt.func_form: case LexToInt.lambda:{
				if(this.retchildnum() < 3) break;
				if (this.retype() == LexToInt.func_form){
					this.setname("=");
					this.settype(LexToInt.equal);
				}
				NSTree temp = (NSTree) this.child.retsibling().clone();
				NSTree temp2; // to build the lamda tree
				NSTree temp3 = this.child; // the new tree
				this.child.setsibling(null);
				while(temp.retsibling() != null){
					temp2 = BuildLamdaTree(temp);
					temp3.setsibling(temp2);
					temp3 = temp3.retsibling().retchild();
					temp = temp.retsibling();
				}
				//temp2 = BuildLamdaTree(temp);
				//temp2.retchild().setsibling(temp.retsibling());
				temp3.setsibling(temp);
				break;
			}
  			default: break;
 		}
  	}

  	private NSTree BuildLamdaTree(NSTree a){
  		NSTree b = new NSTree(LexToInt.lambda,"lambda");
  		b.setchild(a);
  		b.retchild().setsibling(null);
  		return b;
  	}


}
