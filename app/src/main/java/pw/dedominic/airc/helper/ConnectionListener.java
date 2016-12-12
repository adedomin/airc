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

package pw.dedominic.airc.helper;

import android.os.Handler;
import android.os.Message;

import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.ConnectEvent;
import org.pircbotx.hooks.events.DisconnectEvent;
import org.pircbotx.hooks.events.JoinEvent;
import org.pircbotx.hooks.events.KickEvent;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.events.NickChangeEvent;
import org.pircbotx.hooks.events.PartEvent;
import org.pircbotx.hooks.events.QuitEvent;
import org.pircbotx.hooks.types.GenericChannelEvent;
import org.pircbotx.hooks.types.GenericMessageEvent;

import pw.dedominic.airc.model.IrcMessage;

/**
 * Created by prussian on 12/10/16.
 */

public class ConnectionListener extends ListenerAdapter {

    public Handler callback;
    public static final int MESSAGE_EVENT = 0;
    public static final int JOIN_EVENT = 1;
    public static final int PART_EVENT = 2;
    public static final int QUIT_EVENT = 3;
    public static final int STATUS_EVENT = 4;
    public static final int DISCONNECTED_EVENT = 5;
    public static final int CONNECTED_EVENT = 6;
    public static final int NICK_CHANGE_EVENT = 7;

    public ConnectionListener(Handler callback) {
        this.callback = callback;
    }

    @Override
    public void onConnect(ConnectEvent event) throws Exception {
        callback.sendEmptyMessage(CONNECTED_EVENT);
    }

    /**
     * Chat message was received
     *
     * @param event the message
     * @throws Exception
     */
    @Override
    public void onMessage(MessageEvent event) throws Exception {
        Message msg = callback.obtainMessage();
        msg.what = MESSAGE_EVENT;
        msg.obj = new IrcMessage(
                event.getChannel().getName(),
                event.getMessage(),
                event.getUser().getNick(),
                event.getTimestamp());
        callback.sendMessage(msg);
    }

    /**
     * Currently only used to handle ZNC automatically joining you to channels.
     *
     * @param event
     * @throws Exception
     */
    @Override
    public void onJoin(JoinEvent event) throws Exception {
        Message message = callback.obtainMessage();
        message.what = JOIN_EVENT;
        IrcMessage ircMessage = new IrcMessage();
        ircMessage.setTimestamp(event.getTimestamp());
        ircMessage.setNick(event.getUser().getNick());
        ircMessage.setChannel(event.getChannel().getName());
        ircMessage.setBody("* "+ircMessage.getNick()+" HAS JOINED *");
        ircMessage.setStatus(true);
        message.obj = ircMessage;
        callback.sendMessage(message);
    }

    @Override
    public void onPart(PartEvent event) throws Exception {
        Message message = callback.obtainMessage();
        message.what = PART_EVENT;
        IrcMessage ircMessage = new IrcMessage();
        ircMessage.setTimestamp(event.getTimestamp());
        ircMessage.setNick(event.getUser().getNick());
        ircMessage.setChannel(event.getChannel().getName());
        ircMessage.setBody("* "+ircMessage.getNick()+" HAS LEFT *");
        ircMessage.setStatus(true);
        message.obj = ircMessage;
        callback.sendMessage(message);
    }

    @Override
    public void onKick(KickEvent event) throws Exception {
        Message message = callback.obtainMessage();
        message.what = PART_EVENT;
        IrcMessage ircMessage = new IrcMessage();
        ircMessage.setTimestamp(event.getTimestamp());
        ircMessage.setNick(event.getRecipient().getNick());
        ircMessage.setChannel(event.getChannel().getName());
        ircMessage.setBody("* "+ircMessage.getNick()+" HAS BEEN KICKED *");
        ircMessage.setStatus(true);
        message.obj = ircMessage;
        callback.sendMessage(message);
    }

    @Override
    public void onNickChange(NickChangeEvent event) throws Exception {
        Message message = callback.obtainMessage();
        message.what = NICK_CHANGE_EVENT;
        IrcMessage ircMessage = new IrcMessage();
        ircMessage.setNick(event.getOldNick());
        ircMessage.setBody(event.getNewNick());
        message.obj = ircMessage;
        callback.sendMessage(message);
    }

    /**
     * if randomly disconnected
     *
     * @param event disconnect event
     * @throws Exception
     */
    @Override
    public void onDisconnect(DisconnectEvent event) throws Exception {
        callback.sendEmptyMessage(DISCONNECTED_EVENT);
    }
}
