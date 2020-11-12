package nl.sbdeveloper.showapi.commands;

import nl.sbdeveloper.showapi.api.TriggerData;
import nl.sbdeveloper.showapi.data.Shows;
import nl.sbdeveloper.showapi.gui.ShowCueGUI;
import nl.sbdeveloper.showapi.utils.MainUtil;
import nl.sbdeveloper.showapi.utils.TimeUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ShowCMD implements CommandExecutor {
    /*
    /mctpshow create <Naam>
    /mctpshow delete <Naam>
    /mctpshow add <Naam> <Tijd> <Type> <Data ...>
    /mctpshow start <Naam>
    /mctpshow cancel <Naam>
    /mctpshow gui <Naam>
     */

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (label.equalsIgnoreCase("mctpshow")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "Je moet een speler zijn om dit te doen.");
                return false;
            }

            Player p = (Player) sender;
            if (!p.hasPermission("mctp.show")) {
                p.sendMessage(ChatColor.RED + "Je hebt hier geen permissie voor.");
                return false;
            }

            if (args.length == 2 && args[0].equalsIgnoreCase("create")) {
                String name = args[1];
                if (Shows.exists(name)) {
                    p.sendMessage(ChatColor.RED + "Die show bestaat al.");
                    return false;
                }

                Shows.create(name);

                p.sendMessage(ChatColor.GREEN + "De show " + ChatColor.WHITE + name + ChatColor.GREEN + " is aangemaakt!");
                return true;
            } else if (args.length == 2 && args[0].equalsIgnoreCase("delete")) {
                String name = args[1];
                if (!Shows.exists(name)) {
                    p.sendMessage(ChatColor.RED + "Die show bestaat niet.");
                    return false;
                }

                Shows.delete(name);

                p.sendMessage(ChatColor.GREEN + "De show " + ChatColor.WHITE + name + ChatColor.GREEN + " is verwijderd!");
                return true;
            } else if (args.length >= 5 && args[0].equalsIgnoreCase("add")) {
                String name = args[1];
                if (!Shows.exists(name)) {
                    p.sendMessage(ChatColor.RED + "Die show bestaat niet.");
                    return false;
                }

                int seconds = TimeUtil.parseSeconds(args[2]);

                StringBuilder builder = new StringBuilder();
                for (int i = 3; i < args.length; i++) {
                    builder.append(args[i]).append(" ");
                }
                TriggerData data = MainUtil.parseData(builder.toString().trim());

                if (data == null) {
                    p.sendMessage(ChatColor.RED + "Je hebt niet genoeg informatie meegeven voor de trigger.");
                    return false;
                }

                Shows.addPoint(name, seconds, data);

                p.sendMessage(ChatColor.GREEN + "De show " + ChatColor.WHITE + name + ChatColor.GREEN + " bevat nu een extra punt!");
                return true;
            } else if (args.length == 2 && args[0].equalsIgnoreCase("start")) {
                String name = args[1];
                if (!Shows.exists(name)) {
                    p.sendMessage(ChatColor.RED + "Die show bestaat niet.");
                    return false;
                }

                Shows.startShow(name);

                p.sendMessage(ChatColor.GREEN + "De show " + ChatColor.WHITE + name + ChatColor.GREEN + " is gestart!");
                return true;
            } else if (args.length == 2 && args[0].equalsIgnoreCase("cancel")) {
                String name = args[1];
                if (!Shows.exists(name)) {
                    p.sendMessage(ChatColor.RED + "Die show bestaat niet.");
                    return false;
                }

                Shows.cancelShow(name);

                p.sendMessage(ChatColor.GREEN + "De show " + ChatColor.WHITE + name + ChatColor.GREEN + " is gestopt!");
                return true;
            } else if (args.length == 2 && args[0].equalsIgnoreCase("gui")) {
                String name = args[1];
                if (!Shows.exists(name)) {
                    p.sendMessage(ChatColor.RED + "Die show bestaat niet.");
                    return false;
                }

                ShowCueGUI.openGUI(name, p);
                return true;
            }
        }
        return false;
    }
}
