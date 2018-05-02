package dateAndTime;

import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Primer {

	public static final int TIMES = 10;
	
	public static void main(String[] args) {
		Instant start = Instant.now();
		collectionsSort();
		Instant end = Instant.now();
		Duration timeElapsed = Duration.between(start, end);
		long millis = timeElapsed.toMillis();
		System.out.printf("%d milliseconds\n", millis);
		
		Instant start1 = Instant.now();
		shuffleSort();
		Instant end1 = Instant.now();
		Duration timeElapsed1 = Duration.between(start1, end1);
		System.out.printf("%d milliseconds\n", timeElapsed1.toMillis());
		
		boolean overTenTimesFaster = timeElapsed
				.multipliedBy(10).minus(timeElapsed1).isNegative();
		System.out.printf("The first algorithm is %smore than ten times faster",
				overTenTimesFaster ? "" : "not ");
	}
	
	private static void collectionsSort() {
		List<Integer> list = new Random().ints()
				.map(i -> i % 100).limit(TIMES)
				.boxed().collect(Collectors.toList());
		Collections.sort(list);
		Logger.getGlobal().info(list.toString());
	}
	
	private static void shuffleSort() {
		List<Integer> list = new Random().ints()
				.map(i -> i % 100).limit(TIMES)
				.boxed().collect(Collectors.toList());
		while (!IntStream.range(1, list.size()).allMatch((int index) -> {
			return list.get(index - 1).compareTo(list.get(index)) <= 0;
		})) {
			Collections.shuffle(list);
		}
		Logger.getGlobal().info(list.toString());
	}

}
