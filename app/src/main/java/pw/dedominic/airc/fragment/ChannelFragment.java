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

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;

import pw.dedominic.airc.R;
import pw.dedominic.airc.helper.ChannelAdapter;

/**
 * Created by prussian on 12/5/16.
 */
public class ChannelFragment extends Fragment {

    private ListView channelList;
    private OnSelectChannel callback;

    public interface OnSelectChannel {
        public void channelSelected(String channel);
        public void channelRemoved(String channel);
        public void channelAdded(String channel);
    }

    public ChannelFragment() {
    }

    public static ChannelFragment newInstance(ArrayList<String> channels) {
        Log.e("newChannelFrag", "new instance");
        ChannelFragment channelFragment = new ChannelFragment();
        Bundle bundle = new Bundle();
        if (channels != null) {
            bundle.putSerializable("channelList", channels);
        }
        channelFragment.setArguments(bundle);
        return channelFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        callback = (OnSelectChannel) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle bundle) {

        View v = inflater.inflate(R.layout.fragment_channel, null);
        channelList = (ListView) v.findViewById(R.id.channel_list);
        channelList.setAdapter(
                ChannelAdapter.newInstance(
                    getActivity(), (ArrayList<String>) getArguments().getSerializable("channelList")
                )
        );
        channelList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String channel = (String) parent.getItemAtPosition(position);
                if (channel.equals(ChannelAdapter.ADD_CHAN_VALUE)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder((Context) getActivity());
                    builder.setTitle("Add Channel");
                    final EditText input = new EditText((Context) getActivity());
                    input.setInputType(InputType.TYPE_CLASS_TEXT);
                    builder.setView(input);
                    // Set up the buttons
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            callback.channelAdded(input.getText().toString());
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                    return;
                }
                callback.channelSelected(channel);
            }
        });
        channelList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final String channel = (String) parent.getItemAtPosition(position);
                if (channel.equals(ChannelAdapter.ADD_CHAN_VALUE)
                        || channel.equals(ChannelAdapter.STATUS_VALUE)
                        || channel.equals(ChannelAdapter.NOT_CONNECT)) return true;

                AlertDialog.Builder builder = new AlertDialog.Builder((Context) getActivity());
                builder.setTitle("Remove Channel "+channel+"?");
                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        callback.channelRemoved(channel);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
                return true;
            }
        });
        return v;
    }
}
