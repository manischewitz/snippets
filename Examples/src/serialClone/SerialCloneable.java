package serialClone;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class SerialCloneable implements Cloneable, Serializable {

	@Override
	public Object clone() throws CloneNotSupportedException {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
				oos.writeObject(this);
			}
			try (InputStream in = new ByteArrayInputStream(baos.toByteArray())) {
				ObjectInputStream ois = new ObjectInputStream(in);
				return ois.readObject();
			}
		} catch (IOException | ClassNotFoundException e) {
			CloneNotSupportedException cnse = new CloneNotSupportedException();
			cnse.initCause(e);
			throw cnse;
		}
	}
	
}
