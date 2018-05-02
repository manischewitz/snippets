package org.javautests.project.user;

import java.util.Arrays;

public class User {

	private String name;
	private char[] password;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public char[] getPassword() {
		return Arrays.copyOf(password, password.length);
	}

	public void setPassword(char[] password) {
		this.password = password;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("User [name=");
		builder.append(name);
		builder.append(", password=");
		builder.append(Arrays.toString(password));
		builder.append("]");
		return builder.toString();
	}
	
	
	

}
