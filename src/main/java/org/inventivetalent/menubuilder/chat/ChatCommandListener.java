package org.inventivetalent.menubuilder.chat;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.inventivetalent.menubuilder.MenuBuilderPlugin;

import java.util.*;

public class ChatCommandListener implements CommandExecutor {

	private MenuBuilderPlugin plugin;

	private Map<String, List<ChatListener>> listenerMap = new HashMap<>();

	public ChatCommandListener(MenuBuilderPlugin plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
		if ("mbchat".equals(command.getName())) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				if (args.length > 0) {
					String key = args[0];
					if (listenerMap.containsKey(key)) {
						List<ChatListener> listeners = listenerMap.get(key);

						for (ChatListener listener : listeners) {
							listener.onClick(player);
						}
					} else {
						plugin.getLogger().warning(sender.getName() + " tried to run click-command for an unknown listener");
					}
				}
			}
		}
		return false;
	}

	public void registerListener(ChatListener listener, String key) {
		List<ChatListener> list = this.listenerMap.get(key);
		if (list == null) { list = new ArrayList<>(); }
		list.add(listener);
		listenerMap.put(key, list);
	}

	public void registerListener(ChatListener listener, UUID key) {
		registerListener(listener, key.toString().replace("-", ""));
	}

	public String registerListener(ChatListener listener) {
		for (Map.Entry<String, List<ChatListener>> entry : listenerMap.entrySet()) {
			for (ChatListener listener1 : entry.getValue()) {
				if (listener1.equals(listener)) {
					return entry.getKey();
				}
			}
		}

		UUID uuid = UUID.randomUUID();
		String key = uuid.toString().replace("-", "");
		registerListener(listener, key);
		return key;
	}

	public void unregisterListener(String key) {
		this.listenerMap.remove(key);
	}
}
