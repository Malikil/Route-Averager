Average Track Calculator
===

This is a fairly small something I came up with to calculate average paths between gpx tracks.  
Inspired by segments on [strava](strava.com) which don't always line up with the actual road, seemingly usually because the gps for whoever created the segment wasn't reading very accurately. So the thought is that as more activities have matching segments, the 'master segment' can be updated/adjusted by taking the average of all matching segments from activities. That way over time the segment could be brought closer to the actual correct route that people should be matching to.

Strava already has an algorithm for matching segments, so I decided not to try including that as something to take care of. I do have a way of extracting a piece of a track but it's fairly simple and won't hold up well under stress.  
There are also methods for combining points on a track within a certain range in order to address stoppage time, and for smoothing a track because some shapes seem to cause problems after averaging.

The use of Kotlin here is also mainly for learning purposes. I'd never touched kotlin before this project, so I figured this would be as good a situation as any to get language basics figured out.
