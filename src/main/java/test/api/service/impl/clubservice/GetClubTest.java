package test.api.service.impl.clubservice;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import javastrava.api.v3.model.StravaClub;
import javastrava.api.v3.model.reference.StravaResourceState;
import test.api.model.StravaClubTest;
import test.api.service.StravaTest;
import test.utils.RateLimitedTestRunner;
import test.utils.TestUtils;

public class GetClubTest extends StravaTest {
	// 2. Invalid club
	@Test
	public void testGetClub_invalidClub() throws Exception {
		RateLimitedTestRunner.run(() -> {
			final StravaClub club = strava().getClub(TestUtils.CLUB_INVALID_ID);
			assertNull("Got club result despite club being invalid", club);
		} );
	}

	// 3. Private club of which current authenticated athlete is a member
	@Test
	public void testGetClub_privateClubIsMember() throws Exception {
		RateLimitedTestRunner.run(() -> {
			final StravaClub club = strava().getClub(TestUtils.CLUB_PRIVATE_MEMBER_ID);
			assertNotNull(club);
			StravaClubTest.validate(club, TestUtils.CLUB_PRIVATE_MEMBER_ID, club.getResourceState());
		} );
	}

	// 4. Private club of which current authenticated athlete is NOT a member
	@Test
	public void testGetClub_privateClubIsNotMember() throws Exception {
		RateLimitedTestRunner.run(() -> {
			final StravaClub club = strava().getClub(TestUtils.CLUB_PRIVATE_NON_MEMBER_ID);
			final StravaClub comparison = new StravaClub();
			comparison.setId(TestUtils.CLUB_PRIVATE_NON_MEMBER_ID);
			comparison.setResourceState(StravaResourceState.PRIVATE);
			assertNotNull(club);
			assertEquals(comparison, club);
			StravaClubTest.validate(club, TestUtils.CLUB_PRIVATE_NON_MEMBER_ID, club.getResourceState());
		} );
	}

	// Test cases
	// 1. Valid club
	@Test
	public void testGetClub_validClub() throws Exception {
		RateLimitedTestRunner.run(() -> {
			final StravaClub club = strava().getClub(TestUtils.CLUB_VALID_ID);
			assertNotNull(club);
			StravaClubTest.validate(club, TestUtils.CLUB_VALID_ID, club.getResourceState());
		} );
	}

}
