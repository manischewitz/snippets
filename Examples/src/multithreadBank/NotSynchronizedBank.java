package multithreadBank;

import java.util.*;

public class NotSynchronizedBank implements AbstractBank {

  private final double[] accounts;

  public NotSynchronizedBank(int n, double initBalance) {
    accounts = new double[n];
    Arrays.fill(accounts, initBalance);
  }

  public void transfer(int from, int to, double amount) {
    if (accounts[from] >= amount) {
      System.out.print(Thread.currentThread());
      accounts[from] -= amount;
      System.out.printf(" %10.2f from %d to %d", amount, from, to);
      accounts[from] += amount;
      System.out.println(" total amount "+getTotal());
    }
  }

  public double getTotal() {
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
