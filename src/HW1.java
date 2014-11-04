import java.util.regex.Pattern;

public class HW1 {
	private String assign = "[a-z][:][=]";
	private String num = "(0|([1-9][0-9]*))";
	private String varOrNum = "(" + num + "|[a-z])";
	private String specialChar = "([\\+|\\*|\\\\|-])";
	private String exp = "(" + varOrNum + "|" + specialChar + ")*";
	
	private String binOp = "(<|>|[=][=]|[!][=]|[<][=]|[>][=])";
	private String ifStmt = "[i][f][\\(](" + varOrNum + "[ ]" + binOp + "[ ]" + varOrNum + ")[\\)]";
	private String ifStmt2 = "[[ ]" + ifStmt + "]*";
	
	private String goto_str = "(\\bgoto \\b" + num + ")";

	public static void main(String[] args) {
		HW1 h = new HW1();
		if (Pattern.matches(h.assign + h.exp ,
				"x:=+1"))
			System.out.println("here");
		
		if (Pattern.matches(h.ifStmt + h.ifStmt2, "if(a >= b) if(c < 1)"))
			System.out.println("here2");
		
		if (Pattern.matches(h.goto_str, "goto 1"))
			System.out.println("here3");
	}
}
