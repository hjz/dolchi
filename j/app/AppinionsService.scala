package apis

import org.jsoup.Jsoup
import collection.JavaConversions._

object AppinionsService {

  val GROUP_API_KEY = "rdv36eev64tga52ceg4664dd";
  val OPINION_API_KEY = "eafb6qq7csys88tky87cegw9";

  def getGroupResults(url: String) = {
    val doc = Jsoup.connect(url).get()
    doc.select("value") map { _.attr("name") }
  }

  def getOpinionHolders(query: String, numResults: Int) = {
     getGroupResults(getBaseGroupsURL() + "&sent=" + query + "&group=opholder_id&group_rows=" + String.valueOf(numResults));
  }

  def getBaseOpinionsURL() =
    getBaseURL() + "opinions?appkey=" + OPINION_API_KEY;

  def getBaseGroupsURL() =
    getBaseURL() + "groups?appkey=" + GROUP_API_KEY;

  def getBaseURL() = "http://api.appinions.com/search/v2/";
}
