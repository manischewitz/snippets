package streams;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ParallelStreams {

	public static void main(String[] args) throws IOException {
		Path path = Paths.get(System.getProperty("user.dir")+"/alice30.txt");
		String contents = new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
		List<String> words = Arrays.asList(contents.split("\\PL+"));
		
		//bad code
		int[] shortWords = new int[10];
		words.parallelStream().forEach((String val) -> {
			if (val.length() < 10) {
				shortWords[val.length()]++;
			}
		});
		System.out.println(Arrays.toString(shortWords));
		
		//resurt will be different
		Arrays.fill(shortWords, 0);
		words.parallelStream().forEach((String val) -> {
			if (val.length() < 10) {
				shortWords[val.length()]++;
			}
		});
		System.out.println(Arrays.toString(shortWords));

		//remedy
		Map<Integer, Long> shortWordCount = words
				.parallelStream()
				.filter(s -> s.length() < 10)
				.collect(Collectors.groupingBy
						(String::length, Collectors.counting()));
		System.out.println(shortWordCount);
		
		// Downstream order not deterministic
		Map<Integer, List<String>> result = words
				.parallelStream()
				.collect(Collectors.groupingByConcurrent(String::length));
		System.out.println(result.get(14));
		
		result = words.parallelStream().collect(
				Collectors.groupingByConcurrent(String::length));
		System.out.println(result.get(14));
		
		Map<Integer, Long> wordCounts = words.parallelStream().collect(
				Collectors.groupingByConcurrent(
						String::length, Collectors.counting()));
		System.out.println(wordCounts);
	}

}
