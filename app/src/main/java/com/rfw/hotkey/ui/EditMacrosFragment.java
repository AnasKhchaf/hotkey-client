package com.rfw.hotkey.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.rfw.hotkey.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class EditMacrosFragment extends Fragment {
    private static final String TAG = "EditMacrosFragment";
    private  int index;
    private Button saveButton;
    private Button cancelButton;
    private TextView buttonName;


    private String[] array_keys_num = {
            "0","1", "2", "3", "4", "5","6", "7","8", "9"
    };
    private String[] array_keys_char = {
            "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"
    };

    private String[] array_keys_sp = {
            "Esc", "Alt", "Ctrl", "Shift", "Del", "Ins", "Home", "End", "PgUp", "PgDn"
    };

    private List<String> listSourceNum = new ArrayList<>();
    private List<String> listSourceChar = new ArrayList<>();
    private List<String> listSourceSP = new ArrayList<>();
    private List<String> macroButtons = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_edit_macros, container, false);
        saveButton = (Button)v.findViewById(R.id.saveMacroButtonid);
        cancelButton = (Button)v.findViewById(R.id.calcelButtonid);
        buttonName = (TextView)v.findViewById(R.id.macroNameFieldeid);

        Bundle bundle = getArguments();
        if(bundle != null){
            index = bundle.getInt("index");
            setUpList();
            GridView gridViewNum =(GridView) v.findViewById(R.id.macroGridNumid);
            GridView gridViewChar =(GridView) v.findViewById(R.id.macroGridCharid);
            GridView gridViewSP =(GridView) v.findViewById(R.id.macroGridSPid);
            MacroGridViewAdapter adapterNum = new MacroGridViewAdapter(listSourceNum,inflater.getContext(), macroButtons);
            MacroGridViewAdapter adapterChar = new MacroGridViewAdapter(listSourceChar,inflater.getContext(), macroButtons);
            MacroGridViewAdapter adapterSP = new MacroGridViewAdapter(listSourceSP,inflater.getContext(), macroButtons);
            gridViewNum.setAdapter(adapterNum);
            gridViewChar.setAdapter(adapterChar);
            gridViewSP.setAdapter(adapterSP);
        }

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!buttonName.getText().toString().isEmpty()){
                    saveNewMacroData();
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Cancelled Pressed", Toast.LENGTH_SHORT).show();
            }
        });

        return v;
    }

    private void setUpList() {
        for(String item : array_keys_num){
            listSourceNum.add(item);
        }
        for(String item : array_keys_char){
            listSourceChar.add(item.toUpperCase());
        }
        for(String item : array_keys_sp){
            listSourceSP.add(item);
        }
    }
    private void saveNewMacroData(){
        SharedPreferences sharedPref = Objects.requireNonNull(getActivity()).getPreferences(Context.MODE_PRIVATE);
        String macroFile = sharedPref.getString("macroObject", null);
        if (macroFile != null) {
            try {
                JSONObject jsonMacro = new JSONObject(macroFile);

                JSONObject macroKey = new JSONObject();
                try {
                    macroKey.put("type", "macro");
                    macroKey.put("name", buttonName.getText().toString());
                    macroKey.put("size", Integer.toString(macroButtons.size()));
                    for(int i =0; i < macroButtons.size(); i++){
                        macroKey.put(Integer.toString(i), macroButtons.get(i).toString());
                    }
                    jsonMacro.put(Integer.toString(index), macroKey);

                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("macroObject", jsonMacro.toString());
                    editor.apply();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    }
}
