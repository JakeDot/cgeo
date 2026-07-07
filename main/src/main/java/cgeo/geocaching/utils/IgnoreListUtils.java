package cgeo.geocaching.utils;

import cgeo.geocaching.R;
import cgeo.geocaching.activity.ActivityMixin;
import cgeo.geocaching.connector.ConnectorFactory;
import cgeo.geocaching.connector.IConnector;
import cgeo.geocaching.connector.capability.IIgnoreCapability;
import cgeo.geocaching.connector.capability.IIgnoreListCapability;
import cgeo.geocaching.enumerations.LoadFlags;
import cgeo.geocaching.list.StoredList;
import cgeo.geocaching.models.Geocache;
import cgeo.geocaching.settings.Settings;
import cgeo.geocaching.storage.DataStore;

import android.content.Context;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class IgnoreListUtils {
    private IgnoreListUtils() {
        // utility class
    }

    public static void ignoreAll(final Context context, final List<Geocache> caches) {
        updateHandler(context, caches, true);
    }

    public static void unignoreAll(final Context context, final List<Geocache> caches) {
        updateHandler(context, caches, false);
    }

    private static void updateHandler(final Context context, final List<Geocache> caches, final boolean actionIsIgnore) {
        ActivityMixin.showToast(context, LocalizationUtils.getString(R.string.ignorelist_background_started));
        AndroidRxUtils.networkScheduler.scheduleDirect(() -> {
            final List<String> failedCaches = new ArrayList<>();
            for (final Geocache cache : caches) {
                if (actionIsIgnore ? canIgnore(cache) : canUnignore(cache)) {
                    if (!(actionIsIgnore ? ignoreOnline(cache) : unignoreOnline(cache))) {
                        failedCaches.add(cache.getGeocode());
                    }
                }
            }
            ActivityMixin.showToast(context, failedCaches.isEmpty() ? LocalizationUtils.getString(actionIsIgnore ? R.string.caches_ignore_all : R.string.caches_unignore_all) : LocalizationUtils.getString(R.string.err_ignorelist_failed_geocodes, String.join(",", failedCaches)));
        });
    }

    /**
     * Ignores a single cache online and mirrors it into the local ignore list. Must not be called on main thread.
     *
     * @return {@code false} if an error occurred, {@code true} otherwise
     */
    public static boolean ignoreOnline(final Geocache cache) {
        final IConnector connector = ConnectorFactory.getConnector(cache);
        if (!(connector instanceof IIgnoreCapability) || !((IIgnoreCapability) connector).addToIgnorelist(cache)) {
            return false;
        }
        cache.getLists().add(StoredList.IGNORE_LIST_ID);
        DataStore.saveCache(cache, LoadFlags.SAVE_ALL);
        return true;
    }

    /**
     * Removes a single cache from the online ignore list and from the local ignore list. Must not be called on
     * main thread.
     *
     * @return {@code false} if an error occurred, {@code true} otherwise
     */
    public static boolean unignoreOnline(final Geocache cache) {
        final IConnector connector = ConnectorFactory.getConnector(cache);
        if (!(connector instanceof IIgnoreCapability) || !((IIgnoreCapability) connector).removeFromIgnorelist(cache)) {
            return false;
        }
        DataStore.removeFromList(Collections.singletonList(cache), StoredList.IGNORE_LIST_ID);
        return true;
    }

    private static boolean canIgnore(final Geocache cache) {
        final IConnector connector = ConnectorFactory.getConnector(cache);
        return connector instanceof IIgnoreCapability && ((IIgnoreCapability) connector).canIgnoreCache(cache) && !cache.getLists().contains(StoredList.IGNORE_LIST_ID);
    }

    private static boolean canUnignore(final Geocache cache) {
        final IConnector connector = ConnectorFactory.getConnector(cache);
        return connector instanceof IIgnoreCapability && ((IIgnoreCapability) connector).canRemoveFromIgnoreCache(cache) && cache.getLists().contains(StoredList.IGNORE_LIST_ID);
    }

    public static boolean anySupportsIgnoreList(final List<Geocache> caches) {
        return caches.stream().anyMatch(cache -> ConnectorFactory.getConnector(cache) instanceof IIgnoreCapability);
    }

    public static boolean anySupportsIgnoring(final List<Geocache> caches) {
        return caches.stream().anyMatch(IgnoreListUtils::canIgnore);
    }

    public static boolean anySupportsUnignoring(final List<Geocache> caches) {
        return caches.stream().anyMatch(IgnoreListUtils::canUnignore);
    }

    /**
     * Populates the local ignore list with the online ignore list content on first use. Safe to call repeatedly,
     * as it is a no-op once the initial sync has happened. Must not be called on main thread.
     *
     * @param onSyncDone optional callback, invoked on a background thread once the sync attempt finished
     *                    (whether or not anything was actually synced)
     */
    public static void syncOnlineIgnoreListIfNeeded(final Context context, @Nullable final Runnable onSyncDone) {
        if (Settings.isIgnoreListSynced()) {
            return;
        }
        AndroidRxUtils.networkScheduler.scheduleDirect(() -> {
            boolean anySuccess = false;
            boolean anyFailure = false;
            for (final IConnector connector : ConnectorFactory.getActiveConnectors()) {
                if (connector instanceof IIgnoreListCapability) {
                    final List<Geocache> ignoredCaches = ((IIgnoreListCapability) connector).fetchIgnoreList();
                    if (ignoredCaches == null) {
                        anyFailure = true;
                        continue;
                    }
                    anySuccess = true;
                    if (!ignoredCaches.isEmpty()) {
                        DataStore.addToList(ignoredCaches, StoredList.IGNORE_LIST_ID);
                    }
                }
            }
            if (anySuccess) {
                Settings.setIgnoreListSynced(true);
            } else if (anyFailure) {
                ActivityMixin.showToast(context, LocalizationUtils.getString(R.string.ignorelist_sync_failed));
            }
            if (onSyncDone != null) {
                onSyncDone.run();
            }
        });
    }
}
