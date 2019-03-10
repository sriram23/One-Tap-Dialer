package com.example.sriram.onetapdialer;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.nio.Buffer;

import static android.Manifest.permission.CALL_PHONE;

public class MainActivity extends AppCompatActivity {
    Cursor c;
    SQLiteDatabase db;
    EditText no;
    FloatingActionButton fab;
    TextView tv;
    LinearLayout L;
    TextView T;
    ConstraintLayout C;
    int size;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db=openOrCreateDatabase("Onetap", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS contact(name VARCHAR,number VARCHAR);");
        c = db.rawQuery("SELECT name FROM contact",null);
        size = c.getColumnCount();

        c = db.rawQuery("SELECT * FROM contact",null);
        StringBuffer buffer=new StringBuffer();
        int i=0;
        final LinearLayout L = (LinearLayout) findViewById(R.id.mainlinear);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        while(c.moveToNext()){
            buffer.append(c.getString(0)+" - "+c.getString(1));
//            final ListView V1 = new ListView(this);
            final LinearLayout L1 = new LinearLayout(this);
            final String num = c.getString(1);
            L1.setOrientation(LinearLayout.HORIZONTAL);
            L1.setPadding(10,10,10,10);
            L1.setBackgroundColor(getResources().getColor(R.color.Trans));
            final TextView txt = new TextView(this);
            txt.setId(i+1);
            txt.setText(c.getString(0)+" - "+c.getString(1));
            final Button btn = new Button(this);
            btn.setId(i+1);
            btn.setText(R.string.dial);
            btn.setLayoutParams(params);
            btn.setPadding(10,10,10,10);
            btn.setBackgroundColor(getResources().getColor(R.color.call));
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callPhoneNumber(num);
                }
            });
            final Button del = new Button(this);
            del.setId(i+1);
            del.setText("Delete");
            del.setBackgroundColor(getResources().getColor(R.color.danger));
            del.setPadding(10,10,10,10);
//            final AlertDialog.Builder alertDialogBuilder;
//            alertDialogBuilder = new AlertDialog.Builder(this);
            del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    Snackbar.make(v, "Del Clicked", Snackbar.LENGTH_LONG).show();
//                    txt.setVisibility(View.INVISIBLE);
//                    btn.setVisibility(View.INVISIBLE);
//                    del.setVisibility(View.INVISIBLE);
                    c = db.rawQuery("SELECT * FROM contact WHERE number = '" + num + "'", null);
                    if (c.moveToFirst()) {
                        db.execSQL("DELETE FROM contact WHERE number='" + num + "'");
                        Snackbar.make(v, "Record Deleted", Snackbar.LENGTH_LONG).show();
                        L1.removeView(txt);
                        L1.removeView(btn);
                        L1.removeView(del);
                        L.removeView(L1);
                    } else {
                        Snackbar.make(v, "Invalid Record!", Snackbar.LENGTH_LONG).show();
                    }
                }
            });

            L1.addView(txt);
            L1.addView(btn);
            L1.addView(del);
//            V1.addView(L1);
            L.addView(L1);
            i++;
        }

    }
    public void callPhoneNumber(String n)
    {
        try
        {
            if(Build.VERSION.SDK_INT > 22)
            {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 101);

                    return;
                }

                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + n));
                startActivity(callIntent);

            }
            else {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + n));
                startActivity(callIntent);
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
    public void flt(View view){

        Intent intent = new Intent(MainActivity.this,floating.class);
        startActivity(intent);
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mymenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.mybtn) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                finishAffinity();
            }
            else{
                ActivityCompat.finishAffinity(MainActivity.this);
            }
        }
        return super.onOptionsItemSelected(item);
    }
}

