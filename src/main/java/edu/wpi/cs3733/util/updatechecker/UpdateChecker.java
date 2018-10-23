package edu.wpi.cs3733.util.updatechecker;

public class UpdateChecker {


	/**
	 * Default API base url. Can override if desired.
	 */
	protected static String apiURL = "https://ravana.dyn.wpi.edu";

	/**
	 * Cached response from latest query to API server
	 */
	protected static Object lastResult = null;

	/**
	 * Check if a connection to the server can be established. Good for preventing requests that don't need to happen.
	 * @return True if server is up, false otherwise
	 */
	public static boolean checkConnection() {
		return false;
	}

	/**
	 * Get a boolean indicating whether the provided version is up-to-date with the API server's latest version.
	 * @param version API version in #.#.# format (e.g. 17.0.1)
	 * @param artifactId API artifact ID, you can find this on the API site webpage -- it's just the 'name' field in the
	 *                   gradle string.
	 * @param groupId Group ID, also found on the API site webpage (the 'group' field). Looks like
	 *                edu.wpi.cs3733.[]#.team[]
	 * @return True if up-to-date, false otherwise
	 */
	public static boolean isLatestVerison(String version, String artifactId, String groupId) {
		return true;
	}

	/**
	 * Get a boolean indicating whether the provided version is up-to-date with the API server's latest version.
	 * @param version API version in #.#.# format (e.g. 8.4.72)
	 * @param uuid API UUID. To find your API's UUID, go to the API server /list.json
	 *                (default: https://ravana.dyn.wpi/list.json) and check your API entry's "id" field.
	 * @return True if up-to-date, false otherwise
	 */
	public static boolean isLatestVersion(String version, String uuid) {
		return true;
	}

	/**
	 * Get a list of version changes from the changelog since the specified version. E.g. if you provide version 1.0.0
	 * and there's been a version 1.0.1 and version 1.1.0 since, you'll get the changelog for just those two releases.
	 * @param version API version in #.#.# format (e.g. 17.0.1). If null, full version history will be returned.
	 * @param artifactId API artifact ID, you can find this on the API site webpage -- it's just the 'name' field in the
	 *                   gradle string.
	 * @param groupId Group ID, also found on the API site webpage (the 'group' field). Looks like
	 *                edu.wpi.cs3733.[]#.team[]
	 * @return Sorted array of version history entries as strings, most recent change last.
	 */
	public static String[] getChangesSince(String version, String artifactId, String groupId) {
		return null;
	}

	/**
	 * Get a list of version changes from the changelog since the specified version. E.g. if you provide version 1.0.0
	 * and there's been a version 1.0.1 and version 1.1.0 since, you'll get the changelog for just those two releases.
	 * @param version API version in #.#.# format (e.g. 17.0.1). If null, full version history will be returned.
	 * @param uuid API UUID. To find your API's UUID, go to the API server /list.json
	 *                (default: https://ravana.dyn.wpi/list.json) and check your API entry's "id" field.
	 * @return Sorted array of version history entries as strings, most recent change last.
	 */
	public static String[] getChangesSince(String version, String uuid) {
		return null;
	}

	/**
	 * Fetch API info from the server. Useful for retrieving metadata if you need it!
	 * @param artifactId API artifact ID, you can find this on the API site webpage -- it's just the 'name' field in the
	 *                   gradle string.
	 * @param groupId Group ID, also found on the API site webpage (the 'group' field). Looks like
	 *                edu.wpi.cs3733.[]#.team[]
	 * @implNote Returns cached version of API info object if data has already been retrieved.
	 * @return API object containing all available metadata on requested API
	 */
	public static API fetchAPIInfo(String artifactId, String groupId) {
		return null;
	}

	/**
	 * Fetch API info from the server. Useful for retrieving metadata if you need it!
	 * @param uuid API UUID. To find your API's UUID, go to the API server /list.json
	 *                (default: https://ravana.dyn.wpi/list.json) and check your API entry's "id" field.
	 * @implNote Returns cached version of API info object if data has already been retrieved.
	 * @return API object containing all available metadata on requested API
	 */
	public static API fetchAPIInfo(String uuid) {
		return null;
	}

	/**
	 * Get the API base URL in usage.
	 * @return API url as a string.
	 */
	public static String getApiURL(){
		return apiURL;
	}

	/**
	 * Set API base URL
	 * @param apiURL API url as a string. This should be the BASE URL, i.e. https://ravana.dyn.wpi.edu, NOT INCLUDING
	 *               ANY TRAILING CHARACTERS. That means you shouldn't put /api here!
	 */
	public static void setApiURL(String apiURL){
		UpdateChecker.apiURL = apiURL;
	}

	/**
	 * Clear the cached API object. Don't actually do this!
	 */
	static void clearCache() {
		UpdateChecker.lastResult = null;
	}
}
