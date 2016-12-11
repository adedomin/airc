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

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import pw.dedominic.airc.R;
import pw.dedominic.airc.model.Conversation;
import pw.dedominic.airc.model.IrcMessage;

/**
 * Created by prussian on 12/8/16.
 */

public class ChatAdapter extends ArrayAdapter<IrcMessage> {

    private Conversation chat;
    private float fontSize = 16f;
    /**
     * Constructor
     *
     * @param context  The current context.
     * @param resource The resource ID for a layout file containing a TextView to use when
     *                 instantiating views.
     * @param objects  The objects to represent in the ListView.
     */
    private ChatAdapter(Context context, int resource, List<IrcMessage> objects) {
        super(context, resource, objects);
    }

    public static ChatAdapter newInstance(Context context, Conversation conversation) {
        ChatAdapter adapter = new ChatAdapter(context, 0, conversation.getBuffer());
        adapter.setChat(conversation);
        return adapter;
    }

    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        IrcMessage ircMessage = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.chat_view, parent, false);
        }
        if (ircMessage == null) return convertView;
        TextView textView = (TextView) convertView.findViewById(R.id.chat_msg);
        textView.setText(ircMessage.toString());
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, fontSize);
        return convertView;
    }

    private void setChat(Conversation chat) {
        this.chat = chat;
    }

    public void setFontSize(String string) {
        if (string == null || string.equals("")) return;
        fontSize = Float.parseFloat(string);
    }

    public void addMessage(IrcMessage message) {
        chat.addMessage(message);
        notifyDataSetChanged();
    }
}
