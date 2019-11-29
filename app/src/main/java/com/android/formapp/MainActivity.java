package com.android.formapp;

import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.formapp.databinding.ActivityMainBinding;
import com.android.formapp.model.UserModel;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding activityMainBinding;
    private SocketClient client;

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        UserModel userModel = new UserModel();
        userModel.setName("曹操");
        activityMainBinding.setData(userModel);

//        activityMainBinding.btGetValue.setOnClickListener(v -> {
//            activityMainBinding.tvShow.setText(userModel.getName());
//        });

        client = new SocketClient();
        //服务端的IP地址和端口号
        client.clintValue(this, "10.0.3.15", 6666);
        //开启客户端接收消息线程
        client.openClientThread();
        /**
         * 发送消息
         * */
        activityMainBinding.btGetValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                client.sendMsg(activityMainBinding.etContent.getText().toString());
            }
        });
        /**
         *  接受消息
         *
         **/

        SocketClient.mHandler = new Handler() {
            @SuppressLint("HandlerLeak")
            @Override
            public void handleMessage(Message msg) {
                activityMainBinding.tvShow.setText(msg.obj.toString());
                Log.i("msghh", msg.obj.toString());
            }
        };

    }


}
