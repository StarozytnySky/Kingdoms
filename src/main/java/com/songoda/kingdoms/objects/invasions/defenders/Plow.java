package com.songoda.kingdoms.objects.invasions.defenders;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.songoda.kingdoms.Kingdoms;
import com.songoda.kingdoms.events.DefenderDamageByPlayerEvent;
import com.songoda.kingdoms.events.DefenderDamagePlayerEvent;
import com.songoda.kingdoms.events.DefenderPlowEvent;
import com.songoda.kingdoms.objects.DefenderAbility;
import com.songoda.kingdoms.objects.invasions.Invasion;
import com.songoda.kingdoms.objects.kingdom.DefenderInfo;
import com.songoda.kingdoms.objects.player.KingdomPlayer;
import com.songoda.kingdoms.utils.Utils;

public class Plow extends DefenderAbility {

	public Plow() {
		super(false, "plow");
	}

	@Override
	public void tick(LivingEntity defender, DefenderInfo infom, Invasion invasion) {
		KingdomPlayer instigator = invasion.getInstigator();
		Player player = instigator.getPlayer();
		if (player.isDead() || !player.isOnline())
			return;
		Location location = defender.getLocation();
		int radius = 1;
		for (int x = -radius; x <= radius; x++) {
			for (int y = -radius; y <= radius; y++) {
				for (int z = -radius; z <= radius; z++) {
					Block block = location.getBlock().getRelative(x, y, z);
					Material type = block.getType();
					if (type != Utils.materialAttempt("COBWEB", "WEB") && type != Material.LAVA)
						continue;
					DefenderPlowEvent plowEvent = new DefenderPlowEvent(defender, invasion, block);
					if (!plowEvent.isCancelled()) {
						location.getBlock().setType(Material.AIR);
						defender.setFireTicks(0); 
					}
				}
			}
		}
	}

	@Override
	public boolean initialize(Kingdoms instance) {
		return true;
	}

	@Override
	public void onDamaging(DefenderDamagePlayerEvent event) {}

	@Override
	public void onDamage(DefenderDamageByPlayerEvent event) {}

	@Override
	public void onDeath(LivingEntity defender) {}

	@Override
	public void onAdd(LivingEntity defender) {}

}
