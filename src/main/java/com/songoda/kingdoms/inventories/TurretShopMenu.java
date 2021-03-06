package com.songoda.kingdoms.inventories;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.PlayerInventory;

import com.songoda.kingdoms.inventories.structures.NexusInventory;
import com.songoda.kingdoms.manager.inventories.InventoryManager;
import com.songoda.kingdoms.manager.inventories.PagesInventory;
import com.songoda.kingdoms.manager.managers.TurretManager;
import com.songoda.kingdoms.objects.kingdom.Kingdom;
import com.songoda.kingdoms.objects.player.KingdomPlayer;
import com.songoda.kingdoms.objects.turrets.TurretType;
import com.songoda.kingdoms.utils.MessageBuilder;

public class TurretShopMenu extends PagesInventory {

	public TurretShopMenu() {
		super("turret-shop", 18);
	}

	@Override
	protected List<PageItem> getItems(KingdomPlayer kingdomPlayer) {
		List<PageItem> items = new ArrayList<>();
		Kingdom kingdom = kingdomPlayer.getKingdom();
		if (kingdom == null)
			return items;
		Player player = kingdomPlayer.getPlayer();
		for (TurretType type : instance.getManager(TurretManager.class).getTypes()) {
			if (!type.isEnabled())
				continue;
			items.add(new PageItem(type.build(kingdom, true), event -> {
				long cost = type.getCost();
				if (kingdom.getResourcePoints() < cost) {
					new MessageBuilder("turrets.cannot-afford")
							.replace("%turret%", type.getTitle())
							.setPlaceholderObject(kingdomPlayer)
							.replace("%cost%", cost)
							.setKingdom(kingdom)
							.send(player);
					return;
				}
				PlayerInventory playersInventory = player.getInventory();
				if (playersInventory.firstEmpty() == -1) {
					new MessageBuilder("turrets.inventory-full-purchase")
							.replace("%turret%", type.getTitle())
							.setPlaceholderObject(kingdomPlayer)
							.replace("%cost%", cost)
							.setKingdom(kingdom)
							.send(player);
					return;
				}
				new MessageBuilder("turrets.purchase")
						.replace("%turret%", type.getTitle())
						.setPlaceholderObject(kingdomPlayer)
						.replace("%cost%", cost)
						.setKingdom(kingdom)
						.send(player);
				kingdom.subtractResourcePoints(cost);
				playersInventory.addItem(type.build(kingdom, false));
			}));
		}
		return items;
	}

	@Override
	protected Consumer<InventoryClickEvent> getBackAction(KingdomPlayer kingdomPlayer) {
		return event -> instance.getManager(InventoryManager.class).getInventory(NexusInventory.class).open(kingdomPlayer);
	}

}
