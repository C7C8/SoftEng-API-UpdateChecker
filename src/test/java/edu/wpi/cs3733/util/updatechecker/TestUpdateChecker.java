package edu.wpi.cs3733.util.updatechecker;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TestUpdateChecker {
	//Test constants used with known server-side data. Yes, this is a bad way of testing. No, I don't strictly care just yet.
	private static final String testId = "97184785-7f43-4f59-af4d-e12c8fcdbf49";
	private static final String testGroupId = "edu.wpi.cs3733.d18.teamD";
	private static final String testArtifactId = "GiftRequest";
	private static final String testVersion = "1.1.0";

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
		fail();
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
	void fetchByMaven() {
		UpdateChecker.clearCache();
		API res = UpdateChecker.fetchAPIInfo(testArtifactId, testGroupId);
		assertNotNull(res, "Failed to retrieve API by artifact+group ID");
		assertEquals(testVersion, res.version, "Retrieved API but requested version number is incorrect");
	}

	@Test
	void caching() {
		fail();
	}
}
