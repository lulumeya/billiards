package pointer.wbc.com.billiardspointer.util;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class InstantObjectTransporter {
	private static Map<String, Object> objectMap = new HashMap<String, Object>();

	private InstantObjectTransporter() {}

	private static String createUniqueKey() {
		return UUID.randomUUID().toString();
	}

	public static synchronized <E> String put(E var) {
		if (var == null) {
			return null;
		}
		String key = createUniqueKey();
		objectMap.put(key, var);
		return key;
	}
	
	public static synchronized <E> String putWithSoftReference(E var) {
		return put(new SoftReference<E>(var));
	}
	
	public static synchronized <E> E consume(String key) {
		E value = (E) objectMap.remove(key);
		if (value == null) {
			return null;
		}
		if (value instanceof SoftReference) {
			value = ((SoftReference<E>)value).get();
		}
		return value;
	}
}