/* ====================================================================
 * Copyright (c) 2014 Alpha Cephei Inc.  All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY ALPHA CEPHEI INC. ``AS IS'' AND
 * ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL CARNEGIE MELLON UNIVERSITY
 * NOR ITS EMPLOYEES BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * ====================================================================
 */

package edu.cmu.pocketsphinx.demo;

import static android.widget.Toast.makeText;
import static edu.cmu.pocketsphinx.SpeechRecognizerSetup.defaultSetup;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import edu.cmu.pocketsphinx.Assets;
import edu.cmu.pocketsphinx.Hypothesis;
import edu.cmu.pocketsphinx.RecognitionListener;
import edu.cmu.pocketsphinx.SpeechRecognizer;

public class PocketSphinxActivity extends Activity implements
        RecognitionListener {

    private static final String CMD_SEARCH = "cmd";
    private SpeechRecognizer recognizer;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);

        setContentView(R.layout.main);
        ((TextView) findViewById(R.id.caption_text))
                .setText("Preparing the recognizer");

        Intent intent = new Intent(getApplicationContext(),TouchController.class);
        startService(intent);

        try
        {
            Assets assets = new Assets(PocketSphinxActivity.this);
            File assetDir = assets.syncAssets();
            setupRecognizer(assetDir);
        }
        catch (IOException e)
        {
            // oops
        }

        ((TextView) findViewById(R.id.caption_text)).setText("Say up, down, left, right, forwards, backwards");

        reset();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        recognizer.cancel();
        recognizer.shutdown();
    }

    @Override
    public void onPartialResult(Hypothesis hypothesis) {
    }

    /**
     * This callback is called when we stop the recognizer.
     */
    @Override
    public void onResult(Hypothesis hypothesis) {
        ((TextView) findViewById(R.id.result_text)).setText("");
        if (hypothesis != null) {
            String text = hypothesis.getHypstr();
            Intent intent = new Intent(getApplicationContext(),TouchController.class);
            if(text.equalsIgnoreCase("open browser")){
                intent.setAction(Constant.ACTION_OPEN_BROWSER);
                startService(intent);
            } else if(text.equalsIgnoreCase("close browser")){
                intent.setAction(Constant.ACTION_CLOSE_BROWSER);
                startService(intent);
            } else if(text.equalsIgnoreCase("open calculator")){
                intent.setAction(Constant.ACTION_OPEN_CALCULATOR);
                startService(intent);
            } else if(text.equalsIgnoreCase("close calculator")){
                intent.setAction(Constant.ACTION_CLOSE_CALCULATOR);
                startService(intent);
            } else if(text.equalsIgnoreCase("notification")){
                intent.setAction(Constant.ACTION_NOTIFICATION);
                startService(intent);
            } else if(text.equalsIgnoreCase("go home")){
                intent.setAction(Constant.ACTION_GO_Home);
                startService(intent);
            } else if(text.equalsIgnoreCase("go back")){
                intent.setAction(Constant.ACTION_GO_BACK);
                startService(intent);
            } else if(text.equalsIgnoreCase("open camera")){
                intent.setAction(Constant.ACTION_OPEN_CAMERA);
                startService(intent);
            } else if(text.equalsIgnoreCase("shoot")){
                intent.setAction(Constant.ACTION_SHOOT);
                startService(intent);
            } else if(text.equalsIgnoreCase("open game")){
                intent.setAction(Constant.ACTION_OPEN_GAME);
                startService(intent);
            }

            makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBeginningOfSpeech() {
    }

    /**
     * We stop recognizer here to get a final result
     */
    @Override
    public void onEndOfSpeech() {
//        if (!recognizer.getSearchName().equals(KWS_SEARCH))
//            switchSearch(KWS_SEARCH);
        reset();
    }


    private void setupRecognizer(File assetsDir) throws IOException {
        // The recognizer can be configured to perform multiple searches
        // of different kind and switch between them
        
        recognizer = defaultSetup()
                .setAcousticModel(new File(assetsDir, "en-us-ptm"))
                .setDictionary(new File(assetsDir, "keywords.dict"))
                
                // To disable logging of raw audio comment out this call (takes a lot of space on the device)
                .setRawLogDir(assetsDir)
                
                // Threshold to tune for keyphrase to balance between false alarms and misses
                .setKeywordThreshold(1e-20f)
                
                // Use context-independent phonetic search, context-dependent is too slow for mobile
                .setBoolean("-allphone_ci", true)

                //.setInteger("-pl_window",10)


                .getRecognizer();

        recognizer.addListener(this);

        /** In your application you might not need to add all those searches.
         * They are added here for demonstration. You can leave just one.
         */

        // Create keyword-activation search.
//        recognizer.addKeyphraseSearch(KWS_SEARCH, KEYPHRASE);
        
        // Create grammar-based search for selection between demos
//        File cmdGrammar = new File(assetsDir, "cmds.gram");
//        recognizer.addGrammarSearch(CMD_SEARCH, cmdGrammar);

        File keywords = new File(assetsDir,"keywords.gram");
        recognizer.addGrammarSearch(CMD_SEARCH, keywords);

        // Create grammar-based search for digit recognition
        /*File digitsGrammar = new File(assetsDir, "digits.gram");
        recognizer.addGrammarSearch(DIGITS_SEARCH, digitsGrammar);
        
        // Create language model search
        File languageModel = new File(assetsDir, "weather.dmp");
        recognizer.addNgramSearch(FORECAST_SEARCH, languageModel);
        
        // Phonetic search
        File phoneticModel = new File(assetsDir, "en-phone.dmp");
        recognizer.addAllphoneSearch(PHONE_SEARCH, phoneticModel);*/
    }

    @Override
    public void onError(Exception error) {
        ((TextView) findViewById(R.id.caption_text)).setText(error.getMessage());
    }

    @Override
    public void onTimeout() {
//        switchSearch(KWS_SEARCH);
        reset();
    }
    private void reset()
    {
        recognizer.stop();
        recognizer.startListening(CMD_SEARCH);
    }
}
