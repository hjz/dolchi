package code.snippet

import net.liftweb._
import http._
import js._
import JsCmds._
import JE._
import dispatch._
import http._
import util.Helpers._

import scala.io.Source
import scala.xml.parsing.XhtmlParser
import org.jsoup.Jsoup
import java.util.{Date, GregorianCalendar}
import collection.JavaConversions._
import java.net.URLEncoder

case class ArticleInfo (
  geo: String,
  date: Date,
  desc: String,
  keywords: Array[String]
)

object Display {
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
    names map { URLEncoder.encode }map { WikipediaService.getArticle }
  }

  def opinionHolders(keywords: Iterable[String]) = {
    val o = keywords flatMap { AppinionsService.getOpinionHolders(_, 3)  }
    o map { _.name } filter { v => v != "Unknown" && v != "admin" && v != "author" } toSet
  }

  def stories(location: String): Iterable[(String, String)] = {
    if (location.isEmpty) return List()

    try {
      OutsideInService.getStories(OutsideInService.getLocation(location).getUuid).getStories map { s => (s.getStory_url, s.getTitle) }
    } catch {
      case _ => List()
    }
  }

  def renderTest = {
    val u = getUrl(url)
    val names = opinionHolders(u.keywords)
    val w = wikis(names)
    val sts = stories(u.geo)
    println("Geo = %s".format(u.geo))
    sts foreach println
  }

  def render = {
    "#current_time" #> "foobar"
  }
}
