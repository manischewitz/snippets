package streams;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class PrimitiveStreams {

	private static void say(Object smth) {
		System.out.print(smth.toString());
	}
	
	private static void show(String title, IntStream stream) {
		final int SIZE = 10;
		int[] firstElements = stream.limit(SIZE + 1).toArray();
		say(title + ": ");
		for (int i = 0; i < firstElements.length; i++) {
			if (i > 0) {
				say(", ");
			}
			if (i < SIZE) {
				say(firstElements[i]);
			} else {
				say("...");
			}
		}
		say("\n");
	}
	
	public static void main(String[] args) throws IOException {
		IntStream is1 = IntStream.generate(() -> (int) (Math.random() * 100));
		show("first: ", is1);
		IntStream is2 = IntStream.range(5, 10);
		show("second: ", is2);
		IntStream is3 = IntStream.rangeClosed(5, 10);
		show("third: ", is3);
		
		Path path = Paths.get(System.getProperty("user.dir")+"/alice30.txt");
		String contents = new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
		Stream<String> words = Stream.of(contents.split("\\PL+"));
		IntStream is4 = words.mapToInt(String::length);
		show("fourth: ", is4);
		String sentence = "\uD835\uDD46 is the set of octions.\n";
		say(sentence);
		IntStream codes = sentence.codePoints();
		say(codes.mapToObj((int val) -> {
			return String.format("%X ", val);
		}).collect(Collectors.joining()) + "\n");
		Stream<Integer> integers = IntStream.range(0, 100).boxed();
		IntStream is5 = integers.mapToInt(Integer::intValue);
		show("fifth: ", is5);
		
	}

}
