import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Main {
	// TODO Gal: need to think if we want label to be a string or number, I can
	// add another check if we want it string, using int makes it easy to
	// check if its a number and if its monotonous
	// and Yes Tomer, I know you hate globals, we can fix those later :)
	static Map<Integer, String> file_lines = new HashMap<>();
	static Map<Integer, Integer> labels = new HashMap<>();
	static int line_n = 1;

	public static void PrintError(int line, int code) {
		System.out.println("Error! Line:" + line + " Code:" + code);
	}

	private static void create_maps_from_file(String string) {
		String line;
		String[] temp = new String[2];
		Scanner s = null;

		int label;
		int curr_lbl = 0;

		try {
			s = new Scanner(new File("test.txt"));

			while (s.hasNextLine()) {
				line = s.nextLine();
				temp = line.split(" ", 2);

				if (temp.length != 2) {
					file_lines.put(line_n, "no line, only label/empty line");
					// TODO change to code numbers...
				}

				else {
					try {

						label = Integer.parseInt(temp[0]);

						if (curr_lbl > label) {
							file_lines.put(line_n, "label < prev labels");
							// TODO change to code numbers...

							labels.put(line_n, label);
						} else {
							file_lines.put(line_n, temp[1]);
							labels.put(line_n, label);
							curr_lbl = label;
						}

					} catch (Exception e) {
						file_lines.put(line_n, "label not number");
						// TODO change to code numbers...
					}
				}

				line_n++;
			}
		} catch (IOException e) {

		} finally {
			if (s != null) {
				s.close();
			}
		}
	}

	public static void main(String args[]) {
		Lexer lex = new Lexer();
		String curr_line = "";
		String curr_if;
		int if_res;
		boolean skip_line;

		create_maps_from_file("test.txt");
		
		execute.vars.put("x", 5);
		execute.vars.put("z", 3);

		line_n = 1;

		runtime: // Outer loop label for runtime breaking.
		while (true) {// (labels.get(line_n) != null) {
			curr_line = "x := + 1 x ;";//if(x == x) if(z < x) x := 1 ;";// file_lines.get(line_n); TODO: fix when done.

			// Check for ' ;' at the end of a line.
			if (!Pattern.matches(".*[ ][;]", curr_line)) {
				PrintError(line_n, 1);
				break;
			}

			curr_line = curr_line.substring(0, curr_line.length() - 2);
			skip_line = false;

			if (lex.checkIfStmt(curr_line)) {
				
				while ((curr_if = Lexer.getFirstIf(curr_line)) != null) {
					System.out.println(curr_if); // TODO: remove when done debbuging

					if_res = execute.evaluateIfCondition(curr_if.substring(3, curr_if.length() - 1));
					
					if (if_res == 0) {
						skip_line = true;
						break;
					}
					else if (if_res == 2) {
						PrintError(line_n, 4);
						break runtime;
					}
					// Trim first if
					curr_line = curr_line.substring(curr_if.length() + 1,
							curr_line.length());
				}
			}
			
			if (skip_line) {
				// if statement was false, no need to continue
				continue;
			}

			System.out.println(curr_line + "-");
			if (lex.checkAssign(curr_line)) {
				// TODO: Call execute function
				execute.executeAssignment(curr_line);
				System.out.println(execute.vars.get("x"));
			}

			else if (lex.checkPrint(curr_line)) {
				System.out.println("print");
			}

			else if (lex.checkGoto(curr_line)) {
				System.out.println("goto");
			}

			else
				System.out.println("Error");
			break;
		}
	}
}
