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
import java.util.LinkedList;

/**
 * Messages associated with current channel
 */
public class Conversation implements Serializable {

    private int scrollbackSize;
    private LinkedList<IrcMessage> buffer;

    public Conversation(int scrollbackSize) {
        this.scrollbackSize = scrollbackSize;
        this.buffer = new LinkedList<IrcMessage>();
    }

    public void addMessage(IrcMessage message) {
        buffer.add(message);

        if (buffer.size() > scrollbackSize) {
            buffer.remove(0);
        }
    }

    public LinkedList<IrcMessage> getBuffer() {
        return buffer;
    }
}
