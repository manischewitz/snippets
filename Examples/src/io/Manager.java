package io;

import java.io.Serializable;
import java.math.BigDecimal;

public class Manager extends Employee implements Serializable {

	private static final long serialVersionUID = 1L;
	private Employee secretary;
	
	public Manager(String name, BigDecimal salary, int year, int month, int day) {
		super(name, salary, year, month, day);
		secretary = null;
	}

	public Employee getSecretary() {
		return secretary;
	}

	public void setSecretary(Employee secretary) {
		this.secretary = secretary;
	}

	@Override
	public String toString() {
		return "Manager [secretary=" + secretary + "]";
	}

}
