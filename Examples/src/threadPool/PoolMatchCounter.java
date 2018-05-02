package threadPool;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class PoolMatchCounter implements Callable<Integer> {

	private File directory;
	private String keyword;
	private ExecutorService pool;
	private int count;
	
	public PoolMatchCounter(File directory, String keyword, ExecutorService pool) {
		this.directory = directory;
		this.keyword = keyword;
		this.pool = pool;
		this.count = 0;
	}

	@Override
	public Integer call() throws Exception {
		try {
			File[] files = directory.listFiles();
			List<Future<Integer>> results = new ArrayList<>();
			for (File file : files) {
				if (file.isDirectory()) {
					PoolMatchCounter counter = new PoolMatchCounter(file, keyword, pool);
					Future<Integer> result = pool.submit(counter);
					results.add(result);
				} else {
					if (search(file)) {
						count++;
					}
				}
			}
			for (Future<Integer> val : results) {
				try {
					count += val.get();
				} catch (ExecutionException ee) {
					ee.printStackTrace();
				}
			}
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		}
		return count;
	}
	
	private boolean search(File file) {
		try {
			try (Scanner input = new Scanner(file, "UTF-8")) {
				boolean found = false;
				while (!found && input.hasNextLine()) {
					String line = input.nextLine();
					if (line.contains(keyword)) {
						found = true;
					}
				}
				return found;
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
			return false;
		}
	}

}
