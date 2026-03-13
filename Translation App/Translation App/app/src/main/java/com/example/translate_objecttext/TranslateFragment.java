package com.example.translate_objecttext;


import static android.content.Context.MODE_PRIVATE;
import static com.example.translate_objecttext.Constant.BASE_URL1;
import static com.example.translate_objecttext.Constant.DETECT_URL;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.speech.tts.TextToSpeech;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Pair;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.view.inputmethod.InputMethodSubtype;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TranslateFragment extends Fragment {

    public TranslateFragment() {
    }

    public static TranslateFragment newInstance() {
        TranslateFragment fragment = new TranslateFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
    }

    MaterialButton switchButton;
    ToggleButton sourceSyncButton;
    ToggleButton targetSyncButton;
    EditText srcTextView;
    TextView targetTextView;
    TextView downloadModelsTextView;
    Spinner targetLangSelector;
    Spinner sourceLangSelector;
    ImageView conversationImg;
    ImageView speaker;
    LinearLayout targetLy;

    String lng = null;
    ArrayAdapter<TranslateViewModel.Language> adapter = null;

    TextToSpeech textToSpeech;
    Button translatebtn;

    String newstring1;
    public static final String MYPRef = "Myprefence";
    String key1 = "D3YQXIXQS6BPBVAO3JKCAL6XSKLBFBI8";
    String key2 = "HWWQ86ISK6EIKZU26U19140GKFL5SUO2";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_translate2, container, false);

        switchButton = view.findViewById(R.id.buttonSwitchLang);
        sourceSyncButton = view.findViewById(R.id.buttonSyncSource);
        targetSyncButton = view.findViewById(R.id.buttonSyncTarget);
        srcTextView = view.findViewById(R.id.sourceText);
        targetTextView = view.findViewById(R.id.targetText);
        downloadModelsTextView = view.findViewById(R.id.downloadModels);
        targetLangSelector = view.findViewById(R.id.targetLangSelector);
        sourceLangSelector = view.findViewById(R.id.sourceLangSelector);
        conversationImg = view.findViewById(R.id.conversationImg);
        speaker = view.findViewById(R.id.speaker);
        targetLy = view.findViewById(R.id.targetLy);
        translatebtn = view.findViewById(R.id.translate_btn);


        SharedPreferences sharedPreferences = getContext().getSharedPreferences(MYPRef, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("key1", key1);
        editor.putString("key2", key2);

        editor.apply();

        translatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!srcTextView.getText().toString().isEmpty()) {
                    String url = "https://api.sapling.ai/api/v1/edits";

                    try {
                        RequestQueue queue = Volley.newRequestQueue(getContext());

                        JSONObject json = new JSONObject();
                        json.put("key", key1);
                        json.put("text", srcTextView.getText().toString());
                        json.put("session_id", "test_session");

                        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                                Request.Method.POST, url, json,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        Log.d("res", response.toString());
//                                    Toast.makeText(getContext(), response.toString(), Toast.LENGTH_SHORT).show();

                                        try {
                                            JSONObject jsonObject = new JSONObject(String.valueOf(response));


                                            String res = jsonObject.getString("edits");


                                            if (res.compareTo("[]") == 0) {

//                                                Toast.makeText(getContext(), "No Mistake", Toast.LENGTH_SHORT).show();
                                                targetTextView.setVisibility(View.VISIBLE);

                                            } else {
                                                JSONArray jsonArray = jsonObject.getJSONArray("edits");

                                                List<Edits> editsList = new ArrayList<>();

                                                String newstring = srcTextView.getText().toString();


                                                for (int i = 0; i < jsonArray.length(); i++) {

                                                    JSONObject jsonObj1 = jsonArray.getJSONObject(i);

                                                    Edits edits = new Edits(
                                                            jsonObj1.getInt("end"),
                                                            jsonObj1.getString("error_type"),
                                                            jsonObj1.getString("general_error_type"),
                                                            jsonObj1.getString("id"),
                                                            jsonObj1.getString("replacement"),
                                                            jsonObj1.getString("sentence"),
                                                            jsonObj1.getInt("sentence_start"),
                                                            jsonObj1.getInt("start")

                                                    );


                                                    ArrayList replace1 = new ArrayList();
                                                    ArrayList startlist = new ArrayList();
                                                    ArrayList endlist = new ArrayList();
                                                    ArrayList source = new ArrayList();


                                                    String end = String.valueOf(jsonObj1.getInt("end"));
                                                    String start = String.valueOf(jsonObj1.getInt("start"));
                                                    String replacement = jsonObj1.getString("replacement");
                                                    Log.d("check", replacement);
                                                    Log.d("check", end);
                                                    Log.d("check", start);

                                                    replace1.add(replacement);

                                                    startlist.add(start);
                                                    endlist.add(end);


                                                    StringBuffer buffer = new StringBuffer(newstring);

                                                    for (int j = 0; j < startlist.size(); j++) {
//
                                                        String resp = buffer.substring(Integer.parseInt(String.valueOf(startlist.get(j))), Integer.parseInt(String.valueOf(endlist.get(j))));
                                                        source.add(resp);
                                                        Log.d("source", source.toString());
                                                        Log.d("source", String.valueOf(source.size()));

                                                    }

                                                    for (int k = 0; k < startlist.size(); k++) {

                                                        newstring = newstring.replace(source.get(k).toString(), replace1.get(k).toString());
                                                        Log.d("newstring", newstring.toString());


                                                    }

//comment
//                                                    for (int k = 0; k < source.size(); k++) {
//                                                        String newstring1= newstring.replace(source.get(k),replace1.get(k));
//
//                                                    }
//
//
//                                                    StringBuffer buf = new StringBuffer(newstring);
//
//                                                    buf.replace(Integer.parseInt(start), Integer.parseInt(end), replacement);
//                                                    Log.d("new", buf.toString());
//
//                                                    newstring = newstring.replace(newstring, buf.toString());
//                                                    editsList.add(edits);


                                                }


//                                                Log.d("ns", newstring);
////
                                                srcTextView.setText(newstring);

                                                targetTextView.setVisibility(View.VISIBLE);

                                                TranslateViewModel.Language language = adapter.getItem(sourceLangSelector.getSelectedItemPosition());
                                                String lng = language.getCode();
                                                getData(srcTextView.getText().toString().trim(), lng);

                                            }

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }


                                    }
                                }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {

//                                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
//                                Log.d("error", error.getMessage());
                                trnslate();
                            }
                        });


                        queue.add(jsonObjReq);
                        /**
                         * Passing some request headers
                         */


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getContext(), "Please Enter word or sentences ", Toast.LENGTH_SHORT).show();
                }
            }

        });


        final TranslateViewModel viewModel = ViewModelProviders.of(this).get(TranslateViewModel.class);
        adapter = new ArrayAdapter<>(

                getContext(), R.layout.spinner_item, viewModel.getAvailableLanguages());
        sourceLangSelector.setAdapter(adapter);
        targetLangSelector.setAdapter(adapter);
        sourceLangSelector.setSelection(adapter.getPosition(new TranslateViewModel.Language("en")));
        targetLangSelector.setSelection(adapter.getPosition(new TranslateViewModel.Language("es")));

        speaker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!targetTextView.getText().toString().equals(null)) {
                    textToSpeech = new TextToSpeech(getContext(), new TextToSpeech.OnInitListener() {
                        @Override
                        public void onInit(int i) {
                            if (i != TextToSpeech.ERROR) {

                                TranslateViewModel.Language language = adapter.getItem(targetLangSelector.getSelectedItemPosition());
                                textToSpeech.setLanguage(Locale.forLanguageTag(language.getCode()));
                                textToSpeech.speak(targetTextView.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);

                            }
                        }
                    });
                }
            }
        });

        srcTextView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            TranslateViewModel.Language language = adapter.getItem(sourceLangSelector.getSelectedItemPosition());
                            String lng = language.getCode();
                            getData(srcTextView.getText().toString(), lng);


