package org.inventivetalent.menubuilder.chat.component;

import net.md_5.bungee.api.chat.TextComponent;
import org.inventivetalent.menubuilder.chat.LineBuilder;

/**
 * Core class for MenuComponents
 */
public abstract class MenuComponent {

	TextComponent component = new TextComponent();

	public MenuComponent() {
	}

	protected void updateText() {
		component.setText(render());
	}

	/**
	 * Renders the Component
	 */
	public String render() {
		return "";
	}

	/**
	 * Append the component to a {@link LineBuilder}
	 */
	public abstract MenuComponent appendTo(LineBuilder builder);

}
