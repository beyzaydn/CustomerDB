package com.example.customerdb;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    SQLiteDatabase db;
    EditText etFname;
    EditText etLname;
    EditText etEmail;
    ListView lv;
    Customer cst;


    @Override
    protected void onPause() {
        super.onPause();
        db.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
        db= SQLiteDatabase.openOrCreateDatabase(this.getDatabasePath("MyDb.db"),null);
        db.execSQL("Create Table  if not exists Customer( " +
                "id INTEGER PRIMARY KEY Autoincrement," +
                "Fname varChar Not Null," +
                "Lname Char(50) not Null," +
                "email TEXT UNIQUE" +
                ")  ");
        Listele();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etEmail= (EditText) findViewById(R.id.etEmail);
        etFname=(EditText) findViewById(R.id.etFname);
        etLname=(EditText) findViewById(R.id.etLName);
        lv= (ListView) findViewById(R.id.lvCustomers);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                cst = (Customer)lv.getItemAtPosition(i);
                etFname.setText(cst.getFname());
                etLname.setText(cst.getLname());
                etEmail.setText(cst.getEmail());
            }
        });
    }

    public void Save(View v) {
        String[] str = {etFname.getText().toString(), etLname.getText().toString(), etEmail.getText().toString()};
        try {
            db.execSQL(" Insert Into Customer (Fname,Lname,email) values(?,?,?)", str);
            Toast.makeText(this,"KAYIT BAŞARILI OLDU",Toast.LENGTH_LONG).show();
            Listele();
        }catch (Exception ex){
            Toast.makeText(this,ex.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    public void Listele(){

        ArrayList<Customer> CustList= new ArrayList<>();
        Customer cst;


        Cursor c=db.rawQuery("Select * from Customer",null);

        if(c!=null){

            if(c.moveToFirst()){
                do{

                    cst=new Customer(c.getInt(0),c.getString(1),c.getString(2),c.getString(3));
                    CustList.add(cst);
                }while(c.moveToNext());
            }
        }

        ArrayAdapter<Customer> adap= new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,CustList);
        lv.setAdapter(adap);
    }

    public void Update(View v) {

        Object[] str = {etFname.getText().toString(), etLname.getText().toString(), etEmail.getText().toString(),cst.getId()};
        try {
            db.execSQL(" Update Customer Set Fname=?, Lname=?, email=? where id=?", str);
            Listele();
            Toast.makeText(this,"GÜNCELLENDİ",Toast.LENGTH_LONG).show();
        }catch (Exception ex){
            Toast.makeText(this,ex.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    public void Delete(View v) {
        try {
            db.execSQL("DELETE FROM Customer WHERE id=?", new Object[]{
                    cst.getId()});
            Listele();
            Toast.makeText(this, "Kayıt silindi", Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

}