package edu.wpi.cs3733.util.updatechecker;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TestUpdateChecker {
	//Test constants used with known server-side data. Yes, this is a bad way of testing. No, I don't strictly care just yet.
	private static final String testId = "97184785-7f43-4f59-af4d-e12c8fcdbf49";
	private static final String testGroupId = "edu.wpi.cs3733.d18.teamD";
	private static final String testArtifactId = "GiftRequest";
	private static final String testVersion = "1.1.0";

	@BeforeEach
	void connection() {
		assertTrue(UpdateChecker.checkConnection(), "Failed to connect to API server");
	}

	@Test
	void updateCheck() {

	}

	@Test
	void fetchByUUID() {
		UpdateChecker.clearCache();
		API res = UpdateChecker.fetchAPIInfo(testId);
		assertNotNull(res, "Failed to retrieve API by UUID");
		assertEquals(testId, res.id, "Retrieved API, but requested UUID doesn't match returned UUID");
	}

	@Test
	void fetchByMaven() {
		UpdateChecker.clearCache();
		API res = UpdateChecker.fetchAPIInfo(testArtifactId, testGroupId);
		assertNotNull(res, "Failed to retrieve API by artifact+group ID");
	}
}
