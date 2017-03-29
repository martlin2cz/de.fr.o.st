package cz.martlin.defrost.utils;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Internationalisation. Loads the bundle depending on current {@link Locale}
 * and gets strings.
 * 
 * @author martin
 *
 */
public class Msg {
	private static final Logger LOG = LoggerFactory.getLogger(Msg.class);

	public static final String BUNDLE_BASE_NAME = "cz.martlin.defrost.internationalisation.messages"; //$NON-NLS-1$

	public static final ResourceBundle RESOURCE_BUNDLE = inferBundle();

	private Msg() {
	}

	/**
	 * Loads bundle (by current locale or default one if somethin fails.
	 * 
	 * @return
	 */
	private static ResourceBundle inferBundle() {
		String name = BUNDLE_BASE_NAME + "_" + Locale.getDefault().getLanguage();

		ResourceBundle bundle;
		try {
			bundle = ResourceBundle.getBundle(name);
		} catch (Exception e) {
			LOG.warn("Cannot find bundle for locale " + Locale.getDefault(), e);
			bundle = ResourceBundle.getBundle(BUNDLE_BASE_NAME);
		}

		return bundle;
	}

	public static String getString(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			LOG.warn("Cannot find string resource of key" + key, e);
			return '!' + key + '!';
		}
	}
}
