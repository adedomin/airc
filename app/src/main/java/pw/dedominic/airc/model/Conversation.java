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
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Messages associated with current channel
 */
public class Conversation implements Serializable {

    private int scrollbackSize;
    private LinkedList<IrcMessage> buffer;
    private ArrayList<String> nicks;


    public Conversation(int scrollbackSize) {
        this.scrollbackSize = scrollbackSize;
        this.buffer = new LinkedList<IrcMessage>();
        this.nicks = new ArrayList<String>();
    }

    public void addMessage(IrcMessage message) {
        buffer.add(message);

        if (buffer.size() > scrollbackSize) {
            buffer.remove(0);
        }
    }

    public List<String> autocompleteChoices(String partial) {
        List<String> completes = new ArrayList<String>();
        Pattern finder = Pattern.compile("^"+partial);
        for (String nick : nicks) {
            Matcher matcher = finder.matcher(nick);
            if (matcher.find()) completes.add(nick);
        }
        return completes;
    }

    public LinkedList<IrcMessage> getBuffer() {
        return buffer;
    }

    public void setScrollbackSize(int scrollbackSize) {
        this.scrollbackSize = scrollbackSize;
    }

    public ArrayList<String> getNicks() {
        return nicks;
    }
}
