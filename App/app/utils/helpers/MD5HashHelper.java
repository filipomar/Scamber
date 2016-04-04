package utils.helpers;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import play.Logger;

public class MD5HashHelper {

	private static MessageDigest digester;

	private MD5HashHelper() {
	}

	public static String hash(final String source) {
		final MessageDigest digester = getDigester();
		if (digester == null) {
			return null;
		}

		digester.update(source.getBytes());

		final StringBuilder builder = new StringBuilder();
		for (final byte particle : digester.digest()) {
			builder.append(Integer.toString((particle & 0xff) + 0x100, 16).substring(1));
		}

		return builder.toString();
	}

	private static MessageDigest getDigester() {
		if (digester != null) {
			return digester;
		}

		try {
			digester = MessageDigest.getInstance("MD5");
			return digester;
		} catch (final NoSuchAlgorithmException e) {
			Logger.error("Could not get instance of MD5", e);
			return null;
		}
	}
}
