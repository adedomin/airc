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
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pw.dedominic.airc.R;
import pw.dedominic.airc.helper.ChanChatPager;

/**
 * Created by prussian on 12/11/16.
 */

public class ChannelChatPagedFragment extends Fragment {

    private ChannelFragment fragment;
    private ViewPager p;
    private ChanChatPager padapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    public void setChatChannel(ChatFragment fragment) {
        padapter.setCurrentChat(fragment);
        p.setCurrentItem(1);
    }

    public void setChannel(ChannelFragment fragment) {
        padapter.setChannelFragment(fragment);
        p.setCurrentItem(0);
    }

    public ChatFragment getChatChannel() {
        return (ChatFragment) padapter.getItem(1);
    }

    public ChannelFragment getChannel() {
        return (ChannelFragment) padapter.getItem(0);
    }

    public static ChannelChatPagedFragment newInstance(ChannelFragment fragment) {
        ChannelChatPagedFragment frag = new ChannelChatPagedFragment();
        frag.fragment = fragment;
        return frag;
    }

    /**
     * Called to have the fragment instantiate its user interface view.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_viewpager, null);
        p = (ViewPager) v.findViewById(R.id.viewpager);
        padapter = new ChanChatPager(getChildFragmentManager(),
                fragment
        );
        p.setAdapter(padapter);
        p.setCurrentItem(0);
        return v;
    }
}
