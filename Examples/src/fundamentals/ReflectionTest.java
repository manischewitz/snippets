package fundamentals;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Scanner;;
/**
 * This application uses reflection to print all features of a class
 * @author macuser
 * @version 1.0 13.01.2018
 * */
public class ReflectionTest {

	public static void main(String...strings) {
		if (strings.length > 0) {
			try {
				System.out.println(classToString(strings[0]));
			} catch (ClassNotFoundException e) {
				System.out.println("Invalid class name. Try again.");
			}
		} else {
			String input;
			Scanner scanner = new Scanner(System.in);
			System.out.println("!exit to exit");
			while (true) {
				System.out.println("Please enter full class name (e.g. java.util.Date).");
				input = scanner.next();
				try {
					System.out.println(classToString(input));
				} catch (ClassNotFoundException e) {
					System.out.println("Invalid class name. Try again.");
				}
				if (input.equals("!exit") ) {
					break;
				}
			}
			scanner.close();
		}
		System.exit(0);
	}
	
	public static String classToString(String name) throws ClassNotFoundException {
		StringBuffer sb = new StringBuffer();
			Class<?> c = Class.forName(name);
			Class<?> superC = c.getSuperclass();
			String modifiers = Modifier.toString(c.getModifiers());
			if (modifiers.length() > 0) {
				sb.append(modifiers);
				sb.append(' ');
			}
			sb.append("class ");
			sb.append(name);
			if (superC != null && superC != Object.class) {
				sb.append(" extends ");
				sb.append(superC.getName());
			}
			sb.append(" {\n");
			sb.append(fieldsToString(c));
			sb.append('\n');
			sb.append(constructorsToString(c));
			sb.append('\n');
			sb.append(methodsToString(c));
			sb.append("\n}");
			return sb.toString();
	}

	public static String constructorsToString(Class<?> c) {
		StringBuffer sb = new StringBuffer();
		Constructor<?>[] constructors = c.getDeclaredConstructors();
		
		for (Constructor<?> contructor : constructors) {
			String name = contructor.getName();
			sb.append(' ');
			String modifiers = Modifier.toString(contructor.getModifiers());
			if (modifiers.length() > 0) {
				sb.append(modifiers);
				sb.append(' ');
			}
			sb.append(name);
			sb.append('(');
			Class<?>[] paramTypes = contructor.getParameterTypes();
			for (int j = 0; j < paramTypes.length; j++) {
				if (j > 0) {
					sb.append(", ");
				}
				sb.append(paramTypes[j].getName());
			}
			sb.append(");\n");
		}
		return sb.toString();
	}

	public static String methodsToString(Class<?> c) {
		StringBuffer sb = new StringBuffer();
		
		Method[] methods = c.getDeclaredMethods();
		for (Method m : methods) {
			Class<?> retType = m.getReturnType();
			String name = m.getName();
			sb.append(" ");
			String modifiers = Modifier.toString(m.getModifiers());
			if (modifiers.length() > 0) {
				sb.append(modifiers);
				sb.append(' ');
			}
			sb.append(retType.getName());
			sb.append(' ');
			sb.append(name);
			sb.append('(');
			Class<?>[] paramTypes = m.getParameterTypes();
			for (int j = 0; j < paramTypes.length; j++) {
				if (j > 0) {
					sb.append(", ");
				}
				sb.append(paramTypes[j].getName());
			}
			sb.append(");\n");
		}
		return sb.toString();
	}

	public static String fieldsToString(Class<?> c) {
		StringBuffer sb = new StringBuffer();
		Field[] fields = c.getDeclaredFields();
		for (Field f : fields) {
			Class<?> type = f.getType();
			sb.append(' ');
			String modifiers = Modifier.toString(f.getModifiers());
			if (modifiers.length() > 0) {
				sb.append(modifiers);
				sb.append(' ');
			}
			sb.append(type.getName());
			sb.append(' ');
			sb.append(f.getName());
			sb.append(";\n");
		}
		return sb.toString();
	}
	
}
