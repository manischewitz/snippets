package streams;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.function.LongSupplier;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class StreamPrimer {

	public static void main(String... strings) throws IOException {
		String contents = new String(
				Files.readAllBytes(Paths.get(System.getProperty("user.dir")+"/alice30.txt")),
						StandardCharsets.UTF_8);
		List<String> words = Arrays.asList(contents.split("\\PL+"));
		
		proceed(() -> {
			long count = 0;
			for (String word : words) {
				if (word.length() > 12) {
					count++;
				}
			}
			return count;
		}); 
		
		final Predicate<? super String> fun = (String val) -> {
			return val.length() > 12;
		};
		
		proceed(() -> words.stream().filter(fun).count());
		proceed(() -> words.parallelStream().filter(fun).count());
		
		/*
		long count = 0;
		long start;
		long end;
		
		start = System.currentTimeMillis();
		for (String word : words) {
			if (word.length() > 12) {
				count++;
			}
		}
		end = System.currentTimeMillis();
		System.out.println(count + " in " + (end - start));
		
		Predicate<? super String> fun = (String val) -> {
			return val.length() > 12;
		};
		
		start = System.currentTimeMillis();
		count = words.stream().filter(fun).count();
		end = System.currentTimeMillis();
		System.out.println(count + " in " + (end - start));
		
		start = System.currentTimeMillis();
		count = words.parallelStream().filter(fun).count();
		end = System.currentTimeMillis();
		System.out.println(count + " in " + (end - start));*/
	}
	
	private static void proceed(LongSupplier sup) {
		long count = 0;
		long start;
		long end;
		
		start = System.currentTimeMillis();
		count = sup.getAsLong();
		end = System.currentTimeMillis();
		System.out.println(count + " in " + (end - start));
	}
	
}
