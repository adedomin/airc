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

package pw.dedominic.airc.db;

import android.content.Context;

import com.j256.ormlite.dao.RuntimeExceptionDao;

import pw.dedominic.airc.model.Server;

/**
 * Created by prussian on 12/7/16.
 */
public class DatabaseSingleton {

    private int counter = 0;
    private static DatabaseSingleton INSTANCE;
    private static DatabaseHelper helperInstance;
    private RuntimeExceptionDao<Server, String> servers;

    public static synchronized void initializeInstance(Context context) {
        if (INSTANCE != null) return;
        helperInstance = new DatabaseHelper(context);
        INSTANCE = new DatabaseSingleton();
    }

    public static synchronized DatabaseSingleton getInstance() {
        if (INSTANCE == null) {
            throw new IllegalStateException(DatabaseSingleton.class.getSimpleName() +
                    " is not initialized, call initializeInstance(..) method first.");
        }
        return INSTANCE;
    }

    public synchronized RuntimeExceptionDao<Server, String> getDatabase() {
        counter++;
        if (counter == 1) {
            servers = helperInstance.getDao();
        }
        return servers;
    }

    public synchronized void closeDatabase() {
        counter--;
        if (counter == 0) {
            helperInstance.close();
        }
    }
}
