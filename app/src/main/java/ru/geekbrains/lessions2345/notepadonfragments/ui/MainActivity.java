package ru.geekbrains.lessions2345.notepadonfragments.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationView;

import java.sql.Date;

import ru.geekbrains.lessions2345.notepadonfragments.R;
import ru.geekbrains.lessions2345.notepadonfragments.logic.CardNote;
import ru.geekbrains.lessions2345.notepadonfragments.logic.CardSourceImplement;
import ru.geekbrains.lessions2345.notepadonfragments.logic.CardsSourceFirebaseImpl;
import ru.geekbrains.lessions2345.notepadonfragments.model.CONSTANTS;
import ru.geekbrains.lessions2345.notepadonfragments.model.TYPES_DATA;
import ru.geekbrains.lessions2345.notepadonfragments.ui.fragments.EditCardFragment;
import ru.geekbrains.lessions2345.notepadonfragments.ui.fragments.ListNotesFragment;

public class MainActivity extends AppCompatActivity {

    private CardSourceImplement cardSourceImplement;
    private EditCardFragment editCardFragment;
    private CONSTANTS constants = new CONSTANTS();
    private TYPES_DATA typeSourceData = TYPES_DATA.FIREBASE_DATA;

    //    Нужно для тестовой записи в облачную базу данных
//    CardsSourceFirebaseImpl cardsSourceFirebase = new CardsSourceFirebaseImpl();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Получение настроек
        getSettings();

        // Восстановление класса notepad после поворота экрана
        if (savedInstanceState != null) {
            cardSourceImplement = savedInstanceState.getParcelable(constants.KEY_CARD_SOURCE_IMPLEMENT);
        } else {
            // Инициализация класса - временного хранилища всех созданных к данному моменту заметок
            cardSourceImplement = new CardSourceImplement(typeSourceData, this);
        }

        // Отображение фрагмента со списком заметок
        if (savedInstanceState == null) {
            showListNotes();
        }

        // Установка AppBarMenu
        setupAppBarMenu();

        // Установка DrawNavigationMenu
        setupDrawNavigationMenu();

