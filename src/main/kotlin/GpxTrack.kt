import java.awt.geom.Point2D

class GpxTrack(val name: String, track: List<Point2D>) {
    private val track = track.toMutableList()

    fun getTrack(): List<Point2D> {
        return track.toList()
    }
    fun getPointCount(): Int {
        return track.size
    }

    /**
     * Runs through the track and combines points that are too close together
     * @param threshold How close together should two points be for them to be combined.
     * The default value is equivalent to about 3 meters using lat/lon coordinates
     */
    fun simplifyTrack(threshold: Double = 0.00003)
            : GpxTrack {
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

        return GpxTrack(name, simplified)
    }

    /**
     * Smooths a track's points out to make a track less jagged
     */
    fun smoothTrack()
            : GpxTrack {
        // Each point should be averaged with the one before and after it
        val list = track.mapIndexed { i, point ->
            // Leave first and last point untouched
            if (i == 0 || i == track.lastIndex) point
            // Average with the midpoint of the bounding points
            else pointAverage(point, pointAverage(track[i - 1], track[i + 1]))
        }
        return GpxTrack(name, list)
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