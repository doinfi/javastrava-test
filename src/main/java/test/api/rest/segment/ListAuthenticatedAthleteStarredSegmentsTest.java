package test.api.rest.segment;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import javastrava.api.v3.model.StravaSegment;
import javastrava.api.v3.model.reference.StravaResourceState;

import org.junit.Test;

import test.api.model.StravaSegmentTest;
import test.api.rest.util.ArrayCallback;
import test.api.rest.util.PagingArrayMethodTest;
import test.utils.RateLimitedTestRunner;

public class ListAuthenticatedAthleteStarredSegmentsTest extends PagingArrayMethodTest<StravaSegment, Integer> {
	@Override
	protected ArrayCallback<StravaSegment> callback() {
		return (paging -> api().listAuthenticatedAthleteStarredSegments(paging.getPage(), paging.getPageSize()));
	}

	// Test cases:
	// 1. No paging
	@Test
	public void testListAuthenticatedAthleteStarredSegments_noPaging() throws Exception {
		RateLimitedTestRunner.run(() -> {
			final StravaSegment[] segments = api().listAuthenticatedAthleteStarredSegments(null, null);
			assertNotNull(segments);
			assertFalse(segments.length == 0);
			validateList(segments);
		});
	}

	@Test
	public void testListAuthenticatedAthleteStarredSegments_privateWithoutViewPrivate() throws Exception {
		RateLimitedTestRunner.run(() -> {
			final StravaSegment[] segments = api().listAuthenticatedAthleteStarredSegments(null, null);
			for (final StravaSegment segment : segments) {
				if ((segment.getPrivateSegment() != null) && segment.getPrivateSegment().equals(Boolean.TRUE)) {
					fail("Returned at least one private starred segment");
				}
			}
		});
	}

	@Test
	public void testListAuthenticatedAthleteStarredSegments_privateWithViewPrivate() throws Exception {
		RateLimitedTestRunner.run(() -> {
			final StravaSegment[] segments = api().listAuthenticatedAthleteStarredSegments(null, null);
			boolean pass = false;
			for (final StravaSegment segment : segments) {
				if (segment.getResourceState() == StravaResourceState.PRIVATE) {
					pass = true;
				}
			}
			assertTrue(pass);
		});
	}

	@Override
	protected void validate(final StravaSegment segment) {
		StravaSegmentTest.validateSegment(segment);

	}

	@Override
	protected void validate(final StravaSegment segment, final Integer id, final StravaResourceState state) {
		StravaSegmentTest.validateSegment(segment, id, state);

	}

}
