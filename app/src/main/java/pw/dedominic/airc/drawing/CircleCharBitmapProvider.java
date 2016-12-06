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
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;

/**
 * Created by prussian on 12/6/16.
 */

public class CircleCharBitmapProvider {
    public static Bitmap getCircleChar(String first, Resources resources) {
        Bitmap bitmap = Bitmap.createBitmap(300, 300, Bitmap.Config.ARGB_8888);
        BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        Paint paint = PaintFactory.getRandomCirclePaint(resources);
        paint.setShader(shader);
        Rect bounds = new Rect();
        paint.getTextBounds(first, 0, first.length(), bounds);

        Canvas canvas = new Canvas(bitmap);
        canvas.drawCircle(bounds.centerX(), bounds.centerY(),
                bounds.width()/2, paint);
        canvas.drawText(first, bounds.centerX(),
                bounds.centerY(), PaintFactory.getPaint(0,255,255,255));

        return bitmap;
    }
}
