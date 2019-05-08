package com.example.ks.moodle;

import android.app.Activity;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ks.moodle.entity.Student;
import com.example.ks.moodle.util.MyDatabaseHelper;
import com.example.ks.moodle.util.Register;

public class MyActivity extends AppCompatActivity {

    private MyDatabaseHelper dbhelper;
    private Button newUserTv;
    private Button loginBtn;
    private EditText xuehaoTv;
    private EditText pwdTv;
    private SQLiteDatabase db;
    private Student student;
    private CheckBox rememberTv;
    private MyDatabaseHelper myDatabaseHelper;
    private String userId;
    private String name;
    private String major;
    private Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    Toolbar toolbar;//定义标题栏



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        /*设置标题栏*/
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        xuehaoTv=(EditText)findViewById(R.id.xuehao);
        pwdTv=(EditText)findViewById(R.id.pwd);

        newUserTv=(Button) findViewById(R.id.regist);
        loginBtn=(Button)findViewById(R.id.login);
        rememberTv=(CheckBox)findViewById(R.id.remember);


//        sp = PreferenceManager.getDefaultSharedPreferences(this);
//        String account = sp.getString("xuehao", "");
//        String password = sp.getString("mima", "");
//
//        xuehaoTv.setText(account);
//        pwdTv.setText(password);

        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);
        boolean isRemember=sharedPreferences.getBoolean("remember_password",false);
        if(isRemember){
            xuehaoTv.setText(sharedPreferences.getString("xuehao",""));
            pwdTv.setText(sharedPreferences.getString("mima",""));
            rememberTv.setChecked(true);
        }

        Intent i = getIntent();
        if(i != null) {
            boolean value = i.getBooleanExtra("key", false);
            if(!value) {
                isZiDong();
            }
        }




        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isZiDong();
            }
        });

        newUserTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(MyActivity.this,Register.class);
                startActivity(i);
            }
        });
        



        //数据库的建立并且升级
        dbhelper=new MyDatabaseHelper(this,"moodle1.db",null,2);
        //创建数据库

        SQLiteDatabase db=dbhelper.getWritableDatabase();


        }





    private void isZiDong() {
        editor=sharedPreferences.edit();
        userId=xuehaoTv.getText().toString();
        String password=pwdTv.getText().toString();
        /*
        db=new MyDatabaseHelper(this,"moodle1.db",null,2).getReadableDatabase();
        Student student= (Student) db.rawQuery("select name from student where id=1801210729",null);
this,"请输入账号和密码",Toast.LENGTH_SHORT this, "moodle1.db", null, 2
        student.setName("ning");
        Log.d("mo",student.getName());
*/
        if(userId.equals("")||password.equals("")){
            Toast.makeText(MyActivity.this, "请输入账号和密码", Toast.LENGTH_SHORT).show();
        }else{
            db = new MyDatabaseHelper(MyActivity.this,"moodle1.db",null, 2).getReadableDatabase();
            Cursor cursor=db.query("student",new String[]{"xuehao","password"},null,null,null,null,null);
            boolean login=false;
            //从数据库中匹配学号和密码
            while(cursor.moveToNext()){
                if(userId.equals(cursor.getString(cursor.getColumnIndex("xuehao")))&&
                        password.equals(cursor.getString(cursor.getColumnIndex("password")))){
                    login=true;
                    break;
                }
            }
            //如果匹配成功
            if(login){

                myDatabaseHelper=new MyDatabaseHelper(MyActivity.this,"moodle1.db",null,2);
                SQLiteDatabase db=myDatabaseHelper.getWritableDatabase();
                String sql="select * from student where xuehao=?";
                Cursor cursor1=db.rawQuery(sql,new String[]{userId});
                while(cursor1.moveToNext()){
                    name = cursor1.getString(cursor1.getColumnIndex("name"));
                    major=cursor1.getString(cursor1.getColumnIndex("description"));
                    Log.d("ks",name);
                    Log.d("kss",major);
                }


                if(rememberTv.isChecked())
                {
                    editor.putString("xuehao",userId);
                    editor.putString("mima",password);
                    editor.putBoolean("remember_password",true);
                    Toast.makeText(MyActivity.this,"success",Toast.LENGTH_SHORT).show();
                }
                else {
                    editor.clear();
                    Toast.makeText(MyActivity.this,"No",Toast.LENGTH_SHORT).show();
                }
                editor.apply();


                Intent intent=new Intent(MyActivity.this,TestExpendListView.class);
                startActivity(intent);
                finish();



            }else{
                editor.clear();
                editor.apply();
                Toast.makeText(MyActivity.this,"学号或密码错误",Toast.LENGTH_SHORT).show();

            }
        }

    }

    /**1
     * 无法登录？重置密码
     */

    private void ResetPwd() {


    }

    /**
     * 登录账号验证
     */
    private void Login(){


    }
}