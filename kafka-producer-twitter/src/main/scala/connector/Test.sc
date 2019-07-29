import connector._
import org.json4s.DefaultFormats
import org.json4s.JsonDSL._
import org.json4s.native.JsonMethods.{render, _}

import scala.util.{Failure, Success, Try}


def quoteString(s : String) = s"""\"$s\""""



def makeJson(string : String, fields : String*) = {
  val splitString = string.split("::").map(word => quoteString(word))
  fields.map(word => quoteString(word)).zip(splitString).foldLeft("")((x,y) => x + y)
}

val output = makeJson("RT @PTIofficial: InshAllah this is our vision for Pakistan. Merit &amp; Equality for all! " +
  "Even Pakistan cricket team is an example of lack of m…::" +
  "::Entities(List(),List(),List(),Some(List(UserMention(127483019,127483019,List(3, 15),PTI,PTIofficial))))", List("text", "place", "entities"): _*)

val list = List(1,2,3)




list.foldLeft(0)((x,y) => x + y)

implicit val formats = DefaultFormats

case class Winner(id: Long, numbers: List[Int])
case class Lotto(id: Long, winningNumbers: List[Int], winners: List[Winner], drawDate: Option[java.util.Date], shop : Shop)
case class Shop(name : String, area : Area)
case class Area(name : String)

val winners = List(Winner(23, List(2, 45, 34, 23, 3, 5)), Winner(54, List(52, 3, 12, 11, 18, 22)))
val lotto = Lotto(5, List(2, 45, 34, 23, 7, 5, 3), winners, None, Shop("New Market", Area("Addington")))

val json = ("lotto" ->
  ("id" -> lotto.id) ~
    ("winningNumbers" -> lotto.winningNumbers) ~
    ("draw-date" -> lotto.drawDate.map(_.toString)) ~
    ("winners" ->
      (lotto.winners.map { f =>
        ("winner-id" -> f.id) ~
          ("winner_numbers" -> f.numbers)})) ~
    ("shop" -> lotto.shop.name) ~
    ("area" -> (lotto.shop.area.name))
  )

println(pretty(render(json)))

case class TweetKafkaFields (
                              text: String
                              , place: Option[Place]
                              , entities: Entities
                            )

/*
case class Entities(hashtags: Seq[Hashtag], media: Seq[Media], urls: Seq[Url], user_mentions: Option[Seq[UserMention]])
case class Hashtag(indices: Seq[Int], text: String)
case class Url(display_url: String, expanded_url: String, indices: Seq[Int], url: String)
case class UserMention(id: Long, id_str: String, indices: Seq[Int], name: String, screen_name: String)
case class Media(
                  display_url: String,
                  expanded_url: String,
                  id: Long,
                  id_str: String,
                  indices: Seq[Int],
                  media_url: String,
                  media_url_https: String,
                  sizes: Sizes,
                  source_status_id: Option[Long],
                  source_status_id_str: Option[String],
                  `type`: String,
                  url: String
                )
*/

