package greeting;

public class GreetingClassRegistry implements gradingTools.shared.testcases.greeting.GreetingClassRegistry{

	@Override
	public Class<?> getGreetingMain() {
		return Hello.class;
	}

}
