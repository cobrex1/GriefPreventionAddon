package me.cobrex.griefpreventionaddon;

import de.tr7zw.nbtapi.NBTItem;
import me.cobrex.griefpreventionaddon.config.Config;
import me.cobrex.griefpreventionaddon.utilities.ItemBuilder;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import me.ryanhamshire.GriefPrevention.PlayerData;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class ClaimblockManager implements Listener {
	private static GriefPreventionAddon main;

	private static ClaimblockManager instance;

	private GriefPrevention griefPrevention;

	public static ClaimblockManager getInstance() {
		if (instance == null)
			instance = new ClaimblockManager(main);
		return instance;
	}

	public ClaimblockManager(GriefPreventionAddon plugin) {
		main = plugin;
		this.griefPrevention = plugin.getGriefPrevention();
	}

	private static String NBT_KEY = "hazclaimblock";

	public void withdraw(Player player, int amount) {
		PlayerData playerData = this.griefPrevention.dataStore.getPlayerData(player.getUniqueId());
		int limitMin = Config.getInstance().getLimitMin();
		int limitMax = Config.getInstance().getLimitMax();
		if (amount < limitMin || amount > limitMax) {
			String limit = Config.getInstance().getMessageLimit();
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', String.format(limit, new Object[]{Integer.valueOf(amount)})));
			return;
		}
		if (playerData != null && playerData.getRemainingClaimBlocks() >= amount) {
			int toRemove = amount;
			int bonusRemove = Math.min(playerData.getBonusClaimBlocks(), toRemove);
			playerData.setBonusClaimBlocks(Integer.valueOf(playerData.getBonusClaimBlocks() - bonusRemove));
			toRemove -= bonusRemove;
			if (toRemove > 0) {
				int accruedRemove = Math.min(playerData.getAccruedClaimBlocks(), toRemove);
				playerData.setAccruedClaimBlocks(Integer.valueOf(playerData.getAccruedClaimBlocks() - accruedRemove));
			}
			String name = String.format(Config.getInstance().getItemName(), new Object[]{Integer.valueOf(amount)});
			List<String> lore = Config.getInstance().getItemLore();
			lore.replaceAll(s -> ChatColor.translateAlternateColorCodes('&', String.format(s, new Object[]{Integer.valueOf(amount)})));
			ItemBuilder paper = (new ItemBuilder(Material.valueOf(Config.getInstance().getItemMaterialName()))).name(ChatColor.translateAlternateColorCodes('&', name)).lores(lore).addNBTInteger(NBT_KEY, Integer.valueOf(amount));
			if (player.getInventory().firstEmpty() == -1) {
				player.getWorld().dropItem(player.getLocation(), paper.make());
			} else {
				player.getInventory().addItem(new ItemStack[]{paper.make()});
			}
			String withdrew = Config.getInstance().getMessageWithdrew();
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', String.format(withdrew, new Object[]{Integer.valueOf(amount)})));
		} else {
			String notenough = Config.getInstance().getMessageNotEnough();
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', notenough));
		}
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		PlayerData playerData = this.griefPrevention.dataStore.getPlayerData(player.getUniqueId());
		ItemStack clicked = event.getItem();
		if (clicked != null && event.getHand() == EquipmentSlot.HAND && playerData != null) {
			NBTItem nbtItem = new NBTItem(clicked);
			if (nbtItem.getKeys().contains(NBT_KEY)) {
				int amount = nbtItem.getInteger(NBT_KEY).intValue() * clicked.getAmount();
				if (amount > 0) {
					playerData.setBonusClaimBlocks(Integer.valueOf(playerData.getBonusClaimBlocks() + amount));
					String deposit = Config.getInstance().getMessageDeposit();
					player.sendMessage(ChatColor.translateAlternateColorCodes('&', String.format(deposit, new Object[]{Integer.valueOf(amount)})));
				}
				player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
				event.setCancelled(true);
			}
		}
	}
}