val sampleJsonString = "{\"created_at\":\"Sun Jul 21 10:07:48 +0000 2019\",\"id\":1152882860870766592,\"id_str\":\"1152882860870766592\",\"text\":\"RT @PRETEND37593601: Faucet Resets Every 10 Mins (250 - 2500 Per Hour)\\nGames , Casino ,  Offerwalls , Bonuses !!!\\nhttps:\\/\\/t.co\\/G4fS33048L\\u2026\",\"source\":\"\\u003ca href=\\\"https:\\/\\/mobile.twitter.com\\\" rel=\\\"nofollow\\\"\\u003eTwitter Web App\\u003c\\/a\\u003e\",\"truncated\":false,\"in_reply_to_status_id\":null,\"in_reply_to_status_id_str\":null,\"in_reply_to_user_id\":null,\"in_reply_to_user_id_str\":null,\"in_reply_to_screen_name\":null,\"user\":{\"id\":1113236938612310022,\"id_str\":\"1113236938612310022\",\"name\":\"PRETENDER\",\"screen_name\":\"PRETEND37593601\",\"location\":\"Chita, Russia\",\"url\":null,\"description\":\"PRETENDER\",\"translator_type\":\"none\",\"protected\":false,\"verified\":false,\"followers_count\":93,\"friends_count\":1136,\"listed_count\":0,\"favourites_count\":7426,\"statuses_count\":19411,\"created_at\":\"Wed Apr 03 00:29:03 +0000 2019\",\"utc_offset\":null,\"time_zone\":null,\"geo_enabled\":false,\"lang\":null,\"contributors_enabled\":false,\"is_translator\":false,\"profile_background_color\":\"F5F8FA\",\"profile_background_image_url\":\"\",\"profile_background_image_url_https\":\"\",\"profile_background_tile\":false,\"profile_link_color\":\"1DA1F2\",\"profile_sidebar_border_color\":\"C0DEED\",\"profile_sidebar_fill_color\":\"DDEEF6\",\"profile_text_color\":\"333333\",\"profile_use_background_image\":true,\"profile_image_url\":\"http:\\/\\/pbs.twimg.com\\/profile_images\\/1143139690326790144\\/2GDdOJxf_normal.jpg\",\"profile_image_url_https\":\"https:\\/\\/pbs.twimg.com\\/profile_images\\/1143139690326790144\\/2GDdOJxf_normal.jpg\",\"profile_banner_url\":\"https:\\/\\/pbs.twimg.com\\/profile_banners\\/1113236938612310022\\/1561380680\",\"default_profile\":true,\"default_profile_image\":false,\"following\":null,\"follow_request_sent\":null,\"notifications\":null},\"geo\":null,\"coordinates\":null,\"place\":null,\"contributors\":null,\"retweeted_status\":{\"created_at\":\"Sun Jul 21 08:40:25 +0000 2019\",\"id\":1152860873070915585,\"id_str\":\"1152860873070915585\",\"text\":\"Faucet Resets Every 10 Mins (250 - 2500 Per Hour)\\nGames , Casino ,  Offerwalls , Bonuses !!!\\u2026 https:\\/\\/t.co\\/oLPbLYqLs3\",\"display_text_range\":[0,140],\"source\":\"\\u003ca href=\\\"https:\\/\\/mobile.twitter.com\\\" rel=\\\"nofollow\\\"\\u003eTwitter Web App\\u003c\\/a\\u003e\",\"truncated\":true,\"in_reply_to_status_id\":1152757723248943105,\"in_reply_to_status_id_str\":\"1152757723248943105\",\"in_reply_to_user_id\":1113236938612310022,\"in_reply_to_user_id_str\":\"1113236938612310022\",\"in_reply_to_screen_name\":\"PRETEND37593601\",\"user\":{\"id\":1113236938612310022,\"id_str\":\"1113236938612310022\",\"name\":\"PRETENDER\",\"screen_name\":\"PRETEND37593601\",\"location\":\"Chita, Russia\",\"url\":null,\"description\":\"PRETENDER\",\"translator_type\":\"none\",\"protected\":false,\"verified\":false,\"followers_count\":93,\"friends_count\":1136,\"listed_count\":0,\"favourites_count\":7426,\"statuses_count\":19410,\"created_at\":\"Wed Apr 03 00:29:03 +0000 2019\",\"utc_offset\":null,\"time_zone\":null,\"geo_enabled\":false,\"lang\":null,\"contributors_enabled\":false,\"is_translator\":false,\"profile_background_color\":\"F5F8FA\",\"profile_background_image_url\":\"\",\"profile_background_image_url_https\":\"\",\"profile_background_tile\":false,\"profile_link_color\":\"1DA1F2\",\"profile_sidebar_border_color\":\"C0DEED\",\"profile_sidebar_fill_color\":\"DDEEF6\",\"profile_text_color\":\"333333\",\"profile_use_background_image\":true,\"profile_image_url\":\"http:\\/\\/pbs.twimg.com\\/profile_images\\/1143139690326790144\\/2GDdOJxf_normal.jpg\",\"profile_image_url_https\":\"https:\\/\\/pbs.twimg.com\\/profile_images\\/1143139690326790144\\/2GDdOJxf_normal.jpg\",\"profile_banner_url\":\"https:\\/\\/pbs.twimg.com\\/profile_banners\\/1113236938612310022\\/1561380680\",\"default_profile\":true,\"default_profile_image\":false,\"following\":null,\"follow_request_sent\":null,\"notifications\":null},\"geo\":null,\"coordinates\":null,\"place\":null,\"contributors\":null,\"is_quote_status\":false,\"extended_tweet\":{\"full_text\":\"Faucet Resets Every 10 Mins (250 - 2500 Per Hour)\\nGames , Casino ,  Offerwalls , Bonuses !!!\\nhttps:\\/\\/t.co\\/G4fS33048L  \\nFaucet Resets Every 10 Mins (250 - 2500 Per Hour)\\nGames , Casino ,  Offerwalls , Bonuses !!!\\n#cryptocurrency #Crypto #bitcoin https:\\/\\/t.co\\/hrR4oxKP2C\",\"display_text_range\":[0,244],\"entities\":{\"hashtags\":[{\"text\":\"cryptocurrency\",\"indices\":[212,227]},{\"text\":\"Crypto\",\"indices\":[228,235]},{\"text\":\"bitcoin\",\"indices\":[236,244]}],\"urls\":[{\"url\":\"https:\\/\\/t.co\\/G4fS33048L\",\"expanded_url\":\"https:\\/\\/www.bitgames.io\\/?affid=12304791\",\"display_url\":\"bitgames.io\\/?affid=12304791\",\"indices\":[93,116]}],\"user_mentions\":[],\"symbols\":[],\"media\":[{\"id\":1152860866590728193,\"id_str\":\"1152860866590728193\",\"indices\":[245,268],\"media_url\":\"http:\\/\\/pbs.twimg.com\\/media\\/D__I2ZyW4AE2cqd.jpg\",\"media_url_https\":\"https:\\/\\/pbs.twimg.com\\/media\\/D__I2ZyW4AE2cqd.jpg\",\"url\":\"https:\\/\\/t.co\\/hrR4oxKP2C\",\"display_url\":\"pic.twitter.com\\/hrR4oxKP2C\",\"expanded_url\":\"https:\\/\\/twitter.com\\/PRETEND37593601\\/status\\/1152860873070915585\\/photo\\/1\",\"type\":\"photo\",\"sizes\":{\"large\":{\"w\":721,\"h\":118,\"resize\":\"fit\"},\"thumb\":{\"w\":118,\"h\":118,\"resize\":\"crop\"},\"medium\":{\"w\":721,\"h\":118,\"resize\":\"fit\"},\"small\":{\"w\":680,\"h\":111,\"resize\":\"fit\"}}}]},\"extended_entities\":{\"media\":[{\"id\":1152860866590728193,\"id_str\":\"1152860866590728193\",\"indices\":[245,268],\"media_url\":\"http:\\/\\/pbs.twimg.com\\/media\\/D__I2ZyW4AE2cqd.jpg\",\"media_url_https\":\"https:\\/\\/pbs.twimg.com\\/media\\/D__I2ZyW4AE2cqd.jpg\",\"url\":\"https:\\/\\/t.co\\/hrR4oxKP2C\",\"display_url\":\"pic.twitter.com\\/hrR4oxKP2C\",\"expanded_url\":\"https:\\/\\/twitter.com\\/PRETEND37593601\\/status\\/1152860873070915585\\/photo\\/1\",\"type\":\"photo\",\"sizes\":{\"large\":{\"w\":721,\"h\":118,\"resize\":\"fit\"},\"thumb\":{\"w\":118,\"h\":118,\"resize\":\"crop\"},\"medium\":{\"w\":721,\"h\":118,\"resize\":\"fit\"},\"small\":{\"w\":680,\"h\":111,\"resize\":\"fit\"}}}]}},\"quote_count\":0,\"reply_count\":0,\"retweet_count\":1,\"favorite_count\":1,\"entities\":{\"hashtags\":[],\"urls\":[{\"url\":\"https:\\/\\/t.co\\/oLPbLYqLs3\",\"expanded_url\":\"https:\\/\\/twitter.com\\/i\\/web\\/status\\/1152860873070915585\",\"display_url\":\"twitter.com\\/i\\/web\\/status\\/1\\u2026\",\"indices\":[94,117]}],\"user_mentions\":[],\"symbols\":[]},\"favorited\":false,\"retweeted\":false,\"possibly_sensitive\":false,\"filter_level\":\"low\",\"lang\":\"en\"},\"is_quote_status\":false,\"quote_count\":0,\"reply_count\":0,\"retweet_count\":0,\"favorite_count\":0,\"entities\":{\"hashtags\":[],\"urls\":[{\"url\":\"https:\\/\\/t.co\\/G4fS33048L\",\"expanded_url\":\"https:\\/\\/www.bitgames.io\\/?affid=12304791\",\"display_url\":\"bitgames.io\\/?affid=12304791\",\"indices\":[114,137]}],\"user_mentions\":[{\"screen_name\":\"PRETEND37593601\",\"name\":\"PRETENDER\",\"id\":1113236938612310022,\"id_str\":\"1113236938612310022\",\"indices\":[3,19]}],\"symbols\":[]},\"favorited\":false,\"retweeted\":false,\"possibly_sensitive\":false,\"filter_level\":\"low\",\"lang\":\"en\",\"timestamp_ms\":\"1563703668216\"}"

private def convertAttributeToJson(tweet : String) = {

  val tweet = Try(parse(sampleJsonString).extract[Tweet])

  val extractedTweet = tweet match {
    case Success(value) => {
      s"text = ${value.text}, place = ${value.place}, entities = ${value.entities}"
      val entities  =  value.entities

      TweetKafkaFields(value.text, value.place, entities) //value.entities)
    }
    case Failure(value) => TweetKafkaFields("", None,Entities(Seq[Hashtag](),Seq[Media](),Seq[Url](),Some(Seq[UserMention]())))
      //Entities(Seq(),Seq(),Seq(),None))
  }

  val jsonObject = ("text" -> extractedTweet.text) ~ ("place" -> extractedTweet.place.map(_.toString))

  compact(render(jsonObject))
}

println(convertAttributeToJson(sampleJsonString))