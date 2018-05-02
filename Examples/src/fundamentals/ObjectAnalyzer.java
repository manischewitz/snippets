package fundamentals;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class ObjectAnalyzer {

	private List<Object> visited = new ArrayList<>();
	
	public String convertToString(Object object) {
		if (object == null) {
			return "null";
		}
		if (visited.contains(object)) {
			return "...";
		}
		visited.add(object);
		Class<?> cl = object.getClass();
		if (cl == String.class) {
			return (String) object;
		} 
		
		StringBuffer sb = new StringBuffer();
		if (cl.isArray()) {
			sb.append(cl.getComponentType());
			sb.append("[]{");
			for (int i = 0; i < Array.getLength(object); i++) {
				if (i > 0) {
					sb.append(',');
				}
				Object val = Array.get(object, i);
				if (cl.getComponentType().isPrimitive()) {
					sb.append(val);
				} else {
					sb.append(convertToString(val));
				} 
			}
			sb.append('}');
			return sb.toString();
		}
		
		sb.append(cl.getName());
		do {
			sb.append('[');
			Field[] fields = cl.getDeclaredFields();
			AccessibleObject.setAccessible(fields, true);
			for (Field f : fields) {
				if (!Modifier.isStatic(f.getModifiers())) {
					if (!sb.toString().endsWith("[")) {
						sb.append(',');
					}
					sb.append(f.getName());
					sb.append('=');
					
					try {
						Class<?> t = f.getType();
						Object val = f.get(object);
						if (t.isPrimitive()) {
							sb.append(val);
						} else {
							sb.append(convertToString(val));
						} 
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			sb.append(']');
			cl = cl.getSuperclass();
		} while (cl != null);
		return sb.toString();
	}
	
	public static void main(String[] args) {
		List<Integer> squares = new ArrayList<>();
		for (int i = 1; i <= 5; i++) {
			squares.add(i * i);
		} 
		//System.out.println(new ObjectAnalyzer().convertToString(squares));
		System.out.println(new ObjectAnalyzer().convertToString(new ObjectAnalyzer()));
	}

}
