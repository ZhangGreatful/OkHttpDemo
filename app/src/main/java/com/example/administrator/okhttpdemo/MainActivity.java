package com.example.administrator.okhttpdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

public class MainActivity extends AppCompatActivity {
    private ListView               listView;
    private ArrayAdapter<Contributor>   adapter;
    private OkHttpClient           client;
    //    compile 'com.squareup.okhttp3:logging-interceptor:3.4.0-RC1'
//    日志拦截器
    private HttpLoggingInterceptor loggingInterceptor;


//    class GithubTask extends AsyncTask<Void, Void, Response> {
//        @Override
//        protected Response doInBackground(Void... params) {
//            Request request = new Request.Builder()
//                    .url("http://api.github.com")
//                    .build();
//            try {
////                请求(同步)
//                Response response = client.newCall(request).execute();
//                return response;
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return null;
//        }

//        @Override
//        protected void onPostExecute(Response response) {
//            super.onPostExecute(response);
//
//                try {
//                    if (response.isSuccessful()) {
//                        ResponseBody body = response.body();
//                        Toast.makeText(MainActivity.this, body.string(), Toast.LENGTH_SHORT).show();
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        列表视图
        listView = (ListView) findViewById(R.id.listview);
        adapter=new ArrayAdapter<Contributor>(this,android.R.layout.simple_list_item_1);
        listView.setAdapter(adapter);
//        new GithubTask().execute();
        loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);//设置等级

        client = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)//添加一个日志拦截器
                .build();
//        http://api.github.com/repos/square/retrofit/contributors
//        构建好请求
        String owner = "square";//公司
        String repo = "retrofit";//产品
        Request request = new Request.Builder()
                .url("http://api.github.com/repos/" + owner + "/" + repo + "/contributors")
                .build();
//        请求(异步)
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call,final IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
//                解析出body里的信息

//                写出实体类
//                使用Gson
                Gson gson = new Gson();
                TypeToken<List<Contributor>> typeToken = new
                        TypeToken<List<Contributor>>() {
                        };
                final List<Contributor> contributors = gson.fromJson(body, typeToken.getType());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.addAll(contributors);
                    }
                });
            }
        });

//        加依赖
//        实例化
//        OkHttpClient okHttpClient = new OkHttpClient();
//        构建请求
//        Request request = new Request.Builder()
//                .url("http://api.github.com")
//                .build();
//          请求(同步)
//        try {
//            Response response = okHttpClient.newCall(request).execute();
//            if (response.isSuccessful()) {
//                ResponseBody body = response.body();
//                if (body == null) {
//                    Log.d(TAG, "未知错误+++++++++++");
//                }
//                Log.d(TAG, "onResponse: +++++++++++++" + response.body().string());
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

//        请求(异步)
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                Log.d(TAG, "onFailure: ****************");
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                Log.d(TAG, "Success*****************");
//                if (response.isSuccessful()) {
//                    ResponseBody body = response.body();
//                    if (body == null) {
//                        Log.d(TAG, "未知错误+++++++++++");
//                        return;
//                    }
//                    Log.d(TAG, "onResponse: +++++++++++++" + response.body().string());
//                }
//            }
//        });
    }
}

