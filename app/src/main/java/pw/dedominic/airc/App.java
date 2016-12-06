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

package pw.dedominic.airc;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.j256.ormlite.dao.RuntimeExceptionDao;

import java.util.HashMap;
import java.util.Map;

import pw.dedominic.airc.db.DatabaseHelper;
import pw.dedominic.airc.fragment.AddEditServerFragment;
import pw.dedominic.airc.fragment.ChannelFragment;
import pw.dedominic.airc.model.Conversation;
import pw.dedominic.airc.model.Server;

/**
 * Created by prussian on 12/5/16.
 */
public class App extends OrmLiteBaseActivity<DatabaseHelper>
                 implements NavigationView.OnNavigationItemSelectedListener,
                            AddEditServerFragment.OnSubmitAddServer {

    private Context context;

    private Server selectedServer;
    private Map<String, Conversation> channelConvos;
    private DrawerLayout drawer;
    private NavigationView navView;
    private RuntimeExceptionDao<Server, String> dao;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        context = getApplicationContext();
        setContentView(R.layout.app_layout);
        dao = getHelper().getDao();
        if (savedInstance != null) {
            selectedServer = (Server) savedInstance.get("selectedServer");
            channelConvos = (Map<String, Conversation>) savedInstance.get("channelConvos");
        }
        if (channelConvos == null) {
            channelConvos = new HashMap<String, Conversation>();
        }
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navView = (NavigationView) findViewById(R.id.navigation_view);
        navView.setNavigationItemSelectedListener(this);
        recreateDrawerItems();
    }

    public void recreateDrawerItems() {
        Menu menu = navView.getMenu();
        menu.clear();
        for (Server server : dao) {
            menu.add(server.getTitle());
        }
        SubMenu smenu = menu.addSubMenu("Actions");
        for (String str : getResources().getStringArray(R.array.nav_items)) {
            smenu.add(str);
        }
    }

    /**
     * save instance before destroy
     *
     * @param savedInstance a bundle
     */
    @Override
    public void onSaveInstanceState(Bundle savedInstance) {
        // Save the user's current game state
        savedInstance.putSerializable("selectedServer", selectedServer);
        savedInstance.putSerializable("channelConvos", (HashMap) channelConvos);

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstance);
    }

    private boolean editSelect = false;
    private boolean deleteSelect = false;
    private Server editable_server;

    /**
     * edit r create a new irc server
     *
     * @param server a server id (title)
     */
    public void createOrEditServer(String server) {
        Server serv = dao.queryForId(server);
        if (serv == null) {
            serv = Server.getDefaultServer();
            editable_server = null;
        }
        else {
            editable_server = serv;
        }
        AddEditServerFragment fragment = AddEditServerFragment.newInstance(serv);
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                       .replace(R.id.fragment_area, fragment)
                       .commit();
    }

    public void removeServer(String server) {
        dao.deleteById(server);
        recreateDrawerItems();
    }

    /**
     * handles nav items
     *
     * @param item the selected item
     * @return true if handled
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        Server selected = dao.queryForId(item.getTitle().toString());
        Log.e("onnav", item.getTitle().toString());
        Log.e("onnav", editSelect+" "+deleteSelect);
        if (selected != null && editSelect) {
            createOrEditServer(selected.getTitle());
            drawer.closeDrawer(Gravity.LEFT);
            editSelect = false;
            deleteSelect = false;
            return true;
        }
        else if (selected != null && deleteSelect) {
            removeServer(selected.getTitle());
            drawer.closeDrawer(Gravity.LEFT);
            deleteSelect = false;
            editSelect = false;
            return true;
        }

        if (item.getTitle().toString().equals("Add Server")) {
            if (editSelect || deleteSelect) {
                editSelect = false;
                deleteSelect = false;
            }
            createOrEditServer("");
            drawer.closeDrawer(Gravity.LEFT);
        }
        else if (item.getTitle().toString().equals("Edit Server")) {
            if (!editSelect) {
                Toast.makeText(this, "Tap Server to Edit or retap to cancel", Toast.LENGTH_SHORT).show();
            }
            if (deleteSelect) deleteSelect = false;
            editSelect = !editSelect;
        }
        else if (item.getTitle().toString().equals("Delete Server")) {
            if (!deleteSelect) {
                Toast.makeText(this, "Tap Server to delete or retap to cancel", Toast.LENGTH_SHORT).show();
            }
            if (editSelect) editSelect = false;
            deleteSelect = !deleteSelect;
        }
        return true;
    }

    public void addServer(Server server) {
        if (server != null) {
            if (editable_server != null && !editable_server.getTitle().equals(server.getTitle())) {
                dao.delete(editable_server);
            }
            else if (dao.queryForId(server.getTitle()) != null) {
                dao.update(server);
            }
            else {
                dao.create(server);
            }
            recreateDrawerItems();
        }
        editable_server = null;
        getFragmentManager().beginTransaction()
                            .replace(R.id.fragment_area, new ChannelFragment())
                            .commit();
    }
}