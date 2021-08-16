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

import com.google.android.material.navigation.NavigationView;

import ru.geekbrains.lessions2345.notepadonfragments.R;
import ru.geekbrains.lessions2345.notepadonfragments.logic.CardSourceImplement;
import ru.geekbrains.lessions2345.notepadonfragments.model.Constants;
import ru.geekbrains.lessions2345.notepadonfragments.ui.fragments.ListNotesFragment;

public class MainActivity extends AppCompatActivity implements Constants {

    private DATA_SETTINGS typeSourceData = null;
    CardSourceImplement cardSourceImplement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Получение настроек
        getSettings();

        // Восстановление класса notepad после поворота экрана
        if (savedInstanceState != null) {
            cardSourceImplement = savedInstanceState.getParcelable(KEY_CARD_SOURCE_IMPLEMENT);
        } else {
            // Инициализация класса - временного хранилища всех созданных к данному моменту заметок
            cardSourceImplement = new CardSourceImplement(typeSourceData);
        }

        // Отображение фрагмента со списком заметок
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.list_container, ListNotesFragment.newInstance())
                    .commit();
        }

        // Установка AppBarMenu
        setupAppBarMenu();

        // Установка DrawNavigationMenu
        setupDrawNavigationMenu();
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
        outState.putParcelable(KEY_CARD_SOURCE_IMPLEMENT, cardSourceImplement);
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
            Toast.makeText(this, "Закрыть заметку", Toast.LENGTH_SHORT).show();
            return true;
        } else if (itemId == R.id.action_save) {
            Toast.makeText(this, "Сохранить заметку", Toast.LENGTH_SHORT).show();
            return true;
        } else if (itemId == R.id.action_filter) {
            Toast.makeText(this, "Фильтр вывода заметок", Toast.LENGTH_SHORT).show();
            return true;
        } else if (itemId == R.id.action_delete) {
            Toast.makeText(this, "Удалить заметку", Toast.LENGTH_SHORT).show();
            return true;
        } else if (itemId == R.id.action_show_card) {
            Toast.makeText(this, "Показать карточку заметки", Toast.LENGTH_SHORT).show();
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
    private void saveSettings(DATA_SETTINGS typeSourceData) {
        SharedPreferences sharedPreferences = getSharedPreferences(KEY_DATA_SETTINGS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        // Сохранение типа источника данных
        if (typeSourceData == DATA_SETTINGS.FILE_DATA) {
            editor.putInt(KEY_DATA_SETTINGS, 1);
        } else if (typeSourceData == DATA_SETTINGS.FIREBASE_DATA) {
            editor.putInt(KEY_DATA_SETTINGS, 2);
        } else if (typeSourceData == DATA_SETTINGS.DATABASE_DATA) {
            editor.putInt(KEY_DATA_SETTINGS, 3);
        } else {
            // Случай, когда typeSourceData == null или typeSourceData == DATA_SETTINGS.TEST_DATA
            editor.putInt(KEY_DATA_SETTINGS, 0);
        }
        editor.apply();
    }

    // Получение настроек из SharedPreferences
    private void getSettings() {
        SharedPreferences sharedPreferences = getSharedPreferences(KEY_DATA_SETTINGS, MODE_PRIVATE);
        int timeSourceData = sharedPreferences.getInt(KEY_DATA_SETTINGS, 0);
        if (timeSourceData == 1) {
            typeSourceData = DATA_SETTINGS.FILE_DATA;
        } else if (timeSourceData == 2) {
            typeSourceData = DATA_SETTINGS.FIREBASE_DATA;
        } else if (timeSourceData == 3) {
            typeSourceData = DATA_SETTINGS.DATABASE_DATA;
        } else {
            // Случай, когда typeSourceData == null или typeSourceData == DATA_SETTINGS.TEST_DATA
            typeSourceData = DATA_SETTINGS.TEST_DATA;
        }
    }
}