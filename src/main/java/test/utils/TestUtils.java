package test.utils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

import javastrava.api.v3.auth.TokenManager;
import javastrava.api.v3.auth.TokenService;
import javastrava.api.v3.auth.impl.retrofit.TokenServiceImpl;
import javastrava.api.v3.auth.model.Token;
import javastrava.api.v3.auth.model.TokenResponse;
import javastrava.api.v3.auth.ref.AuthorisationScope;
import javastrava.api.v3.model.StravaActivity;
import javastrava.api.v3.model.StravaAthlete;
import javastrava.api.v3.model.reference.StravaActivityType;
import javastrava.api.v3.service.Strava;
import javastrava.api.v3.service.exception.BadRequestException;
import javastrava.api.v3.service.exception.UnauthorizedException;

/**
 * @author Dan Shannon
 *
 */
public abstract class TestUtils {
	public static String USERNAME;

	public static String PASSWORD;

	public static Integer STRAVA_APPLICATION_ID;

	public static String STRAVA_CLIENT_SECRET;

	public static Token INVALID_TOKEN;

	public static Integer ACTIVITY_WITH_EFFORTS;

	public static Integer ACTIVITY_WITH_PHOTOS;

	public static Integer ACTIVITY_WITHOUT_PHOTOS;

	public static Integer ACTIVITY_FOR_AUTHENTICATED_USER;

	public static Integer ACTIVITY_FOR_UNAUTHENTICATED_USER;

	public static Integer ACTIVITY_INVALID;

	public static Integer ACTIVITY_WITH_COMMENTS;

	public static Integer ACTIVITY_WITHOUT_COMMENTS;

	public static Integer ACTIVITY_WITH_KUDOS;

	public static Integer ACTIVITY_WITHOUT_KUDOS;

	public static Integer ACTIVITY_WITH_LAPS;
	public static Integer ACTIVITY_WITHOUT_LAPS;
	public static Integer ACTIVITY_WITH_ZONES;
	public static Integer ACTIVITY_WITHOUT_ZONES;
	public static Integer ACTIVITY_PRIVATE_OTHER_USER;
	public static Integer ACTIVITY_PRIVATE;
	public static Integer ACTIVITY_PRIVATE_WITH_PHOTOS;

	public static Integer ACTIVITY_PRIVATE_WITH_KUDOS;
	public static Integer ACTIVITY_PRIVATE_WITH_LAPS;
	public static Integer ACTIVITY_PRIVATE_WITH_RELATED_ACTIVITIES;
	public static Integer ACTIVITY_RUN_OTHER_USER;
	public static Integer ACTIVITY_RUN_WITH_SEGMENTS;

	public static Integer ATHLETE_AUTHENTICATED_ID;
	public static Integer ATHLETE_VALID_ID;

	public static Integer ATHLETE_INVALID_ID;
	public static Integer ATHLETE_WITHOUT_KOMS;
	public static Integer ATHLETE_WITHOUT_FRIENDS;
	public static Integer ATHLETE_PRIVATE_ID;
	public static Integer CLUB_VALID_ID;
	public static Integer CLUB_INVALID_ID;

	public static Integer CLUB_PUBLIC_MEMBER_ID;
	public static Integer CLUB_PUBLIC_NON_MEMBER_ID;
	public static Integer CLUB_PRIVATE_MEMBER_ID;
	public static Integer CLUB_PRIVATE_NON_MEMBER_ID;
	public static String GEAR_VALID_ID;
	public static String GEAR_INVALID_ID;

	public static String GEAR_OTHER_ATHLETE_ID;

	public static Long SEGMENT_EFFORT_VALID_ID;
	public static Long SEGMENT_EFFORT_INVALID_ID;
	public static Long SEGMENT_EFFORT_PRIVATE_ID;
	public static Long SEGMENT_EFFORT_OTHER_USER_PRIVATE_ID;

	public static Long SEGMENT_EFFORT_PRIVATE_ACTIVITY_ID;
	public static Integer SEGMENT_VALID_ID;
	public static Integer SEGMENT_INVALID_ID;
	public static Integer SEGMENT_PRIVATE_ID;
	public static Integer SEGMENT_OTHER_USER_PRIVATE_ID;

	public static Integer SEGMENT_HAZARDOUS_ID;
	public static Integer SEGMENT_PRIVATE_STARRED_ID;

