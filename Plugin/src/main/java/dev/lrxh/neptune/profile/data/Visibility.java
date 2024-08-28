package dev.lrxh.neptune.profile.data;

import dev.lrxh.neptune.Neptune;
import dev.lrxh.neptune.profile.impl.Profile;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Visibility {
    private final Neptune plugin;
    private final UUID uuid;

    public Visibility(Neptune plugin, UUID playerUUID) {
        this.plugin = plugin;
        this.uuid = playerUUID;
        handle(playerUUID);
    }

    public void handle() {
        for (Player players : Bukkit.getOnlinePlayers()) {
            handle(players.getUniqueId());
        }
    }

    public void handle(UUID otherUUID) {
        Player viewerPlayer = Bukkit.getPlayer(uuid);
        Player otherPlayer = Bukkit.getPlayer(otherUUID);
        if (viewerPlayer == null || otherPlayer == null || viewerPlayer.equals(otherPlayer)) {
            return;
        }

        Profile viewerProfile = plugin.getAPI().getProfile(uuid);
        Profile otherProfile = plugin.getAPI().getProfile(otherUUID);

        if (has(uuid, otherUUID, ProfileState.IN_GAME)
                && viewerProfile.getMatch().getUuid().equals(otherProfile.getMatch().getUuid())) {
            viewerPlayer.showPlayer(plugin, otherPlayer);
            otherPlayer.showPlayer(plugin, viewerPlayer);
            return;
        }

        if (!viewerProfile.getSettingData().isPlayerVisibility()) {
            viewerPlayer.hidePlayer(plugin, otherPlayer);
            return;
        }

        if (!otherProfile.getSettingData().isPlayerVisibility()) {
            otherPlayer.hidePlayer(plugin, viewerPlayer);
            return;
        }

        if (has(uuid, otherUUID, ProfileState.IN_LOBBY, ProfileState.IN_QUEUE, ProfileState.IN_PARTY)) {
            viewerPlayer.showPlayer(plugin, otherPlayer);
            otherPlayer.showPlayer(plugin, viewerPlayer);
            return;
        }

        viewerPlayer.hidePlayer(plugin, otherPlayer);
        otherPlayer.hidePlayer(plugin, viewerPlayer);
    }

    public boolean has(UUID playerUUID, UUID otherUUID, ProfileState... states) {
        Profile viewerProfile = plugin.getAPI().getProfile(playerUUID);
        Profile otherProfile = plugin.getAPI().getProfile(otherUUID);

        Set<ProfileState> stateSet = new HashSet<>(Arrays.asList(states));
        return stateSet.contains(viewerProfile.getState()) && stateSet.contains(otherProfile.getState());
    }
}
