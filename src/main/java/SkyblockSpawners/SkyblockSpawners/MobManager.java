package SkyblockSpawners.SkyblockSpawners;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;


public class MobManager {

	private SkyblockSpawners plugin;
	private Random r;
	
	public MobManager(SkyblockSpawners plugin) {
		this.plugin = plugin;
		r = new Random();
	}
	
	public void onEntityDeath(EntityDeathEvent event) {
		//Find mob spawner index of entity
		int index = -1;
		for(int i = 0; i < plugin.spawnerManager.spawners.length; i++) {
			Spawner s = plugin.spawnerManager.spawners[i];
			EntityType type = getEntityTypeForName(s.getType().toString());
			if(type == event.getEntityType() || s.getType() == SpawnerType.MOOSHROOM && event.getEntityType() == EntityType.MUSHROOM_COW || s.getType() == SpawnerType.ZOMBIE_PIGMAN && event.getEntityType() == EntityType.PIG_ZOMBIE) {
				index = i;
				break;
			}
		}
		
		//Not a spawner mob
		if(index == -1)
			return;
		
		Spawner s = plugin.spawnerManager.spawners[index];
		//event.getDrops().clear();
		
		float chance = r.nextFloat();

		
		if (chance <= s.getHeadChance())
			event.getDrops().add(getHeadForSpawner(s));
	}
	
	public void onBlockBreak(BlockBreakEvent event) {	
		Block b = event.getBlock();
		if(b.getType() != Material.SKULL)
			return;

		Skull skull = (Skull) b.getState();
		if(skull == null)
			return;
		
		if(skull.getOwningPlayer() == null)
			return;
		String owner = skull.getOwningPlayer().getUniqueId().toString();
		Spawner s = getSpawnerForUUID(owner);
		if(s == null)
			return;

		event.setCancelled(true);
		b.setType(Material.AIR);
		b.getLocation().getWorld().dropItem(b.getLocation(), getHeadForSpawner(s));
	}
	
	private Spawner getSpawnerForUUID(String uuid) {
		for(int i = 0; i < plugin.spawnerManager.spawners.length; i++) {
			if(plugin.spawnerManager.spawners[i].getType().getUUID().equals(uuid))
				return plugin.spawnerManager.spawners[i];
		}
		return null;
	}
	
	private Spawner getSpawnerForName(String name) {
		for(int i = 0; i < plugin.spawnerManager.spawners.length; i++) {
			if(plugin.spawnerManager.spawners[i].getType().getName().equalsIgnoreCase(name.replaceAll("_", " ")))
				return plugin.spawnerManager.spawners[i];
		}
		return null;
	}
	
	public ItemStack getHeadForSpawner(Spawner s) {
		ItemStack itemStack = Util.getSkull(s.getType().getUUID(), s.getType().getHeadURL());
		ItemMeta meta = itemStack.getItemMeta();
		
		meta.setDisplayName(ChatColor.YELLOW+s.getType().getName()+" "+ChatColor.WHITE+"Head");
		List<String> lore = new ArrayList<String>();
		lore.add(ChatColor.DARK_GRAY+"Turn this in at the");
		lore.add(ChatColor.DARK_GRAY+"mob spawner shop!");
		meta.setLore(lore);
		itemStack.setItemMeta(meta);
		
		return itemStack;
	}
	
	private EntityType getEntityTypeForName(String name) {
		for(EntityType type : EntityType.values()) {
			if(type.toString().equals(name))
				return type;
		}
		return null;
	}

	public void giveHead(Player p, String name, int amount) {
		Spawner s = getSpawnerForName(name);
		
		if(amount <= 0) {		
			p.sendMessage(ChatColor.RED+"Amount must be greater than 0.");
			return;
		}
		
		if(s == null) {
			String mobStringList = "";
			for(int i = 0; i < plugin.spawnerManager.spawners.length; i++) {
				mobStringList = mobStringList + plugin.spawnerManager.spawners[i].getType().getName().replaceAll(" ", "_").toLowerCase()+", ";
			}
			p.sendMessage(ChatColor.RED+"Invalid mob type. Mob Types: "+mobStringList.substring(0, mobStringList.length()-2));
			return;
		}
		
		int firstEmpty = p.getInventory().firstEmpty();
		if(firstEmpty == -1 || firstEmpty >= 36) {
			p.sendMessage(ChatColor.RED+"Please make more room in your inventory.");
			return;
		}
		ItemStack stackRef = getHeadForSpawner(s);
		if(amount <= 64) {
			ItemStack stack = stackRef.clone();
			stack.setAmount(amount);
			p.getInventory().addItem(stack);
		} else {
			int left = amount;
			for(int i = 0; i < amount; i+=64) {
				int currentIterationAmount = left;
				if(currentIterationAmount > 64)
					currentIterationAmount = 64;
				ItemStack stack = stackRef.clone();
				stack.setAmount(currentIterationAmount);
				p.getInventory().addItem(stack);
				left -= currentIterationAmount;
			}
		}
	}
	
}
