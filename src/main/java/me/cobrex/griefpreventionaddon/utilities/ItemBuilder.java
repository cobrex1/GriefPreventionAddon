package me.cobrex.griefpreventionaddon.utilities;

import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ItemBuilder {
	private ItemStack item;

	private ItemMeta itemM;

	public ItemBuilder(Material itemType) {
		this.item = new ItemStack(itemType);
		this.itemM = this.item.getItemMeta();
	}

	public ItemBuilder(ItemStack itemStack) {
		this.item = itemStack;
		this.itemM = this.item.getItemMeta();
	}

	public ItemBuilder() {
		this.item = new ItemStack(Material.AIR);
		this.itemM = this.item.getItemMeta();
	}

	public ItemBuilder type(Material material) {
		make().setType(material);
		return this;
	}

	public ItemBuilder amount(Integer itemAmt) {
		make().setAmount(itemAmt.intValue());
		return this;
	}

	public ItemBuilder name(String name) {
		meta().setDisplayName(name);
		make().setItemMeta(meta());
		return this;
	}

	public ItemBuilder lore(String lore) {
		List<String> lores = meta().getLore();
		if (lores == null)
			lores = new ArrayList<>();
		lores.add(lore);
		meta().setLore(lores);
		make().setItemMeta(meta());
		return this;
	}

	public ItemBuilder lores(String[] lores) {
		List<String> loresList = meta().getLore();
		if (loresList == null) {
			loresList = new ArrayList<>();
		} else {
			loresList.clear();
		}
		Collections.addAll(loresList, lores);
		meta().setLore(loresList);
		make().setItemMeta(meta());
		return this;
	}

	public ItemBuilder lores(List<String> lores) {
		List<String> loresList = meta().getLore();
		if (loresList == null) {
			loresList = new ArrayList<>();
		} else {
			loresList.clear();
		}
		loresList.addAll(lores);
		meta().setLore(loresList);
		make().setItemMeta(meta());
		return this;
	}

	public ItemBuilder damage(int durability) {
		if (meta() instanceof Damageable) {
			Damageable dmg = (Damageable) meta();
			dmg.setDamage(durability);
			make().setItemMeta(meta());
		}
		return this;
	}

	public ItemBuilder enchantment(Enchantment enchantment, int level) {
		make().addUnsafeEnchantment(enchantment, level);
		return this;
	}

	public ItemBuilder enchantment(Enchantment enchantment) {
		make().addUnsafeEnchantment(enchantment, 1);
		return this;
	}

	public ItemBuilder enchantments(Enchantment[] enchantments, int level) {
		make().getEnchantments().clear();
		for (Enchantment enchantment : enchantments)
			make().addUnsafeEnchantment(enchantment, level);
		return this;
	}

	public ItemBuilder enchantments(Enchantment[] enchantments) {
		make().getEnchantments().clear();
		for (Enchantment enchantment : enchantments)
			make().addUnsafeEnchantment(enchantment, 1);
		return this;
	}

	public ItemBuilder clearEnchantment(Enchantment enchantment) {
		Map<Enchantment, Integer> itemEnchantments = make().getEnchantments();
		for (Enchantment enchantmentC : itemEnchantments.keySet()) {
			if (enchantment == enchantmentC)
				itemEnchantments.remove(enchantmentC);
		}
		return this;
	}

	public ItemBuilder clearEnchantments() {
		make().getEnchantments().clear();
		return this;
	}

	public ItemBuilder clearLore(String lore) {
		meta().getLore().remove(lore);
		make().setItemMeta(meta());
		return this;
	}

	public ItemBuilder clearLores() {
		meta().getLore().clear();
		make().setItemMeta(meta());
		return this;
	}

	public ItemBuilder color(Color color) {
		if (make().getType() == Material.LEATHER_HELMET || make().getType() == Material.LEATHER_CHESTPLATE || make().getType() == Material.LEATHER_LEGGINGS || make().getType() == Material.LEATHER_BOOTS) {
			LeatherArmorMeta meta = (LeatherArmorMeta) meta();
			meta.setColor(color);
			make().setItemMeta((ItemMeta) meta);
		}
		return this;
	}

	public ItemBuilder clearColor() {
		if (make().getType() == Material.LEATHER_HELMET || make().getType() == Material.LEATHER_CHESTPLATE || make().getType() == Material.LEATHER_LEGGINGS || make().getType() == Material.LEATHER_BOOTS) {
			LeatherArmorMeta meta = (LeatherArmorMeta) meta();
			meta.setColor((Color) null);
			make().setItemMeta((ItemMeta) meta);
		}
		return this;
	}

	public ItemBuilder addNBTString(String key, String value) {
		NBTItem nbt = new NBTItem(this.item);
		nbt.setString(key, value);
		this.item = nbt.getItem();
		return this;
	}

	public ItemBuilder addNBTInteger(String key, Integer value) {
		NBTItem nbt = new NBTItem(this.item);
		nbt.setInteger(key, value);
		this.item = nbt.getItem();
		return this;
	}

	public ItemMeta meta() {
		return this.itemM;
	}

	public ItemStack make() {
		return this.item;
	}
}