        // Создание облачной базы данных
        // Тестовая запись данных в облачную базу данных
//        for (int i = 1; i <= cardSourceImplement.size(); i++) {
//            cardsSourceFirebase.addCardNoteFirebase(new CardNote( "name", "description", "text", 2021, 8, 24));
//        }
    }

    private void setupDrawNavigationMenu() {
        // Установка DrawerNavigationMenu
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.app_name, // Строки для людей с ограниченными возможностями
                R.string.app_name // Строки для людей с ограниченными возможностями
        );
        drawerLayout.addDrawerListener(toggle);
        // Здсеь сцепляется шторка с нашим тулбаром (верхней части) и выезжающей шторки. Синхронизирует ToolBar и NavigationDrawer
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                // Метод для закрытия выплывающего меню после нажатия на кнопку
                drawerLayout.closeDrawer(GravityCompat.START);

                Toast.makeText(MainActivity.this, "Вы нажали на кнопку", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }

    private void setupAppBarMenu() {
        // Установка меню AppBarMenu
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    // Метод для считывания класса getCardSourceImplement во фрагменты
    public CardSourceImplement getCardSourceImplement() {
        return cardSourceImplement;
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(constants.KEY_CARD_SOURCE_IMPLEMENT, cardSourceImplement);
        super.onSaveInstanceState(outState);
    }

    // Создание меню AppBarMenu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_appbar, menu);

        MenuItem actionSearch = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) actionSearch.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(MainActivity.this, query, Toast.LENGTH_SHORT).show();
                // Отображение выбранных заметок

                return false;
            }

            @Override
            // Изменение одного за другим
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }

    // Установка слушателя на меню AppBarMenu
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.action_close) {
            // Закрытие заметки - отображение пустого текстового поля и установка текущего индекса заметки на 0
            cardSourceImplement.setActiveNoteIndex(0);
            // Отображение пустого текстового поля
            showEmptyTextFragment();
            return true;
        } else if (itemId == R.id.action_save) {
            // Удаление скачанных из базы данных заметок
            if (typeSourceData == TYPES_DATA.FIREBASE_DATA) {
                int numberFirebaseStoredNotes = cardSourceImplement.getCardsSourceFirebase().size();
                if (numberFirebaseStoredNotes > 0) {
                    for (int i = 0; i < numberFirebaseStoredNotes; i++) {
                        cardSourceImplement.getCardsSourceFirebase().deleteCardNoteFirebase(0);
                    }
                }
            }
            // Сохранение в базу данных вновь созданных и отредактированных скачанных заметок
            for (int i = 1; i <= cardSourceImplement.size(); i++) {
                cardSourceImplement.getCardsSourceFirebase().addCardNoteFirebase(cardSourceImplement.getCardNote(i));
            }
            Toast.makeText(this, "Заметки сохранены в Firebase", Toast.LENGTH_SHORT).show();
            return true;
        } else if (itemId == R.id.action_delete) {
            cardSourceImplement.setDeleteMode(true);
            // Отображение пустого текстового поля
            showEmptyTextFragment();
            cardSourceImplement.setDeleteMode(false);
            String deletedNoteName = "\"" + cardSourceImplement.getCardNote(cardSourceImplement.getActiveNoteIndex()).getName() + "\" (\"" + cardSourceImplement.getCardNote(cardSourceImplement.getActiveNoteIndex()).getDescription() + "\")" ;
            cardSourceImplement.removeCardNote(cardSourceImplement.getActiveNoteIndex());
            // Отображение фрагмента со списком заметок
            showListNotes();
            Toast.makeText(this, "Заметка " + deletedNoteName + " удалена.", Toast.LENGTH_SHORT).show();
            return true;
        } else if (itemId == R.id.action_filter) {
            Toast.makeText(this, "Фильтр вывода заметок", Toast.LENGTH_SHORT).show();
            return true;
        } else if (itemId == R.id.action_show_card) {
//            Toast.makeText(this, "Посмотр/создание карты заметки", Toast.LENGTH_SHORT).show();
            editCardFragment = new EditCardFragment();
            editCardFragment.show(getFragmentManager(), "");
            return true;
        } else if (itemId == R.id.action_send) {
            Toast.makeText(this, "Переслать заметку", Toast.LENGTH_SHORT).show();
            return true;
        } else if (itemId == R.id.action_add_link) {
            Toast.makeText(this, "Добавить ссылку заметке", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Сохранение настроек в SharedPreferences
    private void saveSettings(TYPES_DATA typeSourceData) {
        SharedPreferences sharedPreferences = getSharedPreferences(constants.KEY_DATA_SETTINGS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        // Сохранение типа источника данных
        if (typeSourceData == TYPES_DATA.FILE_DATA) {
            editor.putInt(constants.KEY_DATA_SETTINGS, 1);
        } else if (typeSourceData == TYPES_DATA.FIREBASE_DATA) {
            editor.putInt(constants.KEY_DATA_SETTINGS, 2);
        } else if (typeSourceData == TYPES_DATA.DATABASE_DATA) {
            editor.putInt(constants.KEY_DATA_SETTINGS, 3);
        } else {
            // Случай, когда typeSourceData == null или typeSourceData == DATA_SETTINGS.TEST_DATA
            editor.putInt(constants.KEY_DATA_SETTINGS, 0);
        }
        editor.apply();
    }

    // Получение настроек из SharedPreferences
    private void getSettings() {
        SharedPreferences sharedPreferences = getSharedPreferences(constants.KEY_DATA_SETTINGS, MODE_PRIVATE);
        if (typeSourceData == null) {
            int timeSourceData = sharedPreferences.getInt(constants.KEY_DATA_SETTINGS, 0);
            if (timeSourceData == 1) {
                typeSourceData = TYPES_DATA.FILE_DATA;
            } else if (timeSourceData == 2) {
                typeSourceData = TYPES_DATA.FIREBASE_DATA;
            } else if (timeSourceData == 3) {
                typeSourceData = TYPES_DATA.DATABASE_DATA;
            } else {
                // Случай, когда typeSourceData == null или typeSourceData == DATA_SETTINGS.TEST_DATA
                typeSourceData = TYPES_DATA.TEST_DATA;
            }
        }
    }

    // Отображение пустого текстового поля
    private void showEmptyTextFragment() {
        getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.text_container, new Fragment())
            .commitNow();
    }

    // Отображение фрагмента со списком заметок
    private void showListNotes() {
        getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.list_container, ListNotesFragment.newInstance())
            .commit();
    }

}