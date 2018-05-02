package threadPool;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class BlockingQueueCounter {

	private static final int FILE_QUEUE_SIZE = 10;
	private static final int SEARCH_THREADS = 100;
	private static final File DUMMY = new File("");
	private static BlockingQueue<File> queue = new ArrayBlockingQueue<>(FILE_QUEUE_SIZE);
	
	public static void main(String...strings) {
		try (Scanner input = new Scanner(System.in)) {
			System.out.print("Enter base directory: \n");
			final String directory = input.next();
			System.out.print("Enter keyword: \n");
			final String keyword = input.next();
			
			Runnable enumerator = () -> {
				try {
					enumerate(new File(directory));
					queue.put(DUMMY);
				} catch (InterruptedException ie) {
					ie.printStackTrace();
				}
			};
			new Thread(enumerator).start();
			
			for (int i = 1; i < SEARCH_THREADS; i++) {
				Runnable searcher = () -> {
					try {
						boolean isDone = false;
						while (!isDone) {
							File file = queue.take();
							if (file == DUMMY) {
								queue.put(DUMMY);
								isDone = true;
							} else {
								search(file, keyword);
							}
						}
					} catch (InterruptedException | IOException ie) {
						ie.printStackTrace();
					}
				};
				new Thread(searcher).start();
			}
		} 
	}

	private static void search(File file, String keyword) throws IOException {
		try (Scanner input = new Scanner(file, "UTF-8")) {
			int lineNumber = 0;
			while (input.hasNextLine()) {
				lineNumber++;
				String line = input.nextLine();
				if (line.contains(keyword)) {
					System.out.println("Match at line "+lineNumber
							+ " path: "+file.getPath()+ " line "+ line);
				}
			}
		}
	}

	private static void enumerate(File directory) throws InterruptedException {
		File[] files = directory.listFiles();
		for (File file : files) {
			if (file.isDirectory()) { 
				enumerate(file);
			} else {
				queue.put(file);
			}
		}
	}
}
