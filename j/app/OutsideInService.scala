package apis

import dispatch._

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

/*
// Parse JSON arrays
parse[List[Int]]("[1,2,3]") //=> List(1,2,3)

// Parse JSON objects
parse[Map[String, Int]]("""{"one":1,"two":2}""") //=> Map("one"->1,"two"->2)

// Parse JSON objects as case classes
// (Parsing case classes isn't supported in the REPL.)
case class Person(id: Long, name: String)
parse[Person]("""{"id":1,"name":"Coda"}""") //=> Person(1,"Coda")

// Parse streaming arrays of things
for (person <- stream[Person](inputStream)) {
  println("New person: " + person)
}

         "category": {
                "display_name": "Sub-Metro Area",
                "name": "sub_metro"
            },
            "city": "Training",
            "display_name": "Chicago",
            "hot_for_requested": false,
            "lat": 41.838272094726562,
            "lng": -87.690010070800781,
            "state": "Illinois",
            "state_abbrev": "IL",
            "url": "http://outside.in/chicago-chicagoland-il?utm_content=Chicago&utm_source=API_OutsideIn_0&utm_medium=API&utm_campaign=APILinkback",
            "url_name": "chicago-chicagoland-il",
            "uuid": "e9cf5975-9c01-4fbb-bebd-ab004ff20616"
        },

*/

case class Location(
  display_name: String,
  state: String,
  url: String,
  lng: String,
  lat: String,
  uuid: String,
  city: String,
  category: Map[String, String],
  hot_for_requested: String,
  state_abbrev: String,
  url_name: String
)

case class Locations(total: String, locs: List[Location])

object OutsideInService {

  val API_KEY = "66gs4kyjqcgptdf78d4ax836"
  val SHARED_SECRET = "HfH7DEwCdt"

  def getStories(query: String): Iterable[(String, String)] = {
    val uuid = getLocation(query)
    val json = Http(url(getBaseURL() + uuid + "/stories?" + getSignature()) as_str)
    println(json)
    List()
  }

  def getLocation(query: String): String = {
    val json = Http(url(getBaseURL() + "named/" + query + "?" + getSignature()) as_str)
    val JO = """"uuid":"(.*)".*""".r
    val o = json.split(',')
    val x = o foreach { case JO(uuid) => return uuid }
    ""
  }

  def getBaseURL() = "http://hyperlocal-api.outside.in/v1.1/locations/"

 def getSignature() = {
    val unixTime = System.currentTimeMillis() / 1000L
    new Hex()
    val md5 = Hex.encodeHexString(DigestUtils.md5((API_KEY + SHARED_SECRET + String.valueOf(unixTime))))
    "dev_key=" + API_KEY + "&sig=" + md5
  }

}
