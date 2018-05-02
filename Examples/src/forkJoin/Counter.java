package forkJoin;

import java.util.concurrent.RecursiveTask;
import java.util.function.DoublePredicate;

public class Counter extends RecursiveTask<Integer> {

	private static final long serialVersionUID = 4785869970667869406L;
	public static final int THRESHOLD = 1000;
	private double[] values;
	private int from;
	private int to;
	private DoublePredicate filter;
	
	public Counter(double[] values, int from, int to, DoublePredicate filter) {
		super();
		this.values = values;
		this.from = from;
		this.to = to;
		this.filter = filter;
	}

	@Override
	protected Integer compute() {
		if (to - from < THRESHOLD) {
			int count = 0;
			for (int i = from; i < to; i++) {
				if (filter.test(values[i])) {
					count++;
				}
			}
			return count;
		} else {
			int middle = (from + to) / 2;
			Counter first = new Counter(values, from, middle, filter);
			Counter second = new Counter(values, middle, from, filter);
			super.invokeAll(first, second);
			return first.join() + second.join();
		}
	}

}
