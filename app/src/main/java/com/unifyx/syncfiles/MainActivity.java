package com.unifyx.syncfiles;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.dropbox.sync.android.DbxAccountManager;
import com.dropbox.sync.android.DbxFile;
import com.dropbox.sync.android.DbxFileSystem;
import com.dropbox.sync.android.DbxPath;

import java.io.IOException;


public class MainActivity extends Activity {

    private static final String TAG = "com.unifyx.syncfiles";
    private static final String APP_KEY="1er4u6q40r9b7ll";
    private static final String APP_SECRET="ui4gria08b2qp4v";
    public DbxAccountManager mDbxAcctMgr;
    static final int REQUEST_LINK_TO_DBX = 1010;  // This value is up to you

    public Button linkButton;
    public Button uploadButton;
    public Button unlinkButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "init");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        execute();

        linkButton = (Button) findViewById(R.id.linkButton);
        linkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "clicked linkButton");
                linkAccount();
            }
        });

        uploadButton = (Button) findViewById(R.id.uploadButton);
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "clicked uploadButton");
                if (mDbxAcctMgr.hasLinkedAccount()) {
                    try {
                        Log.d(TAG, "upload success");
                        testUpload();
                    } catch (IOException e) {
                        Log.d(TAG, "error uploading");
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Link an account first", Toast.LENGTH_LONG).show();
                }
            }
        });

        unlinkButton = (Button) findViewById(R.id.unlinkButton);
        unlinkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "clicked unlinkButton");
                mDbxAcctMgr.unlink();
            }
        });

    }

    public void execute() {
        Log.d(TAG, "creating dropbox manager");
        mDbxAcctMgr = DbxAccountManager.getInstance(getApplicationContext(), APP_KEY, APP_SECRET);
    }

    public void linkAccount() {
        //execute();
        Log.d(TAG, "linking account");
        mDbxAcctMgr.startLink((Activity) this, REQUEST_LINK_TO_DBX);
    }

    public void testUpload() throws IOException {
        DbxFileSystem dbxFs = DbxFileSystem.forAccount(mDbxAcctMgr.getLinkedAccount());
        DbxFile testFile = dbxFs.create(new DbxPath("hello.txt"));
        testFile.writeString("Hello Dropbox!");
        testFile.close();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "activity result");
        Log.d(TAG, "resultCode = " + resultCode);
        if (requestCode == REQUEST_LINK_TO_DBX) {
            if (resultCode == Activity.RESULT_OK) {
                // ... Start using Dropbox files.
            } else {
                Log.d(TAG, "error linking account");
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}