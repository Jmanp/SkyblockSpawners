package SkyblockSpawners.SkyblockSpawners;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Shop {

	private SkyblockSpawners plugin;
	private DecimalFormat formatter;

	//Class init
	public Shop(SkyblockSpawners plugin) {
		this.plugin = plugin;	
		formatter = new DecimalFormat("#,##0");
	}

	public void onInventoryCloseEvent(InventoryCloseEvent event) {
		Player p = (Player) event.getPlayer();
		Inventory inventory = event.getInventory();

		//p.sendMessage(inventory.getName());

		if(p == null || p.isOnline() == false)
			return;

		if(inventory.getName().equals(ChatColor.GRAY+"Deposit Heads")) {
			depositHeads(inventory, p, true);
		}
	}
	
	//Scan and credit for heads in deposit inventory
	public void depositHeads(Inventory inventory, OfflinePlayer p, boolean verbose) {
		ItemStack[] contents = inventory.getContents();

		int[] headCount = new int[plugin.spawnerManager.spawners.length];

		int pay = 0;
		int heads = 0;
		boolean save = false;
		//Scan inventory and count heads and pay
		for(int i = 0; i < contents.length; i++) {
			ItemStack stack = contents[i];

			if(stack == null || stack.getType() != Material.SKULL_ITEM || stack.hasItemMeta() == false || stack.getItemMeta().hasDisplayName() == false || stack.getItemMeta().getDisplayName().contains(ChatColor.WHITE+"Head") == false) {
				continue;
			}

			String name = stack.getItemMeta().getDisplayName();			
			Spawner s = getSpawnerForHeadName(name);

			if(s == null) {
				continue;
			}

			int amount = stack.getAmount();
			heads += amount;
			pay += (amount * s.getHeadPay());

			headCount[getSpawnerIndex(s)] += amount;
			inventory.remove(stack);
			save = true; //Found heads, so save everything
		}

		if(save) {
			//Give money and tell player how much they earned and deposited.
			if(verbose && p.isOnline())
				p.getPlayer().sendMessage(ChatColor.GRAY+"You deposit "+ChatColor.GREEN+""+formatter.format(heads)+ChatColor.GRAY+" heads and received "+ChatColor.GREEN+"$"+formatter.format(pay)+""+ChatColor.GRAY+"!");
			plugin.config.giveMoney(p, pay);

			//Save head count
			for(int i = 0; i < headCount.length; i++) {
				Spawner s = plugin.spawnerManager.spawners[i];
				plugin.config.giveHeads(p, s.getType(), headCount[i]);
			}
		}		
	}
	
	public boolean depositHead(OfflinePlayer p, ItemStack stack) {
		int[] headCount = new int[plugin.spawnerManager.spawners.length];

		int pay = 0;
		boolean save = false;
		//Scan inventory and count heads and pay
		if(stack == null || stack.getType() != Material.SKULL_ITEM || stack.hasItemMeta() == false || stack.getItemMeta().hasDisplayName() == false || stack.getItemMeta().getDisplayName().contains(ChatColor.WHITE+"Head") == false) {
			return false;
		}

		String name = stack.getItemMeta().getDisplayName();			
		Spawner s = getSpawnerForHeadName(name);

		if(s == null) {
			return false;
		}

		int amount = stack.getAmount();
		pay += (amount * s.getHeadPay());

		headCount[getSpawnerIndex(s)] += amount;

		save = true; //Found heads, so save everything


		if(save) {
			//Give money
			plugin.config.giveMoney(p, pay);

			//Save head count
			for(int i = 0; i < headCount.length; i++) {
				Spawner s1 = plugin.spawnerManager.spawners[i];
				plugin.config.giveHeads(p, s1.getType(), headCount[i]);
			}
			return true;
		}		

		return false;
	}

	public boolean hasHead(Inventory inventory, boolean limit) {

		if(inventory == null)
			return false;

		ItemStack[] contents = inventory.getContents();
		for(int i = 0; i < contents.length; i++) {
			if(limit && i >= 36)
				break;

			ItemStack stack = contents[i];
			if(stack == null || stack.getType() != Material.SKULL_ITEM || stack.hasItemMeta() == false || stack.getItemMeta().hasDisplayName() == false) {
				continue;
			}
			String name = stack.getItemMeta().getDisplayName();			
			Spawner s = getSpawnerForHeadName(name);

			if(s == null)
				continue;

			return true;

		}

		return false;

	}



	private int getSpawnerIndex(Spawner s) {
		for(int i = 0; i < plugin.spawnerManager.spawners.length; i++) {
			if(plugin.spawnerManager.spawners[i].getType().getName().equals(s.getType().getName()))
				return i;
		}
		return 0;
	}

	public void onInventoryClick(InventoryClickEvent event) {

		Player p = (Player) event.getWhoClicked(); // The player that clicked the item
		ItemStack clicked = event.getCurrentItem(); // The item that was clicked
		Inventory inventory = event.getInventory(); // The inventory that was clicked in

		Inventory clickedInventory = event.getClickedInventory();
		if (clicked != null && inventory.getName().equals(ChatColor.GRAY+"Spawner Shop")) {
			event.setCancelled(true); 
			if(clickedInventory.getName().equals(ChatColor.GRAY+"Spawner Shop")) {
				if (clicked.getType() == Material.BARRIER) { 
					p.closeInventory(); // Closes there inventory
				} else if(clicked.getType() == Material.SKULL_ITEM) {				
					clickShopHead(clicked, p);
				} else if(clicked.getType() == Material.CAULDRON_ITEM) {
					p.openInventory(getHeadInventory());
				}
			}
		} else if (clicked != null && inventory.getName().equals(ChatColor.GRAY+"Deposit Heads")) {
			if(clicked.getType() == Material.SKULL_ITEM) {
				if(clicked == null|| clicked.hasItemMeta() == false || clicked.getItemMeta().hasDisplayName() == false || clicked.getItemMeta().getDisplayName().contains(ChatColor.WHITE+"Head") == false) {
					event.setCancelled(true);
					return;
				}

				String name = clicked.getItemMeta().getDisplayName();		

				if(name.contains(ChatColor.YELLOW+"") == false) {
					event.setCancelled(true);
					return;
				}

				Spawner s = getSpawnerForHeadName(name);

				if(s == null) {
					event.setCancelled(true);
					return;
				}
			} else if(clicked.getType() != Material.AIR) {
				event.setCancelled(true);
			}
		}

	}

	private void clickShopHead(ItemStack clicked, Player p) {
		//Check for Invalid Heads
		if(clicked == null|| clicked.hasItemMeta() == false || clicked.getItemMeta().hasDisplayName() == false || clicked.getItemMeta().getDisplayName().contains(ChatColor.WHITE+"Spawner") == false)
			return;

		String name = clicked.getItemMeta().getDisplayName();		
		Spawner s = getSpawnerForName(name);

		if(s == null)
			return;

		int headCountUpgrade = plugin.config.getHeads(p, s.getUpgradeType());
		int currentShards = plugin.shards.getPlayerShards(p);
		int shardCost = getShardCost(headCountUpgrade, s.getUpgradeCost(), s.getTotalShardCost());
		boolean unlocked = plugin.config.getUnlocked(p, s);			
		boolean canAfford = headCountUpgrade >= s.getUpgradeCost();			
		boolean canAffordCash = plugin.config.hasMoney(p, s.getPrice());
		boolean canAffordShards = currentShards >= shardCost;

		int firstEmpty = p.getInventory().firstEmpty();

		//Inventory full
		if(firstEmpty == -1 || firstEmpty >= 36) {
			p.sendMessage(ChatColor.RED+"Please make more room in your inventory.");
			return;
		}

		if(unlocked) {
			//Can't afford
			if(!canAffordCash) {
				p.sendMessage(ChatColor.RED+"You do not have enough money to purchase this.");
				return;
			}

			//Can Afford, Purchase
			purchaseSpawner(p, s);			
		} else {
			//Not unlocked

			//Has heads to unlock
			if(canAfford) {
				unlockSpawner(p, s);
			} else {
				if(canAffordShards) {
					unlockSpawnerWithShards(p, s, shardCost);
				} else {
					p.sendMessage(ChatColor.RED+"You do not have enough Castian Shards to purchase this.");
					return;
				}
			}
		}

	}

	private void unlockSpawnerWithShards(Player p, Spawner s, int amount) {
		//Take castian shards
		plugin.shards.takePlayerShards(p, amount);		

		//Unlock the spawner
		plugin.config.unlockSpawner(p, s.getType());
		p.sendMessage(ChatColor.WHITE+"You have unlocked "+ChatColor.YELLOW+s.getType().getName()+" "+ChatColor.WHITE+"Spawners!");
		this.openShop(p);
	}

	private void unlockSpawner(Player p, Spawner s) {
		//Take the money
		plugin.config.takeHeads(p, s.getUpgradeType(), s.getUpgradeCost());

		//Unlock
		plugin.config.unlockSpawner(p, s.getType());
		p.sendMessage(ChatColor.WHITE+"You have unlocked "+ChatColor.YELLOW+s.getType().getName()+" "+ChatColor.WHITE+"Spawners!");
		this.openShop(p);
	}

	private void purchaseSpawner(Player p, Spawner s) {
		//Take the money
		plugin.config.takeMoney(p, s.getPrice());
 
		//Add the spawner
		Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "spawner give "+ChatColor.stripColor(p.getName())+" "+s.getTypeString()+" 1");//treasures keys "+ChatColor.stripColor(p.getName())+" treasure add 1");

		//Send message and close Inventory
		p.sendMessage(ChatColor.WHITE+"You have purchased 1x "+ChatColor.YELLOW+s.getType().getName()+" "+ChatColor.WHITE+"Spawner!");
		this.openShop(p);
	}

	private Spawner getSpawnerForName(String name) {
		for(int i = 0; i < plugin.spawnerManager.spawners.length; i++) {
			if(name.toLowerCase().contains("spider") && name.toLowerCase().contains("cave") == false && plugin.spawnerManager.spawners[i].getType().getName().toLowerCase().contains("cave"))
				continue;
			if(name.toLowerCase().contains("spider") && name.toLowerCase().contains("cave") && plugin.spawnerManager.spawners[i].getType().getName().toLowerCase().contains("cave") == false)
				continue;
			if(name.toLowerCase().contains("pig") && name.toLowerCase().contains("pigmen") == false && plugin.spawnerManager.spawners[i].getType().getName().toLowerCase().contains("pigmen"))
				continue;
			if(name.toLowerCase().contains("pig") && name.toLowerCase().contains("pigmen") && plugin.spawnerManager.spawners[i].getType().getName().toLowerCase().contains("pigmen") == false)
				continue;
			if(name.toLowerCase().contains(plugin.spawnerManager.spawners[i].getType().getName().toLowerCase()))
				return plugin.spawnerManager.spawners[i];
		}
		return null;
	}

	private Spawner getSpawnerForHeadName(String name) {
		for(int i = 0; i < plugin.spawnerManager.spawners.length; i++) {
			String desiredHeadName = ChatColor.YELLOW+plugin.spawnerManager.spawners[i].getType().getName()+" "+ChatColor.WHITE+"Head";
			if(desiredHeadName.equalsIgnoreCase(name))
				return plugin.spawnerManager.spawners[i];
		}
		return null;
	}

	private Inventory getInventory(Player p) {

		Inventory inventory = Bukkit.createInventory(null, 54, ChatColor.GRAY+"Spawner Shop");

		for(int i = 0; i < 9; i++)
			Util.createDisplay(Material.STAINED_GLASS_PANE, inventory, 45+i, " ", null, 7);

		Util.createDisplay(Material.BARRIER, inventory, 49, ChatColor.RED+"Close Window", null, 0);	

		Util.createDisplay(Material.CAULDRON_ITEM, inventory, 51, ChatColor.YELLOW+"Deposit Heads", null, 0);
		Util.createDisplay(Material.CAULDRON_ITEM, inventory, 47, ChatColor.YELLOW+"Deposit Heads", null, 0);	
		//38

		for(int i = 0; i < plugin.spawnerManager.spawners.length; i++) {
			Spawner s = plugin.spawnerManager.spawners[i];

			int headCountUpgrade = plugin.config.getHeads(p, s.getUpgradeType());
			int shardCost = getShardCost(headCountUpgrade, s.getUpgradeCost(), s.getTotalShardCost());
			boolean unlocked = plugin.config.getUnlocked(p, s);			
			boolean canAfford = headCountUpgrade >= s.getUpgradeCost();			
			boolean canAffordCash = plugin.config.hasMoney(p, s.getPrice());


			if(headCountUpgrade > s.getUpgradeCost())
				headCountUpgrade = s.getUpgradeCost();


			ItemStack stack = Util.getSkull(UUID.randomUUID().toString(), s.getType().getHeadURL());
			ItemMeta meta = stack.getItemMeta();

			if(unlocked == false)
				meta.setDisplayName(ChatColor.RED+"[LOCKED] "+ChatColor.YELLOW+s.getType().getName()+" "+ChatColor.WHITE+"Spawner");
			else
				meta.setDisplayName(ChatColor.YELLOW+s.getType().getName()+" "+ChatColor.WHITE+"Spawner");

			List<String> lore = new ArrayList<String>();

			if(canAffordCash)
				lore.add(ChatColor.GRAY+"Price: "+ChatColor.GREEN+"$"+formatter.format(s.getPrice()));
			else
				lore.add(ChatColor.GRAY+"Price: "+ChatColor.RED+"$"+formatter.format(s.getPrice()));

			if(unlocked == false) {
				lore.add("");
				if(canAfford) {
					lore.add(ChatColor.GRAY+"Collect "+formatter.format(s.getUpgradeCost())+" "+s.getUpgradeType().getName()+" skulls");
					lore.add(ChatColor.GRAY+"to unlock the spawner!");
					lore.add(ChatColor.GRAY+"Progress: "+ChatColor.GREEN+formatter.format(headCountUpgrade)+ChatColor.GRAY+"/"+ChatColor.GREEN+formatter.format(s.getUpgradeCost()));
				} else {
					lore.add(ChatColor.GRAY+"Collect "+formatter.format(s.getUpgradeCost())+" "+s.getUpgradeType().getName()+" skulls");
					lore.add(ChatColor.GRAY+"to unlock the spawner!");
					lore.add(ChatColor.GRAY+"Progress: "+ChatColor.YELLOW+formatter.format(headCountUpgrade)+ChatColor.GRAY+"/"+ChatColor.YELLOW+formatter.format(s.getUpgradeCost()));
				}
			}

			lore.add("");
			if(unlocked == false) {
				if(canAfford)
					lore.add(ChatColor.GREEN+"Click to Unlock!");
				else {
					//lore.add(ChatColor.RED+"(Unable to Unlock)");
					lore.add(ChatColor.GRAY+"Unlock instantly with");
					lore.add(ChatColor.YELLOW+""+shardCost+""+ChatColor.DARK_AQUA+" Castian Shards!");
				}
			} else {
				if(canAffordCash)
					lore.add(ChatColor.GREEN+"Click to Purchase!");
				else
					lore.add(ChatColor.RED+"Unable to Afford");
			}

			meta.setLore(lore);
			stack.setItemMeta(meta);

			int slot = 10+i;

			if(i >= 7)
				slot+= 2;
			if(i >= 14)
				slot+= 2;
			if(i >= 21)
				slot+= 2;
			if(i >= 28)
				slot+= 2;

			inventory.setItem(slot, stack);
		}

		return inventory;
	}

	private Inventory getHeadInventory() {

		Inventory inventory = Bukkit.createInventory(null, 54, ChatColor.GRAY+"Deposit Heads");		
		return inventory;

	}

	public void openShop(Player p) {
		Inventory inventory = getInventory(p);
		p.openInventory(inventory);
	}

	private int getShardCost(int heads, int upgradeCost, int totalShardCost) {

		//Clean incoming head data
		if(heads > upgradeCost)
			heads = upgradeCost;

		if(heads < 0)
			heads = 0;

		double percent = 1.0d - (double) heads / upgradeCost;
		int count = (int) Math.ceil((double) totalShardCost * percent);

		return count >= 1 ? count : 1;
	}

}
