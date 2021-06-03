import org.w3c.dom.NodeList
import org.xml.sax.InputSource
import java.awt.geom.Point2D
import java.io.File
import java.io.FileOutputStream
import java.io.StringReader
import java.time.Instant
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
    fun writeGpx(track: GpxTrack) {
        writeGpx(track.name, track.getTrack())
    }
    fun writeGpx(name: String, track: List<Point2D>) {
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
    }

    fun readGpx(): GpxTrack {
        // Prepare document
        val xmlFile = File(filename)

        val dbFactory = DocumentBuilderFactory.newInstance()
        val dBuilder = dbFactory.newDocumentBuilder()
        val xmlInput = InputSource(StringReader(xmlFile.readText()))

        val doc = dBuilder.parse(xmlInput)

        // Read track
        val xpFactory = XPathFactory.newInstance()
        val xPath = xpFactory.newXPath()

        val xmlTrack = xPath.evaluate(
            "/gpx/trk/trkseg/trkpt",
            doc, XPathConstants.NODESET
        ) as NodeList

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

        // Track name
        val name = xPath.evaluate(
            "/gpx/trk/name",
            doc, XPathConstants.STRING
        ) as String

        return GpxTrack(name, trackPoints)
    }
}