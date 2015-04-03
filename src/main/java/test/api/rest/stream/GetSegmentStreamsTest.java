package test.api.rest.stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import javastrava.api.v3.model.StravaStream;
import javastrava.api.v3.model.reference.StravaStreamResolutionType;
import javastrava.api.v3.model.reference.StravaStreamSeriesDownsamplingType;
import javastrava.api.v3.model.reference.StravaStreamType;

import org.junit.Test;

import test.api.model.StravaStreamTest;
import test.api.rest.APITest;
import test.utils.RateLimitedTestRunner;
import test.utils.TestUtils;

public class GetSegmentStreamsTest extends APITest {
	// 4. All stream types
	@Test
	public void testGetSegmentStreams_allStreamTypes() throws Exception {
		RateLimitedTestRunner.run(() -> {
			final StravaStream[] streams = api().getSegmentStreams(TestUtils.SEGMENT_VALID_ID, null, null, null);
			validateArray(streams);
		});
	}

	// 7. Downsampled by distance
	@Test
	public void testGetSegmentStreams_downsampledByDistance() throws Exception {
		RateLimitedTestRunner.run(() -> {
			for (final StravaStreamResolutionType resolutionType : StravaStreamResolutionType.values()) {
				if ((resolutionType != StravaStreamResolutionType.UNKNOWN) && (resolutionType != null)) {
					final StravaStream[] streams = api().getSegmentStreams(TestUtils.SEGMENT_VALID_ID, null, resolutionType,
							StravaStreamSeriesDownsamplingType.DISTANCE);
					validateArray(streams);
				}
			}
		});
	}

	// 6. Downsampled by time - can't be done for segment streams as there's no time element
	@Test
	public void testGetSegmentStreams_downsampledByTime() throws Exception {
		RateLimitedTestRunner.run(() -> {
			for (final StravaStreamResolutionType resolutionType : StravaStreamResolutionType.values()) {
				if (resolutionType != StravaStreamResolutionType.UNKNOWN) {
					try {
						api().getSegmentStreams(TestUtils.SEGMENT_VALID_ID, null, resolutionType, StravaStreamSeriesDownsamplingType.TIME);
					} catch (final IllegalArgumentException e) {
						// expected
				return;
			}
			fail("Can't return a segment stream which is downsampled by TIME!");
		}
	}
})	  ;
	}

	// 9. Invalid downsample resolution
	@Test
	public void testGetSegmentStreams_invalidDownsampleResolution() throws Exception {
		RateLimitedTestRunner.run(() -> {
			try {
				api().getSegmentStreams(TestUtils.SEGMENT_VALID_ID, null, StravaStreamResolutionType.UNKNOWN, null);
			} catch (final IllegalArgumentException e) {
				// Expected
				return;
			}
			fail("Didn't throw an exception when asking for an invalid downsample resolution");
		});
	}

	// 10. Invalid downsample type (i.e. not distance or time)
	@Test
	public void testGetSegmentStreams_invalidDownsampleType() throws Exception {
		RateLimitedTestRunner.run(() -> {
			try {
				api().getSegmentStreams(TestUtils.SEGMENT_VALID_ID, null, StravaStreamResolutionType.LOW, StravaStreamSeriesDownsamplingType.UNKNOWN);
			} catch (final IllegalArgumentException e) {
				// Expected
				return;
			}
			fail("Didn't throw an exception when asking for an invalid downsample type");
		});
	}

	// 2. Invalid segment
	@Test
	public void testGetSegmentStreams_invalidSegment() throws Exception {
		RateLimitedTestRunner.run(() -> {
			final StravaStream[] streams = api().getSegmentStreams(TestUtils.SEGMENT_INVALID_ID, null, null, null);
			assertNull(streams);
		});
	}

	// 8. Invalid stream type
	@Test
	public void testGetSegmentStreams_invalidStreamType() throws Exception {
		RateLimitedTestRunner.run(() -> {
			try {
				api().getSegmentStreams(TestUtils.SEGMENT_VALID_ID, StravaStreamType.UNKNOWN.toString(), null, null);
			} catch (final IllegalArgumentException e) {
				// Expected
				return;
			}
			fail("Should have got an IllegalArgumentException, but didn't");
		});
	}

	// 5. Only one stream type
	@Test
	public void testGetSegmentStreams_oneStreamType() throws Exception {
		RateLimitedTestRunner.run(() -> {
			final StravaStream[] streams = api().getSegmentStreams(TestUtils.SEGMENT_VALID_ID, StravaStreamType.DISTANCE.toString(), null, null);
			assertNotNull(streams);
			assertEquals(1, streams.length);
			assertEquals(StravaStreamType.DISTANCE, streams[0].getType());
			validateArray(streams);
		});
	}

	/**
	 * Test method for
	 * {@link javaapi.api.v3.service.impl.StreamServiceImpl#getSegmentStreams(java.lang.String, javaapi.api.v3.model.reference.StravaStreamType[], javaapi.api.v3.model.reference.StravaStreamResolutionType, javaapi.api.v3.model.reference.StravaStreamSeriesDownsamplingType)}
	 * .
	 *
	 * @throws Exception
	 */
	@Test
	// 1. Valid segment for the authenticated user
	public void testGetSegmentStreams_validSegment() throws Exception {
		RateLimitedTestRunner.run(() -> {
			final StravaStream[] streams = api().getSegmentStreams(TestUtils.SEGMENT_VALID_ID, null, null, null);
			validateArray(streams);
		});
	}

	// 3. Valid segment which is private and belongs to another user
	@Test
	public void testGetSegmentStreams_validSegmentUnauthenticatedUser() throws Exception {
		RateLimitedTestRunner.run(() -> {
			final StravaStream[] streams = api().getSegmentStreams(TestUtils.SEGMENT_OTHER_USER_PRIVATE_ID, null, null, null);
			assertNotNull(streams);
			assertEquals("Shouldn't be able to return segment streams for private segments that don't belong to the authenticated user", 0, streams.length);

		});
	}

	@Test
	public void testGetSegmentStreams_privateSegmentWithViewPrivate() throws Exception {
		RateLimitedTestRunner.run(() -> {
			final StravaStream[] streams = apiWithViewPrivate().getSegmentStreams(TestUtils.SEGMENT_PRIVATE_ID, null, null, null);
			assertNotNull(streams);
			assertFalse(streams.length == 0);
		});
	}

	@Test
	public void testGetSegmentStreams_privateSegmentWithoutViewPrivate() throws Exception {
		RateLimitedTestRunner.run(() -> {
			final StravaStream[] streams = api().getSegmentStreams(TestUtils.SEGMENT_PRIVATE_ID, null, null, null);
			assertNotNull(streams);
			assertTrue(streams.length == 0);
		});
	}

	private void validateArray(final StravaStream[] streams) {
		for (final StravaStream stream : streams) {
			StravaStreamTest.validate(stream);
		}
	}
}