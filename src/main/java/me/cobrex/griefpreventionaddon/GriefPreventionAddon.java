package me.cobrex.griefpreventionaddon;

import com.google.common.base.Charsets;
import me.cobrex.griefpreventionaddon.config.Config;
import me.cobrex.griefpreventionaddon.utilities.MessageHandler;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;

public class GriefPreventionAddon extends JavaPlugin {
	public static GriefPreventionAddon gpa;

	public boolean noErrorsInConfigFiles;

	private GriefPrevention griefPrevention;

	@Override
	public void onEnable() {
		gpa = this;
		Config.getInstance();
		MessageHandler.getInstance().setDebugState(Config.getInstance().getDebug());
		MessageHandler.getInstance().setDebugToAdminState(Config.getInstance().getDebugToAdmins());
		if (Bukkit.getPluginManager().getPlugin("GriefPrevention") != null) {
			MessageHandler.getInstance().log(" Hooking into GriefPrevention...");
			this.griefPrevention = GriefPrevention.instance;
		} else {
			MessageHandler.getInstance().log(Level.SEVERE, " GriefPrevention could not be found! Shutting down...");
			Bukkit.getPluginManager().disablePlugin((Plugin) this);
			return;
		}
		getCommand("gpaddon").setExecutor(new GriefpreventionAddonCommand());
		getCommand("cbwithdraw").setExecutor(new ClaimBlockWithdrawCommand());
		Bukkit.getPluginManager().registerEvents(new ClaimblockManager(this), (Plugin) this);
		MessageHandler.getInstance().log(ChatColor.GREEN + "GriefPreventionAddon by Cobrex has been Loaded!");
	}

	@Override
	public void onDisable() {
		MessageHandler.getInstance().log(ChatColor.RED + "GriefPrevention Addon has been disabled!");
	}

	public InputStreamReader getResourceAsReader(String fileName) {
		InputStream in = getResource(fileName);
		return (in == null) ? null : new InputStreamReader(in, Charsets.UTF_8);
	}

	public GriefPrevention getGriefPrevention() {
		return this.griefPrevention;
	}

}
