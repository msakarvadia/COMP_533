package comp533.mvc;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StandAloneTokenCounter {
	final static String SPACE = " ";

	public static void main(final String[] args) {
		processInput();
	}

	public static void processInput() {
		final Scanner input = new Scanner(System.in);

		while (true) {
			System.out.println("Please enter quit or a line of tokens to be processed separated by spaces");
			final String tokens = input.nextLine();
			final String quit = "quit";
			if (quit.equals(tokens)) {
				System.exit(0);
				input.close();
			}

			////////////
			final String[] arrayOfToken = tokens.split(SPACE);
			// make an array that contains unique tokens
			final List<String> uniqueTokens = new ArrayList<>();
			// make a second array that maintains the counts of said unique tokens
			final List<Integer> counts = new ArrayList<>();
			for (String a : arrayOfToken) {
				if (!uniqueTokens.contains(a)) {
					uniqueTokens.add(a);
				}
				final int index = uniqueTokens.indexOf(a);

				if (counts.size() < uniqueTokens.size()) {
					counts.add(1);
				} else {
					counts.set(index, counts.get(index) + 1);
				}
			}

			////////////
			System.out.println(createResult(uniqueTokens, counts));
		}
	}
	
	public static Map<String, Integer> createResult(final List<String> uniqueTokens, final List<Integer> counts) {
		final Map<String, Integer> result = new HashMap<String, Integer>();
		for (int i = 0; i < counts.size(); i++) {
			result.put(uniqueTokens.get(i), counts.get(i));
		}
		return null;
	}

}
