import java.util.regex.Pattern;

public class Lexer {
	public static String assign = "[a-z][:][=]";
	public static String num = "(0|([1-9][0-9]*))";
	public static String var = "[a-z]";
	public static String varOrNum = "(" + num + "|" + var + ")";
	public static String specialChar = "([\\+|\\*|\\\\|-])";
	public static String exp = "(" + varOrNum + "|" + specialChar + ")+";

	public static String binOp = "(<|>|[=][=]|[!][=]|[<][=]|[>][=])";
	public static String ifStmt = "[i][f][\\(](" + var + "[ ]" + binOp + "[ ]"
			+ var + ")[\\)]";

	public static String goto_str = "(\\bgoto \\b" + num + ")";
	
	public static String print = "(\\bprint\\(\\b" + exp + "[\\)])";


	public boolean checkAssign(String line) {
		return Pattern.matches(assign + exp, line);
	}
	
	public boolean checkIfStmt(String line) {
		return Pattern.matches("(" + ifStmt + "[ ])+" + "((" + assign + exp + ")|" + goto_str + "|" + print + ")", line);
	}
	
	public boolean checkGoto(String line) {
		return Pattern.matches(goto_str, line);
	}
	
	public boolean checkPrint(String line) {
		return Pattern.matches(print, line);
	}
	
	public static String getFirstIf(String orig) {
		if (!orig.contains("if")) {
			return null;
		}
		
		return orig.substring(0, orig.indexOf(")") + 1);
	}
}
