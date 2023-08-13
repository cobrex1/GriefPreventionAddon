package me.cobrex.griefpreventionaddon.config;

import me.cobrex.griefpreventionaddon.utilities.MessageHandler;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;

public abstract class AutoUpdateConfigLoader extends ConfigLoader {
	public AutoUpdateConfigLoader(String relativePath, String fileName) {
		super(relativePath, fileName);
	}

	public AutoUpdateConfigLoader(String fileName) {
		super(fileName);
	}

	@Override
	protected void loadFile() {
		super.loadFile();
		YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(plugin.getResourceAsReader(this.fileName));
		Set<String> configKeys = this.config.getKeys(true);
		Set<String> internalConfigKeys = yamlConfiguration.getKeys(true);
		boolean needSave = false;
		Set<String> oldKeys = new HashSet<>(configKeys);
		oldKeys.removeAll(internalConfigKeys);
		Set<String> newKeys = new HashSet<>(internalConfigKeys);
		newKeys.removeAll(configKeys);
		if (!newKeys.isEmpty() || !oldKeys.isEmpty())
			needSave = true;
		for (String key : oldKeys)
			MessageHandler.getInstance().debug("Detected potentially unused key: " + key);
		for (String key : newKeys) {
			MessageHandler.getInstance().debug("Adding new key: " + key + " = " + yamlConfiguration.get(key));
			this.config.set(key, yamlConfiguration.get(key));
		}
		if (needSave) {
			String output = this.config.saveToString();
			output = output.replace("  ", "    ");
			while (output.replaceAll("[//s]", "").startsWith("#"))
				output = output.substring(output.indexOf('\n', output.indexOf('#')) + 1);
			try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(plugin.getResource(this.fileName)));
				LinkedHashMap<String, String> comments = new LinkedHashMap<>();
				String temp = "";
				String line;
				while ((line = reader.readLine()) != null) {
					if (line.contains("#")) {
						temp = temp + line + "\n";
						continue;
					}
					if (line.contains(":")) {
						line = line.substring(0, line.indexOf(":") + 1);
						if (!temp.isEmpty()) {
							if (comments.containsKey(line)) {
								int index = 0;
								while (comments.containsKey(line + index))
									index++;
								line = line + index;
							}
							comments.put(line, temp);
							temp = "";
						}
					}
				}
				HashMap<String, Integer> indexed = new HashMap<>();
				for (String key : comments.keySet()) {
					String actualkey = key.substring(0, key.indexOf(":") + 1);
					int index = 0;
					if (indexed.containsKey(actualkey))
						index = ((Integer) indexed.get(actualkey)).intValue();
					boolean isAtTop = !output.contains("\n" + actualkey);
					index = output.indexOf((isAtTop ? "" : "\n") + actualkey, index);
					if (index >= 0) {
						output = output.substring(0, index) + "\n" + (String) comments.get(key) + output.substring(isAtTop ? index : (index + 1));
						indexed.put(actualkey, Integer.valueOf(index + ((String) comments.get(key)).length() + actualkey.length() + 1));
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				String saveName = this.fileName;
				if (!plugin.getConfig().getBoolean("setting.updateoverwrite", true))
					saveName = saveName + ".new";
				BufferedWriter writer = new BufferedWriter(new FileWriter(new File(plugin.getDataFolder(), saveName)));
				writer.write(output);
				writer.flush();
				writer.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
