package threadPool;

import java.io.File;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class FutureTest {

	public static void main(String[] args) {
		try (Scanner input = new Scanner(System.in)) {
			System.out.print("Enter base directory: \n");
			final String directory = input.next();
			System.out.print("Enter keyword: \n");
			final String keyword = input.next();
			FutureMatchCounter counter = new FutureMatchCounter(new File(directory), keyword);
			FutureTask<Integer> task = new FutureTask<>(counter);
			new Thread(task).start();
			try {
				System.out.print(task.get() + " matching files\n");
			} catch (ExecutionException | InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
