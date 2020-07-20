package com.xdefcon.bungeeping.commands;

import com.xdefcon.bungeeping.BungeePing;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;


public class PingCommand extends Command {
    private BungeePing plugin;

    public PingCommand(BungeePing plugin) {
        super("ping");
        this.plugin = plugin;
    }

    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(new TextComponent(
                    ChatColor.RED + "You can execute this command only as a Player."
            ));
            return;
        }

        ProxiedPlayer p = (ProxiedPlayer) sender;
        if (args.length == 0) {
            if (lacksPerms(p, "bungeeping.ping")) {
                String noPerm = plugin.getConfig().getString("permission-system.no-perm-message");
                p.sendMessage(new TextComponent(
                        ChatColor.translateAlternateColorCodes('&', noPerm)
                ));
                return;
            }

            p.sendMessage(new TextComponent(
                    ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("ping-command.ping-message").replace("%ping%", "" + p.getPing()))
            ));
        } else {
            if (lacksPerms(p, "bungeeping.ping.others")) {
                String noPerm = plugin.getConfig().getString("others-ping.not-allowed-message");
                p.sendMessage(new TextComponent(
                        ChatColor.translateAlternateColorCodes('&', noPerm)
                ));
                return;
            }

            String target = args[0];
            ProxiedPlayer targetP = plugin.getProxy().getPlayer(target);
            if (targetP == null) {
                p.sendMessage(new TextComponent(
                        ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("others-ping.player-not-found"))
                ));
                return;
            }
            p.sendMessage(new TextComponent(
                    ChatColor.translateAlternateColorCodes('&',
                    plugin.getConfig().getString("ping-command.ping-target-message")
                          .replace("%ping%", "" + targetP.getPing())
                          .replace("%target%", targetP.getName()))
            ));
        }
    }


    private boolean lacksPerms(ProxiedPlayer p, String perm) {
        return plugin.getConfig().getBoolean("permission-system.enabled") && !p.hasPermission(perm);
    }
}
