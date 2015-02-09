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

package skinschanger.bukkit.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.comphenix.protocol.wrappers.WrappedSignedProperty;

import skinschanger.bukkit.Skins;
import skinschanger.bukkit.storage.SkinStorage;
import skinschanger.shared.format.SkinProfile;
import skinschanger.shared.format.SkinProperty;
import skinschanger.shared.utils.SkinGetUtils;
import skinschanger.shared.utils.SkinGetUtils.SkinFetchFailedException;

public class LoginListener implements Listener {
	SkinStorage storage = new SkinStorage();
	public SkinStorage getSkinStorage() {
		return storage;
	}
	//load skin data on async prelogin event
	@EventHandler
	public void onPreLoginEvent(AsyncPlayerPreLoginEvent event) {
		String name = event.getName();
		Skins.getInstance().getSkinStorage().loadData();
		if (Skins.getInstance().getSkinStorage().hasLoadedSkinData(name) && !Skins.getInstance().getSkinStorage().getLoadedSkinData(name).isTooDamnOld()) {
			Skins.getInstance().logInfo("Skin for player "+name+" is already cached");
			return;
		}
		try {
			SkinProfile profile = SkinGetUtils.getSkinProfile(name);
			Skins.getInstance().getSkinStorage().addSkinData(name, profile);
			Skins.getInstance().logInfo("Skin for player "+name+" was succesfully fetched and cached");
		} catch (SkinFetchFailedException e) {
			Skins.getInstance().logInfo("Skin fetch failed for player "+name+": "+e.getMessage());
		}
	}

	//fix skin on player login
	@EventHandler
	public void onLoginEvent(PlayerLoginEvent event) {
		Player player = event.getPlayer();
		String name = event.getPlayer().getName();
		Skins.getInstance().getSkinStorage().loadData();
		if (Skins.getInstance().getSkinStorage().hasLoadedSkinData(player.getName())) {
			SkinProperty skinproperty = Skins.getInstance().getSkinStorage().getLoadedSkinData(player.getName()).getPlayerSkinProperty();
			WrappedGameProfile wrappedprofile = WrappedGameProfile.fromPlayer(player);
			WrappedSignedProperty wrappedproperty = WrappedSignedProperty.fromValues(skinproperty.getName(), skinproperty.getValue(), skinproperty.getSignature());
			if (!wrappedprofile.getProperties().containsKey(wrappedproperty.getName())) {
				wrappedprofile.getProperties().put(wrappedproperty.getName(), wrappedproperty);
			}
		}
	}

}
