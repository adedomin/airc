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

import android.widget.EditText;

import java.util.Scanner;

/**
 * Created by prussian on 12/7/16.
 */
public class CursorWord {
    /**
     * helper for getting word that is near the cursor on an edittext
     *
     * @return the word near an edittext
     */
    public static String getWordAtCursor(EditText editText) {
        int cursorAt = editText.getSelectionStart();
        Scanner scanner = new Scanner(editText.getText().toString());
        scanner.useDelimiter("\\s");

        while (scanner.hasNext()) {
            String word = scanner.next();
            if (scanner.match().start() <= cursorAt && cursorAt <= scanner.match().end()) {
                return word;
            }
        }
        return "";
    }
}
