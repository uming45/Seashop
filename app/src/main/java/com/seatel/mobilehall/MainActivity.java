package com.seatel.mobilehall;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.seatel.mobilehall.net.INetworkState;
import com.seatel.mobilehall.net.NetworkState;
import com.seatel.mobilehall.view.NetFailBar;
import com.seatelgroup.xmpp.conn.ConnectionManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jivesoftware.smack.chat2.Chat;
import org.jivesoftware.smack.chat2.IncomingChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smackx.receipts.ReceiptReceivedListener;
import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.jid.Jid;

public class MainActivity extends AppCompatActivity implements IncomingChatMessageListener, ReceiptReceivedListener {

    private EditText textEt;
    private ConnectionManager mConnManager;
    private EditText domainEt;
    private EditText phoneEt;
    private NetFailBar mNetFailBar;
    private NetworkState mNetworkState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNetworkState = new NetworkState(this);
        mConnManager = new ConnectionManager(mNetworkState);
        initView();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("event-bus", "onStart: ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("event-bus", "onStop: ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("event-bus", "onDestroy: ");
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("event-bus", "onPause: ");
    }

    private void initView() {
        this.textEt = (EditText) this.findViewById(R.id.text);
        this.domainEt = (EditText) this.findViewById(R.id.domain);
        this.phoneEt = (EditText) this.findViewById(R.id.phone);
        this.mNetFailBar = (NetFailBar) this.findViewById(R.id.net_state_tv);

        if (!mNetworkState.isAvailable()) {
            this.mNetFailBar.setVisibility(View.VISIBLE);
        }
    }

    public void connect(View v) {
        String phone = phoneEt.getText().toString();
        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(MainActivity.this, "phone can not be empty!", Toast.LENGTH_SHORT).show();
        }

        String domain = domainEt.getText().toString();
        if (TextUtils.isEmpty(domain)) {
            domain = ConnectionManager.DOMAIN;
        }

        mConnManager.connect(phone, "123456", domain, ConnectionManager.PORT);
        // 在回调中调用？
        //mConnManager.setOnReceiptReceivedListener(MainActivity.this);
    }

    public void send(View v) {
        String msg;
        if ((msg = textEt.getText().toString()).length() == 0) {
            return;
        }

        if (!mNetworkState.isAvailable()) {
            Toast.makeText(this, "not connected!", Toast.LENGTH_SHORT).show();
        } else {

        }

    }

    public void persistent(View v) {

    }

    public void disconnect(View v) {
        mConnManager.disconnect();
    }

    @Override
    public void newIncomingMessage(EntityBareJid from, Message message, Chat chat) {
        Log.d("msg-get", "newIncomingMessage: " +
                from + ", " + message + ", " + chat);
    }

    @Override
    public void onReceiptReceived(Jid fromJid, Jid toJid, String receiptId, Stanza receipt) {
        Log.d("rec-get", "onReceiptReceived: " + fromJid + "," + toJid +
                "," + receiptId + ", " + receipt);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(INetworkState.NetStateEvent event) {
        mNetFailBar.onMessageEvent(event.isConnected());
    }
}
