package streams;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.IntSummaryStatistics;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CollectingResults {

	private static Stream<String> noVowels() throws IOException {
		Path path = Paths.get(System.getProperty("user.dir")+"/alice30.txt");
		String contents = new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
		List<String> wordList = Arrays.asList(contents.split("\\PL+"));
		Stream<String> stream = wordList.stream();
		return stream.map((String val) -> {
			return val.replaceAll("[aeiouAEIOU]", "");
		});
	}
	
	private static <T> void show(String label, Set<T> set) {
		System.out.print(label + ": " + set.getClass().getName());
		System.out.println("[" + 
		set.stream()
		.limit(10)
		.map(val -> val.toString())
		.collect(Collectors.joining(", "))
		+ "]");
	}
	
	public static void main(String[] args) throws IOException {
		Iterator<Integer> iterator = Stream.iterate(0, n -> n + 1).limit(10).iterator();
		while (iterator.hasNext()) {
			System.out.println(iterator.next());
		}
		Object[] numbers = Stream.iterate(0, f -> f + 1).limit(10).toArray();
		System.out.println("Object array: " + Arrays.toString(numbers));
		
		try {
			Integer number = (Integer) numbers[0];
			System.out.println("number: " + number);
			System.out.println("The follwing statement throws an exception:");
			Integer[] numbersArray = (Integer[]) numbers;
		} catch (ClassCastException cce) {
			cce.printStackTrace();
		}
		
		Integer[] anotherNumbersArray = Stream.iterate(0, f -> f + 1)
				.limit(10).toArray(Integer[]::new);
		System.out.println("Integer array: " + Arrays.toString(anotherNumbersArray));
		
		Set<String> noVowelSet = noVowels().collect(Collectors.toCollection(TreeSet::new));
		show("noVowelSet", noVowelSet);
		
		String result = noVowels().limit(10).collect(Collectors.joining());
		System.out.println("Joining: " + result);
		
		result = noVowels().limit(10).collect(Collectors.joining(", "));
		System.out.println("Joining with commas: " + result);
		
		IntSummaryStatistics summary = noVowels().collect(
				Collectors.summarizingInt((String val) -> {
					return val.length();
				}));
		System.out.println("Average word length: " + summary.getAverage());
		System.out.println("Max word length: " + summary.getMax());
		noVowels().limit(10).forEach(System.out::println);
	}

}
