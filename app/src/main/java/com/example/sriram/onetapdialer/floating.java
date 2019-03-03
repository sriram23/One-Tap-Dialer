package com.example.sriram.onetapdialer;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class floating extends AppCompatActivity {
    Cursor cursor;
    SQLiteDatabase db;
    EditText name;
    EditText number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_floating);
        name = (EditText) findViewById(R.id.name);
        number = (EditText) findViewById(R.id.number);
    }
    public void Addclk(View view){
        String Name = null;
        String Number = null;
        if(!(name.getText().toString().equals("")) && !(number.getText().toString().equals(""))) {
            Name = name.getText().toString();
            Number = number.getText().toString();
        }
        else {
            Snackbar.make(view, "Name or Phone number is invalid!", Snackbar.LENGTH_LONG).show();
        }
        db=openOrCreateDatabase("Onetap", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS contact(name VARCHAR,number VARCHAR);");
        if(!(Name.equals("")) && !(Number.equals(""))) {
            db.execSQL("INSERT INTO contact (name,number) VALUES('"+ Name + "','" + Number+"');");
            Snackbar.make(view, "Contact Added!", Snackbar.LENGTH_LONG).show();
            name.setText("");
            number.setText("");
        }
        else {
            Snackbar.make(view, "Name or Phone number is invalid!", Snackbar.LENGTH_LONG).show();
        }
    }
    public void Cancelclk(View view){
        Snackbar.make(view,"Cancel Button Clicked!",Snackbar.LENGTH_LONG).show();
        Intent intent = new Intent(floating.this,MainActivity.class);
        startActivity(intent);
    }
}
