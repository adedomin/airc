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

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * irc message body
 */
public class IrcMessage implements Serializable {

    private String channel;
    private String body;
    private String nick;
    private Long timestamp;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    private boolean status;

    public IrcMessage() {
    }

    public IrcMessage(String body, String nick, Long timestamp) {
        this.body = body;
        this.nick = nick;
        this.timestamp = timestamp;
    }

    public IrcMessage(String channel, String body, String nick, Long timestamp) {
        this.channel = channel;
        this.body = body;
        this.nick = nick;
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        String timestamp = "";
        if (this.timestamp != null) {
            timestamp = new SimpleDateFormat("HH:mm", Locale.getDefault())
                    .format(this.timestamp);
        }

        if (status) {
            return timestamp+" "+body;
        }
        else if (nick != null && body != null) {
            return String.format(Locale.getDefault(), "[%s] <%s> %s",
                    timestamp,
                    this.nick,
                    this.body
            );
        }
        return "";
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }
}
