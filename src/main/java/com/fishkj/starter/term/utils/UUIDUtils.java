package com.fishkj.starter.term.utils;

import java.util.UUID;

public class UUIDUtils {

	public static String createSystemDataPrimaryKey() {
		UUID uuid = UUID.randomUUID();
		char[] dest = new char[32];
        char[] src = uuid.toString().toCharArray();
        System.arraycopy(src, 0, dest, 0, 8);
        System.arraycopy(src, 9, dest, 8, 4);
        System.arraycopy(src, 14, dest, 12, 4);
        System.arraycopy(src, 19, dest, 16, 4);
        System.arraycopy(src, 24, dest, 20, 12);
        return new String(dest);
	}
}
