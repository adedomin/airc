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

package pw.dedominic.airc.drawing;

import android.content.res.Resources;
import android.graphics.Paint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;

import pw.dedominic.airc.R;

/**
 * Created by prussian on 12/6/16.
 */
public class PaintFactory {
    private static final Random random = new Random();
    public static Paint getPaint(int a, int r, int g, int b) {
        Paint paint = new Paint();
        paint.setARGB(a, r, g, b);
        return paint;
    }

    public static int getRandomColor(Resources resources, String hash) {
        int code = hash.hashCode() % 8;
        return resources.getIntArray(R.array.random_colors)[code];
    }
}
