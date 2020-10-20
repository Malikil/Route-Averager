Average Track Calculator
===

This is a fairly small something I came up with to calculate average paths between gpx tracks.  
Inspired by segments on [strava](strava.com) which don't always line up with the actual road, seemingly usually because the gps for whoever created the segment wasn't reading very accurately. So the thought is that as more activities have matching segments, the 'master segment' can be updated/adjusted by taking the average of all matching segments from activities. That way over time the segment could be brought closer to the actual correct route that people should be matching to.

The program in its current state just simply averages routes. It assumes the given routes represent the same segment in its entirety, it won't try to extract only part of a route then match with that part. Strava already has an algorithm for matching segments, so I decided not to try including that as something to take care of.

The use of Kotlin here is also mainly for learning purposes. I'd never touched kotlin before this project, so I figured this would be as good a situation as any to get language basics figured out.

---

Eventually I plan on adding the ability to actually create a gpx file with the resulting track, and/or plotting the points visually such as on a map or graph. But we'll see how far with that I continue.