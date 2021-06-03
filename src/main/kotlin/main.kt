import java.awt.geom.Point2D
import java.io.File
import kotlin.math.abs

fun main() {
    val track1 = GpxHandler("tracks/Hollyburn TCT/Blind_Skier.gpx")
    println(track1.getTrackName())
    val track2 = GpxHandler("tracks/Hollyburn TCT/Track_Hollyburn  143605.gpx")
    println(track2.getTrackName())

    val result = average(track1.getSimplifiedTrack(), track2.getSimplifiedTrack())
    GpxHandler.createGpx("tracks/result.gpx", result, "Averaged Track")
}

fun average(list1: List<Point2D>, list2: List<Point2D>)
        : List<Point2D> {
    return average(list1, 1, list2, 1)
}
fun average(list1: List<Point2D>, weight1: Int, list2: List<Point2D>, weight2: Int)
        : List<Point2D> {
    /**
     * If two points that would be added to the track are within this threshold,
     * the average of those points is added instead.
     */
    val DISTANCE_THRESHOLD = 0.00004496 // Equivalent to about 5 meters when using lat/lon
    /**
     * If a point is this close to the previous point, it will be combined with the
     * previous point.
     */
    val COMBINATION_THRESHOLD = 0.00001 // Equivalent to about 1 meter

    // Assume each track is complete (endpoints pair with each other),
    // and 'travelling' in the same direction
    // Then each point from each line should find the closest point from the other track,
    // and average with that point
    val updated = ArrayList<Point2D>()
    // Average the starting point
    updated += pointAverage(list1[0], weight1, list2[0], weight2)
    // Run through both lists and add average points to the new list
    val iter1 = list1.iterator()
    val iter2 = list2.iterator()
    // For each point, find if it's earlier than the one from the other list
    // I feel this is a bit of a naive solution. This method simply finds both
    // alternatives, then just adds whichever is closest to the previous point
    var p1 = averageNearestPoint(iter1.next(), list2, weight1, weight2)
    var p2 = averageNearestPoint(iter2.next(), list1, weight2, weight1)
    // Take points from the lists until a list is exhausted
    while (iter1.hasNext() && iter2.hasNext()) {
        when {
            // If the two points are equidistant, average them
            abs(p1.distance(updated.last()) - p2.distance(updated.last())) < DISTANCE_THRESHOLD -> {
                updated += Point2D.Double((p1.x + p2.x) / 2, (p1.y + p2.y) / 2)
                p1 = averageNearestPoint(iter1.next(), list2, weight1, weight2)
                p2 = averageNearestPoint(iter2.next(), list1, weight2, weight1)
            }
            // Otherwise add the closer point
            p1.distance(updated.last()) < p2.distance(updated.last()) -> {
                updated += p1
                p1 = averageNearestPoint(iter1.next(), list2, weight1, weight2)
            }
            else -> { // p2 distance to last < p1 distance to last
                updated += p2
                p2 = averageNearestPoint(iter2.next(), list1, weight2, weight1)
            }
        }
        // Sanity check to avoid adding way too many points to the updated track.
        // If the new point is within the threshold of the old point, take their average instead
        if (updated.last().distance(updated[updated.lastIndex - 1]) < COMBINATION_THRESHOLD) {
            // Remove the last two points
            val last = updated.removeLast()
            val previous = updated.removeLast()
            updated += pointAverage(last, previous)
        }
    }
    // At this point there will be exactly one point left on one list or the other
    // Add the required remaining points that aren't already accounted for
    // So long as the points in the list with remaining items are closer
    // than the last one from the other list, add those points to the updates
    when {
        iter1.hasNext() -> {
            while (iter1.hasNext() &&
                    p1.distance(updated.last()) < (p2.distance(updated.last()) - DISTANCE_THRESHOLD)) {
                // Add the point to the list
                updated += p1
                p1 = averageNearestPoint(iter1.next(), list2, weight1, weight2)
            }
            // There are now no more points on the second list, and the only
            // remaining points on the first are farther than the list2 point.
            // Add the average of the next two points
            updated += Point2D.Double((p1.x + p2.x) / 2, (p1.y + p2.y) / 2)
            // Every remaining point should be averaged with the endpoint of list2
            iter1.forEachRemaining { point ->
                updated += Point2D.Double(
                    (point.x * weight1 + list2.last().x * weight2) / (weight1 + weight2),
                    (point.y * weight1 + list2.last().y * weight2) / (weight1 + weight2)
                )
            }
        }
        iter2.hasNext() -> {
            while (iter2.hasNext() &&
                    p2.distance(updated.last()) < (p1.distance(updated.last()) - DISTANCE_THRESHOLD)) {
                updated += p2
                p2 = averageNearestPoint(iter2.next(), list1, weight2, weight1)
            }
            updated += Point2D.Double((p1.x + p2.x) / 2, (p1.y + p2.y) / 2)
            iter2.forEachRemaining { point ->
                updated += Point2D.Double(
                    (point.x * weight2 + list1.last().x * weight1) / (weight1 + weight2),
                    (point.y * weight2 + list1.last().y * weight1) / (weight1 + weight2)
                )
            }
        }
        else -> {
            // I'm not sure if this is guaranteed for every case yet, but in the
            // cases I've seen when the second-last point for both lists are
            // averaged with each other there will be the last points on each path
            // that won't otherwise get accounted for.
            // I believe that's the only case where this can happen, as in other
            // cases one of the two lists will still have points left

            // Determine if the points should be averaged or added one at a time
            when {
                p1.distance(updated.last()) < p2.distance(updated.last()) - DISTANCE_THRESHOLD -> {
                    updated += p1
                    updated += p2
                }
                p2.distance(updated.last()) < p1.distance(updated.last()) - DISTANCE_THRESHOLD -> {
                    updated += p2
                    updated += p1
                }
                else ->
                    updated += Point2D.Double((p1.x + p2.x) / 2, (p1.y + p2.y) / 2)
            }
        }
    }
    // In my process for adding points I'll always actually add the first point
    // twice. Once in the initial add and once from whichever line's endpoint
    // extends past the other line. I don't want to rewrite the loop until I've
    // determined if it actually works as expected in the current state. So I'm
    // just removing the first redundant point here after the fact
    //updated.removeAt(0)
    // I believe this is fixed now, as the second point will be a duplicate, and thus
    // combined with the first point
    return updated
}

