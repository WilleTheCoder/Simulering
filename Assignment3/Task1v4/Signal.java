// Denna klass definerar vad som ska finnas i en signal. Det som finns har ar ett minimum. Man kan lagga till mer
// om man vill att en signal ska kunna skicka mer information.

// This class defines a signal. What can be seen here is a mainimum. If one wants to add more
// information just do it here. 

class Signal{
	public Proc destination;
	public double arrivalTime;
	public int signalType;
	public Signal next;
	public Proc sender;
}