import java.awt.geom.Point2D
import kotlin.math.max
import kotlin.math.min

class GpxTrack(var name: String, track: List<Point2D>, var properties: List<String>) {
    private var track = track.toMutableList()

    fun getTrack(): List<Point2D> {
        return track.toList()
    }
    fun getPointCount(): Int {
        return track.size
    }

    fun getStart(): Point2D {
        return track.first();
    }
    fun getEnd(): Point2D {
        return track.last()
    }

    fun reverse() {
        track.reverse()
    }

    /**
     * Runs through the track and combines points that are too close together
     * @param threshold How close together should two points be for them to be combined.
     * The default value is equivalent to about 3 meters using lat/lon coordinates
     */
    fun simplifyTrack(threshold: Double = 0.00003) {
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
        track = simplified
    }

    /**
     * Smooths a track's points out to make a track less jagged
     */
    fun smoothTrack() {
        // Each point should be averaged with the one before and after it
        val list = track.mapIndexed { i, point ->
            // Leave first and last point untouched
            if (i == 0 || i == track.lastIndex) point
            // Average with the midpoint of the bounding points
            else pointAverage(point, pointAverage(track[i - 1], track[i + 1]))
        }
        track = list.toMutableList()
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
    fun extractSegment(start: Point2D, end: Point2D, tolerance: Double)
            : List<Point2D> {
        val trackPoints = ArrayList<Point2D>()
        for (point in track) {
            // There are 2 basic cases:
            //   1. We haven't found the track yet
            //   2. We've found the start of the track

            // If we haven't started taking points yet, all we need is a point
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
    fun extractSegment(start: Point2D, end: Point2D)
            : List<Point2D> {
        val trackPoints = ArrayList<Point2D>()
        // Find the closest point to the start and end
        var first = 0
        var last = 0
        for (i in 0 until track.size) {
            if (start.distance(track[i]) < start.distance(track[first]))
                first = i
            if (end.distance(track[i]) < end.distance(track[last]))
                last = i
        }
        return track.subList(min(first, last), max(first, last) + 1)
    }

    // Functions for taking averages
    fun incorporateSubtrack(other: GpxTrack, primaryWeight: Int = 1) {
        // Extract a subsection of the primary corresponding to the given track
        val subsection = extractSegment(other.getStart(), other.getEnd()) as MutableList<Point2D>
        // Average across the subsection
        val averaged = averageWith(subsection, other.getTrack(), primaryWeight)
        // Replace the old section with the new average
        subsection.clear()
        subsection.addAll(averaged)
    }

    fun appendOverlap(other: GpxTrack, primaryWeight: Int = 1) {
        // These two lines will come in three stages:
        //  1. This line
        //  2. The overlapping section
        //  3. The other line

        /*/ Find the closest point to the beginning of the other line
        var begin = 0
        for (i in 0 until track.size)
            if (other.getStart().distance(track[i]) < other.getStart().distance(track[begin]))
                begin = i // */
        // Find where on the other track our current track ends
        var end = 0
        for (i in 0 until other.track.size)
            if (getEnd().distance(other.track[i]) < getEnd().distance(other.track[end]))
                end = i

        // Nothing needs to be done for our line, we already have that data

        // We can incorporate the beginning of the other track as a subtrack for the overlap
        val subsection = extractSegment(other.getStart(), getEnd()) as MutableList<Point2D>
        val averaged = averageWith(subsection, other.getTrack(), primaryWeight)
        subsection.clear()
        subsection.addAll(averaged)

        // Then we can add the remaining points from the other track
        track.addAll(other.track.subList(end, other.track.size))
    }

    private fun pointAverage(a: Point2D, aWeight: Int, b: Point2D, bWeight: Int): Point2D {
        return a.javaClass.getConstructor(a.x.javaClass, a.y.javaClass).newInstance(
            (a.x * aWeight + b.x * bWeight) / (aWeight + bWeight),
            (a.y * aWeight + b.y * bWeight) / (aWeight + bWeight)
        )
    }
}