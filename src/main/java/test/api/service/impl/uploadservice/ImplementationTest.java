package test.api.service.impl.uploadservice;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import javastrava.api.v3.auth.model.Token;
import javastrava.api.v3.model.StravaUploadResponse;
import javastrava.api.v3.service.UploadService;
import javastrava.api.v3.service.exception.UnauthorizedException;
import javastrava.api.v3.service.impl.UploadServiceImpl;

import org.junit.Test;

import test.api.service.standardtests.spec.InstanceTestSpec;
import test.utils.RateLimitedTestRunner;
import test.utils.TestUtils;

public class ImplementationTest implements InstanceTestSpec {

	@Override
	@Test
	public void testImplementation_differentImplementationIsNotCached() throws Exception {
		RateLimitedTestRunner.run(() -> {
			final UploadService service = UploadServiceImpl.instance(TestUtils.getValidToken());
			final UploadService service2 = UploadServiceImpl.instance(TestUtils.getValidTokenWithWriteAccess());
			assertFalse(service == service2);
		});
	}

	@Override
	@Test
	public void testImplementation_implementationIsCached() throws Exception {
		RateLimitedTestRunner.run(() -> {
			final Token token = TestUtils.getValidToken();
			final UploadService service = UploadServiceImpl.instance(token);
			final UploadService service2 = UploadServiceImpl.instance(token);
			assertEquals(service, service2);
		});
	}

	@Override
	@Test
	public void testImplementation_invalidToken() throws Exception {
		RateLimitedTestRunner.run(() -> {
			try {
				final UploadService service = UploadServiceImpl.instance(TestUtils.INVALID_TOKEN);
				service.checkUploadStatus(TestUtils.ACTIVITY_FOR_AUTHENTICATED_USER);
			} catch (final UnauthorizedException e) {
				// Expected behaviour
				return;
			}
			fail("Got a usable implementation from an invalid token");
		});
	}

	@Override
	@Test
	public void testImplementation_revokedToken() throws Exception {
		RateLimitedTestRunner.run(() -> {
			try {
				final UploadService service = UploadServiceImpl.instance(TestUtils.getRevokedToken());
				service.checkUploadStatus(TestUtils.ACTIVITY_FOR_AUTHENTICATED_USER);
			} catch (final UnauthorizedException e) {
				// Expected
				return;
			}
			fail("Got a service implementation with a valid token!");
		});
	}

	/**
	 * Test method for {@link javastrava.api.v3.service.impl.StreamServiceImpl#instance(java.lang.String)}.
	 *
	 * @throws Exception
	 */
	@Override
	@Test
	public void testImplementation_validToken() throws Exception {
		RateLimitedTestRunner.run(() -> {
			final UploadService service = UploadServiceImpl.instance(TestUtils.getValidToken());
			assertNotNull("Didn't get a service implementation using a valid token", service);
			final StravaUploadResponse response = service.checkUploadStatus(TestUtils.ACTIVITY_FOR_AUTHENTICATED_USER);
			assertNotNull(response);
		});
	}

}
