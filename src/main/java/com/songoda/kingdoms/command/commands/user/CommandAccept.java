package com.songoda.kingdoms.command.commands.user;

import java.util.Optional;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.songoda.kingdoms.Kingdoms;
import com.songoda.kingdoms.command.AbstractCommand;
import com.songoda.kingdoms.manager.managers.InviteManager;
import com.songoda.kingdoms.manager.managers.InviteManager.PlayerInvite;
import com.songoda.kingdoms.manager.managers.PlayerManager;
import com.songoda.kingdoms.manager.managers.RankManager;
import com.songoda.kingdoms.objects.kingdom.Kingdom;
import com.songoda.kingdoms.objects.player.KingdomPlayer;
import com.songoda.kingdoms.utils.MessageBuilder;

public class CommandAccept extends AbstractCommand {

	public CommandAccept() {
		super(false, "accept");
	}

	@Override
	protected ReturnType runCommand(Kingdoms instance, CommandSender sender, String... arguments) {
		Player player = (Player) sender;
		KingdomPlayer kingdomPlayer = instance.getManager(PlayerManager.class).getKingdomPlayer(player);
		Optional<PlayerInvite> invite = instance.getManager(InviteManager.class).getInvite(kingdomPlayer);
		if (!invite.isPresent()) {
			new MessageBuilder("commands.accept.no-invite")
					.setPlaceholderObject(kingdomPlayer)
					.send(player);
			return ReturnType.FAILURE;
		}
		Kingdom kingdom = invite.get().getKingdom();
		RankManager rankManager = instance.getManager(RankManager.class);
		kingdomPlayer.setRank(rankManager.getDefaultRank());
		kingdomPlayer.setKingdom(kingdom.getName());
		kingdom.addMember(kingdomPlayer);
		return ReturnType.SUCCESS;
	}

	@Override
	public String getConfigurationNode() {
		return null;
	}

	@Override
	public String getPermissionNode() {
		return null;
	}

}