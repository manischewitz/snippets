package threadPool;

import java.io.File;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ThreadPoolExecutor;

/**
* This task counts the files in a directory and its subdirectories 
* that contain a given keyword.
*/

public class ThreadPoolTest {

	public static void main(String[] args) {
		try (Scanner input = new Scanner(System.in)) {
			System.out.print("Enter base directory: \n");
			final String directory = input.next();
			System.out.print("Enter keyword: \n");
			final String keyword = input.next();
			ExecutorService pool = Executors.newCachedThreadPool();
			PoolMatchCounter counter = new PoolMatchCounter(new File(directory), keyword, pool);
			Future<Integer> result = pool.submit(counter);
			try {
				System.out.print(result.get() + " matching files\n");
			} catch (ExecutionException | InterruptedException e) {
				e.printStackTrace();
			}
			pool.shutdown();
			int largestPoolSize = ((ThreadPoolExecutor) pool).getLargestPoolSize();
			System.out.print(largestPoolSize + " is largest pool size\n");
		}
	}

}
