/**
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 *
 */

package skinschanger.bukkit;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import skinschanger.bukkit.Skins;
import skinschanger.shared.format.SkinProfile;
import skinschanger.shared.utils.SkinGetUtils;
import skinschanger.shared.utils.SkinGetUtils.SkinFetchFailedException;

public class Commands implements CommandExecutor {

	private ExecutorService executor = Executors.newSingleThreadExecutor();
	  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	  {
	    if ((sender instanceof Player)) {
	      @SuppressWarnings("unused")
		Player localPlayer = (Player)sender;
	    }
	    if (args.length > 0) {
	      if (args[0].equalsIgnoreCase("change"))
	        return onChangeCommand(sender, cmd, label, args);
	      if (args[0].equalsIgnoreCase("default"))
	        return onDefaultCommand(sender, cmd, label, args);
	      if (args[0].equalsIgnoreCase("help"))
		    return onHelpCommand(sender, cmd, label, args);
	    }
	    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eUse '&c/skin help&e' for help."));
	    return true;
	  }
		public boolean onHelpCommand(final CommandSender sender, Command cmd, String label, final String[] args) {
			if (!sender.hasPermission("skinschanger.use")) {
				sender.sendMessage("You don't have permission to do this");
				return true;
			}
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e]&c============&e[ &aSkinsChanger Help &e]&c============&e["));
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e/skin change <skinname> &9-&a Changes your skin. &7&o//requires relog"));
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e/skin default &9-&a Reverts your default skin &7&o//requires relog"));
			return false;
		}
	public boolean onChangeCommand(final CommandSender sender, Command cmd, String label, final String[] args) {
		
		if (!sender.hasPermission("skinschanger.use")) {
			sender.sendMessage("You don't have permission to do this");
			return true;
		}
		if (args.length == 2) {
			executor.execute(
				new Runnable() {
					@Override
					public void run() {
						String name = sender.getName();
						try {
							SkinProfile profile = SkinGetUtils.getSkinProfile(args[1]);
							Skins.getInstance().getSkinStorage().addSkinData(name, profile);
							Skins.getInstance().getSkinStorage().saveData();
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&bYou've changed your skin! (relog to see the changes)"));
						} catch (SkinFetchFailedException e) {
							sender.sendMessage(ChatColor.RED+"Skin fetch failed: "+e.getMessage());
							}
						}
					}
			);
}
		return false;
	}
	public boolean onDefaultCommand(final CommandSender sender, Command cmd, String label, final String[] args) {
		
		if (!sender.hasPermission("skinschanger.use")) {
			sender.sendMessage("You don't have permission to do this");
			return true;
		}
		 if (args.length == 1) {
			executor.execute(
				new Runnable() {
					@Override
					public void run() {
						String name = sender.getName();
						try {
							SkinProfile profile = SkinGetUtils.getSkinProfile(name);
							Skins.getInstance().getSkinStorage().addSkinData(name, profile);
							Skins.getInstance().getSkinStorage().saveData();
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&bYou've reverted your skin! (relog to see the changes)"));
						} catch (SkinFetchFailedException e) {
							sender.sendMessage(ChatColor.RED+"Skin fetch failed: "+e.getMessage());
							}
						}
				}
			);
		 }
		return false;
}
}