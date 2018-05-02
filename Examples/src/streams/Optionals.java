package streams;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

public class Optionals {

	public static void main(String[] args) throws IOException {
		String contents = new String(
				Files.readAllBytes(Paths.get(System.getProperty("user.dir")+"/alice30.txt")),
						StandardCharsets.UTF_8);
		List<String> wordList = Arrays.asList(contents.split("\\PL+"));
		Optional<String> optional = wordList.stream().filter((String val) -> {
			return val.contains("gay");
		}).findFirst();
		System.out.println(optional.orElse("No word") + " contains key word");
		
		Optional<String> optionalString = Optional.empty();
		String result = optionalString.orElse("N/A");
		System.out.println("result is "+result);
		result = optionalString.orElseGet(() -> {
			return Locale.getDefault().getDisplayName();
		});
		System.out.println("result is "+result);
		
		try {
			result = optionalString.orElseThrow(() -> {
				return new IllegalStateException();
			});
		} catch (Throwable t) {
			t.printStackTrace();
		}
		
		optional = wordList.stream().filter(val -> val.contains("red")).findAny();
		optional.ifPresent((String val) -> {
			System.out.println(val + " contains specified key word.");
		});
		
		Set<String> results = new HashSet<>();
		optional.ifPresent(results::add);
		Optional<Boolean> added = optional.map((String val) -> {
			return results.add(val);
		});
		System.out.println(added);
		
		System.out.println(inverse(4.0).flatMap((Double val) -> {
			return Optionals.squareRoot(val);
		}));
		System.out.println(inverse(-1.0).flatMap(Optionals::squareRoot));
		System.out.println(inverse(0.0).flatMap(Optionals::squareRoot));
		Optional<Double> anotherResult = Optional
				.of(-4.0)
				.flatMap(Optionals::inverse)
				.flatMap(Optionals::squareRoot);
		System.out.println(anotherResult);
	}
	
	private static Optional<Double> inverse(Double x) {
		return x == 0 ? Optional.empty() : Optional.of(1 / x);
	}
	
	private static Optional<Double> squareRoot(Double x) {
		return x < 0 ? Optional.empty() : Optional.of(Math.sqrt(x));
	}

}
