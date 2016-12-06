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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

import pw.dedominic.airc.R;
import pw.dedominic.airc.drawing.CircleCharBitmapProvider;

/**
 * Generates channel names with icons of the Second (first is chan prefix) letter
 */
public class ChannelAdapter extends ArrayAdapter<String> {

    public static final String STATUS_VALUE = "- STATUS -";
    public static final String ADD_CHAN_VALUE= "- Add Channel -";
    public static final String NOT_CONNECT = "- Not Connected -";


    private ChannelAdapter(Context context, ArrayList<String> channels, boolean x) {
        super(context, 0, channels);
    }

    /**
     * Factory method to create array adapters for channels
     *
     * @param context app context
     * @param channels channels in current server, or null if not connected
     * @return new ChannelAdapter
     */
    public static ChannelAdapter newInstance(Context context, ArrayList<String> channels) {
        ArrayList<String> arrcopy;
        if (channels == null) {
            arrcopy = new ArrayList<String>();
            arrcopy.add(NOT_CONNECT);
        }
        else {
            arrcopy = (ArrayList<String>) channels.clone();
            Collections.sort(arrcopy);
            arrcopy.add(0, STATUS_VALUE);
            arrcopy.add(ADD_CHAN_VALUE);
        }

        return new ChannelAdapter(context, arrcopy, false);
    }

    /**
     * creates bitmap and fills text with channel name,
     * unless channel name is - STATUS - or - ADD Channel -
     *
     * @param position
     * @param convertView
     * @param parent
     * @return view
     */
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        String channel = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.channel_view, parent, false);
        }
        TextView name = (TextView) convertView.findViewById(R.id.channel_name);
        ImageView icon = (ImageView) convertView.findViewById(R.id.channel_bitmap);

        if (channel != null) {
            name.setText(channel);
            if (!channel.equals(STATUS_VALUE)
                    && !channel.equals(ADD_CHAN_VALUE)
                    && !channel.equals(NOT_CONNECT)) {
                icon.setImageBitmap(
                        CircleCharBitmapProvider.getCircleChar(
                                "" + channel.charAt(1), getContext().getResources()
                        )
                );
            }
        }
        return convertView;
    }
}
