package org.inventivetalent.menubuilder.inventory;

import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.inventivetalent.menubuilder.MenuBuilder;
import org.inventivetalent.menubuilder.MenuBuilderPlugin;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

import static org.bukkit.event.inventory.ClickType.*;

public class InventoryMenuBuilder extends MenuBuilder<Inventory> {

	public static final ClickType[] ALL_CLICK_TYPES = new ClickType[] {
			LEFT,
			SHIFT_LEFT,
			RIGHT,
			SHIFT_RIGHT,
			WINDOW_BORDER_LEFT,
			WINDOW_BORDER_RIGHT,
			MIDDLE,
			NUMBER_KEY,
			DOUBLE_CLICK,
			DROP,
			CONTROL_DROP };

	private Inventory inventory;

	private List<ItemCallback> callbackItems = new ArrayList<>();

	/**
	 * Construct a new InventoryMenuBuilder without content
	 */
	public InventoryMenuBuilder() {
	}

	/**
	 * Construct a new InventoryMenuBuilder with the specified size
	 *
	 * @param size Size of the inventory
	 */
	public InventoryMenuBuilder(int size) {
		this();
		withSize(size);
	}

	/**
	 * Construct a new InventoryMenuBuilder with the specified size and title
	 *
	 * @param size  Size of the inventory
	 * @param title Title of the inventory
	 */
	public InventoryMenuBuilder(int size, String title) {
		this(size);
		withTitle(title);
	}

	/**
	 * Construct a new InventoryMenuBuilder with the specified {@link InventoryType}
	 *
	 * @param type {@link InventoryType}
	 */
	public InventoryMenuBuilder(InventoryType type) {
		this();
		withType(type);
	}

	/**
	 * Construct a new InventoryMenuBuilder with the specified {@link InventoryType} and title
	 *
	 * @param type  {@link InventoryType}
	 * @param title Title of the inventory
	 */
	public InventoryMenuBuilder(InventoryType type, String title) {
		this(type);
		withTitle(title);
	}

	protected void initInventory(Inventory inventory) {
		if (this.inventory != null) { throw new IllegalStateException("Inventory already initialized"); }
		this.inventory = inventory;
	}

	protected void validateInit() {
		if (this.inventory == null) { throw new IllegalStateException("inventory not yet initialized"); }
	}

	/**
	 * @return The {@link Inventory} being built
	 */
	public Inventory getInventory() {
		return inventory;
	}

	/**
	 * Sets the initial size
	 *
	 * @param size Size of the inventory
	 * @return the InventoryMenuBuilder
	 */
	public InventoryMenuBuilder withSize(int size) {
		initInventory(Bukkit.createInventory(null, size));
		return this;
	}

	/**
	 * Sets the initial type
	 *
	 * @param type {@link InventoryType}
	 * @return the InventoryMenuBuilder
	 */
	public InventoryMenuBuilder withType(InventoryType type) {
		initInventory(Bukkit.createInventory(null, type));
		return this;
	}

	/**
	 * Change the title of the inventory and update it to all viewers
	 *
	 * @param title new title of the inventory
	 * @return the InventoryMenuBuilder
	 */
	public InventoryMenuBuilder withTitle(@Nonnull String title) {
		return withTitle(title, true);
	}

	/**
	 * Change the title of the inventory and <i>optionally</i> update it to all viewers
	 *
	 * @param title   new title of the inventory
	 * @param refresh if <code>true</code>, the inventory will be re-opened to all viewers
	 * @return the InventoryMenuBuilder
	 */
	public InventoryMenuBuilder withTitle(@Nonnull String title, boolean refresh) {
		validateInit();
		InventoryHelper.changeTitle(this.inventory, title);

		if (refresh) {
			for (HumanEntity viewer : this.inventory.getViewers()) {
				viewer.closeInventory();
				viewer.openInventory(this.inventory);
			}
		}
		return this;
	}

	/**
	 * Add an <i>optional</i> {@link InventoryEventHandler} to further customize the click-behaviour
	 *
	 * @param eventHandler {@link InventoryEventHandler} to add
	 * @return the InventoryMenuBuilder
	 */
	public InventoryMenuBuilder withEventHandler(@Nonnull InventoryEventHandler eventHandler) {
		try {
			MenuBuilderPlugin.instance.inventoryListener.registerEventHandler(this, eventHandler);
		} catch (IllegalArgumentException e) {
			throw e;
		}
		return this;
	}

