package com.songoda.kingdoms.objects.player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import com.songoda.kingdoms.Kingdoms;
import com.songoda.kingdoms.manager.managers.KingdomManager;
import com.songoda.kingdoms.manager.managers.PlayerManager;
import com.songoda.kingdoms.manager.managers.RankManager;
import com.songoda.kingdoms.manager.managers.RankManager.Rank;
import com.songoda.kingdoms.objects.kingdom.Kingdom;
import com.songoda.kingdoms.objects.land.Land;

public class OfflineKingdomPlayer {

	private final Set<Land> claims = new HashSet<>(); // Kingdom claims that this user has claimed.
	protected final KingdomManager kingdomManager;
	protected final PlayerManager playerManager;
	protected transient Kingdom kingdom;
	protected final Kingdoms instance;
	protected final String name;
	protected final UUID uuid;
	protected boolean marker;
	protected Rank rank;
	
	public OfflineKingdomPlayer(UUID uuid) {
		this(Bukkit.getOfflinePlayer(uuid));
	}
	
	public OfflineKingdomPlayer(OfflinePlayer player) {
		this.name = player.getName();
		this.uuid = player.getUniqueId();
		this.instance = Kingdoms.getInstance();
		this.playerManager = instance.getManager("player", PlayerManager.class);
		this.kingdomManager = instance.getManager("kingdom", KingdomManager.class);
		this.rank = instance.getManager("rank", RankManager.class).getDefaultRank();
	}
	
	public UUID getUniqueId() {
		return uuid;
	}
	
	public String getName() {
		return name;
	}
	
	public Rank getRank() {
		return rank;
	}
	
	public void setRank(Rank rank) {
		this.rank = rank;
	}
	
	public Kingdom getKingdom() {
		return kingdom;
	}
	
	public boolean isOnline() {
		return Bukkit.getPlayer(uuid) != null;
	}
	
	public void setKingdom(Kingdom kingdom) {
		this.kingdom = kingdomManager.getKingdom(kingdom);
	}
	
	public KingdomPlayer getKingdomPlayer() {
		return playerManager.getKingdomPlayer(uuid);
	}
	
	public Set<Land> getClaims() {
		return claims;
	}
	
	public void addClaim(Land land) {
		claims.add(land);
	}
	
	private void resetClaims() {
		claims.clear();
	}
	
	public void onKingdomLeave() {
		resetClaims();
		kingdom = null;
		rank = null;
	}

}
