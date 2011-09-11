package code
package snippet

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
import java.net.URLEncoder

/**
 * A snippet transforms input to output... it transforms
 * templates to dynamic content.  Lift's templates can invoke
 * snippets and the snippets are resolved in many different
 * ways including "by convention".  The snippet package
 * has named snippets and those snippets can be classes
 * that are instantiated when invoked or they can be
 * objects, singletons.  Singletons are useful if there's
 * no explicit state managed in the snippet.
 */
object Process {

  /**
   * The render method in this case returns a function
   * that transforms NodeSeq => NodeSeq.  In this case,
   * the function transforms a form input element by attaching
   * behavior to the input.  The behavior is to send a message
   * to the ApiServer and then returns JavaScript which
   * clears the input.
   */
  def render = {
    var url = ""
    def process() {
      S.redirectTo("/display?url=%s".format(URLEncoder.encode(url)))
    }
    "url=url" #> SHtml.onSubmit(url = _)
    "type=submit" #> SHtml.onSubmitUnit(process)
  }
    // when the form is submitted, process the variable
}
