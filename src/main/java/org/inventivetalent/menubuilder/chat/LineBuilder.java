package org.inventivetalent.menubuilder.chat;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.inventivetalent.menubuilder.MenuBuilderPlugin;
import org.inventivetalent.menubuilder.chat.component.MenuComponent;

import java.util.ArrayList;
import java.util.List;

public class LineBuilder {

	BaseComponent base;
	protected ChatMenuBuilder container;
	protected List<String> listenerKeys = new ArrayList<>();

	/**
	 * Construct a new empty LineBuilder
	 */
	public LineBuilder() {
		this("");
	}

	/**
	 * Construct a new LineBuilder with the specified text
	 *
	 * @param text
	 */
	public LineBuilder(String text) {
		base = new TextComponent(text);
	}

	/**
	 * Append text
	 *
	 * @param text {@link String} to append
	 * @return the LineBuilder
	 */
	public LineBuilder append(String... text) {
		for (String s : text) {
			base.addExtra(s);
		}
		return this;
	}

	/**
	 * Append {@link BaseComponent}s
	 *
	 * @param components {@link BaseComponent}s to append
	 * @return the LineBuilder
	 */
	public LineBuilder append(BaseComponent... components) {
		for (BaseComponent component : components) {
			base.addExtra(component);
		}
		return this;
	}

	/**
	 * Append a {@link MenuComponent} (for example a {@link org.inventivetalent.menubuilder.chat.component.MenuComponentCheckbox})
	 *
	 * @param component {@link MenuComponent} to append
	 * @return the LineBuilder
	 */
	public LineBuilder append(MenuComponent component) {
		component.appendTo(this);
		return this;
	}

	/**
	 * Append {@link BaseComponent}s with a {@link ChatListener}
	 *
	 * @param listener   {@link ChatListener}
	 * @param components {@link BaseComponent}s to append
	 * @return
	 */
	public LineBuilder append(ChatListener listener, BaseComponent... components) {
		for (BaseComponent component : components) {
			String key = MenuBuilderPlugin.instance.chatCommandListener.registerListener(listener);
			component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/mbchat " + key));
			listenerKeys.add(key);

			base.addExtra(component);
		}
		return this;
	}

	protected LineBuilder withContainer(ChatMenuBuilder container) {
		this.container = container;
		return this;
	}

	/**
	 * Internal method to access the {@link ChatMenuBuilder}
	 */
	public ChatMenuBuilder getContainer() {
		return this.container;
	}

	/**
	 * Builds the line
	 *
	 * @return a {@link BaseComponent}
	 */
	public BaseComponent build() {
		return this.base;
	}

}
