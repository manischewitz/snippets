package annotationPrimer;

import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ActionListenerInstaller {

	public static void processAnnotations(Object obj) {
		try {
			Class<?> c = obj.getClass();
			for (Method method : c.getDeclaredMethods()) {
				ActionListenerFor annotation = method
						.getAnnotation(ActionListenerFor.class);
				if (annotation != null) {
					Field field = c.getDeclaredField(annotation.source());
					field.setAccessible(true);
					addListener(field.get(obj), obj, method);
				}
			}
		} catch (ReflectiveOperationException e) {
			e.printStackTrace();
		}
	}

	private static void addListener(Object object, Object obj, Method method) 
			throws NoSuchMethodException, SecurityException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		
		InvocationHandler handler = new InvocationHandler() {
			public Object invoke(Object proxy, Method m, Object[] args) 
					throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
				return method.invoke(obj);
			}
		};
		Object listener = 
				Proxy.newProxyInstance(null, new Class[] {java.awt.event.ActionListener.class}, handler);
		Method adder = object.getClass().getMethod("addActionListener", ActionListener.class);
		adder.invoke(object, listener); // == button.addActionListener(() -> listener());
	}
	
}
