package com.songoda.kingdoms.manager.inventories;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

import com.songoda.kingdoms.Kingdoms;
import com.songoda.kingdoms.objects.player.KingdomPlayer;

public abstract class KingdomInventory {

	protected final Map<Integer, Consumer<InventoryClickEvent>> actions = new HashMap<>();
	protected final FileConfiguration configuration, inventories;
	protected final InventoryManager inventoryManager;
	protected final Inventory inventory;
	protected final Kingdoms instance;

	public KingdomInventory(InventoryType type, String name, int size) {
		this.instance = Kingdoms.getInstance();
		this.configuration = instance.getConfig();
		this.inventories = instance.getConfiguration("inventories").get();
		this.inventoryManager = instance.getManager("inventory", InventoryManager.class);
		if (type == InventoryType.CHEST)
			this.inventory = instance.getServer().createInventory(null, size, name);
		else
			this.inventory = instance.getServer().createInventory(null, type, name);
	}

	public abstract void build(KingdomPlayer kingdomPlayer);

	public Inventory getInventory() {
		return inventory;
	}

	public void setAction(int slot, Consumer<InventoryClickEvent> consummer) {
		actions.put(slot, consummer);
	}

	protected void openInventory(Player player) {
		player.openInventory(inventory);
		inventoryManager.opening(player.getUniqueId(), this);
	}

	protected void reopen(KingdomPlayer kingdomPlayer) {
		inventory.clear();
		build(kingdomPlayer);
	}

	public Optional<Consumer<InventoryClickEvent>> getAction(int slot) {
		return Optional.ofNullable(actions.get(slot));
	}

}
