package multithreadBank;

public class Test {

	public static final int NUMBER_OF_ACCOUNTS = 100;
	public static final double INITIAL_BALANCE = 1000.0;
	public static final double MAX_AMOUNT = 1000.0; 
	public static final int DELAY_MS = 100;
	
	public static void main(String[] args) {
		//final AbstractBank bank = new NotSynchronizedBank(NUMBER_OF_ACCOUNTS, INITIAL_BALANCE);
		//final AbstractBank bank = new BankWithLocksAndConditions(NUMBER_OF_ACCOUNTS, INITIAL_BALANCE);
		final AbstractBank bank = new SynchronizedBank(NUMBER_OF_ACCOUNTS, INITIAL_BALANCE);

		for (int i = 0; i < NUMBER_OF_ACCOUNTS; i++) {
			int fromAccount = i;
			Runnable runnable = () -> {
				try {
					while (true) {
						int toAccount = (int) (Math.random() * bank.size());
						double amount = MAX_AMOUNT * Math.random();
						bank.transfer(fromAccount, toAccount, amount);
						Thread.sleep((int) (DELAY_MS * Math.random()));
					}
				} catch (InterruptedException ie) {
					ie.printStackTrace();
				}
			};
			Thread thread = new Thread(runnable);
			thread.start();
		}
	}

}
