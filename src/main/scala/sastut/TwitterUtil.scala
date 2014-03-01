package sastut

/**
 * A simple case class to store information associated with a Tweet.
 */
case class Tweet(val tweetid: String, val username: String, val content: String)

/**
 * Read in a polarity labeled dataset from XML.
 */
object DatasetReader {

  import nak.data.Example
  import scala.xml._
  import java.io.File

  // Allow NodeSeqs to implicitly convert to Strings when needed.
  implicit def nodeSeqToString(ns: NodeSeq) = ns.text

  def apply(file: File): Seq[Example[String, Tweet]] = {
    val itemsXml = XML.loadFile(file)

    (itemsXml \ "item").flatMap { itemNode =>
      val label: String = itemNode \ "@label"

      // We only want the positive, negative and neutral items.
      label match {
        case "negative" | "positive" | "neutral" =>
          // Note: the target is: (itemNode \ "@target").text,
          val tweet = Tweet(itemNode \ "@tweetid", itemNode \ "@username", itemNode.text.trim)

          // Uncomment these lines and comment out the Some(Example(label,tweet)) line if 
          // you want to just train on pos/neg examples from emoticons.
          //if (file.getPath == "data/emoticon/train.xml" && label == "neutral") None
          //else Some(Example(label,tweet))

          Some(Example(label,tweet))
            
        case _ => None
      }
    }
  }

}

object StanfordToXmlConverter {

  def main(args: Array[String]) {

    println("<dataset>")
    for (line <- io.Source.fromFile(args(0)).getLines) {
      val Array(polarityIndex, id, date, target, user, tweet) = line.split(";;")
      val polarity = polarityIndex match {
        case "0" => "negative"
        case "2" => "neutral"
        case "4" => "positive"
      }
   
      val itemXml = 
        <item tweetid={id} label={polarity} target={target} username={user}>
          <content>{tweet}</content>
        </item>
      
      println(itemXml)

    }
    
    println("</dataset>")

  }

}


object EmoticonToXmlConverter {

  import java.io.File

  def main(args: Array[String]) {
    val emoticonDir = new File(args(0))
    val files = List("happy.txt", "sad.txt", "neutral.txt").map(f=>new File(emoticonDir,f))
    
    val labels = List("positive","negative","neutral")
    println("<dataset>")
    for ((file,label) <- files.zip(labels))
      getItems(file,label).foreach(println)
    println("</dataset>")
  }

  val EmoItemRE = """^(\d+)\t(\d+)\t(.*)$""".r

  def getItems(file: File, label: String) = {
    for (line <- io.Source.fromFile(file).getLines) yield {
      //println("*************")
      //println("--- " + line.split("\\t").foreach(println))
      val EmoItemRE(tweetid, userid, tweet) = line
      <item tweetid={tweetid} label={label} target={"unknown"} username={userid}>
        <content>{tweet}</content>
      </item>
    }
  }

}
