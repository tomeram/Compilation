import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Main {
	public class Line {

		private int error = 0;
		private String command = null;

		public Line() {
		}

		public int getError() {
			return error;
		}

		public void setError(int error) {
			if (this.error == 0 || this.error > error)
				this.error = error;
		}

		public String getCommand() {
			return command;
		}

		public void setCommand(String command) {
			this.command = command;
		}
	}

	static Map<Integer, Line> file_lines = new HashMap<>();
	static Map<Integer, Integer> labels = new HashMap<>();
	static int line_n = 1;
	static boolean errors = false;

	public static void PrintError(int line, int code) {
		System.out.println("Error! Line:" + line + " Code:" + code);
	}

	static String readFile(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}

	private void create_maps_from_file(String string) {
		String line;
		Line cmd_line;
		String[] temp;
		Scanner s = null;

		int label;
		int curr_lbl = 0;

		try {
			s = new Scanner(new File(string));

			while (s.hasNextLine()) {
				cmd_line = new Line();
				temp = new String[3];
				line = s.nextLine();
				temp = line.split(" ", 3);
				if (temp.length != 3 || temp[1] == null || temp[2] == null
						|| temp[1].length() != 1 || temp[1].charAt(0) != ':') {
					cmd_line.setError(1);
					file_lines.put(line_n, cmd_line);
					errors = true;
				}

				else {
					try {

						label = Integer.parseInt(temp[0]);

						if (curr_lbl >= label) {
							cmd_line.setError(3);
							cmd_line.setCommand(temp[2]);
							file_lines.put(line_n, cmd_line);
							labels.put(label, line_n);
							errors = true;
						} else {
							cmd_line.setCommand(temp[2]);
							file_lines.put(line_n, cmd_line);
							labels.put(label, line_n);
							curr_lbl = label;
						}

					} catch (Exception e) {
						cmd_line.setError(1);
						file_lines.put(line_n, cmd_line);
						errors = true;

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
		Main file_parser = new Main();
		
		if (args.length < 1)
			return;

		file_parser.create_maps_from_file(args[0]);
		
		updateErrors();
		updateErrors();
		
		if (errors) {
			printErrors();
		} else {
			line_n = 1;

			runtime: // Outer loop label for runtime breaking.
			while (file_lines.get(line_n) != null) {
				curr_line = file_lines.get(line_n).getCommand();

				curr_line = curr_line.substring(0, curr_line.length() - 2);
				skip_line = false;

				if (lex.checkIfStmt(curr_line)) {

					while ((curr_if = Lexer.getFirstIf(curr_line)) != null) {
						ret_val = execute.evaluateIfCondition(
								curr_if.substring(3, curr_if.length() - 1),
								line_n);

						if (ret_val == 0) {
							skip_line = true;
							line_n++;
							break;
						} else if (ret_val == 2) {
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
					ret_val = execute.executeAssignment(curr_line, line_n);
				}

				else if (lex.checkPrint(curr_line)) {
					execute.executePrint(curr_line, line_n);
				}

				else if (lex.checkGoto(curr_line)) {
					String[] cmd = curr_line.split(" ");
					Integer i = labels.get(Integer.parseInt(cmd[1]));
					line_n = i;
					continue;
				}

				if (ret_val != 2)
					line_n++;
				else
					break;
			}
		}
	}

	public static void updateErrors() {
		Lexer lex = new Lexer();
		String curr_line = "";
		String curr_if;

		line_n = 1;

		while (file_lines.get(line_n) != null) {
			curr_line = file_lines.get(line_n).getCommand();
			// Check for ' ;' at the end of a line.
			if (!Pattern.matches(".*[ ][;]", curr_line)) {
				file_lines.get(line_n).setError(1);
				continue;
			}
			
			curr_line = curr_line.substring(0, curr_line.length() - 2);

			if (lex.checkIfStmt(curr_line)) {
				while ((curr_if = Lexer.getFirstIf(curr_line)) != null) {
					curr_line = curr_line.substring(curr_if.length() + 1,
							curr_line.length());
				}
			}

			if (lex.checkAssign(curr_line)) {
				if (execute.checkSyntaxExpAssignment(curr_line) == 2)
					file_lines.get(line_n).setError(1);
			}

			else if (lex.checkPrint(curr_line)) {
				if (execute.checkSyntaxExpPrint(curr_line) == 2)
					file_lines.get(line_n).setError(1);
			}

			else if (lex.checkGoto(curr_line)) {
				String[] cmd = curr_line.split(" ");
				if (cmd.length < 2) {
					file_lines.get(line_n).setError(1);
				} else {
					Integer i = labels.get(Integer.parseInt(cmd[1]));
					if (i == null || file_lines.get(i).getError() != 0) {
						file_lines.get(line_n).setError(2);
					}
				}
			} else {
				file_lines.get(line_n).setError(1);
			}

			if (file_lines.get(line_n).getError() != 0)
				errors = true;
			
			line_n++;
		}
	}

	public static void printErrors() {
		line_n = 1;

		while (file_lines.get(line_n) != null) {
			if (file_lines.get(line_n).getError() != 0) {
				PrintError(line_n, file_lines.get(line_n).getError());
			}
			line_n++;
		}
	}
}
