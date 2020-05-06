package SkyblockSpawners.SkyblockSpawners;

import java.io.File;
import java.util.UUID;

import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import net.milkbowl.vault.economy.Economy;

public class Config {

	private SkyblockSpawners plugin;
	private String fileDirectory;
	
	//Class Init
	public Config(SkyblockSpawners plugin) {
		
		this.plugin = plugin;
		
		loadConfig();
	}
	
	private void loadConfig() {
		fileDirectory = plugin.getDataFolder() + "/players/";
		FileConfiguration config = plugin.getConfig();

		
		//Cycle through all the items		
		for(SpawnerType spawnerType : SpawnerType.values()) {
			String spawner = spawnerType.toString();
			int price = config.getInt("SPAWNERS."+spawner+".PRICE");
			int unlockCost = config.getInt("SPAWNERS."+spawner+".UNLOCK-COST");
			String unlockType = config.getString("SPAWNERS."+spawner+".UNLOCK-TYPE");
			int headPay = config.getInt("SPAWNERS."+spawner+".HEAD-PAY");
			double headChance = config.getDouble("SPAWNERS."+spawner+".HEAD-CHANCE");
			int totalShardCost = config.getInt("SPAWNERS."+spawner+".TOTAL-SHARD-COST");
			
			//Fallback incase config has an issue.
			if(unlockType == null || unlockType.length() == 0)
				unlockType = "ZOMBIE";
			try {
				
				Spawner s = new Spawner(spawnerType, price, unlockCost, headPay, headChance, totalShardCost, SpawnerType.valueOf(unlockType));
				plugin.spawnerManager.addSpawner(s);
				
			} catch (Exception e) {
				System.out.println("Error parsing SkyblockSpawners. Please check your syntax. Spawner Type:"+spawner);
				e.printStackTrace();
				plugin.getPluginLoader().disablePlugin(plugin);
				return;
			}	
		}
	}
	
	//Returns number of heads for a mob type
	public int getHeads(Player p, SpawnerType type) {		
		YamlConfiguration config = getPlayerConfig(p);

		if(config.isSet(type.toString()+".HEADS"))
			return config.getInt(type.toString()+".HEADS");
		
		//No config, return
		return 0;
	}
	
	//Removed number of heads for a mob type
	public void takeHeads(OfflinePlayer p, SpawnerType type, int amount) {		
		UUID uuid = p.getUniqueId();
		File file = new File(fileDirectory + "" + uuid.toString() + ".yml");
		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

		int amountOwned = config.getInt(type.toString()+".HEADS");
		amountOwned -= amount;
		
		config.set(type.toString()+".HEADS", amountOwned);
		
		try {
			config.save(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//Give number of heads for a mob type
	public void giveHeads(OfflinePlayer p, SpawnerType type, int amount) {		
		UUID uuid = p.getUniqueId();
		File file = new File(fileDirectory + "" + uuid.toString() + ".yml");
		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

		int amountOwned = config.getInt(type.toString()+".HEADS");
		amountOwned += amount;
		
		config.set(type.toString()+".HEADS", amountOwned);
		
		try {
			config.save(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//Unlock Spawner
	public void unlockSpawner(OfflinePlayer p, SpawnerType type) {		
		UUID uuid = p.getUniqueId();
		File file = new File(fileDirectory + "" + uuid.toString() + ".yml");
		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

		
		config.set(type.toString()+".UNLOCKED", true);
		
		try {
			config.save(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//Returns amount of money a player has
	public double getMoney(OfflinePlayer p) {
		
		Economy econ = plugin.econ;		
		return econ.getBalance(p);
		
	}
	
	//Takes an amount of money from a player
	public void takeMoney(OfflinePlayer p, double amount) {
		
		Economy econ = plugin.econ;		
		econ.withdrawPlayer(p, amount);
		
	}
	
	//Gives an amount of money from a player
	public void giveMoney(OfflinePlayer p, double amount) {
		
		Economy econ = plugin.econ;		
		econ.depositPlayer(p, amount);
		
	}
	
	//Confirms if a player has the right amount of money
	public boolean hasMoney(OfflinePlayer p, double amount) {
		
		Economy econ = plugin.econ;		
		return econ.has(p, amount);
		
	}
	
	//Returns if spawner has been unlocked for a player
	public boolean getUnlocked(OfflinePlayer p, Spawner spawner) {		
		if(spawner.getUpgradeCost() == 0)
			return true;

		YamlConfiguration config = getPlayerConfig(p);

		if(config.getBoolean(spawner.getType().toString()+".UNLOCKED"))
			return true;

		//No config, return
		return false;
	}
	
	//Get player's config file
	private YamlConfiguration getPlayerConfig(OfflinePlayer p) {
		UUID uuid = p.getUniqueId();
		File file = new File(fileDirectory + "" + uuid.toString() + ".yml");
		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
		return config;
	}
	
}
