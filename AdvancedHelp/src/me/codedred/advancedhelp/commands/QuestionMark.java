package me.codedred.advancedhelp.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.apache.commons.lang.StringUtils;

import me.codedred.advancedhelp.Main;

public class QuestionMark implements CommandExecutor{

	private Main plugin;
	public QuestionMark(Main plugin) {
		this.plugin = plugin;
	}
	public boolean onCommand(CommandSender sender, Command cmd, String cmdL, String[] args)
	{
        if (sender.hasPermission("help.override"))
            Bukkit.dispatchCommand(sender, "bukkit:help " + StringUtils.join(args, " ", 0, args.length));
        else if (sender.hasPermission("help.use"))
            Bukkit.dispatchCommand(sender, "ehelp " + StringUtils.join(args, " ", 0, args.length));
        else
            sender.sendMessage(plugin.format(plugin.getConfig().getString("prefix") + " " + plugin.getConfig().getString("messages.no-permission")));

        return true;
	}
}

