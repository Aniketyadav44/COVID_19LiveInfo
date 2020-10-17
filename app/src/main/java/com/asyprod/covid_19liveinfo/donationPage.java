package com.asyprod.covid_19liveinfo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class donationPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation_page);
        Button btn = findViewById(R.id.onlinDonBtn);
        getSupportActionBar().hide();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openDonation = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.pmcares.gov.in/en/"));
                startActivity(openDonation);
            }
        });
    }
}