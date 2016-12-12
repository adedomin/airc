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

import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import pw.dedominic.airc.fragment.BlankFragment;
import pw.dedominic.airc.fragment.ChannelFragment;
import pw.dedominic.airc.fragment.ChatFragment;

/**
 * Created by prussian on 12/11/16.
 */
public class ChanChatPager extends FragmentStatePagerAdapter {

    private BlankFragment fragment = new BlankFragment();
    private ChannelFragment channelFragment;
    private ChatFragment currentChat;

    public void setChannelFragment(ChannelFragment channelFragment) {
        this.channelFragment = channelFragment;
        notifyDataSetChanged();
    }

    public void setCurrentChat(ChatFragment currentChat) {
        this.currentChat = currentChat;
        notifyDataSetChanged();
    }

    public ChanChatPager(FragmentManager fm, ChannelFragment channelFragment) {
        super(fm);
        this.channelFragment = channelFragment;

    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    /**
     * Return the Fragment associated with a specified position.
     *
     * @param position
     */
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return channelFragment;
            case 1:
                if (currentChat == null)
                    return fragment;
                return currentChat;
        }
        return null;
    }

    /**
     * Return the number of views available.
     */
    @Override
    public int getCount() {
        return 2;
    }
}