	private static final String PROPERTIES_FILE = "test-config";

	private static ResourceBundle properties;

	static {
		try {
			properties = loadPropertiesFile(PROPERTIES_FILE);
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
		USERNAME = properties.getString("username");
		PASSWORD = properties.getString("password");
		STRAVA_APPLICATION_ID = new Integer(properties.getString("strava_application_id"));
		STRAVA_CLIENT_SECRET = properties.getString("client_secret");

		INVALID_TOKEN = createToken(properties.getString("test.invalidToken"), USERNAME);
		ACTIVITY_WITH_EFFORTS = new Integer(properties.getString("test.activityWithEfforts"));
		ACTIVITY_WITH_PHOTOS = new Integer(properties.getString("test.activityWithPhotos"));
		ACTIVITY_WITHOUT_PHOTOS = new Integer(properties.getString("test.activityWithoutPhotos"));
		ACTIVITY_WITH_COMMENTS = new Integer(properties.getString("test.activityWithComments"));
		ACTIVITY_WITHOUT_COMMENTS = new Integer(properties.getString("test.activityWithoutComments"));
		ACTIVITY_WITH_KUDOS = new Integer(properties.getString("test.activityWithKudos"));
		ACTIVITY_WITHOUT_KUDOS = new Integer(properties.getString("test.activityWithoutKudos"));
		ACTIVITY_WITH_LAPS = new Integer(properties.getString("test.activityWithLaps"));
		ACTIVITY_WITHOUT_LAPS = new Integer(properties.getString("test.activityWithoutLaps"));
		ACTIVITY_WITH_ZONES = new Integer(properties.getString("test.activityWithZones"));
		ACTIVITY_WITHOUT_ZONES = new Integer(properties.getString("test.activityWithoutZones"));
		ACTIVITY_FOR_AUTHENTICATED_USER = new Integer(properties.getString("test.activityBelongingToAuthenticatedUser"));
		ACTIVITY_FOR_UNAUTHENTICATED_USER = new Integer(properties.getString("test.activityBelongingToUnauthenticatedUser"));
		ACTIVITY_INVALID = new Integer(properties.getString("test.activityInvalid"));
		ACTIVITY_PRIVATE_OTHER_USER = integerProperty("test.activityPrivateOtherUser");
		ACTIVITY_PRIVATE = integerProperty("test.activityPrivateAuthenticatedUser");
		ACTIVITY_PRIVATE_WITH_PHOTOS = integerProperty("test.activityPrivatePhotos");
		ACTIVITY_PRIVATE_WITH_KUDOS = integerProperty("test.activityPrivateKudos");
		ACTIVITY_PRIVATE_WITH_LAPS = integerProperty("test.activityPrivateLaps");
		ACTIVITY_PRIVATE_WITH_RELATED_ACTIVITIES = integerProperty("test.activityPrivateRelated");
		ACTIVITY_RUN_OTHER_USER = integerProperty("test.activityRunOtherUser");
		ACTIVITY_RUN_WITH_SEGMENTS = integerProperty("test.activityRunWithSegments");

		ATHLETE_AUTHENTICATED_ID = integerProperty("test.authenticatedAthleteId");
		ATHLETE_VALID_ID = integerProperty("test.athleteId");
		ATHLETE_INVALID_ID = integerProperty("test.athleteInvalidId");
		ATHLETE_WITHOUT_KOMS = integerProperty("test.athleteWithoutKOMs");
		ATHLETE_WITHOUT_FRIENDS = integerProperty("test.athleteWithoutFriends");
		ATHLETE_PRIVATE_ID = integerProperty("test.athletePrivate");

		CLUB_VALID_ID = integerProperty("test.clubId");
		CLUB_INVALID_ID = integerProperty("test.clubInvalidId");
		CLUB_PRIVATE_MEMBER_ID = integerProperty("test.clubPrivateMemberId");
		CLUB_PRIVATE_NON_MEMBER_ID = integerProperty("test.clubPrivateNonMemberId");
		CLUB_PUBLIC_NON_MEMBER_ID = integerProperty("test.clubNonMemberId");
		CLUB_PUBLIC_MEMBER_ID = integerProperty("test.clubPublicMemberId");

		GEAR_VALID_ID = properties.getString("test.gearId");
		GEAR_INVALID_ID = properties.getString("test.gearInvalidId");
		GEAR_OTHER_ATHLETE_ID = properties.getString("test.gearOtherAthleteId");

		SEGMENT_EFFORT_INVALID_ID = longProperty("test.segmentEffortInvalidId");
		SEGMENT_EFFORT_VALID_ID = longProperty("test.segmentEffortId");
		SEGMENT_EFFORT_PRIVATE_ID = longProperty("test.segmentEffortPrivateId");
		SEGMENT_EFFORT_OTHER_USER_PRIVATE_ID = longProperty("test.segmentEffortOtherUserPrivateId");
		SEGMENT_EFFORT_PRIVATE_ACTIVITY_ID = longProperty("test.segmentEffortPrivateActivityId");

		SEGMENT_VALID_ID = integerProperty("test.segmentId");
		SEGMENT_INVALID_ID = integerProperty("test.segmentInvalidId");
		SEGMENT_PRIVATE_ID = integerProperty("test.segmentPrivateId");
		SEGMENT_OTHER_USER_PRIVATE_ID = integerProperty("test.segmentOtherUserPrivateId");
		SEGMENT_HAZARDOUS_ID = integerProperty("test.segmentHazardousId");
		SEGMENT_PRIVATE_STARRED_ID = integerProperty("test.segmentPrivateStarredId");
	}

	/**
	 * @return
	 */
	public static StravaActivity createDefaultActivity(final String name) {
		final StravaActivity activity = new StravaActivity();
		activity.setName(name);
		activity.setType(StravaActivityType.RIDE);
		activity.setStartDateLocal(LocalDateTime.now());
		activity.setElapsedTime(1000);
		activity.setDescription("Created by Strava API v3 Java");
		activity.setDistance(1000.1F);
		return activity;
	}

	/**
	 * @param accessToken
	 * @return
	 */
	private static Token createToken(final String accessToken, final String username) {
		final TokenResponse tokenResponse = new TokenResponse();
		tokenResponse.setAccessToken(accessToken);
		final StravaAthlete athlete = new StravaAthlete();
		athlete.setEmail(username);
		tokenResponse.setAthlete(athlete);
		tokenResponse.setTokenType("Bearer");
		final Token token = new Token(tokenResponse);
		return token;
	}

	public static Token getRevokedToken() throws UnauthorizedException {
		final Token token = getValidToken();
		final TokenService service = TokenServiceImpl.instance(token);
		service.deauthorise(token);
		return token;
	}

	public static Token getValidToken() {
		return tokenWithExactScope();
	}

	public static Token getValidTokenWithFullAccess() {
		return tokenWithExactScope(AuthorisationScope.WRITE, AuthorisationScope.VIEW_PRIVATE);
	}

	public static Token getValidTokenWithViewPrivate() {
		return tokenWithExactScope(AuthorisationScope.VIEW_PRIVATE);
	}

	public static Token getValidTokenWithWriteAccess() {
		return tokenWithExactScope(AuthorisationScope.WRITE);
	}

	/**
	 * @param key
	 * @return
	 */
	private static Integer integerProperty(final String key) {
		return new Integer(properties.getString(key));
	}

	/**
	 * @param propertiesFile
	 * @return
	 */
	private static ResourceBundle loadPropertiesFile(final String propertiesFile) throws IOException {
		return ResourceBundle.getBundle(propertiesFile);
	}

	private static Long longProperty(final String key) {
		return new Long(properties.getString(key));
	}

	public static Strava strava() {
		return new Strava(getValidToken());
	}

	public static Strava stravaWithFullAccess() {
		return new Strava(getValidTokenWithFullAccess());
	}

	/**
	 * @return
	 */
	public static Strava stravaWithViewPrivate() {
		return new Strava(getValidTokenWithViewPrivate());
	}

	public static Strava stravaWithWriteAccess() {
		return new Strava(getValidTokenWithWriteAccess());
	}

	private static Token tokenWithExactScope(final AuthorisationScope... scopes) {
		Token token = TokenManager.instance().retrieveTokenWithExactScope(USERNAME, scopes);
		if (token == null) {
			try {
				token = TestHttpUtils.getStravaAccessToken(USERNAME, PASSWORD, scopes);
				TokenManager.instance().storeToken(token);
			} catch (BadRequestException | UnauthorizedException e) {
				return null;
			}
		}
		return token;

	}

}
