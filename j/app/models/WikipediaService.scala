package apis

import dispatch._

object WikipediaService {

  val Wiki = """\[.*,\["(.*?)".*""".r

  def getArticle(query: String) = {
    Http(url("http://en.wikipedia.org/w/api.php?action=opensearch&search=" + query) as_str) match {
      case Wiki(name) => name
    }
  }

}
