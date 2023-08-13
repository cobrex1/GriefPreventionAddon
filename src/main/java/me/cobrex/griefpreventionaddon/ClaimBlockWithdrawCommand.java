package me.cobrex.griefpreventionaddon;

import me.cobrex.griefpreventionaddon.config.Config;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClaimBlockWithdrawCommand implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			try {
				int amount = Integer.parseInt(args[0]);
				ClaimblockManager.getInstance().withdraw(player, amount);
			} catch (NumberFormatException e) {
				String usage = Config.getInstance().getMessageClaimblockNumber();
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', usage));
			}
		}
		return true;
	}
}
