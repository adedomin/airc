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
import android.app.Fragment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import pw.dedominic.airc.R;
import pw.dedominic.airc.helper.LeftSwipeDetect;
import pw.dedominic.airc.model.Server;
import pw.dedominic.airc.model.Settings;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddEditServerFragment extends Fragment
                                   implements View.OnClickListener {

    private EditText name;
    private EditText host;
    private EditText port;
    private EditText nick;
    private EditText pass;
    private EditText nick_pass;
    private CheckBox tls;

    private Button saveBtn;
    private Button cancelBtn;

    private GestureDetector detector;

    private OnSubmitAddServer callback;

    public AddEditServerFragment() {
        // Required empty public constructor
    }

    public static AddEditServerFragment newInstance(Server server) {
        AddEditServerFragment newInstance = new AddEditServerFragment();
        Bundle bundle = new Bundle();
        if (server != null) {
            bundle.putSerializable("editable", server);
            newInstance.setArguments(bundle);
        }
        return newInstance;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        callback = (OnSubmitAddServer) context;
    }

    public interface OnSubmitAddServer {
        public void addServer(Server server);
        public Settings getSettings();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_addedit_server, null);

        final GestureDetector gesture = new GestureDetector(getActivity(),
                new LeftSwipeDetect(getActivity()));

        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gesture.onTouchEvent(event);
            }
        });

        name = (EditText) view.findViewById(R.id.title_text);
        host = (EditText) view.findViewById(R.id.hostname_text);
        port = (EditText) view.findViewById(R.id.port_text);
        nick = (EditText) view.findViewById(R.id.nick_text);
        nick.setText(callback.getSettings().getDefaultNick());
        pass = (EditText) view.findViewById(R.id.pass_text);
        nick_pass = (EditText) view.findViewById(R.id.nickpass_text);
        tls = (CheckBox) view.findViewById(R.id.tls_check);
        saveBtn = (Button) view.findViewById(R.id.save_button);
        cancelBtn = (Button) view.findViewById(R.id.cancel_button);

        saveBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);

        if (getArguments() != null) {
            editServer((Server) getArguments().get("editable"));
        }
        return view;
    }

    public void editServer(Server server) {
        name.setText(server.getTitle());
        host.setText(server.getHost());
        port.setText(server.getPort()+"");
        if (server.getNick().equals("")) {
            nick.setText(callback.getSettings().getDefaultNick());
        }
        else {
            nick.setText(server.getNick());
        }
        pass.setText(server.getPassword());
        nick_pass.setText(server.getNickpass());
        tls.setChecked(server.isTls());
    }

    public void errString(String err) {
        Toast.makeText(getActivity(), err, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.save_button) {
            Server server = new Server();
            server.setTitle(name.getText().toString());
            if (server.getTitle().equals("")) {
                errString("name field required");
                return;
            }
            server.setHost(host.getText().toString());
            if (server.getHost().equals("")) {
                errString("host field required");
                return;
            }
            try {
                server.setPort(Integer.parseInt(port.getText().toString()));
            } catch (NumberFormatException e) {
                errString("Invalid number for port");
                return;
            }
            server.setPassword(pass.getText().toString());
            server.setNick(nick.getText().toString());
            if (server.getNick().equals("")) {
                errString("nick field required");
                return;
            }
            server.setNickpass(nick_pass.getText().toString());
            server.setTls(tls.isChecked());

            callback.addServer(server);
            return;
        }
        callback.addServer(null);
    }
}
