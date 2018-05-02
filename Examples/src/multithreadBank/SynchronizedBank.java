package multithreadBank;

import java.util.Arrays;

public class SynchronizedBank implements AbstractBank {

	 private final double[] accounts;

	  public SynchronizedBank(int n, double initBalance) {
	    accounts = new double[n];
	    Arrays.fill(accounts, initBalance);
	  }

	  public synchronized void transfer(int from, int to, double amount) throws InterruptedException {
	    while (accounts[from] < amount) { 
	    		wait();
	    }
	    System.out.print(Thread.currentThread());
	    accounts[from] -= amount;
	    System.out.printf(" %10.2f from %d to %d", amount, from, to);
	    accounts[from] += amount;
	    System.out.println(" total amount "+getTotal());
	    notifyAll();
	  }

	  public synchronized double getTotal() {
	    double total = 0;
	    for (double b : accounts) {
	      total += b;
	    }
	    return total;
	  }

	  public int size() {
	    return accounts.length;
	  }


}
