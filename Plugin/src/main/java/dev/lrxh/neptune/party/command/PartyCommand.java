package dev.lrxh.neptune.party.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import dev.lrxh.neptune.Neptune;
import dev.lrxh.neptune.configs.impl.MessagesLocale;
import dev.lrxh.neptune.party.Party;
import dev.lrxh.neptune.party.impl.PartyRequest;
import dev.lrxh.neptune.profile.data.ProfileState;
import dev.lrxh.neptune.profile.impl.Profile;
import dev.lrxh.neptune.providers.clickable.Replacement;
import dev.lrxh.neptune.utils.CC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

@CommandAlias("party|p")
@Description("Party Command.")
public class PartyCommand extends BaseCommand {
    private final Neptune plugin = Neptune.get();

    @Default
    @Subcommand("help")
    public void help(Player player) {
        MessagesLocale.PARTY_HELP.send(player.getUniqueId());
    }

    @Subcommand("create")
    public void create(Player player) {
        Profile profile = plugin.getAPI().getProfile(player);
        profile.createParty();
    }

    @Subcommand("join")
    @Syntax("<player>")
    @CommandCompletion("@names")
    public void join(Player player, String otherPlayer) {
        Player target = Bukkit.getPlayer(otherPlayer);
        if (target == null) {
            player.sendMessage(CC.error("Player isn't online!"));
            return;
        }

        Party party = plugin.getAPI().getProfile(target).getGameData().getParty();
        if (party == null) {
            player.sendMessage(CC.error("Player isn't in any party"));
            return;
        }
        if (!party.getLeader().equals(target.getUniqueId())) {
            player.sendMessage(CC.error("Player isn't a leader of a party."));
            return;
        }
        if (!party.isOpen()) {
            player.sendMessage(CC.error("Party is private"));
            return;

        }
        if (plugin.getAPI().getProfile(player).getGameData().getParty() != null) {
            MessagesLocale.PARTY_ALREADY_IN.send(player.getUniqueId());
            return;
        }

        party.accept(player.getUniqueId());
    }

    @Subcommand("disband")
    public void disband(Player player) {
        plugin.getAPI().getProfile(player).disband();
    }

    @Subcommand("leave")
    public void leave(Player player) {
        plugin.getAPI().getProfile(player).disband();
    }

    @Subcommand("invite")
    @Syntax("<player>")
    @CommandCompletion("@names")
    public void invite(Player player, String otherPlayer) {
        Player target = Bukkit.getPlayer(otherPlayer);
        if (target == null) {
            player.sendMessage(CC.error("Player isn't online!"));
            return;
        }
        Profile targetProfile = plugin.getAPI().getProfile(target);
        Profile profile = plugin.getAPI().getProfile(player);
        Party party = profile.getGameData().getParty();

        if (party == null) {
            MessagesLocale.PARTY_NOT_IN.send(player.getUniqueId());
            return;
        }

        if (!party.getLeader().equals(player.getUniqueId())) {
            MessagesLocale.PARTY_NO_PERMISSION.send(player.getUniqueId());
            return;
        }

        if (!targetProfile.getSettingData().isAllowParty()) {
            MessagesLocale.PARTY_DISABLED.send(player.getUniqueId());
            return;
        }

        if (plugin.getAPI().getProfile(target).getGameData().getParty() != null) {
            MessagesLocale.PARTY_ALREADY_IN.send(player.getUniqueId());
            return;
        }

        if (profile.getGameData().getRequests().contains(player.getUniqueId())) {
            MessagesLocale.PARTY_ALREADY_SENT.send(player.getUniqueId(), new Replacement("<player>", player.getName()));
            return;
        }

        if (targetProfile.getGameData().getRequests().contains(player.getUniqueId())) {
            MessagesLocale.PARTY_ALREADY_SENT.send(player.getUniqueId(), new Replacement("<player>", target.getName()));
            return;
        }

        if (party.getMaxUsers() > party.getMaxUsers() + 1) {
            MessagesLocale.PARTY_MAX_SIZE.send(player.getUniqueId());
            return;
        }

        party.invite(target.getUniqueId());

        MessagesLocale.PARTY_INVITED.send(player.getUniqueId(), new Replacement("<player>", target.getName()));
    }

    @Subcommand("accept")
    @Syntax("<uuid>")
    public void accept(Player player, String otherString) {
        Profile profile = plugin.getAPI().getProfile(player);
        if (!profile.getState().equals(ProfileState.IN_LOBBY)) return;
        try {
            if (profile.getGameData().getParty() != null) {
                MessagesLocale.PARTY_ALREADY_IN.send(player.getUniqueId());
                return;
            }
            UUID otherUUID = UUID.fromString(otherString);
            PartyRequest request = (PartyRequest) profile.getGameData().getRequests().get(otherUUID);
            if (request != null) {
                request.getParty().accept(player.getUniqueId());
            }
        } catch (IllegalArgumentException e) {
            player.sendMessage(CC.error("Invalid UUID!"));
        }
    }

    @Subcommand("kick")
    @Syntax("<player>")
    @CommandCompletion("@names")
    public void kick(Player player, String otherPlayer) {
        Player target = Bukkit.getPlayer(otherPlayer);
        if (target == null) {
            player.sendMessage(CC.error("Player isn't online!"));
            return;
        }
        Party party = plugin.getAPI().getProfile(target).getGameData().getParty();
        if (party == null || !party.getLeader().equals(player.getUniqueId())) {
            MessagesLocale.PARTY_NOT_IN_PARTY.send(player.getUniqueId(), new Replacement("<player>", player.getName()));
            return;
        }

        party.kick(target.getUniqueId());
    }
}
