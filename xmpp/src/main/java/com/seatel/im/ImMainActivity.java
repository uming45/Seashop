package com.seatel.im;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.seatel.im.conn.ConnectionManager;
import com.seatel.im.service.net.NetworkState;
import com.seatel.im.ui.widget.NetFailBar;

import org.jivesoftware.smack.chat2.Chat;
import org.jivesoftware.smack.chat2.IncomingChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smackx.receipts.ReceiptReceivedListener;
import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.jid.Jid;

public class ImMainActivity extends AppCompatActivity implements IncomingChatMessageListener, ReceiptReceivedListener {

    private EditText textEt;
    private ConnectionManager mConnManager;
    private EditText domainEt;
    private EditText phoneEt;
    private NetFailBar mNetFailBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
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

        if (!NetworkState.isConnectivityAvailable()) {
            this.mNetFailBar.setVisibility(View.VISIBLE);
        }
    }

    public void connect(View v) {
        String phone = phoneEt.getText().toString();
        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(ImMainActivity.this, "phone can not be empty!", Toast.LENGTH_SHORT).show();
        }

        String domain = domainEt.getText().toString();
        if (TextUtils.isEmpty(domain)) {
            domain = ConnectionManager.DOMAIN;
        }

        mConnManager = new ConnectionManager(phone, "123456", domain, ConnectionManager.PORT);
        if (NetworkState.isConnectivityAvailable()) {
            mConnManager.connect();
        } else {
            mNetFailBar.show();
        }
        // 在回调中调用？
        //mConnManager.setOnReceiptReceivedListener(ImMainActivity.this);
    }

    public void send(View v) {
        String msg;
        if ((msg = textEt.getText().toString()).length() == 0) {
            return;
        }

        if (!NetworkState.isConnectivityAvailable()) {
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

}
