package com.example.zhiwei.cms;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

//import org.apache.http.HttpResponse;
//import org.apache.http.HttpStatus;
//import org.apache.http.client.ClientProtocolException;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.impl.client.HttpClientBuilder;
//import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private TextView tvSearchResult = null;
    private EditText etSearchContent = null;
    private Button btSearch = null;
    private ProgressDialog  mDlg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(onMenuItemClick);

        mDlg = new ProgressDialog(this);

        tvSearchResult = (TextView)findViewById(R.id.text_search_result);
        etSearchContent = (EditText)findViewById(R.id.edit_search);
        btSearch = (Button)findViewById(R.id.button_search);
        this.btSearch.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                String s = etSearchContent.getText().toString();
                if (s.length() == 0) {
                    Toast.makeText(MainActivity.this,
                            "药材名称不能为空！",
                            Toast.LENGTH_LONG).show();
                    return;
                }

                GetTask task = new GetTask();
                task.execute(s);
            }
        });
    }

    private Toolbar.OnMenuItemClickListener onMenuItemClick = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_add:
                    Toast.makeText(MainActivity.this, "add click", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this, MedicineAddActivity.class);
                    startActivity(intent);
                    break;
                case R.id.action_settings:
                    Toast.makeText(MainActivity.this, "setting click", Toast.LENGTH_LONG).show();
                    break;
            }
            return true;
        }
    };
    private class GetTask extends AsyncTask<String, Void, String>
    {
        @Override
        protected void onPreExecute()
        {
           if (mDlg != null && mDlg.isShowing())
           {
               mDlg.dismiss();
           }
            mDlg.show();
        }

        @Override
        protected String doInBackground(String... params)
        {
            try
            {
                getMethod(params[0]);
            }
            catch (Exception e)
            {

            }
            return null;
        }

        @Override
        protected void onPostExecute(String s)
        {
            if (mDlg != null)
            {
                mDlg.dismiss();
            }
        }
    }

    private String getMethod(String param) throws IOException
    {
        String  str = String.format("http://ayl.me:8002/querymedicine?name=%s", param);
        URL url = new URL(str);
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setConnectTimeout(5*1000);
        conn.setRequestMethod("GET");
        conn.connect();

        InputStream inStream = conn.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inStream));
        String lines;
        while ((lines = reader.readLine()) != null) {
            tvSearchResult.setText(lines);
        }
        return lines;
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
}
