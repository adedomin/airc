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

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by prussian on 12/5/16.
 */
@DatabaseTable(tableName = "server")
public class Server implements Comparable<Server>, Serializable {

    @DatabaseField(id = true)
    private String title;

    @DatabaseField
    private String host;
    @DatabaseField
    private int port;
    @DatabaseField
    private String password;
    @DatabaseField
    private boolean tls;
    @DatabaseField(dataType = DataType.SERIALIZABLE)
    private ArrayList<String> channels;
    @DatabaseField
    private String nick;
    @DatabaseField
    private String nickpass;

    public static Server getDefaultServer() {
        Server server = new Server();
        server.setTitle("");
        server.setHost("");
        server.setPort(6667);
        server.setNick("");
        server.setPassword("");
        server.setNickpass("");
        server.setNick("");
        server.setChannels(new ArrayList<String>());
        return server;
    }

    public Server() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isTls() {
        return tls;
    }

    public void setTls(boolean tls) {
        this.tls = tls;
    }

    public ArrayList<String> getChannels() {
        if (channels == null)
            channels = new ArrayList<String>();
        return channels;
    }

    public void setChannels(ArrayList<String> channels) {
        this.channels = channels;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getNickpass() {
        return nickpass;
    }

    public void setNickpass(String nickpass) {
        this.nickpass = nickpass;
    }

    public void addChannel(String channel) {
        if (this.channels == null) {
            this.channels = new ArrayList<String>();
        }
        if (this.channels.indexOf(channel) > -1) return;
        this.channels.add(channel);
    }

    public boolean removeChannel(String channel) {
        if (this.channels == null) return false;
        return this.channels.remove(channel);
    }

    @Override
    public int compareTo(Server o) {
        return this.title.compareTo(o.getTitle());
    }

    @Override
    public boolean equals(Object s) {
        if (!(s instanceof  Server)) return false;
        return this.title.equals(((Server) s).getTitle());
    }
}
