package com.example.translate_objecttext;


import static com.example.translate_objecttext.Constant.BASE_URL1;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Pair;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Locale;

public class ConversationActivity extends AppCompatActivity implements View.OnClickListener {

    //For First Layout
    LinearLayout SourcetextInputLayout;
    TextView sourceText;
    LinearLayout TargettextInputLayout;
    TextView targetText;
    Spinner targetLangSelector;
    Spinner sourceLangSelector;
    ImageView sourceVoice;
    ImageView targetVoice;
    ArrayAdapter<TranslateViewModel.Language> adapter;

    ImageView sourceText_speaker, targetText_speaker;

    //For Second Layout
    LinearLayout SourcetextInputLayout2;
    TextView sourceText2;
    LinearLayout TargettextInputLayout2;
    TextView targetText2;
    ImageView sourceText_speaker2, targetText_speaker2;


    TextToSpeech textToSpeech;

    private final int REQ_CODE = 100;
    private final int REQ_CODE_1 = 200;
    private final int Req = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation2);

        initUI();
        checkPermission();
    }

    private void initUI() {

        getSupportActionBar().setTitle(Html.fromHtml("<font color='#FFFFFF'>Conversation</font>"));

        SourcetextInputLayout = findViewById(R.id.SourcetextInputLayout);
        TargettextInputLayout = findViewById(R.id.TargettextInputLayout);
        sourceText = findViewById(R.id.sourceText);
        targetText = findViewById(R.id.targetText);
        targetLangSelector = findViewById(R.id.targetLangSelector);
        sourceLangSelector = findViewById(R.id.sourceLangSelector);
        sourceVoice = findViewById(R.id.sourceVoice);
        targetVoice = findViewById(R.id.targetVoice);
        sourceText_speaker = findViewById(R.id.sourceText_speaker);
        targetText_speaker = findViewById(R.id.targetText_speaker);

        SourcetextInputLayout2 = findViewById(R.id.SourcetextInputLayout2);
        sourceText2 = findViewById(R.id.sourceText2);
        TargettextInputLayout2 = findViewById(R.id.TargettextInputLayout2);
        targetText2 = findViewById(R.id.targetText2);
        sourceText_speaker2 = findViewById(R.id.sourceText_speaker2);
        targetText_speaker2 = findViewById(R.id.targetText_speaker2);

        sourceVoice.setOnClickListener(this);
        targetVoice.setOnClickListener(this);

        final TranslateViewModel viewModel = ViewModelProviders.of(this).get(TranslateViewModel.class);
        adapter = new ArrayAdapter<>(ConversationActivity.this, R.layout.spinner_item, viewModel.getAvailableLanguages());
        sourceLangSelector.setAdapter(adapter);
        targetLangSelector.setAdapter(adapter);
        sourceLangSelector.setSelection(adapter.getPosition(new TranslateViewModel.Language("en")));
        targetLangSelector.setSelection(adapter.getPosition(new TranslateViewModel.Language("es")));

        sourceText_speaker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!sourceText.getText().toString().equals(null)) {
                    textToSpeech = new TextToSpeech(ConversationActivity.this, new TextToSpeech.OnInitListener() {
                        @Override
                        public void onInit(int i) {
                            if (i != TextToSpeech.ERROR) {
                                TranslateViewModel.Language language = adapter.getItem(sourceLangSelector.getSelectedItemPosition());
                                textToSpeech.setLanguage(Locale.forLanguageTag(language.getCode()));
                                textToSpeech.speak(sourceText.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
                            }
                        }
                    });
                }
            }
        });

        sourceText_speaker2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!sourceText2.getText().toString().equals(null)) {
                    textToSpeech = new TextToSpeech(ConversationActivity.this, new TextToSpeech.OnInitListener() {
                        @Override
                        public void onInit(int i) {
                            if (i != TextToSpeech.ERROR) {
                                TranslateViewModel.Language language = adapter.getItem(sourceLangSelector.getSelectedItemPosition());
                                textToSpeech.setLanguage(Locale.forLanguageTag(language.getCode()));
                                textToSpeech.speak(sourceText2.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
                            }
                        }
                    });
                }
            }
        });

        targetText_speaker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!targetText.getText().toString().equals(null)) {
                    textToSpeech = new TextToSpeech(ConversationActivity.this, new TextToSpeech.OnInitListener() {
                        @Override
                        public void onInit(int i) {
                            if (i != TextToSpeech.ERROR) {
                                TranslateViewModel.Language language = adapter.getItem(targetLangSelector.getSelectedItemPosition());
                                textToSpeech.setLanguage(Locale.forLanguageTag(language.getCode()));
                                textToSpeech.speak(targetText.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
                            }
                        }
                    });
                }
            }
        });

        targetText_speaker2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!targetText2.getText().toString().equals(null)) {
                    textToSpeech = new TextToSpeech(ConversationActivity.this, new TextToSpeech.OnInitListener() {
                        @Override
                        public void onInit(int i) {
                            if (i != TextToSpeech.ERROR) {
                                TranslateViewModel.Language language = adapter.getItem(targetLangSelector.getSelectedItemPosition());
                                textToSpeech.setLanguage(Locale.forLanguageTag(language.getCode()));
                                textToSpeech.speak(targetText2.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
                            }
                        }
                    });
                }
            }
        });


        sourceText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                TranslateViewModel.Language language = adapter.getItem(targetLangSelector.getSelectedItemPosition());
                String lng = language.getCode();
                getData2(sourceText.getText().toString(), lng);
            }
        });

        targetText2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                TranslateViewModel.Language language = adapter.getItem(sourceLangSelector.getSelectedItemPosition());
                String lng = language.getCode();
                getData4(targetText2.getText().toString(), lng);
            }
        });
    }

    //Get Translated Data From API
    private void getData(String text, String lng) {
        try {
            RequestQueue queue = Volley.newRequestQueue(ConversationActivity.this);
            String URL = BASE_URL1;
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("q", text);
            jsonBody.put("target", lng);
            final String requestBody = jsonBody.toString();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (Helper.checkConnection(response)) {
                        Pair<String, String> pair = Helper.GetErrorMessage(response);
                        Helper.ShowAlertDialog(ConversationActivity.this, pair.first, pair.second, false);
                    } else {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject jsonObj = jsonObject.getJSONObject("data");
                            JSONArray jsonArray = jsonObj.getJSONArray("translations");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jObj = jsonArray.getJSONObject(i);
                                String translatedText = jObj.getString("translatedText");
                                Toast.makeText(ConversationActivity.this, translatedText, Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, lng);
                                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Need to speak");
                                try {
                                    startActivityForResult(intent, REQ_CODE);
                                } catch (ActivityNotFoundException a) {
                                    Toast.makeText(getApplicationContext(),
                                            "Sorry your device not supported",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }, error -> {
                if (Helper.checkConnection(error.getMessage())) {
                    Pair<String, String> pair = Helper.GetErrorMessage(error.getMessage());
                    Helper.ShowAlertDialog(ConversationActivity.this, pair.first, pair.second, false);
                } else {
                    ShowConnErrorMsg(ConversationActivity.this, "Unable To Connect", "Check your Internet Connection", "Ok");
                }
            }) {

                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return requestBody == null ? null : requestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                        return null;
                    }
                }

            };
            queue.add(stringRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //Get Translated Data From API
    private void getData2(String text, String lng) {
        try {
            RequestQueue queue = Volley.newRequestQueue(ConversationActivity.this);
            String URL = BASE_URL1;
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("q", text);
            jsonBody.put("target", lng);
            final String requestBody = jsonBody.toString();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (Helper.checkConnection(response)) {
                        Pair<String, String> pair = Helper.GetErrorMessage(response);
                        Helper.ShowAlertDialog(ConversationActivity.this, pair.first, pair.second, false);
                    } else {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject jsonObj = jsonObject.getJSONObject("data");
                            JSONArray jsonArray = jsonObj.getJSONArray("translations");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jObj = jsonArray.getJSONObject(i);
                                String translatedText = jObj.getString("translatedText");
                                targetText.setText(translatedText);

                                textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                                    @Override
                                    public void onInit(int i) {
                                        if (i != TextToSpeech.ERROR) {
                                            TranslateViewModel.Language language = adapter.getItem(targetLangSelector.getSelectedItemPosition());
                                            textToSpeech.setLanguage(Locale.forLanguageTag(language.getCode()));
                                            textToSpeech.speak(translatedText, TextToSpeech.QUEUE_FLUSH, null);
                                        }
                                    }
                                });
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }, error -> {
                if (Helper.checkConnection(error.getMessage())) {
                    Pair<String, String> pair = Helper.GetErrorMessage(error.getMessage());
                    Helper.ShowAlertDialog(ConversationActivity.this, pair.first, pair.second, false);
                } else {
                    ShowConnErrorMsg(ConversationActivity.this, "Unable To Connect", "Check your Internet Connection", "Ok");
                }
            }) {

                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return requestBody == null ? null : requestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                        return null;
                    }
                }

            };
            queue.add(stringRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //Get Translated Data From API
    private void getData3(String text, String lng) {
        try {
            RequestQueue queue = Volley.newRequestQueue(ConversationActivity.this);
            String URL = BASE_URL1;
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("q", text);
            jsonBody.put("target", lng);
            final String requestBody = jsonBody.toString();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (Helper.checkConnection(response)) {
                        Pair<String, String> pair = Helper.GetErrorMessage(response);
                        Helper.ShowAlertDialog(ConversationActivity.this, pair.first, pair.second, false);
                    } else {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject jsonObj = jsonObject.getJSONObject("data");
                            JSONArray jsonArray = jsonObj.getJSONArray("translations");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jObj = jsonArray.getJSONObject(i);
                                String translatedText = jObj.getString("translatedText");
                                Toast.makeText(ConversationActivity.this, translatedText, Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, lng);
                                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Need to speak");
                                try {
                                    startActivityForResult(intent, REQ_CODE_1);
                                } catch (ActivityNotFoundException a) {
                                    Toast.makeText(getApplicationContext(),
                                            "Sorry your device not supported",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }, error -> {
                if (Helper.checkConnection(error.getMessage())) {
                    Pair<String, String> pair = Helper.GetErrorMessage(error.getMessage());
                    Helper.ShowAlertDialog(ConversationActivity.this, pair.first, pair.second, false);
                } else {
                    ShowConnErrorMsg(ConversationActivity.this, "Unable To Connect", "Check your Internet Connection", "Ok");
                }
            }) {

                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return requestBody == null ? null : requestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                        return null;
                    }
                }

            };
            queue.add(stringRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //Get Translated Data From API
    private void getData4(String text, String lng) {
        try {
            RequestQueue queue = Volley.newRequestQueue(ConversationActivity.this);
            String URL = BASE_URL1;
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("q", text);
            jsonBody.put("target", lng);
            final String requestBody = jsonBody.toString();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (Helper.checkConnection(response)) {
                        Pair<String, String> pair = Helper.GetErrorMessage(response);
                        Helper.ShowAlertDialog(ConversationActivity.this, pair.first, pair.second, false);
                    } else {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject jsonObj = jsonObject.getJSONObject("data");
                            JSONArray jsonArray = jsonObj.getJSONArray("translations");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jObj = jsonArray.getJSONObject(i);
                                String translatedText = jObj.getString("translatedText");
                                sourceText2.setText(translatedText);

                                textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                                    @Override
                                    public void onInit(int i) {
                                        if (i != TextToSpeech.ERROR) {
                                            TranslateViewModel.Language language = adapter.getItem(sourceLangSelector.getSelectedItemPosition());
                                            textToSpeech.setLanguage(Locale.forLanguageTag(language.getCode()));
                                            textToSpeech.speak(translatedText, TextToSpeech.QUEUE_FLUSH, null);
                                        }
                                    }
                                });
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }, error -> {
                if (Helper.checkConnection(error.getMessage())) {
                    Pair<String, String> pair = Helper.GetErrorMessage(error.getMessage());
                    Helper.ShowAlertDialog(ConversationActivity.this, pair.first, pair.second, false);
                } else {
                    ShowConnErrorMsg(ConversationActivity.this, "Unable To Connect", "Check your Internet Connection", "Ok");
                }
            }) {

                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return requestBody == null ? null : requestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                        return null;
                    }
                }

            };
            queue.add(stringRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //Error Message For API
    public void ShowConnErrorMsg(Context context, String title, String message, String positive) {
        android.app.AlertDialog.Builder aDialogBuilder = new android.app.AlertDialog.Builder(context);
        aDialogBuilder.setTitle(title);
        aDialogBuilder.setMessage(message);
        aDialogBuilder.setIcon(R.drawable.error);
        aDialogBuilder.setPositiveButton(positive, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        android.app.AlertDialog alertDialog = aDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sourceVoice:
                sourceVoice();
                break;
            case R.id.targetVoice:
                targetVoice();
                break;
        }
    }

    //For source voice
    private void sourceVoice() {

        sourceText.setText(null);
        targetText.setText(null);
        sourceText2.setText(null);
        targetText2.setText(null);

        SourcetextInputLayout.setVisibility(View.VISIBLE);
        TargettextInputLayout.setVisibility(View.VISIBLE);

        SourcetextInputLayout2.setVisibility(View.GONE);
        TargettextInputLayout2.setVisibility(View.GONE);


        TranslateViewModel.Language language = adapter.getItem(sourceLangSelector.getSelectedItemPosition());
        String lng = language.getCode();
        getData("Speak Now", lng);
    }

    //For Target voice
    private void targetVoice() {

        sourceText.setText(null);
        targetText.setText(null);
        sourceText2.setText(null);
        targetText2.setText(null);

        SourcetextInputLayout.setVisibility(View.GONE);
        TargettextInputLayout.setVisibility(View.GONE);

        SourcetextInputLayout2.setVisibility(View.VISIBLE);
        TargettextInputLayout2.setVisibility(View.VISIBLE);


        TranslateViewModel.Language language = adapter.getItem(targetLangSelector.getSelectedItemPosition());
        String lng = language.getCode();
        getData3("Speak Now", lng);


    }

    //Check Permission For Recording
    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED)) {

//
//                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
//                        Uri.parse("package:" + getPackageName()));
//                startActivity(intent);
//                finish();

                checkPermission(Manifest.permission.RECORD_AUDIO, Req);

//


            }
        }


    }

    public void checkPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(ConversationActivity.this, permission) == PackageManager.PERMISSION_DENIED) {

            // Requesting the permission
            ActivityCompat.requestPermissions(ConversationActivity.this, new String[]{permission}, requestCode);
        } else {
            Toast.makeText(ConversationActivity.this, "Permission already granted", Toast.LENGTH_SHORT).show();
        }
    }


    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case Req:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    fetchLocation();
                    checkPermission();


                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQ_CODE:
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    sourceText.setText(result.get(0).toString());
                }
                break;
            case REQ_CODE_1:
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    targetText2.setText(result.get(0).toString());
                }
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (textToSpeech != null) {
            if (textToSpeech.isSpeaking()) {
                textToSpeech.stop();
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (textToSpeech != null) {
            if (textToSpeech.isSpeaking()) {
                textToSpeech.stop();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (textToSpeech != null) {
            if (textToSpeech.isSpeaking()) {
                textToSpeech.stop();
            }
        }
    }


}
