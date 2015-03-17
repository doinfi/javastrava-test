package test.api.model;

import static org.junit.Assert.assertNotNull;
import javastrava.api.v3.model.StravaActivityPhotos;
import test.utils.BeanTest;

/**
 * @author Dan Shannon
 *
 */
public class StravaActivityPhotosTest extends BeanTest<StravaActivityPhotos> {

	/**
	 * @see test.utils.BeanTest#getClassUnderTest()
	 */
	@Override
	protected Class<StravaActivityPhotos> getClassUnderTest() {
		return StravaActivityPhotos.class;
	}
	
	public static void validate(StravaActivityPhotos photos) {
		assertNotNull(photos.getCount());
		assertNotNull(photos.getPrimary());
		StravaPhotoTest.validatePhoto(photos.getPrimary(), photos.getPrimary().getId(), photos.getPrimary().getResourceState());
	}

}