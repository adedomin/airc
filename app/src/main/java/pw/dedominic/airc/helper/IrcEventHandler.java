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

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import pw.dedominic.airc.model.IrcMessage;

/**
 * Created by prussian on 12/10/16.
 */
public class IrcEventHandler extends Handler {

        private OnIrcEvent callback;

        public interface OnIrcEvent {
            public void newMessage(IrcMessage msg);
            public void channelAdded(IrcMessage msg);
            public void disconnect();
        }

        public IrcEventHandler(Looper looper, Activity activity) {
            super(looper);
            this.callback = (OnIrcEvent) activity;
        }

        /**
         * Dispatch the appropriate activity functions to handle irc events
         *
         * @param msg the message
         */
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ConnectionListener.MESSAGE_EVENT:
                    if (!(msg.obj instanceof IrcMessage)) return;
                    callback.newMessage((IrcMessage) msg.obj);
                    break;
                case ConnectionListener.JOIN_EVENT:
                    if (!(msg.obj instanceof IrcMessage)) return;
                    callback.channelAdded((IrcMessage) msg.obj);
                    break;
                case ConnectionListener.PART_EVENT:
                    break;
                case ConnectionListener.QUIT_EVENT:
                    break;
                case ConnectionListener.STATUS_EVENT:
                    break;
                case ConnectionListener.DISCONNECTED_EVENT:
                    callback.disconnect();
                    break;
            }
        }
}
