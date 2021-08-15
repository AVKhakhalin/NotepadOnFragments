package ru.geekbrains.lessions2345.notepadonfragments.ui;

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
import ru.geekbrains.lessions2345.notepadonfragments.logic.Notepad;
import ru.geekbrains.lessions2345.notepadonfragments.model.Constants;
import ru.geekbrains.lessions2345.notepadonfragments.ui.fragments.ListNotesFragment;

public class MainActivity extends AppCompatActivity implements Constants {

    private Notepad notepad = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Восстановление класса notepad после поворота экрана
        if (savedInstanceState != null) {
            notepad = savedInstanceState.getParcelable(KEY_NOTEPAD);
        }

        // Инициализация класса notepad
        initNotepad();

        // Отображение фрагмента со списком заметок
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.list_container, ListNotesFragment.newInstance(notepad))
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

    private void initNotepad() {
        // Инициализация класса Notepad
        notepad = new Notepad();
        notepad.add("ПЕРВ.ЗАМ.", "Первая заметка");
        notepad.add("ВТОР.ЗАМ.", "Вторая заметка");
        notepad.add("ТРЕТ.ЗАМ.", "Третья заметка");
        notepad.add("ЧЕТВ.ЗАМ.", "Четвёртая заметка");
        notepad.add("ПЯТ.ЗАМ.", "Пятая заметка");
        notepad.setText(5, "Текст первой заметки");
        notepad.setText(4, "Текст второй заметки");
        notepad.setText(3, "Текст третьей заметки");
        notepad.setText(2, "Текст четвёртой заметки");
        notepad.setText(1, "Текст пятой заметки");
    }

    // Метод для считывания класса во фрагменты
    public Notepad getNotepad() {
        return notepad;
    }

    // Метод для изменения класса notepad через фрагменты
    public void setNotepad(Notepad notepad) {
        this.notepad = notepad;
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(KEY_NOTEPAD, notepad);
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
}