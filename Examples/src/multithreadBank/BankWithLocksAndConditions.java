package multithreadBank;

import java.util.Arrays;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BankWithLocksAndConditions implements AbstractBank {

	private final double[] accounts;
	private Lock bankLock;
	private Condition sufficentFunds;
	
	public BankWithLocksAndConditions(int n, double initBalance) {
	    accounts = new double[n];
	    Arrays.fill(accounts, initBalance);
	    bankLock = new ReentrantLock();
	    sufficentFunds = bankLock.newCondition();
	  }
	
	@Override
	public int size() {
		return accounts.length;
	}

	@Override
	public void transfer(int from, int to, double amount) throws InterruptedException {
		bankLock.lock();
		try {
			while (accounts[from] < amount) {
				sufficentFunds.await();
			}
			System.out.print(Thread.currentThread());
		    accounts[from] -= amount;
		    System.out.printf(" %10.2f from %d to %d", amount, from, to);
		    accounts[from] += amount;
		    System.out.println(" total amount "+getTotal());
		    sufficentFunds.signalAll();
		} finally {
			bankLock.unlock();
		}
	}

	@Override
	public double getTotal() {
		bankLock.lock();
		try {
			double total = 0;
		    for (double b : accounts) {
		      total += b;
		    }
		    return total;
		} finally {
			bankLock.unlock();
		}
	}

}
