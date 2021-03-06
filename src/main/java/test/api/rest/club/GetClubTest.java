package test.api.rest.club;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.Test;

import javastrava.api.v3.model.StravaClub;
import javastrava.api.v3.service.exception.UnauthorizedException;
import test.api.model.StravaClubTest;
import test.api.rest.APIGetTest;
import test.utils.RateLimitedTestRunner;
import test.utils.TestUtils;

public class GetClubTest extends APIGetTest<StravaClub, Integer> {
	/**
	 *
	 */
	public GetClubTest() {
		this.getCallback = (api, id) -> api.getClub(id);
	}

	/**
	 * @see test.api.rest.APIGetTest#invalidId()
	 */
	@Override
	protected Integer invalidId() {
		return TestUtils.CLUB_INVALID_ID;
	}

	/**
	 * @see test.api.rest.APIGetTest#privateId()
	 */
	@Override
	protected Integer privateId() {
		return null;
	}

	/**
	 * @see test.api.rest.APIGetTest#privateIdBelongsToOtherUser()
	 */
	@Override
	protected Integer privateIdBelongsToOtherUser() {
		return null;
	}

	// 3. Private club of which current authenticated athlete is a member
	@Test
	public void testGetClub_privateClubIsMember() throws Exception {
		RateLimitedTestRunner.run(() -> {
			final StravaClub club = api().getClub(TestUtils.CLUB_PRIVATE_MEMBER_ID);
			assertNotNull(club);
			StravaClubTest.validate(club, TestUtils.CLUB_PRIVATE_MEMBER_ID, club.getResourceState());
		} );
	}

	// 4. Private club of which current authenticated athlete is NOT a member
	@Test
	public void testGetClub_privateClubIsNotMember() throws Exception {
		RateLimitedTestRunner.run(() -> {
			try {
				api().getClub(TestUtils.CLUB_PRIVATE_NON_MEMBER_ID);
			} catch (final UnauthorizedException e) {
				// expected
				return;
			}
			fail("Returned details of a private club of which the authenticated athlete is not a member");
		} );
	}

	/**
	 * @see test.api.rest.APITest#validate(java.lang.Object)
	 */
	@Override
	protected void validate(final StravaClub result) throws Exception {
		StravaClubTest.validate(result);

	}

	/**
	 * @see test.api.rest.APIGetTest#validId()
	 */
	@Override
	protected Integer validId() {
		return TestUtils.CLUB_VALID_ID;
	}

	/**
	 * @see test.api.rest.APIGetTest#validIdBelongsToOtherUser()
	 */
	@Override
	protected Integer validIdBelongsToOtherUser() {
		return null;
	}

}
