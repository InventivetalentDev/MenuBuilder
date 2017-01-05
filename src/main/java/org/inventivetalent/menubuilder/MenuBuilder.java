package org.inventivetalent.menubuilder;

import org.bukkit.entity.HumanEntity;

/**
 * Core MenuBuilder class
 * Use {@link org.inventivetalent.menubuilder.inventory.InventoryMenuBuilder} or {@link org.inventivetalent.menubuilder.chat.ChatMenuBuilder}
 *
 * @param <T> Type of the builder
 * @see org.inventivetalent.menubuilder.inventory.InventoryMenuBuilder
 * @see org.inventivetalent.menubuilder.chat.ChatMenuBuilder
 */
public abstract class MenuBuilder<T> {

	public MenuBuilder() {
	}

	/**
	 * Shows the Menu to the viewers
	 */
	public abstract MenuBuilder show(HumanEntity... viewers);

	/**
	 * Refreshes the content of the menu
	 */
	public abstract MenuBuilder refreshContent();

	/**
	 * Builds the menu
	 */
	public abstract <T> T build();

	public abstract void dispose();

}
