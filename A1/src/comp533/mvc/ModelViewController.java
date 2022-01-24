package comp533.mvc;

public class ModelViewController {
	public static void main(final String[] args) {
		
		
		final ViewInterface view = new View();
		
		final ModelInterface model = new Model();
		model.addPropertyChangeListener(view);
		
		final ControllerInterface controller = new Controller();
		controller.setModel(model);
		controller.processInput();
		
		
	}
}
