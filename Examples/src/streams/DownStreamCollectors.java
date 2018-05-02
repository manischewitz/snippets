package streams;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.IntSummaryStatistics;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DownStreamCollectors {

	private static class City {
			final String name;
			final String state;
			final int population;
			
			public City(String name, String state, int population) {
				this.name = name;
				this.state = state;
				this.population = population;
			}

			public String getName() {
				return name;
			}

			public String getState() {
				return state;
			}

			public int getPopulation() {
				return population;
			}
	}
	
	private static Stream<City> readCities(String fileName) throws IOException {
		return Files.lines(Paths.get(fileName)).map(
				line -> line.split(", ")).map((String[] array) -> {
					return new City(array[0], array[1], Integer.parseInt(array[2]));
				});
	}
	
	public static void main(String[] args) throws IOException {
		Stream<Locale> locales = Stream.of(Locale.getAvailableLocales());
		Map<String, Set<Locale>> countryToLocaleSet = locales.collect(
				Collectors.groupingBy(Locale::getCountry, Collectors.toSet()));
		System.out.println("countryToLocaleSet: " + countryToLocaleSet);
		System.out.println(Locale.getDefault());
		locales = Stream.of(Locale.getAvailableLocales());
		Map<String, Long> countryToLocaleCounts = locales.collect(
				Collectors.groupingBy(Locale::getCountry, Collectors.counting()));
		System.out.println("countryToLocaleCounts: " + countryToLocaleCounts);
		
		Stream<City> cities = readCities(System.getProperty("user.dir")+"/cities.txt");
		Map<String, Optional<String>> stateToLongestCityName = cities.collect(
				Collectors.groupingBy(City::getState, Collectors.mapping(
						City::getName, Collectors.maxBy(Comparator.comparing((String val) -> {
							return val.length();
						})))));
		System.out.println("stateToLongestCityName: " + stateToLongestCityName);
		
		locales = Stream.of(Locale.getAvailableLocales());
		Map<String, Set<String>> countryToLanguages = locales.collect(
				Collectors.groupingBy(Locale::getDisplayCountry,
						Collectors.mapping(Locale::getDisplayLanguage,
								Collectors.toSet())));
		System.out.println("countryToLanguages: " + countryToLanguages);
		
		cities = readCities(System.getProperty("user.dir")+"/cities.txt");
		Map<String, IntSummaryStatistics> stateToCityPopulation = cities.collect(
				Collectors.groupingBy(City::getState, 
						Collectors.summarizingInt(City::getPopulation)));
		System.out.println(stateToCityPopulation.get("Russia"));
		
		cities = readCities(System.getProperty("user.dir")+"/cities.txt");
		Map<String, String> stateToCityNames = cities.collect(Collectors.groupingBy(
				City::getState, Collectors.reducing("", City::getName,
						(old, newOne) -> old.length() == 0 ? newOne : old + ", " + newOne)));
		System.out.println("stateToCityNames: " + stateToCityNames);
		
		cities = readCities(System.getProperty("user.dir")+"/cities.txt");
		stateToCityNames = cities.collect(Collectors.groupingBy(
				City::getState, Collectors.mapping(City::getName, 
						Collectors.joining(", "))));
		System.out.println("stateToCityNames: " + stateToCityNames);
		
	}

}
