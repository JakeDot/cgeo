package cgeo.geocaching.connector.al;

import cgeo.geocaching.models.Waypoint;

import java.lang.reflect.Method;
import java.util.List;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class ALApiTest {

    @Test
    public void parseWaypointsSetsVisitedForCompletedStage() throws Exception {
        final ArrayNode waypointsJson = JsonNodeFactory.instance.arrayNode()
                .add(createWaypoint("Stage 1", 1.0, 2.0, true))
                .add(createWaypoint("Stage 2", 3.0, 4.0, false));

        final List<Waypoint> parsedWaypoints = parseWaypoints(waypointsJson, false);

        assertThat(parsedWaypoints.get(0).isVisited()).isTrue();
        assertThat(parsedWaypoints.get(1).isVisited()).isFalse();
    }

    @Test
    public void parseWaypointsSetsAllVisitedWhenAdventureComplete() throws Exception {
        final ArrayNode waypointsJson = JsonNodeFactory.instance.arrayNode()
                .add(createWaypoint("Stage 1", 1.0, 2.0, false))
                .add(createWaypoint("Stage 2", 3.0, 4.0, false));

        final List<Waypoint> parsedWaypoints = parseWaypoints(waypointsJson, true);

        assertThat(parsedWaypoints.get(0).isVisited()).isTrue();
        assertThat(parsedWaypoints.get(1).isVisited()).isTrue();
    }

    @SuppressWarnings("unchecked")
    private static List<Waypoint> parseWaypoints(final ArrayNode waypointsJson, final boolean isAdventureComplete) throws Exception {
        final Method parseWaypointsMethod = ALApi.class.getDeclaredMethod("parseWaypoints", ArrayNode.class, String.class, boolean.class);
        parseWaypointsMethod.setAccessible(true);
        return (List<Waypoint>) parseWaypointsMethod.invoke(null, waypointsJson, "AL12345", isAdventureComplete);
    }

    private static ObjectNode createWaypoint(final String title, final double latitude, final double longitude, final boolean isComplete) {
        final ObjectNode waypointNode = JsonNodeFactory.instance.objectNode();
        waypointNode.put("Title", title);
        waypointNode.put("KeyImageUrl", "");
        waypointNode.put("Description", "");
        waypointNode.put("GeofencingRadius", 10.0);
        waypointNode.put("IsComplete", isComplete);
        final ObjectNode locationNode = waypointNode.putObject("Location");
        locationNode.put("Latitude", latitude);
        locationNode.put("Longitude", longitude);
        return waypointNode;
    }
}
