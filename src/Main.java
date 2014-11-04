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
		String[] temp = new String[3];
		Scanner s = null;

		int label;
		int curr_lbl = 0;

		try {
			s = new Scanner(new File(string));

			while (s.hasNextLine()) {
				temp = new String[3];
				line = s.nextLine();
				temp = line.split(" ", 3);
				if (temp[1] == null || temp[2] == null || temp[1] != ":") {
					file_lines.put(line_n, "1");
				}

				else {
					try {

						label = Integer.parseInt(temp[0]);

						if (curr_lbl > label) {
							file_lines.put(line_n, "3");
							labels.put(label, line_n);
						} else {
							file_lines.put(line_n, temp[2]);
							labels.put(label, line_n);
							curr_lbl = label;
						}

					} catch (Exception e) {
						file_lines.put(line_n, "1");
				
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
		int ret_val = 0;
		boolean skip_line;

		create_maps_from_file("example3.txt");
		
		line_n = 1;

		runtime: // Outer loop label for runtime breaking.
		while (file_lines.get(line_n) != null) {
			curr_line = file_lines.get(line_n);
			
			//System.out.println(curr_line);

			// Check for ' ;' at the end of a line.
			if (!Pattern.matches(".*[ ][;]", curr_line)) {
				PrintError(line_n, 1);
				break;
			}

			curr_line = curr_line.substring(0, curr_line.length() - 2);
			skip_line = false;

			if (lex.checkIfStmt(curr_line)) {
				
				while ((curr_if = Lexer.getFirstIf(curr_line)) != null) {
					ret_val = execute.evaluateIfCondition(curr_if.substring(3, curr_if.length() - 1));
					
					if (ret_val == 0) {
						skip_line = true;
						line_n++;
						break;
					}
					else if (ret_val == 2) {
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

			if (lex.checkAssign(curr_line)) {
				ret_val = execute.executeAssignment(curr_line);
			}

			else if (lex.checkPrint(curr_line)) {
				execute.executePrint(curr_line);
			}

			else if (lex.checkGoto(curr_line)) {
				String[] cmd = curr_line.split(" ");
				if (cmd.length < 2) {
					PrintError(line_n, 1);
					ret_val = 2;
				}
				else {
					Integer i = labels.get(Integer.parseInt(cmd[1]));
					if (i == null) {
						PrintError(line_n, 2);
						ret_val = 2;
					}
					else {
						line_n = i;
						//System.out.println("goto: " + line_n);
						continue;
					}
				}
			}

			else {
				PrintError(line_n, 1);
				ret_val = 2;
			}
			
			if (ret_val != 2)
				line_n++;
			else
				break;
		}
	}
}
