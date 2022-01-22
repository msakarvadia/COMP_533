package comp533.mvc;

public class mvc {
	public static void main(String[] args) {
		
		
		final ViewInterface view = new View();
		
		final ModelInterface model = new Model();
		model.addPropertyChangeListener(view);
		
		final ControllerInterface controller = new Controller();
		controller.setModel(model);
		controller.processInput();
		
		
	}
}
