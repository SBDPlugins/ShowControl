package tech.sbdevelopment.showcontrol.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import tech.sbdevelopment.showcontrol.api.SCAPI;
import tech.sbdevelopment.showcontrol.api.exceptions.InvalidTriggerException;
import tech.sbdevelopment.showcontrol.api.exceptions.TooFewArgumentsException;
import tech.sbdevelopment.showcontrol.api.triggers.Trigger;
import tech.sbdevelopment.showcontrol.gui.ShowCueGUI;
import tech.sbdevelopment.showcontrol.utils.TimeUtil;

@CommandAlias("showcontrol|sc")
@CommandPermission("sc.admin")
public class ShowCMD extends BaseCommand {
    @HelpCommand
    @CatchUnknown
    @Default
    public static void onHelp(CommandSender sender, CommandHelp help) {
        help.showHelp();
    }

    @Subcommand("create")
    @Description("")
    public void onCreate(CommandSender sender, @Single String name) {
        if (SCAPI.exists(name)) {
            sender.sendMessage(ChatColor.RED + "That show already exists.");
            return;
        }

        SCAPI.create(name);

        sender.sendMessage(ChatColor.GREEN + "The show " + ChatColor.WHITE + name + ChatColor.GREEN + " has been created!");
    }

    @Subcommand("delete")
    @Description("")
    @CommandCompletion("@showname")
    public void onDelete(CommandSender sender, @Single String name) {
        if (!SCAPI.exists(name)) {
            sender.sendMessage(ChatColor.RED + "That show doesn't exists.");
            return;
        }

        SCAPI.delete(name);

        sender.sendMessage(ChatColor.GREEN + "The show " + ChatColor.WHITE + name + ChatColor.GREEN + " has been removed!");
    }

    @Subcommand("add")
    @Description("")
    @CommandCompletion("@showname @empty @showtype @cuearg")
    public void onAdd(CommandSender sender, String name, String time, String args) {
        if (!SCAPI.exists(name)) {
            sender.sendMessage(ChatColor.RED + "That show doesn't exists.");
            return;
        }

        Long timeMilli;
        try {
            timeMilli = TimeUtil.toMillis(time);
        } catch (Exception e) {
            sender.sendMessage(ChatColor.RED + "Provide a valid time, for example 5s (5 seconds) or 10m (10 minutes).");
            return;
        }

        Trigger data;
        try {
            data = SCAPI.getTrigger(args);
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
            sender.sendMessage(ChatColor.RED + "Something went wrong! Please ask a server admin.");
            return;
        } catch (InvalidTriggerException e) {
            sender.sendMessage(ChatColor.RED + "The provided trigger does not exists.");
            return;
        } catch (TooFewArgumentsException e) {
            sender.sendMessage(ChatColor.RED + "You did not provide enough information for the chosen trigger! Required arguments: " + e.getMessage());
            return;
        } catch (IllegalArgumentException e) {
            sender.sendMessage(ChatColor.RED + "Couldn't create the trigger: " + e.getMessage());
            return;
        }

        SCAPI.getShow(name).get().addPoint(timeMilli, data);
        sender.sendMessage(ChatColor.GREEN + "The show " + ChatColor.WHITE + name + ChatColor.GREEN + " now contains an extra point!");
    }

    @Subcommand("start")
    @Description("")
    @CommandCompletion("@showname")
    public void onStart(CommandSender sender, @Single String name) {
        if (!SCAPI.exists(name)) {
            sender.sendMessage(ChatColor.RED + "That show doesn't exists.");
            return;
        }

        SCAPI.getShow(name).get().start();

        sender.sendMessage(ChatColor.GREEN + "The show " + ChatColor.WHITE + name + ChatColor.GREEN + " has been started!");
    }

    @Subcommand("cancel")
    @Description("")
    @CommandCompletion("@showname")
    public void onCancel(CommandSender sender, @Single String name) {
        if (!SCAPI.exists(name)) {
            sender.sendMessage(ChatColor.RED + "That show doesn't exists.");
            return;
        }

        SCAPI.getShow(name).get().cancel();

        sender.sendMessage(ChatColor.GREEN + "The show " + ChatColor.WHITE + name + ChatColor.GREEN + " has been stopped!");
    }

    @Subcommand("gui")
    @Description("")
    @CommandCompletion("@showname")
    public void onGUI(Player sender, @Single String name) {
        if (!SCAPI.exists(name)) {
            sender.sendMessage(ChatColor.RED + "That show doesn't exists.");
            return;
        }

        new ShowCueGUI(sender, name);
    }
}
