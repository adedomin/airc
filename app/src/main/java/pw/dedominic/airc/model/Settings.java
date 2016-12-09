/*
 * <one line to give the program's name and a brief idea of what it does.>
 * Copyright (C)  2016  prussian <genunrest@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package pw.dedominic.airc.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;

import pw.dedominic.airc.R;

/**
 * Settings model for getting preferred settings easily
 */
public class Settings {

    private final SharedPreferences preferences;
    private final Resources resources;

    /**
     * Create a new Settings instance
     *
     * @param context application context
     */
    public Settings(Context context) {
        this.preferences = PreferenceManager.getDefaultSharedPreferences(context);
        this.resources = context.getApplicationContext().getResources();
    }

    /**
     * if messages should include a timestamp
     *
     * @return true if user wants timestamps
     */
    public boolean showTimestamp() {
        return preferences.getBoolean("show-timestamps", true);
    }

    /**
     * Show IRC color?
     *
     * @return true if set
     */
    public boolean showMircColor() {
        return preferences.getBoolean("show-mirc-color", false);
    }

    /**
     * Notifications enabled?
     *
     * @return true if set
     */
    public boolean showNotifications() {
        return preferences.getBoolean("show-notifications", true);
    }

    /**
     * global show join/part/quits?
     *
     * @return true, overridden by channel settings
     */
    public boolean showJoinPartQuit() {
        return preferences.getBoolean("show-join-part-quit", true);
    }

    /**
     * override requiring channel names to start with: * # & ! + ~ .
     * <p>See RFC 2811 Section 2.1</p>
     * @return true if overridden
     */
    public boolean overridePrefixReq() {
        return preferences.getBoolean("override-prefix", false);
    }

    /**
     * set maximum scrollback buffer
     *
     * @return scrollback buffer size, default 150
     */
    public int getScrollbackSize() {
        return Integer.parseInt(preferences.getString("scrollback", "150"));
    }

    public String getFontSize() {
        return preferences.getString("get-font-size", "16");
    }

    /**
     * Gets default nick if not configured in the server
     *
     * @return the default nick
     */
    public String getDefaultNick() {
        return preferences.getString("default-nick", "airc_user");
    }
}
