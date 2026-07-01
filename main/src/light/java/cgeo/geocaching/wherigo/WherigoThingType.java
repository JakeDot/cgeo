package cgeo.geocaching.wherigo;

import cgeo.geocaching.R;
import cgeo.geocaching.utils.EnumValueMapper;
import cgeo.geocaching.utils.LocalizationUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Collections;
import java.util.List;

/** Light build stub — Wherigo game engine not available. */
public enum WherigoThingType {

    LOCATION(R.string.wherigo_thingtype_location, R.drawable.wherigo_icon_locations),
    ITEM(R.string.wherigo_thingtype_item, R.drawable.wherigo_icon_search),
    INVENTORY(R.string.wherigo_thingtype_inventory, R.drawable.wherigo_icon_inventory),
    TASK(R.string.wherigo_thingtype_task, R.drawable.wherigo_icon_tasks),
    THING(R.string.wherigo_thingtype_thing, R.drawable.ic_menu_list);

    private final int displayResId;
    private final int iconId;

    private static final EnumValueMapper<Integer, WherigoThingType> WHERIGOSCREENID_TO_TYPE = new EnumValueMapper<>();

    WherigoThingType(final int displayResId, final int iconId) {
        this.displayResId = displayResId;
        this.iconId = iconId;
    }

    @NonNull
    public String toUserDisplayableString() {
        return LocalizationUtils.getString(this.displayResId);
    }

    public int getIconId() {
        return iconId;
    }

    public List<?> getThingsForUserDisplay() {
        return Collections.emptyList();
    }

    @Nullable
    public static WherigoThingType getByWherigoScreenId(final int wherigoScreenId) {
        return WHERIGOSCREENID_TO_TYPE.get(wherigoScreenId);
    }

    public static List<?> getEverything() {
        return Collections.emptyList();
    }

    public static String getVisibleThingState() {
        return "";
    }
}
