import org.w3c.dom.NodeList
import org.xml.sax.InputSource
import java.io.File
import java.io.StringReader
import java.util.*
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.xpath.XPathConstants
import javax.xml.xpath.XPathFactory
import kotlin.collections.ArrayList

class Config private constructor(
    val rootFolder: String,
    val trackFolder: String,
    val trackList: List<TrackConfig>
) {
    class TrackConfig(
        val name: String,
        val properties: List<String>
    ) {

    }

    companion object {
        fun load(): Config { return loadFrom("config.xml") }
        fun loadFrom(path: String): Config {
            val xmlFile = File(path)

            val dbFactory = DocumentBuilderFactory.newInstance()
            val dBuilder = dbFactory.newDocumentBuilder()
            val xmlInput = InputSource(StringReader(xmlFile.readText()))

            val doc = dBuilder.parse(xmlInput)

            val xpFactory = XPathFactory.newInstance()
            val xPath = xpFactory.newXPath()

            val root = xPath.evaluate(
                "/config/base-folder",
                doc, XPathConstants.STRING
            ) as String
            val folder = xPath.evaluate(
                "/config/track-folder",
                doc, XPathConstants.STRING
            ) as String

            val trackListNodes = xPath.evaluate(
                "/config/tracks/gpx",
                doc, XPathConstants.NODESET
            ) as NodeList
            val trackList = ArrayList<TrackConfig>()
            for (i in 0 until trackListNodes.length) {
                val gpx = trackListNodes.item(i)
                val attributes = gpx.attributes.getNamedItem("attr")
                    ?.nodeValue?.split(' ') ?: ArrayList<String>()
                trackList += TrackConfig(
                    gpx.textContent,
                    attributes
                )
            }

            return Config(root, folder, trackList)
        }
    }
}