//                             getDetect(srcTextView.getText().toString());
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });

        srcTextView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                printInputLanguages();
                return false;
            }
        });


        sourceLangSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position,
                                       long id) {

                // here
                setProgressText(targetTextView);
                viewModel.sourceLang.setValue(adapter.getItem(position));

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                targetTextView.setText("");
            }
        });

        targetLangSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position,
                                       long id) {
                setProgressText(targetTextView);
                viewModel.targetLang.setValue(adapter.getItem(position));


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                targetTextView.setText("");
            }
        });

        switchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setProgressText(targetTextView);

                int sourceLangPosition = sourceLangSelector.getSelectedItemPosition();
                sourceLangSelector.setSelection(targetLangSelector.getSelectedItemPosition());
                targetLangSelector.setSelection(sourceLangPosition);

            }
        });

        sourceSyncButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                TranslateViewModel.Language language = adapter.getItem(sourceLangSelector.getSelectedItemPosition());

                if (isChecked) {
                    viewModel.downloadLanguage(language);
                } else {
                    viewModel.deleteLanguage(language);
                }

            }
        });

        targetSyncButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                TranslateViewModel.Language language = adapter.getItem(targetLangSelector.getSelectedItemPosition());

                if (isChecked) {
                    viewModel.downloadLanguage(language);
                } else {
                    viewModel.deleteLanguage(language);
                }
            }
        });

        srcTextView.addTextChangedListener(new

                                                   TextWatcher() {
                                                       @Override
                                                       public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                                       }

                                                       @Override
                                                       public void onTextChanged(CharSequence s, int start, int before, int count) {
                                                           if (srcTextView.getText().toString().length() != 0) {
                                                               targetLy.setVisibility(View.VISIBLE);


                                                           } else {
//                    targetLy.setVisibility(View.GONE);
                                                           }
                                                       }


                                                       @Override
                                                       public void afterTextChanged(Editable s) {
                                                           setProgressText(targetTextView);
                                                           viewModel.sourceText.postValue(s.toString());
                                                           if (srcTextView.getText().toString().length() != 0) {
                                                               targetLy.setVisibility(View.VISIBLE);


                                                           } else {
//                    targetLy.setVisibility(View.GONE);
                                                           }
                                                       }
                                                   });

        viewModel.translatedText.observe(

                getViewLifecycleOwner(), new Observer<TranslateViewModel.ResultOrError>() {
                    @Override
                    public void onChanged(TranslateViewModel.ResultOrError resultOrError) {
                        if (resultOrError.error != null) {
                            srcTextView.setError(resultOrError.error.getLocalizedMessage());

                        } else {

                            targetTextView.setText(resultOrError.result);
                        }
                    }
                });

        viewModel.availableModels.observe(

                getViewLifecycleOwner(), new Observer<List<String>>() {
                    @Override
                    public void onChanged(List<String> translateRemoteModels) {
                        String output = getContext().getString(R.string.download_models_label, translateRemoteModels);
                        downloadModelsTextView.setText(output);
                        sourceSyncButton.setChecked(translateRemoteModels.contains(
                                adapter.getItem(sourceLangSelector.getSelectedItemPosition()).getCode()));
                        targetSyncButton.setChecked(translateRemoteModels.contains(
                                adapter.getItem(targetLangSelector.getSelectedItemPosition()).getCode()));
                    }
                });

        conversationImg.setOnClickListener(v ->

        {
            Intent intent = new Intent(getContext(), ConversationActivity.class);
            startActivity(intent);
        });

        return view;
    }

    private void trnslate() {
        if (!srcTextView.getText().toString().isEmpty()) {
            String url = "https://api.sapling.ai/api/v1/edits";

            try {
                RequestQueue queue = Volley.newRequestQueue(getContext());

                JSONObject json = new JSONObject();
                json.put("key", key2);
                json.put("text", srcTextView.getText().toString());
                json.put("session_id", "test_session");

                JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                        Request.Method.POST, url, json,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d("res", response.toString());
//                                    Toast.makeText(getContext(), response.toString(), Toast.LENGTH_SHORT).show();

                                try {
                                    JSONObject jsonObject = new JSONObject(String.valueOf(response));


                                    String res = jsonObject.getString("edits");


                                    if (res.compareTo("[]") == 0) {

//                                                Toast.makeText(getContext(), "No Mistake", Toast.LENGTH_SHORT).show();
                                        targetTextView.setVisibility(View.VISIBLE);

                                    } else {
                                        JSONArray jsonArray = jsonObject.getJSONArray("edits");

                                        List<Edits> editsList = new ArrayList<>();

                                        String newstring = srcTextView.getText().toString();


                                        for (int i = 0; i < jsonArray.length(); i++) {

                                            JSONObject jsonObj1 = jsonArray.getJSONObject(i);

                                            Edits edits = new Edits(
                                                    jsonObj1.getInt("end"),
                                                    jsonObj1.getString("error_type"),
                                                    jsonObj1.getString("general_error_type"),
                                                    jsonObj1.getString("id"),
                                                    jsonObj1.getString("replacement"),
                                                    jsonObj1.getString("sentence"),
                                                    jsonObj1.getInt("sentence_start"),
                                                    jsonObj1.getInt("start")

                                            );


                                            ArrayList replace1 = new ArrayList();
                                            ArrayList startlist = new ArrayList();
                                            ArrayList endlist = new ArrayList();
                                            ArrayList source = new ArrayList();


                                            String end = String.valueOf(jsonObj1.getInt("end"));
                                            String start = String.valueOf(jsonObj1.getInt("start"));
                                            String replacement = jsonObj1.getString("replacement");
                                            Log.d("check", replacement);
                                            Log.d("check", end);
                                            Log.d("check", start);

                                            replace1.add(replacement);

                                            startlist.add(start);
                                            endlist.add(end);


                                            StringBuffer buffer = new StringBuffer(newstring);

                                            for (int j = 0; j < startlist.size(); j++) {
//
                                                String resp = buffer.substring(Integer.parseInt(String.valueOf(startlist.get(j))), Integer.parseInt(String.valueOf(endlist.get(j))));
                                                source.add(resp);
                                                Log.d("source", source.toString());
                                                Log.d("source", String.valueOf(source.size()));

                                            }


                                            for (int k = 0; k < startlist.size(); k++) {

                                                newstring = newstring.replace(source.get(k).toString(), replace1.get(k).toString());
                                                Log.d("newstring", newstring.toString());


                                            }


                                        }


//                                                Log.d("ns", newstring);
////
                                        srcTextView.setText(newstring);

                                        targetTextView.setVisibility(View.VISIBLE);

                                        TranslateViewModel.Language language = adapter.getItem(sourceLangSelector.getSelectedItemPosition());
                                        String lng = language.getCode();
                                        getData(srcTextView.getText().toString().trim(), lng);

                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                }


                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(getContext(), "Try after some time...", Toast.LENGTH_SHORT).show();
//                                Log.d("error", error.getMessage());
//                        trnslate2();

                    }
                });


                queue.add(jsonObjReq);
                /**
                 * Passing some request headers
                 */


            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getContext(), "Please Enter word or sentences ", Toast.LENGTH_SHORT).show();
        }
    }

    private void trnslate2() {
        if (!srcTextView.getText().toString().isEmpty()) {
            String url = "https://api.sapling.ai/api/v1/edits";

            try {
                RequestQueue queue = Volley.newRequestQueue(getContext());

                JSONObject json = new JSONObject();
                json.put("key", key1);
                json.put("text", srcTextView.getText().toString());
                json.put("session_id", "test_session");

                JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                        Request.Method.POST, url, json,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d("res", response.toString());
//                                    Toast.makeText(getContext(), response.toString(), Toast.LENGTH_SHORT).show();

                                try {
                                    JSONObject jsonObject = new JSONObject(String.valueOf(response));


                                    String res = jsonObject.getString("edits");


                                    if (res.compareTo("[]") == 0) {

//                                                Toast.makeText(getContext(), "No Mistake", Toast.LENGTH_SHORT).show();
                                        targetTextView.setVisibility(View.VISIBLE);

                                    } else {
                                        JSONArray jsonArray = jsonObject.getJSONArray("edits");

                                        List<Edits> editsList = new ArrayList<>();

                                        String newstring = srcTextView.getText().toString();


                                        for (int i = 0; i < jsonArray.length(); i++) {

                                            JSONObject jsonObj1 = jsonArray.getJSONObject(i);

                                            Edits edits = new Edits(
                                                    jsonObj1.getInt("end"),
                                                    jsonObj1.getString("error_type"),
                                                    jsonObj1.getString("general_error_type"),
                                                    jsonObj1.getString("id"),
                                                    jsonObj1.getString("replacement"),
                                                    jsonObj1.getString("sentence"),
                                                    jsonObj1.getInt("sentence_start"),
                                                    jsonObj1.getInt("start")

                                            );


                                            ArrayList replace1 = new ArrayList();
                                            ArrayList startlist = new ArrayList();
                                            ArrayList endlist = new ArrayList();
                                            ArrayList source = new ArrayList();


                                            String end = String.valueOf(jsonObj1.getInt("end"));
                                            String start = String.valueOf(jsonObj1.getInt("start"));
                                            String replacement = jsonObj1.getString("replacement");
                                            Log.d("check", replacement);
                                            Log.d("check", end);
                                            Log.d("check", start);

                                            replace1.add(replacement);

                                            startlist.add(start);
                                            endlist.add(end);


                                            StringBuffer buffer = new StringBuffer(newstring);

                                            for (int j = 0; j < startlist.size(); j++) {
//
                                                String resp = buffer.substring(Integer.parseInt(String.valueOf(startlist.get(j))), Integer.parseInt(String.valueOf(endlist.get(j))));
                                                source.add(resp);
                                                Log.d("source", source.toString());
                                                Log.d("source", String.valueOf(source.size()));

                                            }

                                            for (int k = 0; k < startlist.size(); k++) {

                                                newstring = newstring.replace(source.get(k).toString(), replace1.get(k).toString());
                                                Log.d("newstring", newstring.toString());


                                            }

//comment
//                                                    for (int k = 0; k < source.size(); k++) {
//                                                        String newstring1= newstring.replace(source.get(k),replace1.get(k));
//
//                                                    }
//
//
//                                                    StringBuffer buf = new StringBuffer(newstring);
//
//                                                    buf.replace(Integer.parseInt(start), Integer.parseInt(end), replacement);
//                                                    Log.d("new", buf.toString());
//
//                                                    newstring = newstring.replace(newstring, buf.toString());
//                                                    editsList.add(edits);


                                        }


//                                                Log.d("ns", newstring);
////
                                        srcTextView.setText(newstring);

                                        targetTextView.setVisibility(View.VISIBLE);

                                        TranslateViewModel.Language language = adapter.getItem(sourceLangSelector.getSelectedItemPosition());
                                        String lng = language.getCode();
                                        getData(srcTextView.getText().toString().trim(), lng);

                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                }


                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

//                                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
//                                Log.d("error", error.getMessage());
                        trnslate();
                    }
                });


                queue.add(jsonObjReq);
                /**
                 * Passing some request headers
                 */


            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getContext(), "Please Enter word or sentences ", Toast.LENGTH_SHORT).show();
        }
    }


    private void printInputLanguages() {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        InputMethodSubtype ims = imm.getCurrentInputMethodSubtype();
        String locale = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            locale = String.valueOf(getResources().getConfiguration().getLocales());

            TranslateViewModel.Language language = adapter.getItem(sourceLangSelector.getSelectedItemPosition());
            String lng = language.getCode();
            String lg = language.getDisplayName();
            if (locale.contains(lng)) {

            } else {
                ShowErrorAlert(getContext(), "Error", "Please download " + lg + " language!", "Go To Settings", "Cancel");
            }
        }
    }

    public static void setSnackbar(View root, String snackTitle) {
        Snackbar snackbar = Snackbar.make(root, snackTitle, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

    private void setProgressText(TextView tv) {
        tv.setText(getContext().getString(R.string.translate_progress));
    }

    private void setProgressText2(EditText tv) {
        tv.getText().clear();
        tv.setText(getContext().getString(R.string.translate_progress));
    }

    //Detect Language
    private void getDetect(String text) {
        try {
            RequestQueue queue = Volley.newRequestQueue(getContext());
            String URL = DETECT_URL;
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("q", text);
            final String requestBody = jsonBody.toString();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (Helper.checkConnection(response)) {
                        Pair<String, String> pair = Helper.GetErrorMessage(response);
                        Helper.ShowAlertDialog(getContext(), pair.first, pair.second, false);
                    } else {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject jsonObj = jsonObject.getJSONObject("data");
                            JSONArray jsonArray = jsonObj.getJSONArray("detections");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONArray jsonArray1 = jsonArray.getJSONArray(i);
                                String language = null;
                                for (int j = 0; j < jsonArray1.length(); j++) {
                                    JSONObject jObj = jsonArray1.getJSONObject(i);
                                    language = jObj.getString("language");
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
                    Helper.ShowAlertDialog(getContext(), pair.first, pair.second, false);
                } else {
                    ShowConnErrorMsg(getContext(), "Unable To Connect", "Check your Internet Connection", "Ok");
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
    private void getData(String text, String lng) {
        try {
            RequestQueue queue = Volley.newRequestQueue(getContext());
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
                        Helper.ShowAlertDialog(getContext(), pair.first, pair.second, false);
                    } else {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject jsonObj = jsonObject.getJSONObject("data");
                            JSONArray jsonArray = jsonObj.getJSONArray("translations");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jObj = jsonArray.getJSONObject(i);
                                String translatedText = jObj.getString("translatedText");
                                srcTextView.setText(null);
                                srcTextView.setText(translatedText);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }, error -> {
                if (Helper.checkConnection(error.getMessage())) {
                    Pair<String, String> pair = Helper.GetErrorMessage(error.getMessage());
                    Helper.ShowAlertDialog(getContext(), pair.first, pair.second, false);
                } else {
                    ShowConnErrorMsg(getContext(), "Unable To Connect", "Check your Internet Connection", "Ok");
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

    public void ShowConnErrorMsg(Context context, String title, String message, String positive) {
        AlertDialog.Builder aDialogBuilder = new AlertDialog.Builder(context);
        aDialogBuilder.setTitle(title);
        aDialogBuilder.setMessage(message);
        aDialogBuilder.setIcon(R.drawable.error);
        aDialogBuilder.setPositiveButton(positive, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = aDialogBuilder.create();
        alertDialog.show();
    }

    public void ShowErrorAlert(Context context, String title, String message, String
            positive, String negtive) {
        AlertDialog.Builder aDialogBuilder = new AlertDialog.Builder(context);
        aDialogBuilder.setTitle(title);
        aDialogBuilder.setMessage(message);
        aDialogBuilder.setIcon(R.drawable.ic_error);
        aDialogBuilder.setCancelable(false);
        aDialogBuilder.setPositiveButton(positive, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                startActivityForResult(
                        new Intent(Settings.ACTION_LOCALE_SETTINGS), 0);
            }
        });
        aDialogBuilder.setNegativeButton(negtive, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = aDialogBuilder.create();
        alertDialog.show();
    }

}