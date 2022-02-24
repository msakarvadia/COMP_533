package comp533.mvc;

public class StandAloneIntSumming {
	public static void main(final String[] args) {
		
		
		final ViewInterface view = new View();
		
		final ModelInterface model = new SummingModel();
		model.addPropertyChangeListener(view);
		IntSummingMapperFactory.setMapper(IntSummingMapperFactory.getMapper());
		
		final ControllerInterface controller = new Controller();
		controller.setModel(model);
		controller.processInput();
		
		
	}
}

