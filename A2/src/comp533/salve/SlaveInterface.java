package comp533.salve;

public interface SlaveInterface extends Runnable{
	public void run();
	public void notifySlave();
	public String toString();
}
