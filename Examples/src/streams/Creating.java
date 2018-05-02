package streams;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class Creating {

	public static void main(String[] args) throws IOException {
		Path path = Paths.get(System.getProperty("user.dir")+"/alice30.txt");
		String contents = new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
		
		Stream<String> words = Stream.of(contents.split("\\PL+"));
		show("words", words);
		Stream<String> message = Stream.of("this", "is", "a", "number", "of", "words");
		show("message", message);
		Stream<String> empty = Stream.empty();
		show("empty", empty);
		
		Stream<String> echoes = Stream.generate(() -> "Echo");
		show("echoes", echoes);
		
		Stream<Double> randoms = Stream.generate(() -> {
			return Math.random();
		});
		show("randoms", randoms);
		
		Stream<BigInteger> integers = Stream.iterate(BigInteger.ONE, 
				n -> n.add(BigInteger.ONE));
		show("integers", integers);
		
		Stream<String> wordsAnotherWay = Pattern
				.compile("\\PL+")
				.splitAsStream(contents);
		show("wordsAnotherWay", wordsAnotherWay);
		
		try (Stream<String> lines = Files.lines(path, StandardCharsets.UTF_8)) {
			show("lines", lines);
		}
		
	}
	
	private static void say(Object smth) {
		System.out.print(smth.toString());
	}
	
	private static <T> void show(String title, Stream<T> stream) {
		final int SIZE = 10;
		List<T> firstElement = stream
				.limit(SIZE + 1)
				.collect(Collectors.toList());
		say(title + ": ");
		for (int i = 0; i < firstElement.size(); i++) {
			if (i > 0) {
				say(", ");
			}
			if (i < SIZE) {
				say(firstElement.get(i));
			} else {
				say("...");
			}
		}
		say("\n");
	}

}
