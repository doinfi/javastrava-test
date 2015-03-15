package test.apicheck;

import java.io.IOException;

import javastrava.api.v3.model.StravaActivity;
import javastrava.api.v3.model.StravaActivityZone;
import javastrava.api.v3.model.StravaAthlete;
import javastrava.api.v3.model.StravaComment;
import javastrava.api.v3.model.StravaLap;
import javastrava.api.v3.model.StravaPhoto;
import javastrava.api.v3.service.exception.NotFoundException;
import javastrava.api.v3.service.impl.retrofit.Retrofit;
import javastrava.util.exception.JsonSerialisationException;

import org.junit.Test;

import retrofit.client.Response;
import test.apicheck.api.ActivityAPI;
import test.apicheck.api.ResponseValidator;
import test.utils.TestUtils;

/**
 * @author Dan Shannon
 *
 */
public class ActivityAPITest {
	@Test
	public void testAPI_getActivity() throws NotFoundException, JsonSerialisationException, IOException {
		Response response = api().getActivity(TestUtils.ACTIVITY_FOR_AUTHENTICATED_USER, Boolean.FALSE);
		ResponseValidator.validate(response, StravaActivity.class);
	}
	
	@Test
	public void testAPI_listActivityComments() throws NotFoundException, IOException, JsonSerialisationException {
		Response response = api().listActivityComments(TestUtils.ACTIVITY_WITH_COMMENTS, null, null, null);
		ResponseValidator.validate(response, StravaComment.class);
	}
	
	@Test
	public void testAPI_listActivityKudoers() throws JsonSerialisationException, IOException, NotFoundException {
		Response response = api().listActivityKudoers(TestUtils.ACTIVITY_WITH_KUDOS, null, null);
		ResponseValidator.validate(response, StravaAthlete.class);
	}
	
	@Test
	public void testAPI_listActivityLaps() throws NotFoundException, JsonSerialisationException, IOException {
		Response response = api().listActivityLaps(TestUtils.ACTIVITY_WITH_LAPS);
		ResponseValidator.validate(response, StravaLap.class);
	}
	
	@Test
	public void testAPI_listActivityPhotos() throws JsonSerialisationException, IOException, NotFoundException {
		Response response = api().listActivityPhotos(TestUtils.ACTIVITY_WITH_PHOTOS);
		ResponseValidator.validate(response, StravaPhoto.class);
	}
	
	@Test
	public void testAPI_listActivityZones() throws NotFoundException, JsonSerialisationException, IOException {
		Response response = api().listActivityZones(TestUtils.ACTIVITY_WITH_ZONES);
		ResponseValidator.validate(response, StravaActivityZone.class);
	}
	
	@Test
	public void testAPI_listAuthenticatedAthleteActivities() throws JsonSerialisationException, IOException {
		Response response = api().listAuthenticatedAthleteActivities(null, null, null, null);
		ResponseValidator.validate(response, StravaActivity.class);
	}
	
	@Test
	public void testAPI_listFriendsActivities() throws JsonSerialisationException, IOException {
		Response response = api().listFriendsActivities(null, null);
		ResponseValidator.validate(response, StravaActivity.class);
	}
	
	@Test
	public void testAPI_listRelatedActivities() throws NotFoundException, JsonSerialisationException, IOException {
		Response response = api().listRelatedActivities(TestUtils.ACTIVITY_FOR_AUTHENTICATED_USER, null, null);
		ResponseValidator.validate(response, StravaActivity.class);
	}
	
	private ActivityAPI api() {
		return Retrofit.retrofit(ActivityAPI.class, TestUtils.getValidToken());
	}
	
}