	/**
	 * Add a {@link InventoryMenuListener} for the specified {@link ClickType}s
	 *
	 * @param listener the {@link InventoryMenuListener} to add
	 * @param actions  the {@link ClickType}s the listener should listen for (you can also use {@link #ALL_CLICK_TYPES} or {@link ClickType#values()}
	 * @return the InventoryMenuBuilder
	 */
	public InventoryMenuBuilder onInteract(@Nonnull InventoryMenuListener listener, @Nonnull ClickType... actions) {
		if (actions == null || (actions != null && actions.length == 0)) {
			throw new IllegalArgumentException("must specify at least one action");
		}
		try {
			MenuBuilderPlugin.instance.inventoryListener.registerListener(this, listener, actions);
		} catch (IllegalArgumentException e) {
			throw e;
		}
		return this;
	}

	/**
	 * Set the item for the specified slot
	 *
	 * @param slot Slot of the item
	 * @param item {@link ItemStack} to set
	 * @return the InventoryMenuBuilder
	 */
	public InventoryMenuBuilder withItem(@Nonnegative int slot, @Nonnull ItemStack item) {
		validateInit();
		this.inventory.setItem(slot, item);
		return this;
	}

	/**
	 * Set the item for the specified slot and add a {@link ItemListener} for it
	 *
	 * @param slot     Slot of the item
	 * @param item     {@link ItemStack} to set
	 * @param listener {@link ItemListener} for the item
	 * @param actions  the {@link ClickType}s the listener should listen for (you can also use {@link #ALL_CLICK_TYPES} or {@link ClickType#values()}
	 * @return the InventoryMenuBuilder
	 */
	public InventoryMenuBuilder withItem(@Nonnegative final int slot, @Nonnull final ItemStack item, @Nonnull final ItemListener listener, @Nonnull ClickType... actions) {
		withItem(slot, item);
		onInteract(new InventoryMenuListener() {
			@Override
			public void interact(Player player, ClickType action, int slot_) {
				if (slot_ == slot) { listener.onInteract(player, action, item); }
			}
		}, actions);
		return this;
	}

	/**
	 * Add an item using a {@link ItemCallback}
	 * The callback will be called when {@link #show(HumanEntity...)} or {@link #refreshContent()} is called
	 *
	 * @param callback {@link ItemCallback}
	 * @return the InventoryMenuBuilder
	 */
	public InventoryMenuBuilder withItem(@Nonnull ItemCallback callback) {
		callbackItems.add(callback);
		return this;
	}

	/**
	 * Builds the {@link Inventory}
	 *
	 * @return a {@link Inventory}
	 */
	public Inventory build() {
		return this.inventory;
	}

	/**
	 * Shows the inventory to the viewers
	 *
	 * @param viewers Array of {@link HumanEntity}
	 * @return the InventoryMenuBuilder
	 */
	public InventoryMenuBuilder show(HumanEntity... viewers) {
		refreshContent();
		for (HumanEntity viewer : viewers) {
			viewer.openInventory(this.build());
		}
		return this;
	}

	/**
	 * Refresh the content of the inventory
	 * Will call all {@link ItemCallback}s registered with {@link #withItem(ItemCallback)}
	 *
	 * @return the InventoryMenuBuilder
	 */
	public InventoryMenuBuilder refreshContent() {
		for (ItemCallback callback : callbackItems) {
			int slot = callback.getSlot();
			ItemStack item = callback.getItem();

			withItem(slot, item);
		}
		return this;
	}

	@Override
	public void dispose() {
		MenuBuilderPlugin.instance.inventoryListener.unregisterAllListeners(getInventory());
	}

	public void unregisterListener(InventoryMenuListener listener) {
		try {
			MenuBuilderPlugin.instance.inventoryListener.registerListener(this, listener, ALL_CLICK_TYPES);
		} catch (IllegalArgumentException e) {
			throw e;
		}
	}
}
