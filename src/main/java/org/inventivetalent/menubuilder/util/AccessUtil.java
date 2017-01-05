package org.inventivetalent.menubuilder.util;

import java.lang.reflect.Field;

public class AccessUtil {

	/**
	 * Set a specified Field accessible
	 *
	 * @param f Field set accessible
	 * @return <code>true</code> if the field was previously accessible
	 */
	public static Field setAccessible(Field f) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		f.setAccessible(true);
		Field modifiersField = Field.class.getDeclaredField("modifiers");
		modifiersField.setAccessible(true);
		modifiersField.setInt(f, f.getModifiers() & 0xFFFFFFEF);
		return f;
	}

}