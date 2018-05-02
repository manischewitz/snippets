
@SuppressWarnings("unchecked")
public class MergeSort<T extends Comparable<T>> implements Sorting<T> {
	
	private T[] array;
	
	private void doMergeSort(Comparable<T>[] temp, int low, int upper) {
		if (low != upper) {
			int middle = (low + upper) / 2;
			doMergeSort(temp, low, middle);
			doMergeSort(temp, middle + 1, upper);
			merge(temp, low, middle + 1, upper);
		}
	}
	
	private void merge(Comparable<T>[] temp, int low, int middle, int upper) {
		int j = 0;
		int lowerBound = low;
		int mid = middle - 1;
		int n = upper - low + 1;
		
		while (low <= mid && middle <= upper) {
			if (array[low].compareTo(array[middle]) < 0) {
				temp[j] = array[low];
				low++;		
			} else {
				temp[j] = array[middle];
				middle++;
			}
			j++;
		}
		
		while (low <= mid) {
			temp[j] = array[low];
			low++;
			j++;
		}
		
		while (middle <= upper) {
			temp[j] = array[middle];
			middle++;
			j++;
		}
		
		for (j = 0; j < n; j++) {
			array[lowerBound + j] = (T) temp[j];
		}
	}

	@Override
	public void sort(T[] array) {
		this.array = array;
		final int length = array.length;
		Comparable<T>[] temp = new Comparable[length];
		doMergeSort(temp, 0, length - 1);
	}
}
