package test.api.service.impl.clubservice;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Test;

import javastrava.api.v3.model.StravaActivity;
import test.api.model.StravaActivityTest;
import test.api.service.StravaTest;
import test.utils.RateLimitedTestRunner;
import test.utils.TestUtils;

public class ListAllRecentClubActivitiesTest extends StravaTest {
	/**
	 * Check that no activity flagged as private is returned
	 */
	@Test
	public void testListAllRecentClubActivities_checkPrivacy() throws Exception {
		RateLimitedTestRunner.run(() -> {
			final List<StravaActivity> activities = strava()
					.listAllRecentClubActivities(TestUtils.CLUB_PUBLIC_MEMBER_ID);
			for (final StravaActivity activity : activities) {
				if (activity.getPrivateActivity().equals(Boolean.TRUE)) {
					fail("List recent club activities returned an activity flagged as private!");
				}
			}
		} );
	}

	@Test
	public void testListAllRecentClubActivities_invalidClub() throws Exception {
		RateLimitedTestRunner.run(() -> {
			final List<StravaActivity> activities = strava().listAllRecentClubActivities(TestUtils.CLUB_INVALID_ID);
			assertNull(activities);
		} );
	}

	@Test
	public void testListAllRecentClubActivities_privateClubMember() throws Exception {
		RateLimitedTestRunner.run(() -> {
			final List<StravaActivity> activities = strava()
					.listAllRecentClubActivities(TestUtils.CLUB_PRIVATE_MEMBER_ID);
			assertNotNull(activities);
			for (final StravaActivity activity : activities) {
				StravaActivityTest.validateActivity(activity);
			}
		} );
	}

	@Test
	public void testListAllRecentClubActivities_privateClubNonMember() throws Exception {
		RateLimitedTestRunner.run(() -> {
			final List<StravaActivity> activities = strava()
					.listAllRecentClubActivities(TestUtils.CLUB_PRIVATE_NON_MEMBER_ID);
			assertNotNull(activities);
			assertEquals(0, activities.size());
		} );
	}

	@Test
	public void testListAllRecentClubActivities_publicClubNonMember() throws Exception {
		RateLimitedTestRunner.run(() -> {
			final List<StravaActivity> activities = strava()
					.listAllRecentClubActivities(TestUtils.CLUB_PUBLIC_NON_MEMBER_ID);
			assertNotNull(activities);
			for (final StravaActivity activity : activities) {
				StravaActivityTest.validateActivity(activity);
			}
		} );
	}

	@Test
	public void testListAllRecentClubActivities_validClub() throws Exception {
		RateLimitedTestRunner.run(() -> {
			final List<StravaActivity> activities = strava().listAllRecentClubActivities(TestUtils.CLUB_VALID_ID);
			assertNotNull(activities);
			for (final StravaActivity activity : activities) {
				StravaActivityTest.validateActivity(activity);
			}
		} );
	}
}
