import org.w3c.dom.NodeList
import org.xml.sax.InputSource
import java.awt.geom.Point2D
import java.io.File
import java.io.FileOutputStream
import java.io.StringReader
import java.time.Instant
import java.time.format.DateTimeFormatter
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.OutputKeys
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult
import javax.xml.xpath.XPathConstants
import javax.xml.xpath.XPathFactory
import kotlin.collections.ArrayList

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

    companion object {
        fun createGpx(filename: String, track: List<Point2D>, name: String = ""): GpxHandler {
            // These constants seem to exist as attributes in the gpx tag
            // I have a gpx from a gps and one downloaded from strava, these
            // are the attributes common to each
            val XMLNS = "http://www.topografix.com/GPX/1/1"
            val XMLNS_GPXX = "http://www.garmin.com/xmlschemas/GpxExtensions/v3"
            val XMLNS_GPXTPX = "http://www.garmin.com/xmlschemas/TrackPointExtension/v1"
            val XMLNS_XSI = "http://www.w3.org/2001/XMLSchema-instance"
            val XSI_SCHEMALOCATIONS = arrayOf(
                "http://www.topografix.com/GPX/1/1",
                "http://www.topografix.com/GPX/1/1/gpx.xsd",
                "http://www.garmin.com/xmlschemas/GpxExtensions/v3",
                "http://www.garmin.com/xmlschemas/GpxExtensionsv3.xsd",
                "http://www.garmin.com/xmlschemas/TrackPointExtension/v1",
                "http://www.garmin.com/xmlschemas/TrackPointExtensionv1.xsd"
            )
            val VERSION = "1.1"

            val dbFactory = DocumentBuilderFactory.newInstance()
            val dBuilder = dbFactory.newDocumentBuilder()
            val doc = dBuilder.newDocument()

            val gpxNode = doc.createElement("gpx")
            gpxNode.setAttribute("creator", "TrackAverager")
            gpxNode.setAttribute("xmlns:xsi", XMLNS_XSI)
            gpxNode.setAttribute("xsi:schemaLocation", XSI_SCHEMALOCATIONS.joinToString(" "))
            gpxNode.setAttribute("version", VERSION)
            gpxNode.setAttribute("xmlns", XMLNS)
            gpxNode.setAttribute("xmlns:gpxtpx", XMLNS_GPXTPX)
            gpxNode.setAttribute("xmlns:gpxx", XMLNS_GPXX)

            val meta = doc.createElement("metadata")
            val metaTime = doc.createElement("time")
            metaTime.appendChild(doc.createTextNode(Instant.now().toString()))
            meta.appendChild(metaTime)
            gpxNode.appendChild(meta)

            val trk = doc.createElement("trk")
            val trkName = doc.createElement("name")
            trkName.appendChild(doc.createTextNode(name))
            trk.appendChild(trkName)

            val trkSeg = doc.createElement("trkseg")
            // Add all the track points
            track.forEach { trackPoint ->
                val trkpt = doc.createElement("trkpt")
                trkpt.setAttribute("lat", trackPoint.y.toString())
                trkpt.setAttribute("lon", trackPoint.x.toString())
                trkSeg.appendChild(trkpt)
            }

            trk.appendChild(trkSeg)
            gpxNode.appendChild(trk)
            doc.appendChild(gpxNode)
            val tr = TransformerFactory.newInstance().newTransformer()
            tr.setOutputProperty(OutputKeys.INDENT, "yes")
            tr.setOutputProperty(OutputKeys.METHOD, "xml")
            tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8")

            tr.transform(
                DOMSource(doc),
                StreamResult(FileOutputStream(filename))
            )

            return GpxHandler(filename)
        }
    }

    fun getTrackName(): String {
        val xpFactory = XPathFactory.newInstance()
        val xPath = xpFactory.newXPath()
        val xpath = "/gpx/trk/name"

        return xPath.evaluate(xpath, doc, XPathConstants.STRING) as String
    }

    /**
     * Runs through the track and combines points that are too close together
     * @param threshold How close together should two points be for them to be combined.
     * The default value is equivalent to about 3 meters using lat/lon coordinates
     */
    fun getSimplifiedTrack(threshold: Double = 0.00003)
            : List<Point2D> {
        val simplified = ArrayList<Point2D>()

        // Figure out the point to add
        // I've tried about 3 different ways of doing this iteration, and I
        // couldn't figure out any of them. Please kotlin just give me a normal
        // for loop sometimes
        var i = 0
        while (i < track.size) {
            var add = track[i++]
            var count = 1
            // When points are close together, subsequent points should be
            // combined into the running average
            while (i < track.size && track[i].distance(add) < threshold)
                add = pointAverage(add, count++, track[i++], 1)
            // The averaged point should be added to the simplified list
            simplified.add(add)
        }

        return simplified
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

    private fun pointAverage(a: Point2D, aWeight: Int, b: Point2D, bWeight: Int): Point2D {
        return a.javaClass.getConstructor(a.x.javaClass, a.y.javaClass).newInstance(
            (a.x * aWeight + b.x * bWeight) / (aWeight + bWeight),
            (a.y * aWeight + b.y * bWeight) / (aWeight + bWeight)
        )
    }
}