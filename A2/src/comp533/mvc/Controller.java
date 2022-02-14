package comp533.mvc;

import java.util.Scanner;
import gradingTools.comp533s19.assignment0.AMapReduceTracer; 

public class Controller extends AMapReduceTracer implements ControllerInterface{
	private ModelInterface model;
	final String quit = "quit";
	final String empty = "";
	
	@Override
	public void processInput() {
		final Scanner input = new Scanner(System.in);
		traceThreadPrompt();
		final String numThreads = input.nextLine();
		model.setNumThreads(Integer.valueOf(numThreads));
		
		while (true) {
			traceNumbersPrompt();
			final String tokens = input.nextLine();
			final String original_line = tokens;
			model.setInputString(tokens);
			
			if (quit.equals(tokens)) {
				model.terminate();
				traceQuit();
				input.close();
				break;
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
