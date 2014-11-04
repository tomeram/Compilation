import java.util.regex.Pattern;

public class Lexer {
	public String assign = "[a-z][:][=]";
	public String num = "(0|([1-9][0-9]*))";
	public String varOrNum = "(" + num + "|[a-z])";
	public String specialChar = "([\\+|\\*|\\\\|-])";
	public String exp = "(" + varOrNum + "|" + specialChar + ")*";

	public String binOp = "(<|>|[=][=]|[!][=]|[<][=]|[>][=])";
	public String ifStmt = "[i][f][\\(](" + varOrNum + "[ ]" + binOp + "[ ]"
			+ varOrNum + ")[\\)]";
	public String ifStmt2 = "[[ ]" + ifStmt + "]*";

	public String goto_str = "(\\bgoto \\b" + num + ")";
	
	public String print = "(\\bprint\\(\\b" + exp + "[\\)])";


	public boolean checkAssign(String line) {
		return Pattern.matches(assign + exp, line);
	}
	
	public boolean checkIfStmt(String line) {
		return Pattern.matches(ifStmt + ifStmt2, line);
	}
	
	public boolean checkGoto(String line) {
		return Pattern.matches(goto_str, line);
	}
	
	public boolean checkPrint(String line) {
		return Pattern.matches(print, line);
	}
}
