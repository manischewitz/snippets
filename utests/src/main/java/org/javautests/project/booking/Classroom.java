package org.javautests.project.booking;

public class Classroom {

	private int capacity;
	private boolean projector;
	
	public int getCapacity() {
		return capacity;
	}
	
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	
	public boolean isHasProjector() {
		return projector;
	}
	
	public void setProjector(boolean projector) {
		this.projector = projector;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Classroom [capacity=");
		builder.append(capacity);
		builder.append(", projector=");
		builder.append(projector);
		builder.append("]");
		return builder.toString();
	}
	
}
