package controllers

import play._
import play.mvc._

import dispatch.{Http => DHttp, url => Durl}
import scala.io.Source
import scala.xml.parsing.XhtmlParser
import org.jsoup.Jsoup
import java.util.{Date, GregorianCalendar}
import collection.JavaConversions._
import java.net.URLEncoder
import apis._

case class ArticleInfo (
  geo: String,
  date: Date,
  desc: String,
  keywords: Array[String]
)

object Application extends Controller {

    import views.Application._

  var url = "http://www.nytimes.com/2011/09/11/us/11cncsports.html"

  val DateRegex = """(\d{4})(\d{2})(\d{2})""".r

  def getUrl(u: String): ArticleInfo = {
    val doc = Jsoup.connect(u).get()

    def getStr(name: String): String =
      doc.select("meta[name=%s]".format(name)).attr("content")

    def getDate(name: String): Date =
      doc.select("meta[name=%s]".format(name)).attr("content") match {
        case DateRegex(year, month, day) =>
          new GregorianCalendar(year.toInt, month.toInt, day.toInt).getTime()
      }

    val CityRegex = """(\w+) \(\w+\)""".r

    val g = getStr("geo") match {
      case CityRegex(city) => city
      case v => v
    }

    ArticleInfo (
      geo = URLEncoder.encode(g),
      date = getDate("pdate"),
      desc = getStr("des"),
      keywords = getStr("keywords").split(",") map { URLEncoder.encode }
    )
  }

  def wikis(names: Iterable[String]) = {
    names map { URLEncoder.encode } map { WikipediaService.getArticle }
  }

  def opinionHolders(keywords: Iterable[String]) = {
    val o = keywords flatMap { AppinionsService.getOpinionHolders(_, 3)  }
    o  filter { v => v != "Unknown" && v != "admin" && v != "author" } toSet
  }

  def stories(location: String): Iterable[(String, String)] = {
    if (location.isEmpty) return List()

    try {
      // OutsideInService.getStories(location) map { s => (s.getStory_url, s.getTitle) }
      List()
    } catch {
      case _ => List()
    }
  }

  def index = {
    val u = getUrl(url)
    val names = opinionHolders(u.keywords).toSeq
    val w = wikis(names).toSeq
    val query = u.keywords.mkString(" OR ")
    println(names)
    println(w)
    println(query)
    html.index("Your Scala application is ready! not", names, w, query, url,
    DHttp(Durl(url) as_str))
  }

}
