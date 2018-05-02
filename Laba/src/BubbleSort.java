public class BubbleSort<T extends Comparable<T>> implements Sorting<T> {

	@Override
	public void sort(T[] array) {
		int i, k;
		T current, next;
		for (i = array.length - 1; i > 1; i--) {
			for (k = 0; k < i; k++) {
				current = array[k];
				next = array[k + 1];
				if (next.compareTo(current) < 0) {
					array[k + 1] = current;
					array[k] = next;
				}
			}
		}
	}
}
