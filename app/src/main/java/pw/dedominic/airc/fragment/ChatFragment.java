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

package pw.dedominic.airc.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import pw.dedominic.airc.R;
import pw.dedominic.airc.helper.ChatAdapter;
import pw.dedominic.airc.model.Conversation;
import pw.dedominic.airc.model.IrcMessage;
import pw.dedominic.airc.model.Settings;

/**
 * Fragment that shows chat.
 */
public class ChatFragment extends Fragment {

    private OnChatInput callback;

    private ListView chatList;
    private EditText chatInput;
    private ChatAdapter chatAdapter;

    public ChatFragment() {}

    public static ChatFragment newInstance(Context context, Conversation conversation) {
        ChatFragment chatFragment = new ChatFragment();
        chatFragment.setChatAdapter(ChatAdapter.newInstance(context, conversation));
        return chatFragment;
    }

    public interface OnChatInput {
        public void sendMessage(IrcMessage message);
        public void sendCommand(String quotable);
        public String getNickname();
        public Settings getSettings();
    }

    public synchronized void newMessage() {
        chatAdapter.notifyDataSetChanged();
    }

    private void setChatAdapter(ChatAdapter chatAdapter) {
        this.chatAdapter = chatAdapter;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        callback = (OnChatInput) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle bundle) {
        View v = inflater.inflate(R.layout.fragment_chat, null);

        if (callback != null && callback.getSettings() != null && chatAdapter != null)
            chatAdapter.setFontSize(callback.getSettings().getFontSize());
        chatList = (ListView) v.findViewById(R.id.chat_list);
        chatList.setAdapter(chatAdapter);
        chatList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (chatAdapter.getItem(position) == null) return;
                String nick = chatAdapter.getItem(position).getNick();
                int inputpos = chatInput.getSelectionStart();
                if (inputpos < 1) nick = nick+":";
                chatInput.setText(chatInput.getText().insert(inputpos, nick+" "));
                chatInput.setSelection(inputpos+nick.length()+1);
                chatInput.requestFocus();
            }
        });
        chatList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (chatAdapter.getItem(position) == null) return true;
                String body = chatAdapter.getItem(position).toString() + " <--";
                chatInput.setText(body);
                chatInput.setSelection(body.length()+1);
                chatInput.requestFocus();
                return true;
            }
        });
        chatInput = (EditText) v.findViewById(R.id.chat_input);
        chatInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    if (v.getText().toString().equals("")) return true;
                    IrcMessage msg = new IrcMessage(
                        v.getText().toString(),
                        callback.getNickname(),
                        System.currentTimeMillis()
                    );
                    callback.sendMessage(msg);
                    chatAdapter.notifyDataSetChanged();
                    chatInput.setText("");
                }
                return true;
            }
        });
        return v;
    }

}
