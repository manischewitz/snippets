
public class Test {

	public static void main(String[] args) {
		//perfomance(new MergeSort<Person>());
		//test(new BubbleSort<Person>());
		perfomance(new BubbleSort<Person>());
	}

	private static void perfomance(Sorting<Person> algorithm) {
		Person[] people;
		for (int i = 500; i <= 10000; i+=500) { 
			people = generateArray(i); 
			long start = System.currentTimeMillis();
			algorithm.sort(people);
			long end = System.currentTimeMillis();
			System.out.println((end - start) + "ms "+i+" элементов были отсортированы.");
		}
	}
	
	private static void test(Sorting<Person> algorithm) {
		Person[] people = generateArray(10);
		System.out.println("BEFORE SORTING");
		for (int i = 0; i < people.length; i++) { 
			System.out.println(people[i]);
		}
		algorithm.sort(people);
		System.out.println("AFTER SORTING");
		for (int i = 0; i < people.length; i++) { 
			System.out.println(people[i]);
		}
	}
	
	private static Person[] generateArray(int x) {
		Person[] array = new Person[x];
		for (int i = 0; i < x; i++) {
			long n = (long) (Math.random()*(10000));
			array[i] = new Person(n, "Agent "+n);
		}
		return array;
	}
}
