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

import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.j256.ormlite.dao.RuntimeExceptionDao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import pw.dedominic.airc.db.DatabaseSingleton;
import pw.dedominic.airc.fragment.AddEditServerFragment;
import pw.dedominic.airc.fragment.ChannelFragment;
import pw.dedominic.airc.fragment.ChatFragment;
import pw.dedominic.airc.fragment.PrefFragment;
import pw.dedominic.airc.helper.ChannelAdapter;
import pw.dedominic.airc.model.Conversation;
import pw.dedominic.airc.model.IrcMessage;
import pw.dedominic.airc.model.Server;
import pw.dedominic.airc.model.Settings;

/**
 * Created by prussian on 12/5/16.
 */
public class App extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        AddEditServerFragment.OnSubmitAddServer,
        ChannelFragment.OnSelectChannel,
        ChatFragment.OnChatInput {

    public static final String CHAN_PREFIX = "[#&!+~.]";
    public static final Pattern CHAN_MATCH = Pattern.compile("^[#&!+~]");
    public static final Pattern SPACE_MATCH = Pattern.compile("[ \t\n,:]");

    private DatabaseSingleton databaseSingleton;

    private Server selectedServer;
    private Map<String, Conversation> channelConvos;
    private DrawerLayout drawer;
    private NavigationView navView;
    private ActionBar actionBar;
    private ActionBarDrawerToggle toggle;
    private RuntimeExceptionDao<Server, String> dao;

    private Settings settings;

    public Settings getSettings() {
        return settings;
    }

    public Server getSelectedServer() {
        return selectedServer;
    }

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);

        // recover state from previous run
        if (savedInstance != null) {
            selectedServer = (Server) savedInstance.get("selectedServer");
            channelConvos = (Map<String, Conversation>) savedInstance.get("channelConvos");
        }
        if (channelConvos == null) {
            channelConvos = new HashMap<String, Conversation>();
        }

        // database init
        DatabaseSingleton.initializeInstance(this);
        databaseSingleton = DatabaseSingleton.getInstance();
        dao = databaseSingleton.getDatabase();
        // settings
        settings = new Settings(this);

        // layout
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        setContentView(R.layout.app_layout);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navView = (NavigationView) findViewById(R.id.navigation_view);
        navView.setNavigationItemSelectedListener(this);

        // init layout
        if (selectedServer == null) {
            actionBar.setTitle("Not Connected");
        }
        toggle = new ActionBarDrawerToggle(this, drawer, 0, 0) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }
        };
        drawer.setDrawerListener(toggle);
        recreateDrawerItems();
        changeServer(selectedServer);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) return true;
        int menuid = item.getItemId();
        switch (menuid) {
            case R.id.add_chan:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Add Channel");
                final EditText input = new EditText(this);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);
                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        channelAdded(input.getText().toString());
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
                break;
            case R.id.close_server:
                changeServer(null);
        }
        return true;
    }

    public void recreateDrawerItems() {
        Menu menu = navView.getMenu();
        menu.clear();
        SubMenu smenu = menu.addSubMenu("Servers");
        for (Server server : dao) {
            if (server.equals(selectedServer))
                smenu.add(server.getTitle()).setIcon(R.drawable.green_circle);
            else smenu.add(server.getTitle());
        }
        smenu = menu.addSubMenu("Actions");
        String[] actions = getResources().getStringArray(R.array.nav_items);
        if (actions.length != 5) throw new RuntimeException("LOLWUT?");
        smenu.add(actions[0]).setIcon(android.R.drawable.ic_input_add);
        smenu.add(actions[1]).setIcon(android.R.drawable.ic_menu_edit);
        smenu.add(actions[2]).setIcon(android.R.drawable.ic_menu_delete);
        smenu.add(actions[3]).setIcon(android.R.drawable.ic_menu_preferences);
        smenu.add(actions[4]);

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

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            super.onBackPressed();
        }
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // closing causes issues with testing lol
        //databaseSingleton.closeDatabase();
    }

    private boolean editSelect = false;
    private boolean deleteSelect = false;
    private Server editable_server;

    private void changeServer(Server server) {
        selectedServer = server;
        channelConvos = new HashMap<String, Conversation>();
        ArrayList<String> channels = null;
        if (server != null) {
            actionBar.setTitle(server.getTitle());
            channels = server.getChannels();
            if (channels == null) {
                channels = new ArrayList<String>();
            }
        }

        getFragmentManager().beginTransaction()
                .replace(R.id.fragment_area,
                        ChannelFragment.newInstance(channels))
                .commit();

        recreateDrawerItems();
    }

    /**
     * edit r create a new irc server
     *
     * @param server a server id (title)
     */
    private void createOrEditServer(String server) {
        Server serv = dao.queryForId(server);
        if (serv == null) {
            serv = Server.getDefaultServer();
            editable_server = null;
        } else {
            editable_server = serv;
        }

        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        AddEditServerFragment fragment = AddEditServerFragment.newInstance(serv);
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.enter_left, R.anim.exit_right,
                        R.anim.enter_left, R.anim.exit_right)
                .replace(R.id.fragment_area, fragment)
                .addToBackStack(null)
                .commit();
    }

    private void removeServer(String server) {
        dao.deleteById(server);
        recreateDrawerItems();
    }

    private void showSettings() {

        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        getFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.enter_left, R.anim.exit_right,
                        R.anim.enter_left, R.anim.exit_right)
                .replace(R.id.fragment_area, new PrefFragment())
                .addToBackStack(null)
                .commit();
    }

    private void showChat(String channel) {

        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        getFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.enter_left, R.anim.exit_right,
                        R.anim.enter_left, R.anim.exit_right)
                .replace(R.id.fragment_area, ChatFragment.newInstance(this, getChat(channel)))
                .addToBackStack(null)
                .commit();
    }

    /**
     * handles nav items
     *
     * @param item the selected item
     * @return true if handled
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            onBackPressed();
        }
        Server selected = dao.queryForId(item.getTitle().toString());
        if (selected != null && editSelect) {
            createOrEditServer(selected.getTitle());
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            drawer.closeDrawer(Gravity.LEFT);
            editSelect = false;
            deleteSelect = false;
            return true;
        } else if (selected != null && deleteSelect) {
            removeServer(selected.getTitle());
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            drawer.closeDrawer(Gravity.LEFT);
            deleteSelect = false;
            editSelect = false;
            return true;
        } else if (selected != null) {
            changeServer(selected);
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            drawer.closeDrawer(Gravity.LEFT);
        }

        if (item.getTitle().toString().equals("Add Server")) {
            if (editSelect || deleteSelect) {
                editSelect = false;
                deleteSelect = false;
            }
            createOrEditServer("");
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            drawer.closeDrawer(Gravity.LEFT);
        } else if (item.getTitle().toString().equals("Edit Server")) {
            if (!editSelect) {
                Toast.makeText(this, "Tap Server to Edit or retap to cancel", Toast.LENGTH_SHORT).show();
                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);
            } else {
                Toast.makeText(this, "Canceled", Toast.LENGTH_SHORT).show();
                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            }
            if (deleteSelect) deleteSelect = false;
            editSelect = !editSelect;
        } else if (item.getTitle().toString().equals("Delete Server")) {
            if (!deleteSelect) {
                Toast.makeText(this, "Tap Server to delete or retap to cancel", Toast.LENGTH_SHORT).show();
                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);
            } else {
                Toast.makeText(this, "Canceled", Toast.LENGTH_SHORT).show();
                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            }
            if (editSelect) editSelect = false;
            deleteSelect = !deleteSelect;
        } else if (item.getTitle().toString().equals("Settings")) {
            editSelect = false;
            deleteSelect = false;
            showSettings();
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            drawer.closeDrawer(Gravity.LEFT);
        }
        else if (item.getTitle().toString().equals("Exit")) {
            finishAffinity();
            System.exit(0);
        }
        return true;
    }

    public void addServer(Server server) {
        if (server != null) {
            if (editable_server != null && !editable_server.getTitle().equals(server.getTitle())) {
                dao.delete(editable_server);
            } else if (dao.queryForId(server.getTitle()) != null) {
                server.setChannels(editable_server.getChannels());
                dao.update(server);
            } else {
                dao.create(server);
            }
            recreateDrawerItems();
        }
        editable_server = null;
        this.onBackPressed();
    }

    public Conversation getChat(String channel) {
        if (!channelConvos.containsKey(channel)) {
            channelConvos.put(channel, new Conversation(settings.getScrollbackSize()));
        }
        return channelConvos.get(channel);
    }

    @Override
    public void channelSelected(String channel) {
        if (channel.equals(ChannelAdapter.NOT_CONNECT)) return;
        showChat(channel);
    }

    @Override
    public void channelRemoved(String channel) {
        if (selectedServer == null) return;
        Server getServer = dao.queryForId(selectedServer.getTitle());
        if (getServer == null) return;
        getServer.removeChannel(channel);
        dao.update(getServer);
        changeServer(getServer);
    }

    @Override
    public void channelAdded(String channel) {

        if (!CHAN_MATCH.matcher(channel).find() || settings.overridePrefixReq()) {
            Toast.makeText(this, "Channel must have a valid prefix " + CHAN_PREFIX, Toast.LENGTH_SHORT).show();
            return;
        }
        if (SPACE_MATCH.matcher(channel).find()) {
            Toast.makeText(this, "Channel cannot have spaces, commas or colons", Toast.LENGTH_SHORT).show();
            return;
        }
        if (selectedServer == null) return;
        Server getServer = dao.queryForId(selectedServer.getTitle());
        if (getServer == null) return;
        getServer.addChannel(channel);
        dao.update(getServer);
        changeServer(getServer);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void sendMessage(IrcMessage message) {
       return;
    }

    @Override
    public void sendCommand(String quotable) {
        return;
    }

    @Override
    public String getNickname() {
        if (selectedServer != null)
            return selectedServer.getNick();
        return settings.getDefaultNick();
    }
}