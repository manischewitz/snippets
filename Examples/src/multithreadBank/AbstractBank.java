package multithreadBank;

public interface AbstractBank {

	public int size();
	public void transfer(int from, int to, double amount)  throws InterruptedException;
	public double getTotal();
}
