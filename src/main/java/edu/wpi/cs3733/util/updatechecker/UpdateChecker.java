package edu.wpi.cs3733.util.updatechecker;

import com.google.gson.Gson;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class UpdateChecker {

	/**
	 * Default API base url. Can override if desired.
	 */
	protected static String apiURL = "https://api.apisite.crmyers.dev";

	/**
	 * Cached response from latest query to API server
	 */
	protected static API lastResult = null;

	/**
	 * Check if a connection to the server can be established. Good for preventing requests that don't need to happen.
	 * @return True if server is up, false otherwise
	 */
	public static boolean checkConnection() {
		try{
			//Create a connection object and use it to connect to the server. If no IOException is generated we can
			//assume the connection works, so just disconnect and return true. If ANY exception is generated, return
			//false.
			URL url = new URL(apiURL);
			HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
			connection.connect();
			connection.disconnect();
			return true;
		} catch (IOException e){
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Get a boolean indicating whether the provided version is up-to-date with the API server's latest version.
	 * @param version API version in #.#.# format (e.g. 8.4.72)
	 * @param uuid API UUID. To find your API's UUID, go to the API server /list.json
	 *                (default: https://apisite.crmyers.dev/list.json) and check your API entry's "id" field.
	 * @return True if up-to-date, false otherwise. Returns true as a fallback if the server can't be contacted.
	 */
	public static boolean isLatestVersion(String version, String uuid) {
		API api = fetchAPIInfo(uuid);
		if (api == null)
			return true;

		return compareVersion(version, api.version) != -1;
	}

	/**
	 * Get a list of version changes from the changelog since the specified version. E.g. if you provide version 1.0.0
	 * and there's been a version 1.0.1 and version 1.1.0 since, you'll get the changelog for just those two releases.
	 * @param version API version in #.#.# format (e.g. 17.0.1). If null, full version history will be returned.
	 * @param uuid API UUID. To find your API's UUID, go to the API server /list.json
	 *                (default: https://apisite.crmyers.dev/list.json) and check your API entry's "id" field.
	 * @return Sorted array of version history entries as strings, most recent change last.
	 */
	public static String[] getChangesSince(String version, String uuid) {
		API api = fetchAPIInfo(uuid);
		if (api == null)
			return null;
		return filterHistory(version, api.history);
	}

	protected static String[] filterHistory(String version, String[] history) {
		ArrayList<String> temp = new ArrayList<>();
		for (String item : history) {
			//No need for validation here, the server automatically inserts a colon and a version number into each entry
			String curVersion = item.split(":")[0];
			if (compareVersion(curVersion, version) == 1)
				temp.add(item);
		}

		String[] ret = {};
		return temp.toArray(ret);
	}

	/**
	 * Fetch API info from the server. Useful for retrieving metadata if you need it!
	 * @param uuid API UUID. To find your API's UUID, go to the API server /list.json
	 *                (default: https://apisite.crmyers.dev/list.json) and check your API entry's "id" field.
	 * @implNote Returns cached version of API info object if data has already been retrieved.
	 * @return API object containing all available metadata on requested API
	 */
	public static API fetchAPIInfo(String uuid) {
		if (lastResult != null && lastResult.id.equals(uuid))
			return lastResult;
		try{
			URL url = new URL(apiURL + "/list?id=" + uuid);
			lastResult = fetchByURL(url);
			return lastResult;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Fetch API from the server using an already-created URL object.
	 * @param url HTTPS URL.
	 * @return API object if found, null if otherwise or if error.
	 */
	private static API fetchByURL(URL url) {
		API api;
		try {
			HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
			Gson gson = new Gson();
			api = gson.fromJson(new InputStreamReader(connection.getInputStream()), API.class);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return api;
	}

	/**
	 * Utility function for comparing version number strings.
	 * @param orig First version string
	 * @param next Next version string
	 * @return -1 if first < next, 0 if first = next (or error), 1 if first > next.
	 */
	static int compareVersion(String orig, String next) {
		//Split strings into numbers, parse the numbers, and turn them into appropriate lists
		List<Integer> orig_nums = Arrays.stream(orig.split("\\.")).map(Integer::parseInt).collect(toList());
		List<Integer> next_nums = Arrays.stream(next.split("\\.")).map(Integer::parseInt).collect(toList());

		//Error checking in case of bad version numbers passed in
		if (orig_nums.size() != 3 || next_nums.size() != 3)
			return 0;

		//Loop through the numbers. If the two are equal, proceed to the next most significant. If the two are not equal,
		//return the result of their comparison.
		for (int i = 0 ; i < 3; i++) {
			final int comp = orig_nums.get(i).compareTo(next_nums.get(i));
			if (comp == 0)
				continue;
			return comp;
		}
		return 0;
	}

	/**
	 * Clear the cached API object; implemented for testing purposes only, don't actually use this!
	 */
	static void clearCache() {
		UpdateChecker.lastResult = null;
	}
}
