package threadPool;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

public class FutureMatchCounter implements Callable<Integer> {

	private File directory;
	private String keyword;
	
	public FutureMatchCounter(File directory, String keyword) {
		this.directory = directory;
		this.keyword = keyword;
	}
	
	@Override
	public Integer call() {
		int count = 0;
		try {
			File[] files = directory.listFiles();
			List<Future<Integer>> results = new ArrayList<>();
			for (File file : files) {
				if (file.isDirectory()) {
					FutureMatchCounter counter = new FutureMatchCounter(file, keyword);
					FutureTask<Integer> task = new FutureTask<>(counter);
					results.add(task);
					new Thread(task).start();
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
