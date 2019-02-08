package com.songoda.kingdoms.manager.managers;

import com.songoda.kingdoms.manager.Manager;
import com.songoda.kingdoms.utils.Formatting;
import com.songoda.kingdoms.utils.Utils;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

import org.bukkit.Server.Spigot;
import org.bukkit.entity.Player;

public class ActionbarManager extends Manager {

	static {
		registerManager("actionbar", new ActionbarManager());
	}
	
	// Caching
	private final boolean classes, method;
	
	protected ActionbarManager() {
		super(false);
		this.classes = Utils.classExists("net.md_5.bungee.api.ChatMessageType") && Utils.classExists("net.md_5.bungee.api.chat.TextComponent");
		if (!classes) {
			method = false;
			return;
		}
		this.method = Utils.methodExists(Spigot.class, "sendMessage", ChatMessageType.class, TextComponent.class);
	}
	
	public void sendActionBar(Player player, String... messages) {
		if (classes && method) {
			for (String message : messages) {
				player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(Formatting.color(message)));
			}
		}
	}

	@Override
	public void onDisable() {}

}
