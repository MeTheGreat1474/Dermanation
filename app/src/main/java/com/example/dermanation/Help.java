package com.example.dermanation;

import android.os.Bundle;
import android.util.Xml;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;

public class Help extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_help);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.LLHelp), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        LinearLayout faqLayout = findViewById(R.id.LLFaq);

        try {
            InputStream is = getResources().openRawResource(R.raw.faq);
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(is, null);

            int eventType = parser.getEventType();
            TextView questionView = null;
            TextView answerView = null;

            while (eventType != XmlPullParser.END_DOCUMENT) {
                String name = null;
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        name = parser.getName();
                        if (name.equals("question")) {
                            questionView = new TextView(this);
                            questionView.setText(parser.nextText());
                            questionView.setTypeface(null, android.graphics.Typeface.BOLD);
                            faqLayout.addView(questionView);
                        } else if (name.equals("answer")) {
                            answerView = new TextView(this);
                            answerView.setText(parser.nextText());
                            answerView.setPadding(15, 0, 0, 10);
                            faqLayout.addView(answerView);
                        }
                        break;
                }
                eventType = parser.next();
            }
            is.close();
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
    }
}