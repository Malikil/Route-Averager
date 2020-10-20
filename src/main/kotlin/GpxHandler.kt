import org.w3c.dom.NodeList
import org.xml.sax.InputSource
import java.awt.geom.Point2D
import java.io.File
import java.io.StringReader
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.xpath.XPathConstants
import javax.xml.xpath.XPathFactory

class GpxHandler(
    private val filename: String
) {
    private val doc by lazy {
        val xmlFile = File(filename)

        val dbFactory = DocumentBuilderFactory.newInstance()
        val dBuilder = dbFactory.newDocumentBuilder()
        val xmlInput = InputSource(StringReader(xmlFile.readText()))

        return@lazy dBuilder.parse(xmlInput)
    }

    val track: List<Point2D> by lazy {
        val xpFactory = XPathFactory.newInstance()
        val xPath = xpFactory.newXPath()
        val xpath = "/gpx/trk/trkseg/trkpt"

        val xmlTrack = xPath.evaluate(xpath, doc, XPathConstants.NODESET) as NodeList
        val trackPoints = ArrayList<Point2D>()
        for (i in 0 until xmlTrack.length) {
            val attr = xmlTrack.item(i).attributes
            val lat = attr.getNamedItem("lat")
            val lon = attr.getNamedItem("lon")
            trackPoints += Point2D.Double(
                    lon.nodeValue.toDouble(),
                    lat.nodeValue.toDouble()
            )
        }

        return@lazy trackPoints
    }

    fun getTrackName(): String {
        val xpFactory = XPathFactory.newInstance()
        val xPath = xpFactory.newXPath()
        val xpath = "/gpx/trk/name"

        return xPath.evaluate(xpath, doc, XPathConstants.STRING) as String
    }

    /**
     * Extracts part of the track from this gpx, the extracted segment will be
     * as small as possible. Optionally a tolerance can be given to adjust how
     * close to the given points the track needs to be to match.
     * Only the first matching segment will be returned.
     * @param tolerance The absolute distance a point should be from the
     * start or end to be considered. The default value of 0.0001 is equivalent
     * to about 11 meters when using lat/lon for points
     */
    fun extractSegment(start: Point2D, end: Point2D, tolerance: Double = 0.0001)
            : List<Point2D> {
        val trackPoints = ArrayList<Point2D>()
        for (point in track) {
            // There are 2 basic cases:
            //   1. We haven't found the track yet
            //   2. We've found the start of the track

            // If we've haven't started taking points yet, all we need is a point
            // matching where we should begin
            if (trackPoints.size == 0) {
                if (point.distance(start) <= tolerance)
                    trackPoints += point
            }
            // If we've started adding points, there are three more cases:
            //   1. We found a better starting point that will give a shorter result
            //   2. We found the end of the segment
            //   3. We haven't found the end of the segment
            else if (point.distance(start) <= tolerance) {
                trackPoints.clear()
                trackPoints += point
            }
            else {
                // Both finding the end of the segment or not finding it still
                // require adding the current point
                trackPoints += point
                if (point.distance(end) <= tolerance)
                    break
            }
        }

        return trackPoints
    }
}