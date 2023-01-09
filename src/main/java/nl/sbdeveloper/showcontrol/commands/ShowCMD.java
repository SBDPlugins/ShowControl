package nl.sbdeveloper.showcontrol.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import nl.sbdeveloper.showcontrol.api.InvalidTriggerException;
import nl.sbdeveloper.showcontrol.api.ShowAPI;
import nl.sbdeveloper.showcontrol.api.TooFewArgumentsException;
import nl.sbdeveloper.showcontrol.api.triggers.Trigger;
import nl.sbdeveloper.showcontrol.data.Shows;
import nl.sbdeveloper.showcontrol.gui.ShowCueGUI;
import nl.sbdeveloper.showcontrol.utils.TimeUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("mctpshow|show")
@CommandPermission("mctp.show")
public class ShowCMD extends BaseCommand {
    /*
    /mctpshow create <Naam>
    /mctpshow delete <Naam>
    /mctpshow add <Naam> <Tijd> <Type> <Data ...>
    /mctpshow start <Naam>
    /mctpshow cancel <Naam>
    /mctpshow gui <Naam>
     */

    @Subcommand("create")
    @Description("")
    public void onCreate(CommandSender sender, @Single String name) {
        if (Shows.exists(name)) {
            sender.sendMessage(ChatColor.RED + "Die show bestaat al.");
            return;
        }

        Shows.create(name);

        sender.sendMessage(ChatColor.GREEN + "De show " + ChatColor.WHITE + name + ChatColor.GREEN + " is aangemaakt!");
    }

    @Subcommand("delete")
    @Description("")
    @CommandCompletion("@showname")
    public void onDelete(CommandSender sender, @Single String name) {
        if (!Shows.exists(name)) {
            sender.sendMessage(ChatColor.RED + "Die show bestaat niet.");
            return;
        }

        Shows.delete(name);

        sender.sendMessage(ChatColor.GREEN + "De show " + ChatColor.WHITE + name + ChatColor.GREEN + " is verwijderd!");
    }

    @Subcommand("add")
    @Description("")
    @CommandCompletion("@showname @empty @showtype")
    public void onAdd(CommandSender sender, String name, String time, String args) {
        if (!Shows.exists(name)) {
            sender.sendMessage(ChatColor.RED + "Die show bestaat niet.");
            return;
        }

        Long timeMilli;
        try {
            timeMilli = TimeUtil.toMilis(time);
        } catch (Exception e) {
            sender.sendMessage(ChatColor.RED + "Geef een correcte tijd mee.");
            return;
        }

        Trigger data;
        try {
            data = ShowAPI.getTrigger(args);
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
            sender.sendMessage(ChatColor.RED + "Er is iets fout gegaan! Vraag de server eigenaar voor meer informatie.");
            return;
        } catch (InvalidTriggerException e) {
            sender.sendMessage(ChatColor.RED + "De meegegeven trigger bestaat niet.");
            return;
        } catch (TooFewArgumentsException e) {
            sender.sendMessage(ChatColor.RED + "Je hebt niet genoeg informatie meegeven voor de trigger.");
            return;
        }

        Shows.addPoint(name, timeMilli, data);
        sender.sendMessage(ChatColor.GREEN + "De show " + ChatColor.WHITE + name + ChatColor.GREEN + " bevat nu een extra punt!");
    }

    @Subcommand("start")
    @Description("")
    @CommandCompletion("@showname")
    public void onStart(CommandSender sender, @Single String name) {
        if (!Shows.exists(name)) {
            sender.sendMessage(ChatColor.RED + "Die show bestaat niet.");
            return;
        }

        Shows.startShow(name);

        sender.sendMessage(ChatColor.GREEN + "De show " + ChatColor.WHITE + name + ChatColor.GREEN + " is gestart!");
    }

    @Subcommand("cancel")
    @Description("")
    @CommandCompletion("@showname")
    public void onCancel(CommandSender sender, @Single String name) {
        if (!Shows.exists(name)) {
            sender.sendMessage(ChatColor.RED + "Die show bestaat niet.");
            return;
        }

        Shows.cancelShow(name);

        sender.sendMessage(ChatColor.GREEN + "De show " + ChatColor.WHITE + name + ChatColor.GREEN + " is gestopt!");
    }

    @Subcommand("gui")
    @Description("")
    @CommandCompletion("@showname")
    public void onGUI(Player sender, @Single String name) {
        if (!Shows.exists(name)) {
            sender.sendMessage(ChatColor.RED + "Die show bestaat niet.");
            return;
        }

        new ShowCueGUI(sender, name);
    }
}
