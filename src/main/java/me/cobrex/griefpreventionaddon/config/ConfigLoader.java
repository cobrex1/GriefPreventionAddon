package me.cobrex.griefpreventionaddon.config;

import me.cobrex.griefpreventionaddon.GriefPreventionAddon;
import me.cobrex.griefpreventionaddon.utilities.MessageHandler;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.List;

public abstract class ConfigLoader {
	protected static final GriefPreventionAddon plugin = GriefPreventionAddon.gpa;

	protected String fileName;

	private File configFile;

	protected FileConfiguration config;

	public ConfigLoader(String relativePath, String fileName) {
		this.fileName = fileName;
		this.configFile = new File(plugin.getDataFolder(), relativePath + File.separator + fileName);
		loadFile();
	}

	public ConfigLoader(String fileName) {
		this.fileName = fileName;
		this.configFile = new File(plugin.getDataFolder(), fileName);
		loadFile();
	}

	protected void loadFile() {
		if (!this.configFile.exists()) {
			MessageHandler.getInstance().debug("Creating GriefpreventionAddon " + this.fileName + " File...");
			try {
				plugin.saveResource(this.fileName, false);
			} catch (IllegalArgumentException ex) {
				plugin.saveResource(this.configFile.getParentFile().getName() + File.separator + this.fileName, false);
			}
		} else {
			MessageHandler.getInstance().debug("Loading GriefpreventionAddon " + this.fileName + " File...");
		}
		this.config = (FileConfiguration) YamlConfiguration.loadConfiguration(this.configFile);
	}

	protected abstract void loadKeys();

	protected boolean validateKeys() {
		return true;
	}

	protected boolean noErrorsInConfig(List<String> issues) {
		for (String issue : issues)
			plugin.getLogger().warning(issue);
		return issues.isEmpty();
	}

	protected void validate() {
		if (validateKeys()) {
			MessageHandler.getInstance().debug("No errors found in " + this.fileName + "!");
		} else {
			plugin.getLogger().warning("Errors were found in " + this.fileName + "! VoidRPG will be disabled!");
			plugin.getServer().getPluginManager().disablePlugin((Plugin) plugin);
			plugin.noErrorsInConfigFiles = false;
		}
	}

	public File getFile() {
		return this.configFile;
	}

	public void backup() {
		plugin.getLogger().warning("You are using an old version of the " + this.fileName + " file.");
		plugin.getLogger().warning("Your old file has been renamed to " + this.fileName + ".old and has been replaced by an updated version.");
		this.configFile.renameTo(new File(this.configFile.getPath() + ".old"));
		if (plugin.getResource(this.fileName) != null)
			plugin.saveResource(this.fileName, true);
		plugin.getLogger().warning("Reloading " + this.fileName + " with new values...");
		loadFile();
		loadKeys();
	}
}

