package SkyblockSpawners.SkyblockSpawners;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;
import net.milkbowl.vault.economy.Economy;
import vegan.CastiaShards.main.CastiaShards;

public class SkyblockSpawners extends JavaPlugin {

	public Shop shop;
	public Config config;
	public SpawnerManager spawnerManager;
	public Economy econ;
	public CastiaShards shards;
	public SpawnerListener spawnerListener;
	public MobManager mobManager;
	
	//Called upon plugin enable
	public void onEnable() {
		
		//Hook into economy
		if(setupEconomy() == false) {
			System.out.println("Economy (Vault) not found. Disabling Plugin.");
			Bukkit.getPluginManager().disablePlugin(this);
			return;
		}
		
		//Hook into shards plugin
		if(setupShards() == false) {
			System.out.println("CastiaShards plugin not found. Disabling Plugin.");
			Bukkit.getPluginManager().disablePlugin(this);
			return;
		}
			
		
		spawnerManager = new SpawnerManager();
		config = new Config(this);		
		shop = new Shop(this);
		spawnerListener = new SpawnerListener(this);
		mobManager = new MobManager(this);
		Bukkit.getServer().getPluginManager().registerEvents(spawnerListener, this);

	}
	
	//Called upon plugin disable
	public void onDisable() {
		
	}
	
	//Read Commands
	public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {

		String commandName = cmd.getName();
		Player p = (Player) sender;

		switch(commandName) {

		case "spawners":
			shop.openShop(p);
			break;

		case "head":
			if(args.length == 2) {
				if(sender.hasPermission("spawners.head")) {
					try {
						mobManager.giveHead(p, args[0], Integer.parseInt(args[1]));
					} catch (Exception e) {
						((Player) sender).sendMessage(ChatColor.RED+"Invalid Number/Syntax! Type /head [name] [amount]");
					}
				}
			} else {
				((Player) sender).sendMessage(ChatColor.RED+"Invalid Syntax! Type /head [name] [amount]");
			}
			break;

		}

		return true;
	}
	
    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }
    
    private boolean setupShards() {
        Plugin shardsPlugin = Bukkit.getPluginManager().getPlugin("CastiaShards");
        if (shardsPlugin == null || shardsPlugin.isEnabled() == false || (shardsPlugin instanceof CastiaShards) == false) {
            return false;
        }

    	shards = (CastiaShards) shardsPlugin;
    	return shards != null;
    }
    
   

}
