package me.codedred.advancedhelp.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import me.codedred.advancedhelp.Main;

public class QuestionMark implements CommandExecutor{

	private Main plugin;
	public QuestionMark(Main plugin) {
		this.plugin = plugin;
	}
	public boolean onCommand(CommandSender sender, Command cmd, String cmdL, String[] args)
	{
		if (sender.hasPermission("help.override")) {
			if (args.length == 0) 
				Bukkit.dispatchCommand(sender, "bukkit:help");
			if (args.length == 1) 
				Bukkit.dispatchCommand(sender, "bukkit:help " + args[0]);
			if (args.length >= 2 )
				Bukkit.dispatchCommand(sender, "bukkit:help " + args[0] + " " + args[1]);
		}
		else {
			if (sender.hasPermission("help.use")) {
				if (args.length == 0) 
					Bukkit.dispatchCommand(sender, "help");
				if (args.length == 1) 
					Bukkit.dispatchCommand(sender, "help " + args[0]);
				if (args.length >= 2 )
					Bukkit.dispatchCommand(sender, "help " + args[0] + " " + args[1]);
			}
			else {
				sender.sendMessage(plugin.format(plugin.getConfig().getString("prefix") + " " + plugin.getConfig().getString("messages.no-permission")));
				return true;
			}
		}
		
		
		return true;
	}
}

