package skinschanger.bungee;

import org.bukkit.entity.Player;

import skinschanger.bungee.Skins;
import skinschanger.shared.format.SkinProfile;
import skinschanger.shared.utils.SkinGetUtils;
import skinschanger.shared.utils.SkinGetUtils.SkinFetchFailedException;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

public class AdminCommands extends Command {

	public AdminCommands() {
		super("skina", "skinschanger.admin", new String[] {"skina"});
	}
	  public void execute(final CommandSender sender, final String[] args)
	  {
	    if (args.length > 0) {
		  if (args[0].equalsIgnoreCase("default")) {
		  onAdminDefaultCommand(sender, args);
	    }else if (args[0].equalsIgnoreCase("change")) {
		    	  onAdminChangeCommand(sender, args);
	   }else if (args[0].equalsIgnoreCase("help")) {
	    	  onAdminHelpCommand(sender, args);
		        return;
	      }
}else
	    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eUse '&c/skina help&e' for help."));
	  }
		public void onAdminHelpCommand(final CommandSender sender, final String[] args) {
			if (args.length > 0) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e]&c===========&e[ &aSkinsChanger Admin Help &e]&c===========&e["));
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e/skinadmin change <player> <skinname> &9-&a Changes your skin. &7&o//requires relog"));
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e/skinadmin default <player> &9-&a Reverts your default skin &7&o//requires relog"));
			return;
		}
		}
	public void onAdminChangeCommand(final CommandSender sender, final String[] args) {
		if (args.length == 3) {
			final String name = args[1];
			ProxyServer.getInstance().getScheduler().runAsync(
				Skins.getInstance(),
				new Runnable() {
					@Override
					public void run() {
						try {
							SkinProfile profile = SkinGetUtils.getSkinProfile(args[2]);
							Skins.getInstance().getSkinStorage().addSkinData(name, profile);
							Skins.getInstance().getSkinStorage().saveData();
							TextComponent component = new TextComponent(ChatColor.translateAlternateColorCodes('&', "&bYou've changed " + ChatColor.YELLOW + "" + args[1] + ChatColor.AQUA + "'s skin!"));
							component.setColor(ChatColor.AQUA);
							sender.sendMessage(component);
						} catch (SkinFetchFailedException e) {
							TextComponent component = new TextComponent("Skin fetch failed: "+e.getMessage());
							component.setColor(ChatColor.RED);
							sender.sendMessage(component);
						}
					}
				}
			);
		}
	}
	public void onAdminDefaultCommand(final CommandSender sender, final String[] args) {
		if (args.length == 2) {
			final String name = args[1];
			ProxyServer.getInstance().getScheduler().runAsync(
				Skins.getInstance(),
				new Runnable() {
					@Override
					public void run() {
						try {
							SkinProfile profile = SkinGetUtils.getSkinProfile(args[1]);
							Skins.getInstance().getSkinStorage().addSkinData(name, profile);
							Skins.getInstance().getSkinStorage().saveData();
							TextComponent component = new TextComponent(ChatColor.translateAlternateColorCodes('&', "&bYou've reverted " + ChatColor.YELLOW + "" + args[1] + ChatColor.AQUA + "'s skin!"));
							component.setColor(ChatColor.AQUA);
							sender.sendMessage(component);
						} catch (SkinFetchFailedException e) {
							TextComponent component = new TextComponent("Skin fetch failed: "+e.getMessage());
							component.setColor(ChatColor.RED);
							sender.sendMessage(component);
						}
					}
				}
			);
		}
	}
}