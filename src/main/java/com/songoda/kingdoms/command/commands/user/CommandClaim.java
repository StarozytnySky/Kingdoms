package com.songoda.kingdoms.command.commands.user;

import com.songoda.kingdoms.Kingdoms;
import com.songoda.kingdoms.command.AbstractCommand;
import com.songoda.kingdoms.objects.player.KingdomPlayer;
import com.songoda.kingdoms.utils.MessageBuilder;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandClaim extends AbstractCommand {

	static {
		registerCommand(new CommandClaim());
	}

	protected CommandClaim() {
		super("claim", false);
	}

	@Override
	protected ReturnType runCommand(Kingdoms instance, CommandSender sender, String... arguments) {
		Player player = (Player) sender;
		KingdomPlayer kingdomPlayer = playerManager.getKingdomPlayer(player);
		landManager.playerClaimLand(kingdomPlayer);
		if (arguments.length > 0 && arguments[0].equalsIgnoreCase("auto") || arguments[0].equalsIgnoreCase("automatic")) {
			kingdomPlayer.setAutoClaiming(!kingdomPlayer.isAutoClaiming());
			if (kingdomPlayer.isAutoClaiming()) {
				new MessageBuilder("commands.claim.auto-claim-on")
						.setPlaceholderObject(kingdomPlayer)
						.send();
			} else {
				new MessageBuilder("commands.claim.auto-claim-off")
						.setPlaceholderObject(kingdomPlayer)
						.send();
			}
		}
		return ReturnType.SUCCESS;
	}

	@Override
	protected String getConfigurationNode() {
		return "claim";
	}

	@Override
	public String getPermissionNode() {
		return "kingdoms.claim";
	}

}