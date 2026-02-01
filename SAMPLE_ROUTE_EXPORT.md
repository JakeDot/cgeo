# Sample Route GPX Export

This is a sample GPX export from a route containing three items:
1. **A geocache** (GC1TEST - "Sample Traditional Cache")
2. **A waypoint** of that cache (Parking waypoint)
3. **A simple coordinate** point

## Full GPX Output

```xml
<?xml version='1.0' encoding='UTF-8' standalone='yes' ?>
<gpx version="1.1" creator="c:geo - http://www.cgeo.org/" 
     xmlns="http://www.topografix.com/GPX/1/1" 
     xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
     xmlns:groundspeak="http://www.groundspeak.com/cache/1/0/1" 
     xmlns:gsak="http://www.gsak.net/xmlv1/6" 
     xsi:schemaLocation="http://www.topografix.com/GPX/1/1 http://www.topografix.com/GPX/1/1/gpx.xsd http://www.groundspeak.com/cache/1/0/1 http://www.groundspeak.com/cache/1/0/1/cache.xsd http://www.gsak.net/xmlv1/6 http://www.gsak.net/xmlv1/6/gsak.xsd">
  
  <metadata>
    <name>c:geo individual route</name>
    <time>2026-02-01T08:20:00Z</time>
  </metadata>

  <!-- Waypoint 1: Geocache with full metadata -->
  <wpt lat="48.859683" lon="9.1874">
    <name>GC1TEST</name>
    <desc>Sample Traditional Cache</desc>
    <groundspeak:cache id="123456" available="True" archived="False">
      <groundspeak:name>Sample Traditional Cache</groundspeak:name>
      <groundspeak:placed_by>CacheOwner</groundspeak:placed_by>
      <groundspeak:type>Traditional Cache</groundspeak:type>
      <groundspeak:container>regular</groundspeak:container>
      <groundspeak:difficulty>2.5</groundspeak:difficulty>
      <groundspeak:terrain>3</groundspeak:terrain>
    </groundspeak:cache>
    <gsak:wptExtension>
      <gsak:Watch>true</gsak:Watch>
      <gsak:IsPremium>false</gsak:IsPremium>
      <gsak:FavPoints>15</gsak:FavPoints>
      <gsak:GcNote>Great cache with nice view!</gsak:GcNote>
      <gsak:UserFound>2021-01-01T00:00:00Z</gsak:UserFound>
    </gsak:wptExtension>
  </wpt>

  <!-- Waypoint 2: Cache waypoint with parent reference -->
  <wpt lat="48.86" lon="9.1875">
    <name>GC1TEST-01</name>
    <desc>Parking</desc>
    <gsak:wptExtension>
      <gsak:Parent>GC1TEST</gsak:Parent>
    </gsak:wptExtension>
  </wpt>

  <!-- Waypoint 3: Simple coordinate (no extensions) -->
  <wpt lat="48.8605" lon="9.188">
    <name>COORDS! N 48° 51.630 E 009° 11.280</name>
    <!-- No extensions for simple coordinates -->
  </wpt>

  <!-- Route definition -->
  <rte>
    <name>c:geo individual route 2026-02-01T08:20:00Z</name>
    <rtept lat="48.859683" lon="9.1874">
      <name>GC1TEST</name>
    </rtept>
    <rtept lat="48.86" lon="9.1875">
      <name>GC1TEST-01</name>
    </rtept>
    <rtept lat="48.8605" lon="9.188">
      <name>COORDS! N 48° 51.630 E 009° 11.280</name>
    </rtept>
  </rte>

</gpx>
```

## Breakdown by Item Type

### 1. Geocache Waypoint (GC1TEST)

The geocache includes:
- **Basic waypoint data**: coordinates, name, description
- **Groundspeak cache extension** with:
  - Cache ID, availability, and archived status
  - Cache name, owner, type, and container size
  - Difficulty and terrain ratings
- **GSAK extension** with:
  - Watchlist status (`Watch`)
  - Premium membership requirement (`IsPremium`)
  - Favorite points count (`FavPoints`)
  - Personal note (`GcNote`)
  - Found date (`UserFound`)

### 2. Cache Waypoint (Parking)

The cache waypoint includes:
- **Basic waypoint data**: coordinates, name (geocode + prefix), description
- **GSAK extension** with:
  - Parent cache reference (`Parent: GC1TEST`)
  - Would also include `Child_ByGSAK: true` if it was user-defined

### 3. Simple Coordinate Point

The coordinate point includes:
- **Only basic waypoint data**: coordinates and formatted name
- **No extensions** - keeps the GPX clean for simple points

## Key Differences from Original Implementation

Before this enhancement, all waypoints would have looked like waypoint #3 - just coordinates and a name. Now:

- **Geocaches** get full metadata that can be used by GSAK and other tools
- **Cache waypoints** maintain their relationship to parent caches
- **User-defined waypoints** are properly flagged
- **Simple coordinates** remain clean and simple

This allows exported routes to be much more useful when imported into other geocaching tools!
