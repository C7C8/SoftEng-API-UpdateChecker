package edu.wpi.cs3733.util.updatechecker;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TestUpdateChecker {
	//Test constants used with known server-side data. Yes, this is a bad way of testing. No, I don't strictly care just yet.
	private static final String testId = "97184785-7f43-4f59-af4d-e12c8fcdbf49";
	private static final String testVersion = "1.1.0";

	@BeforeAll
	static void connection() {
		if (!UpdateChecker.checkConnection())
			fail();
	}

	@Test
	void compareVersions() {
		//Basic test series: Make sure regular comparisons work without issue
		assertEquals(-1, UpdateChecker.compareVersion("1.0.0", "1.0.1"));
		assertEquals(1, UpdateChecker.compareVersion("1.0.1", "1.0.0"));
		assertEquals(0, UpdateChecker.compareVersion("1.0.0", "1.0.0"));
		assertEquals(-1, UpdateChecker.compareVersion("1.0.0", "1.1.0"));
		assertEquals(1, UpdateChecker.compareVersion("1.1.0", "1.0.0"));
		assertEquals(-1, UpdateChecker.compareVersion("1.0.0", "2.0.0"));
		assertEquals(1, UpdateChecker.compareVersion("2.0.0", "1.0.0"));

		//More complicated tests to ensure multi-digit parsing works, or that odd version numbers are processed correctly
		assertEquals(1, UpdateChecker.compareVersion("2.0.0", "1.999.999"));
		assertEquals(-1, UpdateChecker.compareVersion("2.999.999", "3.0.0"));
		assertEquals(0, UpdateChecker.compareVersion("2.01.050", "02.1.50"));
	}

	@Test
	void updateCheck() {
		//Check to make sure we're up to date
		assertTrue(UpdateChecker.isLatestVersion(testVersion, testId), "Couldn't validate latest version");

		//Now simulate a lower version number
		assertFalse(UpdateChecker.isLatestVersion("1.0.0", testId), "Couldn't validate lower version");

		//Make sure that, even if this version is somehow higher than
		//the registered version, we still call this the latest version
		assertTrue(UpdateChecker.isLatestVersion("2.0.0", testId), "Future version not validated");

		//APIs that couldn't be found should still return true
		assertTrue(UpdateChecker.isLatestVersion("17.0.1", "NotAnId"), "Bad return for invalid API");
	}

	@Test
	void fetchByUUID() {
		UpdateChecker.clearCache();
		API res = UpdateChecker.fetchAPIInfo(testId);
		assertNotNull(res, "Failed to retrieve API by UUID");
		assertEquals(testId, res.id, "Retrieved API, but requested UUID doesn't match returned UUID");
		assertEquals(testVersion, res.version, "Retrieved API but requested version number is incorrect");
	}

	@Test
	void caching() {
		//Make sure that we can request two different APIs and get the right data out; the caching system should help
		//speed things up, but it needs to return correct data.
		UpdateChecker.clearCache();
		API res = UpdateChecker.fetchAPIInfo(testId);
		assertNotNull(res);
		assertEquals(testId, res.id);

		//Repeat the same series without clearing the cache to make sure the data retrieved before is the same. This
		//time the request should go through to the cached object.
		res = UpdateChecker.fetchAPIInfo(testId);
		assertNotNull(res);
		assertEquals(testId, res.id);

		//Get a different object without clearing the cache and make sure the data returned is different
		res = UpdateChecker.fetchAPIInfo("9cabb706-37bc-40a0-a024-5936a4573a6b");
		assertNotNull(res);
		assertEquals("9cabb706-37bc-40a0-a024-5936a4573a6b", res.id);

		//Go back to the original testing ID and make sure that data can still be retrieved
		res = UpdateChecker.fetchAPIInfo(testId);
		assertNotNull(res);
		assertEquals(testId, res.id);
	}

	@Test
	void historySince() {
		//Ensure the filtered results have at least one entry
		String[] history = UpdateChecker.getChangesSince("1.0.0", testId);
		assertNotNull(history);
		assertTrue(history.length > 0, "History filtering failure");

		history = UpdateChecker.getChangesSince(testVersion, testId);
		assertNotNull(history);
		assertEquals(0, history.length, "Returned history that shouldn't exist; has the API updated?");
	}
}
