package SkyblockSpawners.SkyblockSpawners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.inventory.ItemStack;
import net.brcdev.shopgui.ShopGuiPlusApi;

public class SpawnerListener implements Listener {

	private SkyblockSpawners plugin;

	public SpawnerListener(SkyblockSpawners skyblockSpawners) {
		this.plugin = skyblockSpawners;
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		this.plugin.shop.onInventoryClick(event);
	}

	@EventHandler
	public void onInventoryCloseEvent(InventoryCloseEvent event) {
		this.plugin.shop.onInventoryCloseEvent(event);
	}

	@EventHandler (priority = EventPriority.HIGH)
	public void onEntityDeath(EntityDeathEvent event) {
		this.plugin.mobManager.onEntityDeath(event);
	}

	@EventHandler
	private void onBlockBreak(BlockBreakEvent event) {			
		this.plugin.mobManager.onBlockBreak(event);
	}

	//Intercept
	@EventHandler
	public void cmd(PlayerCommandPreprocessEvent e) {


		String message = e.getMessage();
		String[] args = message.substring(1).split(" ");
		String command = args[0].toLowerCase();

		switch(command) {

		case "sell":
			if(args.length > 1 && args[1].equalsIgnoreCase("all") && e.getPlayer().hasPermission("shopguiplus.sell.all")) {
				if(e.getPlayer() == null)
					break;
				
				boolean hasItems = false;
				ItemStack[] contents = e.getPlayer().getInventory().getContents();
				for(int i = 0; i < contents.length; i++) {
					if(i >= 36)
						break;
					double price = ShopGuiPlusApi.getItemStackPriceSell(e.getPlayer(), contents[i]);
					if(price < 0) 
						continue;
					
					hasItems = true;
				}
				
				boolean hasHead = plugin.shop.hasHead(e.getPlayer().getInventory(), true);
				//System.out.println("Player:"+e.getPlayer().getName()+" Has Head:"+hasHead);
				
				if(hasHead)
				plugin.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
					@Override
					public void run()
					{
						plugin.shop.depositHeads(e.getPlayer().getInventory(), e.getPlayer(), true);
					}
				}, 1);
				
				if(hasItems == false && hasHead)
					e.setCancelled(true);
				

			}
			break;
		}

	}
}
