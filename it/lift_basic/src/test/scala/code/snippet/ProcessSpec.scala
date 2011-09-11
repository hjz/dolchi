package code.snippet

import org.specs.Specification

object ProcessSpec extends Specification {
  "Process" should {
    "parse" in {
      val url = "http://www.nytimes.com/2011/09/11/us/sept-11-reckoning/queens.html"
      println(Process.getUrl(url))
    }
  }
}
