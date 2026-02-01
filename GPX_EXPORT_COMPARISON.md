# GPX Route Export Comparison

## Summary of Changes

This document compares the exported route GPX format between the original implementation and the enhanced version with groundspeak/gsak extensions.

## Changes Made

### 1. Namespace Declarations

**Before:**
```xml
<gpx version="1.1" creator="c:geo - http://www.cgeo.org/" 
     xmlns="http://www.topografix.com/GPX/1/1"
     xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
     xsi:schemaLocation="http://www.topografix.com/GPX/1/1 http://www.topografix.com/GPX/1/1/gpx.xsd">
```

**After:**
```xml
<gpx version="1.1" creator="c:geo - http://www.cgeo.org/" 
     xmlns="http://www.topografix.com/GPX/1/1"
     xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
     xmlns:groundspeak="http://www.groundspeak.com/cache/1/0/1"
     xmlns:gsak="http://www.gsak.net/xmlv1/6"
     xsi:schemaLocation="http://www.topografix.com/GPX/1/1 http://www.topografix.com/GPX/1/1/gpx.xsd http://www.groundspeak.com/cache/1/0/1 http://www.groundspeak.com/cache/1/0/1/cache.xsd http://www.gsak.net/xmlv1/6 http://www.gsak.net/xmlv1/6/gsak.xsd">
```

### 2. Waypoint Entries for Geocaches

**Before:**
```xml
<wpt lat="48.859683" lon="9.1874">
  <name>GC1BKP3</name>
</wpt>
```

**After (for geocaches):**
```xml
<wpt lat="48.859683" lon="9.1874">
  <name>GC1BKP3</name>
  <desc>Die Schatzinsel / treasure island</desc>
  <groundspeak:cache id="859046" available="True" archived="False">
    <groundspeak:name>Die Schatzinsel / treasure island</groundspeak:name>
    <groundspeak:placed_by>Die unbesiegbaren Geo - Geparden</groundspeak:placed_by>
    <groundspeak:type>Traditional Cache</groundspeak:type>
    <groundspeak:container>Micro</groundspeak:container>
    <groundspeak:difficulty>1</groundspeak:difficulty>
    <groundspeak:terrain>5</groundspeak:terrain>
  </groundspeak:cache>
  <gsak:wptExtension>
    <gsak:Watch>false</gsak:Watch>
    <gsak:IsPremium>false</gsak:IsPremium>
    <gsak:FavPoints>42</gsak:FavPoints>
    <gsak:GcNote>Personal note goes here</gsak:GcNote>
    <gsak:UserFound>2021-03-20T00:00:00Z</gsak:UserFound>
  </gsak:wptExtension>
</wpt>
```

### 3. Waypoint Entries for Cache Waypoints

**Before:**
```xml
<wpt lat="48.860000" lon="9.187500">
  <name>GC1BKP3-01</name>
</wpt>
```

**After:**
```xml
<wpt lat="48.860000" lon="9.187500">
  <name>GC1BKP3-01</name>
  <desc>Parking Area</desc>
  <gsak:wptExtension>
    <gsak:Parent>GC1BKP3</gsak:Parent>
  </gsak:wptExtension>
</wpt>
```

For user-defined waypoints:
```xml
<wpt lat="48.860000" lon="9.187500">
  <name>GC1BKP3-02</name>
  <desc>My Custom Waypoint</desc>
  <gsak:wptExtension>
    <gsak:Parent>GC1BKP3</gsak:Parent>
    <gsak:Child_ByGSAK>true</gsak:Child_ByGSAK>
  </gsak:wptExtension>
</wpt>
```

## Benefits

1. **Enhanced Compatibility**: The exported GPX files now include industry-standard extensions used by popular geocaching applications like GSAK (Geocaching Swiss Army Knife) and official Groundspeak tools.

2. **Richer Metadata**: Route exports now include comprehensive cache information such as:
   - Cache attributes (difficulty, terrain, container size)
   - Cache status (available, archived, found)
   - User-specific data (watchlist status, favorite points, personal notes, found dates)
   - Waypoint relationships (parent cache references)

3. **Better Import/Export Round-Trip**: GPX files exported from c:geo can now be imported into other geocaching tools with full preservation of cache metadata.

4. **User-Defined Waypoint Support**: The export now properly identifies and marks user-created waypoints with the GSAK Child_ByGSAK flag.

## Technical Implementation

The changes were made to `IndividualRouteExportTask.java`:

1. Added namespace constants for groundspeak and GSAK schemas
2. Enhanced the `exportWaypoint()` method to detect route item types (GEOCACHE, WAYPOINT, COORDS)
3. Added `addGeocacheExtensions()` to write groundspeak:cache and gsak:wptExtension data for geocaches
4. Added `addWaypointExtensions()` to write gsak:wptExtension data for cache waypoints
5. Helper methods `gpxBoolean()` and `integerIfPossible()` ensure XML schema compliance

All changes maintain backward compatibility - the route/track structure remains unchanged, only additional metadata is added to waypoint entries.
