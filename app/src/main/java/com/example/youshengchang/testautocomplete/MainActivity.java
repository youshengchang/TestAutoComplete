package com.example.youshengchang.testautocomplete;

import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


public class MainActivity extends ActionBarActivity {

    private AutoCompleteTextView actv;

    private SharedPreferences store;
    private final String LIST_KEY = "key";
    private String[] countries;
    Set<String> set;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        store = getPreferences(MODE_PRIVATE);
        actv = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView1);

        setupAutoList();
        updateView();

    }

    private void setupAutoList() {
        SharedPreferences.Editor edit = store.edit();
        set = store.getStringSet(LIST_KEY, null);
        if(set != null){
            Object[] objs = set.toArray();
            countries = new String[objs.length];
            for(int i = 0; i < countries.length;i++){
                countries[i] = (String)objs[i];
            }
        }else{
            countries = new String[]{""};
            set = new HashSet<String>();

        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void getText(View v){
        boolean duplicated = false;
        AutoCompleteTextView actv = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView1);
        String selectionText = actv.getText().toString();
        Log.i("AUTOCOMPLETE:", "selected text = " + selectionText);
        Toast.makeText(this, "Text is " + selectionText, Toast.LENGTH_LONG).show();
        for(String country: set){
            if(selectionText.trim().equalsIgnoreCase(country))
                duplicated = true;
        }
        if(!duplicated){

            set.add(selectionText);
            Object[] obj = set.toArray();
            Iterator<String> it = set.iterator();
            Log.i("AUTO", "obj array length = " + obj.length);
            countries = new String[obj.length];
            for(int i = 0; i< obj.length; i++){
                countries[i] = (String)obj[i];
            }


            SharedPreferences.Editor edit = store.edit();
            edit.putStringSet(LIST_KEY, set);
            edit.commit();
            updateView();
        }

    }

    private void updateView() {
        ArrayAdapter adapter = new ArrayAdapter
                (this,android.R.layout.simple_list_item_1,countries);
        actv.setAdapter(adapter);

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        setupAutoList();
        updateView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupAutoList();
        updateView();
    }
}
