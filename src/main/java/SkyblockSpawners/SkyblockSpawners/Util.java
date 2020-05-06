package SkyblockSpawners.SkyblockSpawners;

import java.util.List;
import java.util.UUID;

import org.apache.commons.codec.binary.Base64;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

public class Util {
	
	public static ItemStack getSkull(String owner, String url) {
		ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
		if(url.isEmpty())return head;


		SkullMeta headMeta = (SkullMeta) head.getItemMeta();
		GameProfile profile = new GameProfile(UUID.fromString(owner), null);
		byte[] encodedData = Base64.encodeBase64(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes());
		profile.getProperties().put("textures", new Property("textures", new String(encodedData)));
		java.lang.reflect.Field profileField = null;
		try
		{
			profileField = headMeta.getClass().getDeclaredField("profile");
			profileField.setAccessible(true);
			profileField.set(headMeta, profile);
		}
		catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e)
		{
			e.printStackTrace();
		}
		head.setItemMeta(headMeta);
		return head;
	}
	
	//Used to place an item on UI
	public static void createDisplay(Material material, Inventory inv, int Slot, String name, List<String> lore, int byteId) {
		ItemStack item = new ItemStack(material, 1, (byte) byteId);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		if (lore != null)
			meta.setLore(lore);
		item.setItemMeta(meta);

		inv.setItem(Slot, item);

	}
	
	
}
