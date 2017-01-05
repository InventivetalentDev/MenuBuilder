package org.inventivetalent.menubuilder;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.inventivetalent.menubuilder.chat.ChatCommandListener;
import org.inventivetalent.menubuilder.inventory.InventoryListener;

public class MenuBuilderPlugin extends JavaPlugin {

	public static MenuBuilderPlugin instance;

	public InventoryListener   inventoryListener;
	public ChatCommandListener chatCommandListener;

	@Override
	public void onEnable() {
		instance = this;

		Bukkit.getPluginManager().registerEvents(inventoryListener = new InventoryListener(this), this);
		getCommand("mbchat").setExecutor(chatCommandListener = new ChatCommandListener(this));
	}
}
