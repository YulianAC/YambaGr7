package co.edu.udea.cmovil.gr1.yamba;

import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.marakana.android.yamba.clientlib.YambaClient;
import com.marakana.android.yamba.clientlib.YambaClientException;


public class StatusActivity extends ActionBarActivity {

    private EditText editStatus;
    private TextView textCount;
    private int defaultTextColor;
    private static final String TAG = "StatusActivity"; //variable del punto 7-d
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        editStatus = (EditText) findViewById(R.id.textoTweet);
        Button buttonTweet = (Button) findViewById(R.id.btntweet);
        textCount = (TextView) findViewById(R.id.TxtCont);
        defaultTextColor = textCount.getTextColors().getDefaultColor();

        Log.d(TAG,"OK point 7-d"); // Implementacio del Log.d en el onCreate

        /**
         * Implementamos un metodo para saber cuando el usuario excede el limite de caracteres
         */
        editStatus.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                int count = 140 - editStatus.length();
                textCount.setText(Integer.toString(count));
                textCount.setTextColor(Color.GREEN);
                if (count < 10)
                    textCount.setTextColor(Color.RED);
                else
                    textCount.setTextColor(defaultTextColor);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_status, menu);
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


    /**
     * Creamos la clase con la cual vamos a manejar los eventos que haga o desepeñe el boton
     * @param v
     */
    public void onclick(View v){
        String status = editStatus.getText().toString();
        Log.d("Test","click ok");
        new PostTask().execute(status);

    }

    /**
     * Creamos el metodo que nos permititara manejar multiples hilos en android
     */
    private final class PostTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            YambaClient HCloud = new YambaClient("student", "password");
            try {
                HCloud.postStatus(params[0]);
                return "Successfully posted";
            } catch (YambaClientException e) {
                e.printStackTrace();
                return "Failed to post to yamba service";
            }
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Toast.makeText(StatusActivity.this, result, Toast.LENGTH_LONG).show();
        }
    }
}
