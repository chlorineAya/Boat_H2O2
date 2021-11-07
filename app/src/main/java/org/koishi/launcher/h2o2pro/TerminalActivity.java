package org.koishi.launcher.h2o2pro;

import android.graphics.Typeface;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.TextInputEditText;
import com.jrummyapps.android.shell.CommandResult;
import com.jrummyapps.android.shell.Shell;

import java.util.Objects;

import ren.qinc.edit.PerformEdit;

public class TerminalActivity extends AppCompatActivity {

    public MaterialCardView card;
    public EditText outputText,commandText;
    public ProgressDialog dialog;
    public ImageButton backText;
    public MaterialButton clearText,exec;
    public MaterialCheckBox cbRoot;
    public PerformEdit mPerformEdit,mPerformEdit2;
    public TextInputEditText inputText;
    public Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terminal);
        getWindow().setStatusBarColor(getResources().getColor(R.color.material_card_background));
        toolbar = findViewById(R.id.terminal_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.menu_terminal);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish());
        Typeface tf = Typeface.createFromAsset(this.getAssets(),
                "Sans.ttf");
        TextView bigTitle= (TextView) toolbar.getChildAt(0);
        bigTitle.setTypeface(tf);
        bigTitle.setText(getResources().getString(R.string.menu_terminal));
        inputText = findViewById(R.id.terminal_input);
        backText = findViewById(R.id.terminal_backspace);
        commandText = findViewById(R.id.terminal_command);
        clearText = findViewById(R.id.terminal_clear);
        card = findViewById(R.id.terminal_card);
        outputText = findViewById(R.id.terminal_output);

        cbRoot = findViewById(R.id.root_check);
        exec = findViewById(R.id.terminal_exec);

        commandText.setKeyListener(null);
        outputText.setKeyListener(null);
        outputText.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence p1, int p2, int p3, int p4) {
            }

            @Override
            public void onTextChanged(CharSequence p1, int p2, int p3, int p4) {
            }

            @Override
            public void afterTextChanged(Editable p1) {
                if (outputText.getText().toString().equals("")){
                    card.setVisibility(View.GONE);
                } else {
                    card.setVisibility(View.VISIBLE);
                }
            }
        });

        mPerformEdit = new PerformEdit(outputText);
        mPerformEdit2 = new PerformEdit(commandText);
        mPerformEdit.setDefaultText("");

        //outputText.setMovementMethod(new ScrollingMovementMethod());

        backText.setOnClickListener(v->inputText.setText(""));
        clearText.setOnClickListener(v-> {
            outputText.setText("");
            commandText.setText("");
            mPerformEdit.clearHistory();
            mPerformEdit2.clearHistory();
        });

        exec.setOnClickListener(v-> {
            if (!inputText.getText().toString().equals("")) {
                //exec();
                //InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                //imm.hideSoftInputFromWindow(inputText.getWindowToken(), 0);
                String command = inputText.getText().toString();
                boolean asRoot = cbRoot.isChecked();
                new RunCommandTask(asRoot).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, command);
            }else{

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_terminal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_undo:
                mPerformEdit.undo();
                mPerformEdit2.undo();
                break;
            case R.id.action_redo:
                mPerformEdit.redo();
                mPerformEdit2.redo();
                break;
            case R.id.action_del_all:
                outputText.setText("");
                commandText.setText("");
                mPerformEdit.clearHistory();
                mPerformEdit2.clearHistory();
                break;
            default:
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        if (isFinishing()) {
            Shell.SU.closeConsole();
        }
    }

    // Ignore the bad AsyncTask usage.
    final class RunCommandTask extends AsyncTask<String, Void, CommandResult> {

        private final boolean asRoot;

        RunCommandTask(boolean asRoot) {
            this.asRoot = asRoot;
        }

        @Override protected void onPreExecute() {
            dialog = ProgressDialog.show(TerminalActivity.this, getResources().getString(R.string.menu_terminal), "Please Wait...");
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(true);
        }

        @Override protected CommandResult doInBackground(String... commands) {
            if (asRoot) {
                return Shell.SU.run(commands);
            } else {
                return Shell.SH.run(commands);
            }
        }

        @Override protected void onPostExecute(CommandResult result) {
            if (!isFinishing()) {
                if (dialog.isShowing()){
                    dialog.dismiss();
                    card.setVisibility(View.VISIBLE);
                    outputText.append(resultToHtml(result));
                    commandText.setText(inputText.getText().toString());
                    inputText.setText("");
                } else {
                }
            }
        }

        private Spanned resultToHtml(CommandResult result) {
            StringBuilder html = new StringBuilder();
            // exit status
            html.append("<p>");
            html.append("<font>").append("Command: ").append("</font>");
            html.append("<font color = @color/?colorPrimary >").append(inputText.getText().toString()).append("</font>");
            html.append("</p>");
            html.append("<p>Exit Code: ");
            if (result.isSuccessful()) {
                html.append("<font color='green'>").append(result.exitCode).append("</font>");
            } else {
                html.append("<font color='red'>").append(result.exitCode).append("</font>");
            }
            html.append("</p>");
            // stdout
            if (result.stdout.size() > 0) {
                html.append("<p>>STDOUT:</p><p>")
                        .append(result.getStdout().replaceAll("\n", "<br>"))
                        .append("</p>");
            }
            // stderr
            if (result.stderr.size() > 0) {
                html.append("<p>STDERR:</p><p><font color='red'>")
                        .append(result.getStderr().replaceAll("\n", "<br>"))
                        .append("</font></p>");
            }
            html.append("<p>");
            html.append("<font>").append("\n-----Finished-----").append("</font>");
            html.append("</p>");
            return Html.fromHtml(html.toString());
        }

    }

    @Override
    public void onBackPressed() {
        if (dialog != null){
            if (dialog.isShowing()){
                dialog.dismiss();
                //Shell.SU.closeConsole();
            } else {
                finish();
            }
        } else {
            finish();
        }
    }

    /*

    public String getShellResult() {
        try {
            String cmds = "";
            String[] arr = cmds.split("\n"); // 用,分割
            for (String cmd : arr) {
                cmds += cmd + ";";
            }
            String[] cmdA = {"/bin/sh", "-c", cmds};
            Process process = Runtime.getRuntime().exec(cmdA);
            LineNumberReader br = new LineNumberReader(new InputStreamReader(
                    process.getInputStream()));
            StringBuffer sb = new StringBuffer();
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
                sb.append(line).append("\n");
                return sb.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return e.toString();
        }
        return null;

    }

    public void exec(){
        new Thread(() -> {
            b = getShellResult();
            han.sendEmptyMessage(0);
        }).start();
    }

    Handler han = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                outputText.append(b+"\n");
            }
        }
    };

     */
}