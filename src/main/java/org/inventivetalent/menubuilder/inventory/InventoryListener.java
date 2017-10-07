package org.inventivetalent.menubuilder.inventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.inventivetalent.menubuilder.MenuBuilderPlugin;

public class InventoryListener implements Listener {

	MenuBuilderPlugin plugin;

	private final Map<Inventory, Map<ClickType, List<InventoryMenuListener>>> listenerMap     = new HashMap<>();
	private final Map<Inventory, List<InventoryEventHandler>>                 eventHandlerMap = new HashMap<>();

	public InventoryListener(MenuBuilderPlugin plugin) {
		this.plugin = plugin;
	}

	public void registerListener(InventoryMenuBuilder builder, InventoryMenuListener listener, ClickType[] actions) {
		Map<ClickType, List<InventoryMenuListener>> map = listenerMap.get(builder.getInventory());
		if (map == null) { map = new HashMap<>(); }
		for (ClickType action : actions) {
			List<InventoryMenuListener> list = map.get(action);
			if (list == null) { list = new ArrayList<>(); }
			if (list.contains(listener)) { throw new IllegalArgumentException("listener already registered"); }
			list.add(listener);

			map.put(action, list);
		}

		listenerMap.put(builder.getInventory(), map);
	}

	public void unregisterListener(InventoryMenuBuilder builder, InventoryMenuListener listener, ClickType[] actions) {
		Map<ClickType, List<InventoryMenuListener>> map = listenerMap.get(builder.getInventory());
		if (map == null) { return; }
		for (ClickType action : actions) {
			List<InventoryMenuListener> list = map.get(action);
			if (list == null) {continue; }
			list.remove(listener);
		}
	}

	public void unregisterAllListeners(Inventory inventory) {
		listenerMap.remove(inventory);
	}

	public void registerEventHandler(InventoryMenuBuilder builder, InventoryEventHandler eventHandler) {
		List<InventoryEventHandler> list = eventHandlerMap.get(builder.getInventory());
		if (list == null) { list = new ArrayList<>(); }
		if (!list.contains(eventHandler)) { list.add(eventHandler); }

		eventHandlerMap.put(builder.getInventory(), list);
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onInventoryClick(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		Inventory inventory = event.getClickedInventory();
		ClickType type = event.getClick();
                
                Inventory playerOpenInventory = player.getOpenInventory().getTopInventory();
                
                if(listenerMap.containsKey(playerOpenInventory) ||eventHandlerMap.containsKey(playerOpenInventory)){
                    event.setCancelled(true);
                    event.setResult(Result.DENY);
                    return;
                }

		if (listenerMap.containsKey(inventory)) {
			event.setCancelled(true);
			event.setResult(Result.DENY);

			Map<ClickType, List<InventoryMenuListener>> actionMap = listenerMap.get(inventory);
			if (actionMap.containsKey(type)) {
				List<InventoryMenuListener> listeners = actionMap.get(type);

				for (InventoryMenuListener listener : listeners) {
					try {
						listener.interact(player, type, event.getSlot());
					} catch (Throwable throwable) {
						throwable.printStackTrace();
					}
				}
			}
		}
		if (eventHandlerMap.containsKey(inventory)) {
			List<InventoryEventHandler> list = eventHandlerMap.get(inventory);

			for (InventoryEventHandler handler : list) {
				try {
					handler.handle(event);
				} catch (Throwable throwable) {
					throwable.printStackTrace();
				}
			}
		}
	}

}
