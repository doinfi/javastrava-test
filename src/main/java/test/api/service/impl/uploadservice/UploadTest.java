package test.api.service.impl.uploadservice;

import static org.junit.Assert.fail;

import java.io.File;

import javastrava.api.v3.model.StravaActivity;
import javastrava.api.v3.model.StravaUploadResponse;
import javastrava.api.v3.model.reference.StravaActivityType;
import javastrava.api.v3.model.reference.StravaResourceState;
import javastrava.api.v3.service.exception.NotFoundException;
import javastrava.api.v3.service.exception.UnauthorizedException;

import org.junit.Test;

import test.api.service.StravaTest;
import test.utils.RateLimitedTestRunner;

public class UploadTest extends StravaTest {
	@Test
	public void testUpload_badActivityType() throws Exception {
		RateLimitedTestRunner.run(() -> {
			final File file = new File("hyperdrive.gpx");
			final StravaUploadResponse response = stravaWithWriteAccess().upload(StravaActivityType.UNKNOWN,
					"UploadServicesImplTest,testUpload_badActivityType", null, null, null, null, "gpx", "ABC", file);
			waitForCompletionAndDelete(response);
		});
	}

	@Test
	public void testUpload_badDataType() throws Exception {
		RateLimitedTestRunner.run(() -> {
			final File file = new File("hyperdrive.gpx");
			StravaUploadResponse response = null;
			try {
				response = stravaWithWriteAccess().upload(StravaActivityType.RIDE, "UploadServicesImplTest.testUpload_badDataType", null, null, null, null,
						"UNKNOWN", "ABC", file);
			} catch (final IllegalArgumentException e) {
				// Expected
				return;
			}

			stravaWithWriteAccess().deleteActivity(response.getActivityId());
			fail("Uploaded a file with a bad data type!");
		});
	}

	@Test
	public void testUpload_badFileContent() throws Exception {
		RateLimitedTestRunner.run(() -> {
			final File file = new File("baddata.gpx");
			StravaUploadResponse response = null;
			response = stravaWithWriteAccess().upload(StravaActivityType.RIDE, "UploadServicesImplTest.testUpload_noName", null, null, null, null, "gpx",
					"ABC", file);
			response = waitForUploadCompletion(response);
			if (response.getStatus().equals("There was an error processing your activity.")) {
				return;
			}
			stravaWithWriteAccess().deleteActivity(response.getActivityId());
			fail("Uploaded a file with an invalid file!");
		});
	}

	/**
	 * @param response
	 * @return
	 */
	private StravaUploadResponse waitForUploadCompletion(StravaUploadResponse response) {
		while (response.getStatus().equals("Your activity is still being processed.")) {
			response = stravaWithWriteAccess().checkUploadStatus(response.getId());
			try {
				Thread.sleep(1000);
			} catch (final InterruptedException e) {
				// ignore
			}
		}
		return response;
	}

	@Test
	public void testUpload_noFile() throws Exception {
		RateLimitedTestRunner.run(() -> {
			final File file = null;
			StravaUploadResponse response = null;
			try {
				response = stravaWithWriteAccess().upload(StravaActivityType.RIDE, "UploadServicesImplTest.testUpload_noName", null, null, null, null, "gpx",
						"ABC", file);
			} catch (final IllegalArgumentException e) {
				// Expected
				return;
			}

			stravaWithWriteAccess().deleteActivity(response.getActivityId());
			fail("Uploaded a file with no actual file!");
		});
	}

	@Test
	public void testUpload_noName() throws Exception {
		RateLimitedTestRunner.run(() -> {
			final File file = new File("hyperdrive.gpx");
			final StravaUploadResponse response = stravaWithWriteAccess().upload(StravaActivityType.RIDE, null, "UploadServicesImplTest.testUpload_noName",
					null, null, null, "gpx", "ABC", file);
			waitForCompletionAndDelete(response);
		});
	}

	@Test
	public void testUpload_noWriteAccess() throws Exception {
		RateLimitedTestRunner.run(() -> {
			final File file = new File("hyperdrive.gpx");
			StravaUploadResponse response = null;
			try {
				response = strava().upload(StravaActivityType.RIDE, "UploadServicesImplTest.testUpoad_noWriteAccess", null, Boolean.TRUE, null, null, "gpx",
						"testUpload_noWriteAccess", file);
			} catch (final UnauthorizedException e) {
				// Expected
				return;
			}

			// Delete the activity again (if we get there, it's been created in error)
			stravaWithWriteAccess().deleteActivity(response.getActivityId());

			// Fail
			fail("Uploaded an activity without write access!");

		});
	}

	@Test
	public void testUpload_valid() throws Exception {
		RateLimitedTestRunner.run(() -> {
			final File file = new File("hyperdrive.gpx");
			final StravaUploadResponse response = stravaWithWriteAccess().upload(StravaActivityType.RIDE, "UploadServicesImplTest", null, null, null, null,
					"gpx", "ABC", file);
			waitForCompletionAndDelete(response);
		});
	}

	private void waitForCompletionAndDelete(final StravaUploadResponse response) throws NotFoundException {
		final Integer id = response.getId();
		StravaUploadResponse localResponse = null;
		boolean loop = true;
		while (loop) {
			localResponse = strava().checkUploadStatus(id);
			if (!localResponse.getStatus().equals("Your activity is still being processed.")) {
				loop = false;
			} else {
				try {
					Thread.sleep(1000);
				} catch (final InterruptedException e) {
					// Ignore and continue
				}
			}
		}
		if (localResponse.getStatus().equals("Your activity is ready.")) {
			loop = true;
			while (loop) {
				final StravaActivity activity = strava().getActivity(localResponse.getActivityId());
				if ((activity != null) && (activity.getResourceState() != StravaResourceState.UPDATING)) {
					loop = false;
				} else {
					try {
						Thread.sleep(1000);
					} catch (final InterruptedException e) {
						// Ignore and continue
					}
				}
			}
			stravaWithWriteAccess().deleteActivity(localResponse.getActivityId());
		}

	}

}
