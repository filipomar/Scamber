package application;

import play.Application;
import play.GlobalSettings;
import play.Logger;

public class EscamberSettings extends GlobalSettings {
	@Override
	public void onStart(final Application app) {
		Logger.info("FretOnde - Application started");
		super.onStart(app);

		InitialDataInserter.insert(app);
	}
}
