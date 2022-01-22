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
			String[] arrayOfToken = tokens.split(" ");
			// make an array that contains unique tokens
			List<String> uniqueTokens = new ArrayList<>();
			// make a second array that maitains the counts of said unique tokens
			List<Integer> counts = new ArrayList<>();
			for (String a : arrayOfToken) {
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
			for (int i = 0; i < counts.size(); i++) {

				String temp = uniqueTokens.get(i) + "=" + String.valueOf(counts.get(i));
				out = out + temp;
				if (i + 1 < counts.size()) {
					out = out + ", ";
				}
			}
			System.out.println(out);
		}
	}

}
