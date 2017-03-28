package cz.martlin.defrost.gui;

import cz.martlin.defrost.forums.base.BaseForumDescriptor;
import cz.martlin.defrost.forums.impl.EmiminoForumDesc;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * The main application. Contains {@link #main(String[])} method, console params
 * processing and showing up the Main frame.
 * 
 * @author martin
 *
 */
public class DefrostApplication extends Application {

	private static final BaseForumDescriptor DEFAULT_DESCRIPTOR = new EmiminoForumDesc();
	// new IDnesForumDesc();

	@Override
	public void start(Stage stage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("Main.fxml"));

		BaseForumDescriptor descriptor = inferDescriptor();
		MainController controller = new MainController(descriptor);
		loader.setController(controller);

		Parent root = loader.load();

		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.setTitle("de.fr.o.st");
		stage.show();
	}

	/**
	 * Tries to use first param as {@link BaseForumDescriptor}'s child class
	 * name. If so, tries to instatite, else returns the
	 * {@link #DEFAULT_DESCRIPTOR}.
	 * 
	 * @return
	 */
	private BaseForumDescriptor inferDescriptor() {
		Parameters params = getParameters();

		if (params.getRaw().size() == 1) {
			String className = params.getRaw().get(0);
			try {
				Class<?> clazz = Class.forName(className);
				Object instance = clazz.newInstance();
				BaseForumDescriptor descriptor = (BaseForumDescriptor) instance;
				return descriptor;
			} catch (Exception e) {
				System.err.println("Cannot instantite descriptor: " + e.getMessage());
			}
		}

		return DEFAULT_DESCRIPTOR;
	}

	public static void main(String[] args) {
		if (args.length == 1 && //
				("-h".equals(args[0]) || "--help".equals(args[0]) //
						|| "-v".equals(args[0]) || "--version".equals(args[0]))) {
			doHelpAndVersion();
		} else {
			launch(args);
		}
	}

	/**
	 * Prints help, credits and version.
	 */
	private static void doHelpAndVersion() {
		System.out.println("de.fr.o.st 1.0");
		System.out.println("Discuss forums offline statistics (de.fr.o.st)");
		System.out.println("m@rtlin, 20. - 28. 03. 2017");
		System.out.println("Usage: de.fr.o.st [class name of forum descriptor]");
		
		System.exit(0);
	}

}
