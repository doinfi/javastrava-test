package test.api.service.standardtests.callbacks;

import java.util.List;

import javastrava.api.v3.service.Strava;
import javastrava.util.Paging;

public interface ListCallback<T, U> {
	public List<T> getList(Strava strava, Paging paging, U parentId);
}
