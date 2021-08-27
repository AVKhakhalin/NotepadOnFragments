package ru.geekbrains.lessions2345.notepadonfragments_2.logic.authorise;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import ru.geekbrains.lessions2345.notepadonfragments_2.ui.Navigation;
import ru.geekbrains.lessions2345.notepadonfragments_2.R;

public class GoogleAuthorise extends AppCompatActivity {

    private Navigation navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_authorise);
        navigation = new Navigation(getSupportFragmentManager());
        getNavigation().addFragment(StartFragment.newInstance(), false);
    }

    public Navigation getNavigation() {
        return navigation;
    }
}