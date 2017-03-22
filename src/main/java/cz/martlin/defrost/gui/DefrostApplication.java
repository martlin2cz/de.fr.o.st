package cz.martlin.defrost.gui;

import cz.martlin.defrost.base.BaseForumDescriptor;
import cz.martlin.defrost.impls.EmiminoForumDesc;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class DefrostApplication extends Application {

	private static final BaseForumDescriptor DEFAULT_DESCRIPTOR = new EmiminoForumDesc();

	@Override
	public void start(Stage stage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("Main.fxml"));
		Parent root = loader.load();

		MainController controller = loader.getController();
		BaseForumDescriptor descriptor = inferDescriptor();
		controller.setDescriptor(descriptor);

		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.setTitle("de.fr.o.st");
		stage.show();
	}

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
				//TODO handle error
				e.printStackTrace();
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

	private static void doHelpAndVersion() {
		System.out.println("de.fr.o.st");
		System.out.println("Discuss forums offline statistics (de.fr.o.st)");
		System.out.println("m@rtlin, 20. - 22. 03. 2017");
		System.out.println("Usage: de.fr.o.st [class name of forum descriptor]");
	}

}
