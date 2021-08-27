package ru.geekbrains.lessions2345.notepadonfragments_2.ui;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import ru.geekbrains.lessions2345.notepadonfragments_2.R;

public class Navigation {

    private final FragmentManager fragmentManager;

    public Navigation(FragmentManager fragmentManager){
        this.fragmentManager = fragmentManager;
    }

    public void addFragment(Fragment fragment, boolean useBackStack) {
        // Открыть транзакцию
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_start, fragment);
        if (useBackStack) {
            fragmentTransaction.addToBackStack(null);
        }
        // Закрыть транзакцию
        fragmentTransaction.commit();
    }
}
