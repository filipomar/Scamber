package utils;

import play.Configuration;
import play.Play;

public class ConfigurationUtils {
	private static final String APPLICATION_USERS_PICTURES = "application.users.picturesPath";
	private static final String APPLICATION_YML_FILE = "application.ymlFile";

	private static Configuration getPlayConfiguration() {
		return Play.application().configuration();
	}

	public static String getYmlFilePath() {
		return getPlayConfiguration().getString(APPLICATION_YML_FILE);
	}

	public static String getUserPicturesPath(final String picture) {
		return String.format(getPlayConfiguration().getString(APPLICATION_USERS_PICTURES), picture);
	}

}
