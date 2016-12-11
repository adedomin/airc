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

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import pw.dedominic.airc.R;
import pw.dedominic.airc.helper.ChatAdapter;
import pw.dedominic.airc.helper.LeftSwipeDetect;
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
        final GestureDetector gesture = new GestureDetector(getActivity(),
            new LeftSwipeDetect(getActivity()));
        if (callback != null && callback.getSettings() != null)
            chatAdapter.setFontSize(callback.getSettings().getFontSize());
        chatList = (ListView) v.findViewById(R.id.chat_list);
        chatList.setAdapter(chatAdapter);
        chatInput = (EditText) v.findViewById(R.id.chat_input);
        chatInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    IrcMessage msg = new IrcMessage(
                        v.getText().toString(),
                        callback.getNickname(),
                        System.currentTimeMillis()
                    );
                    chatAdapter.addMessage(msg);
                    callback.sendMessage(msg);
                    chatInput.setText("");
                }
                return true;
            }
        });
        return v;
    }

}
