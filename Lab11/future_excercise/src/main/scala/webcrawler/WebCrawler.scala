package webcrawler

object WebCrawler extends App {
  import scala.io.Source
  import org.htmlcleaner.HtmlCleaner
  import java.net.URL

  val url = "http://google.com"
  // val html = Source.fromURL(url)

  val cleaner = new HtmlCleaner
  val props = cleaner.getProperties

  //val rootNode = cleaner.clean(html.mkString) 
  val rootNode = cleaner.clean(new URL(url))

  val elements = rootNode.getElementsByName("a", true) 
  elements map { elem => 
    val url = elem.getAttributeByName("href")
    println(url.toString) 
  }
}
