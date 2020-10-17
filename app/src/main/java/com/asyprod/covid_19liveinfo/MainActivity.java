package com.asyprod.covid_19liveinfo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    CardView liveUpdatesCard;
    CardView faqCard;
    CardView donateCard;
    CardView appLinkCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        liveUpdatesCard = findViewById(R.id.liveUpdatesLink);
        faqCard = findViewById(R.id.faq);
        donateCard = findViewById(R.id.donate);
        appLinkCard = findViewById(R.id.setuAppLink);

        liveUpdatesCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),LiveUpdates.class));
            }
        });

        faqCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent faqIntent = new Intent(Intent.ACTION_VIEW,Uri.parse("https://www.mohfw.gov.in/pdf/FAQ.pdf"));
                startActivity(faqIntent);
            }
        });

        donateCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),donationPage.class));
            }
        });

        appLinkCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent appLink = new Intent(Intent.ACTION_VIEW,Uri.parse("https://play.google.com/store/apps/details?id=nic.goi.aarogyasetu"));
                startActivity(appLink);
            }
        });

    }
}