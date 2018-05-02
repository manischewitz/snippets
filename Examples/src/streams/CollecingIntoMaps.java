package streams;

import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CollecingIntoMaps {

	private static class Person {
		final int id;
		final String name;
		
		Person (int id, String name) {
			this.id = id;
			this.name = name;
		}

		@Override
		public String toString() {
			return "Person [id=" + id + ", name=" + name + "]";
		}
		
	}
	
	private static Stream<CollecingIntoMaps.Person> people() {
		return Stream.of(
				new CollecingIntoMaps.Person(1, "John Doe"),
				new CollecingIntoMaps.Person(2, "Anna Lee"),
				new CollecingIntoMaps.Person(3, "Mary Ann"));
	}
	
	public static void main(String...strings) {
		Map<Integer, String> idToName = people().collect((
				Collectors.toMap((CollecingIntoMaps.Person val) -> {
					return val.id;
				}, (CollecingIntoMaps.Person val) -> {
					return val.name;
				})));
		System.out.println("id to name" + idToName);
		
		Map<Integer, CollecingIntoMaps.Person> idToPerson = people().collect(
				Collectors.toMap(val -> val.id, Function.identity()));
		System.out.println("id to person " + 
				idToPerson.getClass().getName() + idToPerson);
		
		idToPerson = people().collect(
				Collectors.toMap(v -> v.id,
						Function.identity(),
						(existing, newValue) -> {
							throw new IllegalStateException();
						}, TreeMap::new));
		System.out.println("id to person " + 
				idToPerson.getClass().getName() + idToPerson);
		
		Stream<Locale> locales = Stream.of(Locale.getAvailableLocales());
		Map<String, String> languageNames = locales.collect(
				Collectors.toMap(
						Locale::getDisplayLanguage,
						l -> l.getDisplayLanguage(l),
						(existing, newValue) -> existing));
		System.out.println("Language names: " + languageNames);
		
		locales = Stream.of(Locale.getAvailableLocales());
		Map<String, Set<String>> countryLanguageSets = locales.collect(
				Collectors.toMap(
						Locale::getDisplayCountry,
						(Locale l) -> /*Collections.singleton(l.getDisplayLanguage())*/{return new HashSet<String>() {{ super.add(l.getDisplayLanguage());}};},
						(old, newOne) -> {
							/*Set<String> union = new HashSet<>(old);
							union.addAll(newOne);
							return union;*/
							old.addAll(newOne);
							return old;
						}));
		System.out.println("Country Language Sets: " + countryLanguageSets);
		
	}
	
}
