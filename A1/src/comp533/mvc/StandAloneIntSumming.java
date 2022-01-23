package comp533.mvc;

public class StandAloneIntSumming {
	public static void main(String[] args) {
		
		
		final ViewInterface view = new View();
		
		final ModelInterface model = new SummingModel();
		model.addPropertyChangeListener(view);
		
		final ControllerInterface controller = new Controller();
		controller.setModel(model);
		controller.processInput();
		
		
	}
}

