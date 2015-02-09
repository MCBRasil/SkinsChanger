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

public class Commands extends Command {

	public Commands() {
		super("skin", "skinschanger.use", new String[] {"skin"});
	}
	  public void execute(final CommandSender sender, final String[] args)
	  {
	    if (args.length > 0) {
		  if (args[0].equalsIgnoreCase("default")) {
		  onDefaultCommand(sender, args);
	    }else if (args[0].equalsIgnoreCase("change")) {
		    	  onChangeCommand(sender, args);
	   }else if (args[0].equalsIgnoreCase("help")) {
	    	  onHelpCommand(sender, args);
		        return;
	      }
}else
	    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eUse '&c/skin help&e' for help."));
	  }
		public void onHelpCommand(final CommandSender sender, final String[] args) {
			if (args.length > 0) {
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e]&c============&e[ &aSkinsChanger Help &e]&c============&e["));
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e/skin change <skinname> &9-&a Changes your skin. &7&o//requires relog"));
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e/skin default &9-&a Reverts your default skin &7&o//requires relog"));
			return;
		}
		}
	public void onChangeCommand(final CommandSender sender, final String[] args) {
		if (args.length == 2) {
			final String name = sender.getName();
			ProxyServer.getInstance().getScheduler().runAsync(
				Skins.getInstance(),
				new Runnable() {
					@Override
					public void run() {
						try {
							SkinProfile profile = SkinGetUtils.getSkinProfile(args[1]);
							Skins.getInstance().getSkinStorage().addSkinData(name, profile);
							Skins.getInstance().getSkinStorage().saveData();
							TextComponent component = new TextComponent(ChatColor.translateAlternateColorCodes('&', "&bYou've changed your skin! (relog to see the changes)"));
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
	public void onDefaultCommand(final CommandSender sender, final String[] args) {
		if (args.length == 1) {
			final String name = sender.getName();
			ProxyServer.getInstance().getScheduler().runAsync(
				Skins.getInstance(),
				new Runnable() {
					@Override
					public void run() {
						try {
							SkinProfile profile = SkinGetUtils.getSkinProfile(sender.getName());
							Skins.getInstance().getSkinStorage().addSkinData(name, profile);
							Skins.getInstance().getSkinStorage().saveData();
							TextComponent component = new TextComponent(ChatColor.translateAlternateColorCodes('&', "&bYou've reverted your skin! (relog to see the changes)"));
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