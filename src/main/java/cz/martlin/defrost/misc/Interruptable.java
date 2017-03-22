package cz.martlin.defrost.misc;

public interface Interruptable {
	
	public void interrupt();
	public boolean isInterrupted();
	public void restart();

}
