package com.unifyx.syncfiles;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.dropbox.sync.android.DbxAccountManager;
import com.dropbox.sync.android.DbxFile;
import com.dropbox.sync.android.DbxFileSystem;
import com.dropbox.sync.android.DbxPath;

import java.io.IOException;


public class MainActivity extends Activity {

    private static final String APP_KEY="1er4u6q40r9b7ll";
    private static final String APP_SECRET="ui4gria08b2qp4v";
    public DbxAccountManager mDbxAcctMgr;
    static final int REQUEST_LINK_TO_DBX = 1010;  // This value is up to you

    public Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("debug this","init");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = (Button) findViewById(R.id.button);

        execute();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linkAccount();
            }
        });

        Log.d("debug this", "onCreate() done");
    }


    public void execute() {
        Log.d("debug this","creating dropbox manager");
        mDbxAcctMgr = DbxAccountManager.getInstance(MainActivity.this.getApplicationContext(), APP_KEY, APP_SECRET);
    }

    public void linkAccount() {
        //execute();
        Log.d("debug this","linking account");
        mDbxAcctMgr.startLink(this, REQUEST_LINK_TO_DBX);
    }

    public void testUpload() throws IOException {
        DbxFileSystem dbxFs = DbxFileSystem.forAccount(mDbxAcctMgr.getLinkedAccount());
        DbxFile testFile = dbxFs.create(new DbxPath("hello.txt"));
        testFile.writeString("Hello Dropbox!");
        testFile.close();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("debug this","activity result");
        if (requestCode == REQUEST_LINK_TO_DBX) {
            if (resultCode == Activity.RESULT_OK) {
                // ... Start using Dropbox files.
                try {
                    Log.d("debug this","uploading test file");
                    testUpload();
                } catch (IOException e) {
                    Log.d("debug this","error 1");
                }
            } else {
                Log.d("debug this","error 2");  // ERROR GOES HERE
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}
