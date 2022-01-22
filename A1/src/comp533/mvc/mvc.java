package comp533.mvc;

public class mvc {
	public static void main(String[] args) {
		
		
		View view = new View();
		
		Model model = new Model();
		model.addPropertyChangeListener(view);
		
		Controller controller = new Controller();
		controller.gatherInput(model);
		
		
		
	}
}
