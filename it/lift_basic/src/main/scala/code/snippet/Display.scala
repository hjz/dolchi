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

object Display {

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

    ArticleInfo (
      geo = getStr("geo"),
      date = getDate("pdate"),
      desc = getStr("des"),
      keywords = getStr("keywords").split(",")
    )
  }

  def render = "#current_time" #> S.param("url")
}