/**
 * Finds the average between a given point and the closest spot along a path
 * given by a list of points
 */
fun averageNearestPoint(point: Point2D, path: List<Point2D>)
        : Point2D {
    return averageNearestPoint(point, path, 1, 1)
}
fun averageNearestPoint(point: Point2D, path: List<Point2D>, pointWeight: Int, lineWeight: Int)
        : Point2D {
    // Find the nearest point on each line segment, then take the average with the closest one
    var nearestPoint = nearestPointOnLine(point, Pair(path[0], path[1]))
    var nearestDist = nearestPoint.distance(point)
    for (i in 2 until path.size) {
        val candidate = nearestPointOnLine(point, Pair(path[i - 1], path[i]))
        val candidateDist = point.distance(candidate)
        if (candidateDist < nearestDist) {
            nearestDist = candidateDist
            nearestPoint = candidate
        }
    }
    // Average with the nearest point
    return Point2D.Double(
        (point.x * pointWeight + nearestPoint.x * lineWeight) / (pointWeight + lineWeight),
        (point.y * pointWeight + nearestPoint.y * lineWeight) / (pointWeight + lineWeight)
    )
}

fun pointAverage(a: Point2D, b: Point2D): Point2D { return pointAverage(a, 1, b, 1) }
fun pointAverage(a: Point2D, aWeight: Int, b: Point2D, bWeight: Int): Point2D {
    return a.javaClass.getConstructor(a.x.javaClass, a.y.javaClass).newInstance(
        (a.x * aWeight + b.x * bWeight) / (aWeight + bWeight),
        (a.y * aWeight + b.y * bWeight) / (aWeight + bWeight)
    )
}

/**
 * Finds the closest point to point P on line segment
 */
fun nearestPointOnLine(p: Point2D, line: Pair<Point2D, Point2D>)
        : Point2D {
    // Find vectors for A->P and A->B
    val aToP = Point2D.Double(p.x - line.first.x, p.y - line.first.y)
    val aToB = Point2D.Double(line.second.x - line.first.x, line.second.y - line.first.y)

    val atb2 = aToB.x * aToB.x + aToB.y * aToB.y
    val dotProd = aToP.x * aToB.x + aToP.y * aToB.y
    val dist = dotProd / atb2

    // Find the point
    val nearest = Point2D.Double(line.first.x + aToB.x * dist, line.first.y + aToB.y * dist)

    // Make sure the point actually lies on the line
    // There are four cases:
    //    1. The point is left of the line
    //    2. The point is right of the line
    //    3. The point is below the line
    //    4. The point is above the line
    // Normally checking only left/right would work fine, but if the line is
    // vertical those won't work, so checking above/below is also needed as
    // a special case
    // Only check one value at a time, as the actual slope of the line
    // can't be guaranteed, and we can't guarantee the order of A/B
    return if (nearest.x < line.first.x && nearest.x < line.second.x)
        if (line.first.x < line.second.x)
            Point2D.Double(line.first.x,  line.first.y)
        else
            Point2D.Double(line.second.x, line.second.y)
    else if (nearest.y < line.first.y && nearest.y < line.second.y)
        if (line.first.y < line.second.y)
            Point2D.Double(line.first.x, line.first.y)
        else
            Point2D.Double(line.second.x, line.second.y)
    else if (nearest.x > line.first.x && nearest.x > line.second.x)
        if (line.first.x > line.second.x)
            Point2D.Double(line.first.x, line.first.y)
        else
            Point2D.Double(line.second.x, line.second.y)
    else if (nearest.y > line.first.y && nearest.y > line.second.y)
        if (line.first.y > line.second.y)
            Point2D.Double(line.first.x, line.first.y)
        else
            Point2D.Double(line.second.x, line.second.y)
    else
        nearest
}
