package comp533.mvc;

import java.util.Scanner;
import gradingTools.comp533s19.assignment0.AMapReduceTracer; 

public class Controller extends AMapReduceTracer implements ControllerInterface{
	private ModelInterface model;
	
	public void processInput() {

		final Scanner input = new Scanner(System.in);
		while (true) {
			traceNumbersPrompt();
			final String tokens = input.nextLine();
			model.setInputString(tokens);
			if (tokens.equals("quit")) {
				traceQuit();
				input.close();
			}
			else {
				model.computeResult();
			}
		}
	}
	
	public void setModel(final ModelInterface newModel) {
		model = newModel;
	}

	public String toString() {// overriding the toString() method
		return CONTROLLER;
	}

}
