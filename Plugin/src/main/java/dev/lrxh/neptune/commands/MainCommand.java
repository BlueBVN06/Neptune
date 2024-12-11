package dev.lrxh.neptune.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import dev.lrxh.neptune.Neptune;
import dev.lrxh.neptune.utils.CC;
import org.bukkit.entity.Player;


@CommandAlias("neptune")
@CommandPermission("neptune.admin.main")
@Description("Main Command for Neptune Practice Core.")
public class MainCommand extends BaseCommand {
    private final Neptune plugin = Neptune.get();

    @Default
    @Subcommand("help")
    public void help(Player player) {
        player.sendMessage(CC.color("&7&m-----------------------------------------"));
        player.sendMessage(CC.color("&9Neptune Core"));
        player.sendMessage(" ");
        player.sendMessage(CC.color("&7* &9/neptune setspawn &7- &fSet server spawn"));
        player.sendMessage(CC.color("&7* &9/neptune reload &7- &fReload all configs"));
        player.sendMessage(" ");
        player.sendMessage(CC.color("&7&m-----------------------------------------"));
    }

    @Subcommand("setspawn")
    public void setspawn(Player player) {
        plugin.getCache().setSpawn(player.getLocation());
        plugin.getCache().save();
        player.sendMessage(CC.color("&aSuccessfully set spawn!"));
    }


    @Subcommand("reload")
    public void reload(Player player) {
        plugin.getConfigManager().load();
        plugin.getCosmeticManager().load();
        player.sendMessage(CC.color("&aSuccessfully reloaded configs!"));
    }
}