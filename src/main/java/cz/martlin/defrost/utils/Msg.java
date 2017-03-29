package cz.martlin.defrost.utils;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Msg {
	public static final String BUNDLE_BASE_NAME = "cz.martlin.defrost.internationalisation.messages"; //$NON-NLS-1$

	public static final ResourceBundle RESOURCE_BUNDLE;

	static {
		String localizedBN = BUNDLE_BASE_NAME + "_" + Locale.getDefault().getLanguage();
		ResourceBundle bundle;

		try {
			bundle = ResourceBundle.getBundle(localizedBN);
		} catch (Exception e) {
			bundle = ResourceBundle.getBundle(BUNDLE_BASE_NAME);
		}

		RESOURCE_BUNDLE = bundle;
	}

	private Msg() {
	}

	public static String getString(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
}
