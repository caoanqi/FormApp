package com.example.server;

import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.server.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    /**
     * 启动服务端端口
     * 服务端IP为手机IP
     */
    private SocketServer server = new SocketServer(6666);

    ActivityMainBinding activityMainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        /**socket服务端开始监听*/
        server.beginListen();

        activityMainBinding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**socket发送数据*/
                server.sendMessage(activityMainBinding.etContent.getText().toString());
            }
        });

        /**socket收到消息线程*/
        SocketServer.ServerHandler = new Handler() {
            @SuppressLint("HandlerLeak")
            @Override
            public void handleMessage(Message msg) {
                activityMainBinding.tvResult.setText(msg.obj.toString());
            }
        };

    }
}
