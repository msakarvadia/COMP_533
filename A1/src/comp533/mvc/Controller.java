package comp533.mvc;

import java.util.Scanner;

public class Controller extends gradingTools.comp533s19.assignment0.AMapReduceTracer {
	public Model model;
	
	public void gatherInput(Model mdl) {

		Scanner input = new Scanner(System.in);
		while (true) {
			setModel(mdl);
			traceNumbersPrompt();
			String tokens = input.nextLine();
			model.setInput(tokens);
			if (model.quit()) {
				System.exit(0);
				input.close();
			}
			else {
				model.computeResult();
			}
		}
	}
	
	public void setModel(Model mdl) {
		model = mdl;
		
	}

	public String toString() {// overriding the toString() method
		return CONTROLLER;
	}

}
