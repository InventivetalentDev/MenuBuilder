package org.inventivetalent.menubuilder.chat;

import org.bukkit.entity.Player;

/**
 * Chat interaction listener
 */
public interface ChatListener {

	/**
	 * Called when a player clicks the chat component
	 *
	 * @param player {@link Player} who clicked
	 */
	void onClick(Player player);

}
