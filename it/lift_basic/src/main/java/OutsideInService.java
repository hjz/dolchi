

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import com.google.gson.Gson;


public class OutsideInService {

	public static final String API_KEY = "66gs4kyjqcgptdf78d4ax836";
	public static final String SHARED_SECRET = "HfH7DEwCdt";
	
	public static Stories getStories(String uuid) throws IllegalStateException, UnsupportedEncodingException, NoSuchAlgorithmException, IOException {
		String json = HTTPHelper.getHTML(getBaseURL() + uuid + "/stories?" + getSignature()).toString();
		Gson gson = new Gson();
		return gson.fromJson(json, Stories.class);
	}
		
	public static Location getLocation(String query) throws IllegalStateException, UnsupportedEncodingException, NoSuchAlgorithmException, IOException {
		String json = HTTPHelper.getHTML(getBaseURL() + "named/" + query + "?" + getSignature()).toString();
		Gson gson = new Gson();
		Locations locations = gson.fromJson(json, Locations.class);
		if(locations.getLocations() != null && locations.getLocations().size() > 0)
			return locations.getLocations().get(0);
		return null;
	}
	
	public static String getBaseURL() {
		return "http://hyperlocal-api.outside.in/v1.1/locations/";
	}
	
	public static String getSignature() throws UnsupportedEncodingException, NoSuchAlgorithmException {
		long unixTime = System.currentTimeMillis() / 1000L;
		new Hex();
		String md5 = Hex.encodeHexString(DigestUtils.md5((API_KEY + SHARED_SECRET + String.valueOf(unixTime))));
		return "dev_key=" + API_KEY + "&sig=" + md5;
	}
	
	public static void main(String[] args) throws NoSuchAlgorithmException, IllegalStateException, IOException {
		System.out.println(getStories(getLocation("california").uuid));
	}
	
	class Locations {
		private List<Location> locations;

		public Locations(List<Location> locations) {
			this.locations = locations;
		}

		public List<Location> getLocations() {
			return locations;
		}

		public void setLocations(List<Location> locations) {
			this.locations = locations;
		}
		
		public String toString() {
			return locations.toString();
		}
		
	}
	
	class Location {
		private String uuid;
		private String lat;
		private String lng;

		public Location(String uuid, String lat, String lng) {
			this.uuid = uuid;
			this.lat = lat;
			this.lng = lng;
		}
		
		public String getLat() {
			return lat;
		}

		public void setLat(String lat) {
			this.lat = lat;
		}

		public String getLng() {
			return lng;
		}

		public void setLng(String lng) {
			this.lng = lng;
		}
		
		public String getUuid() {
			return uuid;
		}

		public void setUuid(String uuid) {
			this.uuid = uuid;
		}
		
		public String toString() {
			return uuid;
		}
	}
	
	class Stories {
		private List<Story> stories;

		public Stories(List<Story> stories) {
			this.stories = stories;
		}

		public List<Story> getStories() {
			return stories;
		}

		public void setStories(List<Story> stories) {
			this.stories = stories;
		}
		
		public String toString() {
			String result = "";
			for(Story story : stories) {
				result += story.toString() + "\n";
			}
			return result;
		}
	}
	
	class Story {
		private String story_url;

		private String title;
		private String uuid;
		private String summary;
		
		public Story(String story_url, String title, String uuid,
				String summary, String published_at) {
			this.story_url = story_url;
			this.title = title;
			this.uuid = uuid;
			this.summary = summary;
			this.published_at = published_at;
		}
		
		private String published_at;
		
		public String getStory_url() {
			return story_url;
		}
		public void setStory_url(String story_url) {
			this.story_url = story_url;
		}
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public String getUuid() {
			return uuid;
		}
		public void setUuid(String uuid) {
			this.uuid = uuid;
		}
		public String getSummary() {
			return summary;
		}
		public void setSummary(String summary) {
			this.summary = summary;
		}
		public String getPublished_at() {
			return published_at;
		}
		public void setPublished_at(String published_at) {
			this.published_at = published_at;
		}
		
		@Override
		public String toString() {
			return "Story: \n\tstory_url: " + story_url + "\n\ttitle: " + title
					+ "\n\tuuid: " + uuid + "\n\tsummary: " + summary
					+ "\n\tpublished_at: " + published_at + "";
		}
	}
}
