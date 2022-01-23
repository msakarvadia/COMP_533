package comp533.mvc;

import java.util.Scanner;
import gradingTools.comp533s19.assignment0.AMapReduceTracer; 

public class Controller extends AMapReduceTracer implements ControllerInterface{
	private ModelInterface model;
	
	@Override
	public void processInput() {

		final Scanner input = new Scanner(System.in);
		while (true) {
			traceNumbersPrompt();
			final String tokens = input.nextLine();
			model.setInputString(tokens);
			if ("quit".equals(tokens)) {
				traceQuit();
				input.close();
			}
			else {
				model.computeResult();
			}
		}
	}
	
	@Override
	public void setModel(final ModelInterface newModel) {
		model = newModel;
	}

	@Override
	public String toString() {// overriding the toString() method
		return CONTROLLER;
	}

}
