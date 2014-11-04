import java.util.Map;
import java.util.Stack;


public class execute {

	
	static Map<String, Integer> vars;

	/* returns int[]: { result, errorCode }. errorCode = 1 if no runtime error occurred */
	public static int[] evaluateExp(String str) {
		int i, firstItem, secondItem, result = 0;
		int[] finalResult = { 0 , 0 } ;
		String[] tokens = str.split(" ");
		Stack<Integer> valuesStack = new Stack<Integer>();
		
		for (i = tokens.length-1; i > -1; i --) {
			if (checkIfNum(tokens[i])) {
				valuesStack.push(Integer.parseInt(tokens[i]));
			}
			
			else if (checkIfVar(tokens[i])) {
				if (vars.containsKey(tokens[i])) {
					valuesStack.push(vars.get(tokens[i]));
				}
				else { /* runtime error */
					finalResult[0] = 0;
					finalResult[1] = 4;
					return finalResult;
				}
			}
			
			else if (checkIfBinaryOp(tokens[i])) {
				firstItem = valuesStack.pop();
				secondItem = valuesStack.pop();
				
				if (tokens[i].equals("+"))
					result = firstItem + secondItem;
				else if (tokens[i].equals("-"))
					result = firstItem - secondItem;
				else if (tokens[i].equals("*"))
					result = firstItem * secondItem;
				else if (tokens[i].equals("\\"))
					result = firstItem / secondItem;
				
				valuesStack.push(result);
			}
		}
		
		result = valuesStack.pop();
		finalResult[0] = result;
		finalResult[1] = 1;
		return finalResult;
	}
	
	/* line should be in format: print(Exp) */
	/* returns 1 if succeeded, 2 if runtime error */
	public static int executePrint(String line) {
		int[] expResult = evaluateExp(line.substring(6, line.length()-1));
		if (expResult[1] == 1) {
			Print(expResult[0]);
			return 1;
		} else {
			return 2; /* runtime error */
		}
	}
	
	/* line should be in format: var := exp */
	/* returns 1 if succeeded, 2 if runtime error */
	public static int executeAssignment (String line) {
		String[] tokens = line.split(" ",3);
		int[] expResult;
		
		expResult = evaluateExp(tokens[2]);
		if (expResult[1] == 1) { /* if exp is valid, assign */
			vars.put(tokens[0], expResult[0]);
			return 1;
		} else {
			return 2; /* runtime error */
		}
	}
	
	
	/* line should be in format: Var BoolOp Var */
	/* returns 1 if true, 0 if false, 2 if runtime error */
	public static int evaluateIfCondition(String line) {
		String[] tokens = line.split(" ");
		if ((vars.containsKey(tokens[0])) && (vars.containsKey(tokens[2]))) {
			if (tokens[1].equals("<"))
				return (vars.get(tokens[0]) < vars.get(tokens[2])) ? 1 : 0;
			else if (tokens[1].equals(">"))
				return (vars.get(tokens[0]) > vars.get(tokens[2])) ? 1 : 0;
			else if (tokens[1].equals("<="))
				return (vars.get(tokens[0]) <= vars.get(tokens[2])) ? 1 : 0;
			else if (tokens[1].equals(">="))
				return (vars.get(tokens[0]) >= vars.get(tokens[2])) ? 1 : 0;
			else if (tokens[1].equals("=="))
				return (vars.get(tokens[0]) == vars.get(tokens[2])) ? 1 : 0;
			else if (tokens[1].equals("!="))
				return (vars.get(tokens[0]) != vars.get(tokens[2])) ? 1 : 0;
		}
		else {
			return 2; /* runtime error */
		}
		return 0;
	}
	
	public static boolean checkIfVar(String str) {
		if(str == null || str.isEmpty())
			return false;
		
		char c = str.charAt(0);
		if ((c >= 97) && (c <= 122)) { /* c in [a-z] */
			return true;
		} else
			return false;
	}
	
	public static boolean checkIfNum(String num) {
		if(num == null || num.isEmpty())
			return false;
					
		char digit = num.charAt(0);
		if ((digit >= '0') && (digit <= '9')) { /* digit in [0-9] */
			return checkIfNumInternalDigits(num.substring(1));
		} 
		
		return false; /* First char is not a digit */
	}
	
	public static boolean checkIfNumInternalDigits(String num) {
		if(num == null || num.isEmpty())
			return true;
					
		char digit = num.charAt(0);
		if ((digit >= '0') && (digit <= '9')) { /* digit in [0-9] */
			return checkIfNumInternalDigits(num.substring(1));
		} 
		
		return false; /* First char is not a digit */
	}
	
	public static boolean checkIfBoolOp(String str) {
		if ((str.equals("<")) || (str.equals(">")) || (str.equals("<=")) || (str.equals(">=")) || (str.equals("==")) || (str.equals("!=")))
			return true;
		return false;
	}
	
	public static boolean checkIfBinaryOp(String str) {
		if ((str.equals("+")) || (str.equals("*")) || (str.equals("-")) || (str.equals("\\")))
			return true;
		return false;
	}



	public static void PrintError(int line , int code) {
		System.out.println("Error! Line:"+line+" Code:"+code);
	}

	public static void Print(int val) {
		System.out.println(""+val);
	}

}

