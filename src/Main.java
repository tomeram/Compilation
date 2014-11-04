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
	static Map<String, Integer> vars;
	static int line_n = 1;

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

		create_maps_from_file("test.txt");
		execute.vars = vars;

		line_n = 1;

		while (labels.get(line_n) != null) {// line_n < file_lines.size()) {
			curr_line = file_lines.get(line_n);
			
			if (Pattern.matches(".*\\b ;\\b", curr_line)) {
				// TODO: print error and break;
			}
			break;
		}
	}
}