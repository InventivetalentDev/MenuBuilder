package org.inventivetalent.menubuilder;

import org.bukkit.entity.Player;

/**
 * Listener for value changes
 *
 * @param <T> Type of the value
 */
public interface ValueListener<T> {

	/**
	 * Called when the value changes
	 *
	 * @param player   {@link Player} who caused the value to change
	 * @param oldValue The old value
	 * @param newValue The new value
	 */
	void onChange(Player player, T oldValue, T newValue);

}
