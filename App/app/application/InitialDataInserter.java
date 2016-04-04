package application;

import java.util.ArrayList;
import java.util.List;

import models.EBaseModel;

import org.apache.commons.lang3.StringUtils;

import play.Application;
import play.Logger;
import play.libs.Yaml;
import utils.ConfigurationUtils;

import com.avaje.ebean.Ebean;

class InitialDataInserter {

	static void insert(final Application app) {
		Logger.info("================== Inserting Initial Data ==================");

		try {
			Ebean.beginTransaction();
			insertInitialData(app);
			Ebean.commitTransaction();
		} catch (final Exception e) {
			Logger.error("There was an issue loading initial data", e);
			Ebean.rollbackTransaction();
		} finally {
			Ebean.endTransaction();
		}

		Logger.info("================== Initial Data Load Ended ==================");

	}

	private static void insertInitialData(final Application app) {
		Logger.info("INITIAL DATA - Retrieving Yaml File Path");
		final String path = ConfigurationUtils.getYmlFilePath();

		Logger.info("INITIAL DATA - Retrieved Yaml File Path");

		if (StringUtils.isBlank(path)) {
			Logger.error("INITIAL DATA - Yaml File is invalid");
			throw new NullPointerException();
		}

		Logger.info("INITIAL DATA - Converting YAML file to java object");
		@SuppressWarnings("unchecked")
		final List<EBaseModel> entitiesPool = (List<EBaseModel>) Yaml.load(path);

		Logger.info("INITIAL DATA - Retrieving list of entities to be added");
		final List<EBaseModel> entitiesToBeAdded = new ArrayList<EBaseModel>();
		for (final EBaseModel entity : entitiesPool) {
			if (Ebean.find(entity.getClass()).findRowCount() == 0) {
				entitiesToBeAdded.add(entity);
			}
		}

		Logger.info("INITIAL DATA - " + entitiesToBeAdded.size() + " entity[ies] will be added");
		for (final EBaseModel entity : entitiesToBeAdded) {
			entity.save();
		}
	}

}
