package dev.lrxh.neptune.match;

import dev.lrxh.neptune.Neptune;
import dev.lrxh.neptune.arena.Arena;
import dev.lrxh.neptune.arena.impl.StandAloneArena;
import dev.lrxh.neptune.kit.Kit;
import dev.lrxh.neptune.match.impl.FfaFightMatch;
import dev.lrxh.neptune.match.impl.SoloFightMatch;
import dev.lrxh.neptune.match.impl.participant.Participant;
import dev.lrxh.neptune.match.impl.participant.ParticipantColor;
import dev.lrxh.neptune.match.impl.team.MatchTeam;
import dev.lrxh.neptune.match.impl.team.TeamFightMatch;
import dev.lrxh.neptune.match.tasks.MatchStartRunnable;
import dev.lrxh.neptune.utils.PlayerUtil;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class MatchManager {
    public final HashSet<Match> matches = new HashSet<>();
    private final Neptune plugin;

    public MatchManager() {
        this.plugin = Neptune.get();
    }

    public void startMatch(List<Participant> participants, Kit kit, Arena arena, boolean duel, int rounds) {
        for (Participant ignored : participants) {
            kit.addPlaying();
        }

        //Create teams
        Participant playerRed = participants.get(0);
        Participant playerBlue = participants.get(1);

        playerRed.setOpponent(playerBlue);
        playerRed.setColor(ParticipantColor.RED);

        playerBlue.setOpponent(playerRed);
        playerBlue.setColor(ParticipantColor.BLUE);

        //Create match
        SoloFightMatch match = new SoloFightMatch(arena, kit, duel, participants, playerRed, playerBlue, rounds);

        matches.add(match);

        //Setup players
        match.setupParticipants();

        //Apply kit rules for players
        match.checkRules();

        //Teleport the Players to their spawn
        match.teleportToPositions();

        //Start match start runnable
        new MatchStartRunnable(match, plugin).start(0L, 20L, plugin);
    }

    public void startMatch(MatchTeam teamA, MatchTeam teamB, Kit kit, Arena arena) {

        for (Participant participant : teamA.getParticipants()) {
            for (Participant opponent : teamB.getParticipants()) {
                participant.setOpponent(opponent);
                participant.setColor(ParticipantColor.RED);
                opponent.setOpponent(participant);
                opponent.setColor(ParticipantColor.BLUE);
            }
        }

        List<Participant> participants = new ArrayList<>(teamA.getParticipants());
        participants.addAll(teamB.getParticipants());

        //Create match
        TeamFightMatch match = new TeamFightMatch(arena, kit, participants, teamA, teamB);

        matches.add(match);

        //Setup players
        match.setupParticipants();

        //Apply kit rules for players
        match.checkRules();

        //Teleport the Players to their spawn
        match.teleportToPositions();

        //Start match start runnable
        new MatchStartRunnable(match, plugin).start(0L, 20L, plugin);
    }

    public void startMatch(List<Participant> participants, Kit kit, Arena arena) {
        for (Participant participant : participants) {
            participant.setColor(ParticipantColor.RED);
        }

        //Create match
        FfaFightMatch match = new FfaFightMatch(arena, kit, participants);

        matches.add(match);

        //Setup players
        match.setupParticipants();

        //Apply kit rules for players
        match.checkRules();

        //Teleport the Players to their spawn
        match.teleportToPositions();

        //Start match start runnable
        new MatchStartRunnable(match, plugin).start(0L, 20L, plugin);
    }

    public void stopAllGames() {
        for (Match match : matches) {
            if (match.getArena() instanceof StandAloneArena standAloneArena) {
                standAloneArena.restoreSnapshot();
            }
            for (Participant participant : match.getParticipants()) {
                Player player = participant.getPlayer();
                if (player == null) continue;
                PlayerUtil.kick(player.getUniqueId(), "&cServer is restarting...");
            }
        }
    }

}
