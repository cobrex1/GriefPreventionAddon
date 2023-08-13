package me.cobrex.griefpreventionaddon.config;

import me.cobrex.griefpreventionaddon.utilities.MessageHandler;

import java.util.ArrayList;
import java.util.List;

public class Config extends AutoUpdateConfigLoader {
	private static Config instance;

	private Config() {
		super("config.yml");
		validate();
	}

	public static Config getInstance() {
		if (instance == null)
			instance = new Config();
		return instance;
	}

	@Override
	protected void loadKeys() {
	}

	@Override
	protected boolean validateKeys() {
		List<String> reason = new ArrayList<>();
		if (getDebug()) {
			MessageHandler.getInstance().setDebugState(true);
			MessageHandler.getInstance().debug("Debugging has been enabled.");
		}
		if (getDebugToAdmins()) {
			MessageHandler.getInstance().setDebugState(true);
			MessageHandler.getInstance().debug("Debug loggint to admins has been enabled.");
		}
		return noErrorsInConfig(reason);
	}

	private String getStringIncludingInts(String key) {
		String str = this.config.getString(key);
		if (str == null)
			str = String.valueOf(this.config.getInt(key));
		if (str.equals("0"))
			str = "No value set for '" + key + "'";
		return str;
	}

	public String getLocale() {
		return this.config.getString("General.Locale", "en_us");
	}

	public boolean getDebug() {
		return this.config.getBoolean("debug", false);
	}

	public boolean getDebugToAdmins() {
		return this.config.getBoolean("debugToAdmins", false);
	}

	public String getMessageClaimblockNumber() {
		return this.config.getString("claimblock.message.number", "&cNot a valid number.");
	}

	public String getMessageNotEnough() {
		return this.config.getString("claimblock.message.notenough", "&cYou don't have enough claim blocks.");
	}

	public String getMessageWithdrew() {
		return this.config.getString("claimblock.message.withdrew", "&aYou have withdrew %d claim blocks.");
	}

	public String getMessageLimit() {
		return this.config.getString("claimblock.message.limit", "&cYou can't withdraw that many claim blocks.");
	}

	public String getMessageDeposit() {
		return this.config.getString("claimblock.message.deposit", "&a%d claim blocks has been added to your account.");
	}

	public String getItemMaterialName() {
		return this.config.getString("claimblock.item.material", "PAPER");
	}

	public String getItemName() {
		return this.config.getString("claimblock.item.name", "&6&l%d &e&l&nClaim Blocks");
	}

	public List<String> getItemLore() {
		return this.config.getStringList("claimblock.item.lore");
	}

	public int getLimitMin() {
		return this.config.getInt("claimblock.limit.min", 1);
	}

	public int getLimitMax() {
		return this.config.getInt("claimblock.limit.max", 10000);
	}
}

