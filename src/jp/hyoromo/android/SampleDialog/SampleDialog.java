package jp.hyoromo.android.SampleDialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View.OnClickListener;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SampleDialog extends Activity implements OnClickListener {

    private static final String TAG = "SampleDialog";
    private static AlertDialog mAlertDialog;
    private static ProgressDialog mProgressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Button progressDialogButton = (Button) findViewById(R.id.ProgressDialogButton);
        progressDialogButton.setOnClickListener(this);

        Button alertDialogButton1 = (Button) findViewById(R.id.AlertDialogButton1);
        alertDialogButton1.setOnClickListener(this);

        Button alertDialogButton2 = (Button) findViewById(R.id.AlertDialogButton2);
        alertDialogButton2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
        case R.id.ProgressDialogButton:
            mProgressDialog = showProgressDialog();
            new Thread(new Runnable() {
                public void run() {
                    sleepProgressDialog();
                }
            }).start();
            break;
        case R.id.AlertDialogButton1:
            mAlertDialog = showAlertDialog1();
            mAlertDialog.show();
            break;
        case R.id.AlertDialogButton2:
            mAlertDialog = showAlertDialog2();
            mAlertDialog.show();
        }
    }

    public ProgressDialog showProgressDialog() {
        String title = getResources().getString(R.string.progress_dialog_name_title);
        String mes = getResources().getString(R.string.progress_dialog_name_mes);
        return ProgressDialog.show(this, title, mes, true);
    }

    public void sleepProgressDialog() {
        try {
            // 3秒間お待ちください
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mProgressDialog.dismiss();
    }

    private AlertDialog showAlertDialog1() {
        LayoutInflater factory = LayoutInflater.from(this);
        final View entryView = factory.inflate(R.layout.dialog_entry, null);
        final EditText edit = (EditText) entryView.findViewById(R.id.username_edit);

        // キーハンドリング
        edit.setOnKeyListener(new View.OnKeyListener(){
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                 // Enterキーハンドリング
                if (KeyEvent.KEYCODE_ENTER == keyCode) {
                    // 押したときに改行を挿入防止処理
                    if (KeyEvent.ACTION_DOWN == event.getAction()) {
                        return true;
                    }
                     // 離したときにダイアログ上の[OK]処理を実行
                    else if (KeyEvent.ACTION_UP == event.getAction()) {
                        if (edit != null && edit.length() != 0) {
                            // ここで[OK]が押されたときと同じ処理をさせます
                            String editStr = edit.getText().toString();
                            // OKボタン押下時のハンドリング
                            Log.v(TAG, editStr);

                            // AlertDialogを閉じます
                            mAlertDialog.dismiss();
                        }
                        return true;
                    }
                }
                return false;
            }
        });

        // AlertDialog作成
        return new AlertDialog.Builder(this)
            .setIcon(R.drawable.icon)
            .setTitle(R.string.alert_dialog_name1_title)
            .setView(entryView)
            .setPositiveButton(R.string.alert_dialog_name1_button1, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    String editStr = edit.getText().toString();
                    // OKボタン押下時のハンドリング
                    Log.v(TAG, editStr);
                }
            }).setNegativeButton(R.string.alert_dialog_name1_button2, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    // NOボタン押下時のハンドリング
                }
            }).create();
    }

    public AlertDialog showAlertDialog2() {
        return new AlertDialog.Builder(this)
        .setIcon(R.drawable.icon)
        .setTitle(R.string.alert_dialog_name2_title)
        .setMessage(R.string.alert_dialog_name2_mes)
        .setPositiveButton(R.string.alert_dialog_name2_button, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                Log.v(TAG, "Push OK button.");
            }
        })
        .create();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mProgressDialog = null;
        mAlertDialog = null;
    }
}