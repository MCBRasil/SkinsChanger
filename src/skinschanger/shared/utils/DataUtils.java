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

package skinschanger.shared.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

import skinschanger.libs.com.mojang.api.profiles.HttpProfileRepository;
import skinschanger.libs.com.mojang.api.profiles.Profile;
import skinschanger.libs.org.json.simple.JSONArray;
import skinschanger.libs.org.json.simple.JSONObject;
import skinschanger.libs.org.json.simple.parser.JSONParser;
import skinschanger.libs.org.json.simple.parser.ParseException;
import skinschanger.shared.format.SkinProperty;
import skinschanger.shared.utils.SkinGetUtils.SkinFetchFailedException;
import skinschanger.shared.utils.apacheutils.IOUtils;

public class DataUtils {

	public static Profile getProfile(String nick) throws SkinFetchFailedException {
		HttpProfileRepository repo = new HttpProfileRepository("minecraft");
		Profile[] profiles = repo.findProfilesByNames(nick);
		if (profiles.length >= 1) {
			return profiles[0];
		}
		throw new SkinFetchFailedException(SkinFetchFailedException.Reason.NO_PREMIUM_PLAYER);
	}

	private static final String skullbloburl = "https://sessionserver.mojang.com/session/minecraft/profile/";
	public static SkinProperty getProp(String id) throws IOException, ParseException, SkinFetchFailedException {
		URL url = new URL(skullbloburl+id+"?unsigned=false");
		URLConnection connection = url.openConnection();
		connection.setConnectTimeout(10000);
		connection.setReadTimeout(10000);
		connection.setUseCaches(false);
		InputStream is = connection.getInputStream();
		String result = IOUtils.toString(is, StandardCharsets.UTF_8);
		IOUtils.closeQuietly(is);
		JSONArray properties = (JSONArray) ((JSONObject) new JSONParser().parse(result)).get("properties");
		for (int i = 0; i < properties.size(); i++) {
			JSONObject property = (JSONObject) properties.get(i);
			String name = (String) property.get("name");
			String value = (String) property.get("value");
			String signature = (String) property.get("signature");
			if (name.equals("textures")) {
				return new SkinProperty(name, value, signature);
			}
		}
		throw new SkinFetchFailedException(SkinFetchFailedException.Reason.NO_SKIN_DATA);
	}

}
