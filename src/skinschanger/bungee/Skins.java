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

package skinschanger.bungee;

import java.util.logging.Logger;

import skinschanger.bungee.listeners.LoginListener;
import skinschanger.bungee.storage.SkinStorage;
import net.md_5.bungee.api.plugin.Plugin;

public class Skins extends Plugin {

	private static Skins instance;
	public static Skins getInstance() {
		return instance;
	}

	private Logger log;
	public void logInfo(String message) {
		log.info(message);
	}

	private SkinStorage storage = new SkinStorage();
	public SkinStorage getSkinStorage() {
		return storage;
	}

	@Override
	public void onEnable() {
		instance = this;
		log = getLogger();
		storage.loadData();
		this.getProxy().getPluginManager().registerListener(this, new LoginListener());
		this.getProxy().getPluginManager().registerCommand(this, new Commands());
		this.getProxy().getPluginManager().registerCommand(this, new AdminCommands());
	}

	@Override
	public void onDisable() {
		storage.saveData();
		instance = null;
	}

}
