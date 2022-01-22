package comp533.mvc;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class StandAloneTokenCounter {

	public static void main(String[] args) {
		processInput();
	}

	public static void processInput() {
		Scanner input = new Scanner(System.in);
		while (true) {
			System.out.println("Please enter quit or a line of tokens to be processed separated by spaces");
			String tokens = input.nextLine();
			if (tokens.equals("quit")) {
				System.exit(0);
				input.close();
			}
			String[] arrOfTok = tokens.split(" ");
			// make an array that contains unique tokens
			List<String> uniqueTokens = new ArrayList<>();
			// make a second array that maitains the counts of said unique tokens
			List<Integer> counts = new ArrayList<>();
			for (String a : arrOfTok) {
				if (!uniqueTokens.contains(a)) {
					uniqueTokens.add(a);
				}
				int index = uniqueTokens.indexOf(a);

				if (counts.size() < uniqueTokens.size()) {
					counts.add(1);
				} else {
					counts.set(index, counts.get(index) + 1);
				}
			}
			System.out.println("Output:");
			String out = "";
			for (int tok_idx = 0; tok_idx < counts.size(); tok_idx++) {

				String temp = uniqueTokens.get(tok_idx) + "=" + String.valueOf(counts.get(tok_idx));
				out = out + temp;
				if (tok_idx + 1 < counts.size()) {
					out = out + ", ";
				}
			}
			System.out.println(out);
		}
	}

}
