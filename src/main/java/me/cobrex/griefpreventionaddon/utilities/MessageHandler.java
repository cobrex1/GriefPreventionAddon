package me.cobrex.griefpreventionaddon.utilities;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.logging.Level;

public class MessageHandler {
	private static MessageHandler instance;

	public String prefix = ChatColor.GREEN + "" + ChatColor.BOLD + "GriefpreventionAddon" + ChatColor.GRAY;

	private boolean debug = false;

	private boolean debugToAdmins = false;

	public void updatePrefix(String prefix) {
		this.prefix = ChatColor.translateAlternateColorCodes('&', prefix);
	}

	public static MessageHandler getInstance() {
		if (instance == null)
			instance = new MessageHandler();
		return instance;
	}

	public void debug(String msg) {
		if (this.debug)
			Bukkit.getLogger().log(Level.INFO, this.prefix + msg);
		if (this.debugToAdmins)
			for (Player p : Bukkit.getOnlinePlayers()) {
				if (p.hasPermission("GriefpreventionAddon.admin"))
					p.sendMessage(this.prefix + msg);
			}
	}

	public void log(Level lvl, String msg) {
		Bukkit.getLogger().log(lvl, this.prefix + msg);
		if (this.debugToAdmins)
			for (Player p : Bukkit.getOnlinePlayers()) {
				if (p.hasPermission("GriefpreventionAddon.admin"))
					p.sendMessage(this.prefix + msg);
			}
	}

	public void log(String msg) {
		log(Level.INFO, msg);
	}

	public void setDebugState(boolean b) {
		this.debug = b;
	}

	public void setDebugToAdminState(boolean b) {
		this.debugToAdmins = b;
	}

	public boolean getDebugState() {
		return this.debug;
	}

	public boolean getDebugToAdminState() {
		return this.debugToAdmins;
	}
}

