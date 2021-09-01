package ru.geekbrains.lessions2345.notepadonfragments_2.ui;

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

import ru.geekbrains.lessions2345.notepadonfragments_2.R;
import ru.geekbrains.lessions2345.notepadonfragments_2.logic.CardSourceImplement;
import ru.geekbrains.lessions2345.notepadonfragments_2.model.Constants;
import ru.geekbrains.lessions2345.notepadonfragments_2.model.DataTypes;
import ru.geekbrains.lessions2345.notepadonfragments_2.model.DeleteAnswersTypes;
import ru.geekbrains.lessions2345.notepadonfragments_2.observe.Observer;
import ru.geekbrains.lessions2345.notepadonfragments_2.observe.Publisher;
import ru.geekbrains.lessions2345.notepadonfragments_2.observe.PublisherGetter;
import ru.geekbrains.lessions2345.notepadonfragments_2.ui.fragments.DeleteFragment;
import ru.geekbrains.lessions2345.notepadonfragments_2.ui.fragments.EditCardFragment;
import ru.geekbrains.lessions2345.notepadonfragments_2.ui.fragments.GoogleAuthoriseFragment;
import ru.geekbrains.lessions2345.notepadonfragments_2.ui.fragments.ListNotesFragment;

public class MainActivity extends AppCompatActivity implements PublisherGetter, Observer, NavigationGetter {

    private CardSourceImplement cardSourceImplement;
    private EditCardFragment editCardFragment;
    public Constants constants = new Constants();
    private DataTypes typeSourceData = DataTypes.FIREBASE_DATA;
    private Navigation navigation = new Navigation(getSupportFragmentManager());
    private GoogleAuthoriseFragment googleAuthoriseFragment;
    private Publisher publisher = new Publisher();
    private boolean completeGoogleAuthorise = false;
    private DeleteAnswersTypes userDecision = DeleteAnswersTypes.UNKNOWN;
    private DeleteFragment deleteFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Подключение к MainActivity паблишера для получения результата аутентификации через Google
        publisher.subscribe((Observer) this);

        // Получение настроек
        getSettings();

        // Загрузка фрагмента с аутентикацией Google
        if (completeGoogleAuthorise == false) {
            googleAuthoriseFragment = new GoogleAuthoriseFragment();
            googleAuthoriseFragment.setCancelable(false);
            googleAuthoriseFragment.show(getFragmentManager(), "");
        }

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
        // Здесь сцепляется шторка с нашим тулбаром (верхней части) и выезжающей шторки. Синхронизирует ToolBar и NavigationDrawer
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
            if (typeSourceData == DataTypes.FIREBASE_DATA) {
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
            // Отображение диалогового фрагмента DeleteFragment с подтверждением удаления заметки
            if (getCardSourceImplement().getActiveNoteIndex() > 0) {
                deleteFragment = new DeleteFragment();
//                deleteFragment.show(getFragmentManager(), "");
                deleteFragment.show(getSupportFragmentManager(), "");
            } else {
                Toast.makeText(this, "Для удаления заметки её нужно сначала выбрать в списке слева.", Toast.LENGTH_SHORT).show();
            }
            return true;
        } else if (itemId == R.id.action_filter) {
            Toast.makeText(this, "Фильтр вывода заметок", Toast.LENGTH_SHORT).show();
            return true;
        } else if (itemId == R.id.action_show_card) {
            // Отображение диалогового фрагмента с просмотром/редактированием карточки заметки
            editCardFragment = new EditCardFragment();
//            editCardFragment.show(getFragmentManager(), "");
            editCardFragment.show(getSupportFragmentManager(), "");
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
    private void saveSettings(DataTypes typeSourceData) {
        SharedPreferences sharedPreferences = getSharedPreferences(constants.KEY_DATA_SETTINGS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        // Сохранение типа источника данных
        if (typeSourceData == DataTypes.FILE_DATA) {
            editor.putInt(constants.KEY_DATA_SETTINGS, 1);
        } else if (typeSourceData == DataTypes.FIREBASE_DATA) {
            editor.putInt(constants.KEY_DATA_SETTINGS, 2);
        } else if (typeSourceData == DataTypes.DATABASE_DATA) {
            editor.putInt(constants.KEY_DATA_SETTINGS, 3);
        } else {
            // Случай, когда typeSourceData == null или typeSourceData == DATA_SETTINGS.TEST_DATA
            editor.putInt(constants.KEY_DATA_SETTINGS, 0);
        }
        editor.putBoolean(constants.KEY_GOOGLE_AUTHORISE, completeGoogleAuthorise);
        editor.apply();
    }

    // Получение настроек из SharedPreferences
    private void getSettings() {
        SharedPreferences sharedPreferences = getSharedPreferences(constants.KEY_DATA_SETTINGS, MODE_PRIVATE);
        if (typeSourceData == null) {
            int timeSourceData = sharedPreferences.getInt(constants.KEY_DATA_SETTINGS, 0);
            if (timeSourceData == 1) {
                typeSourceData = DataTypes.FILE_DATA;
            } else if (timeSourceData == 2) {
                typeSourceData = DataTypes.FIREBASE_DATA;
            } else if (timeSourceData == 3) {
                typeSourceData = DataTypes.DATABASE_DATA;
            } else {
                // Случай, когда typeSourceData == null или typeSourceData == DATA_SETTINGS.TEST_DATA
                typeSourceData = DataTypes.TEST_DATA;
            }
            completeGoogleAuthorise = sharedPreferences.getBoolean(constants.KEY_GOOGLE_AUTHORISE, false);
        } else {
            sharedPreferences.getInt(constants.KEY_DATA_SETTINGS, 0);
            completeGoogleAuthorise = sharedPreferences.getBoolean(constants.KEY_GOOGLE_AUTHORISE, false);
        }
    }

    // Отображение пустого текстового поля
    private void showEmptyTextFragment() {
        navigation.addFragment(new Fragment(), R.id.text_container, false);
/*
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.text_container, new Fragment())
                .commitNow();
*/
    }

    // Отображение фрагмента со списком заметок
    private void showListNotes() {
        navigation.addFragment(ListNotesFragment.newInstance(), R.id.list_container, false);
/*
        getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.list_container, ListNotesFragment.newInstance())
            .commit();
*/
    }

    // Метод для получения класса Navigation
    public Navigation getNavigation() {
        return navigation;
    }

    // Метод для получения класса Publisher
    public Publisher getPublisher() {
        return publisher;
    }

    @Override
    public void completeGoogleAuthorise(boolean completeGoogleAuthorise) {
        completeGoogleAuthorise = completeGoogleAuthorise;
        saveSettings(typeSourceData);
        if (completeGoogleAuthorise == true) {
            showListNotes();
        }
    }

    @Override
    public void updateDatesFromFireBase(int numberDownloadedNotes) {
        if (numberDownloadedNotes > 0) {
            showListNotes();
        }
    }

    @Override
    public void updateUserChooseDeleteFile(DeleteAnswersTypes userDecision) {
        this.userDecision = userDecision;
        if (this.userDecision == DeleteAnswersTypes.YES) {
            cardSourceImplement.setDeleteMode(true);
            // Отображение пустого текстового поля
            showEmptyTextFragment();
            cardSourceImplement.setDeleteMode(false);
            String deletedNoteName = "\"" + cardSourceImplement.getCardNote(cardSourceImplement.getActiveNoteIndex()).getName() + "\" (\"" + cardSourceImplement.getCardNote(cardSourceImplement.getActiveNoteIndex()).getDescription() + "\")";
            cardSourceImplement.removeCardNote(cardSourceImplement.getActiveNoteIndex());
            cardSourceImplement.setActiveNoteIndex(0);
            // Отображение обновлённого фрагмента со списком заметок
            showListNotes();
            Toast.makeText(this, "Заметка " + deletedNoteName + " удалена.", Toast.LENGTH_SHORT).show();
        }
        this.userDecision = DeleteAnswersTypes.UNKNOWN;
    }

    @Override
    public void updateUserChooseDeleteFileFromContextMenu(DeleteAnswersTypes userDecision, int indexChoosedNoteInContextMenu) {
        int oldActiveNoteIndex = getCardSourceImplement().getActiveNoteIndex();
        if (oldActiveNoteIndex > 0) {
            getCardSourceImplement().setActiveNoteIndex(indexChoosedNoteInContextMenu);
            // Удаление заметки
            String deletedNoteName = "\"" + getCardSourceImplement().getCardNote(getCardSourceImplement().getActiveNoteIndex()).getName() + "\" (\"" + getCardSourceImplement().getCardNote(getCardSourceImplement().getActiveNoteIndex()).getDescription() + "\")";
            getCardSourceImplement().removeCardNote(getCardSourceImplement().getActiveNoteIndex());
            Toast.makeText(this, "Заметка " + deletedNoteName + " удалена.", Toast.LENGTH_SHORT).show();

            // Отображение обновлённой информации
            if (oldActiveNoteIndex != indexChoosedNoteInContextMenu) {
                if (oldActiveNoteIndex > indexChoosedNoteInContextMenu) {
                    oldActiveNoteIndex--;
                    getCardSourceImplement().setOldActiveNoteIndexBeforeDelete(oldActiveNoteIndex);
                } else {
                    getCardSourceImplement().setOldActiveNoteIndexBeforeDelete(oldActiveNoteIndex);
                }
                getCardSourceImplement().setActiveNoteIndex(oldActiveNoteIndex);
            } else {
                getCardSourceImplement().setActiveNoteIndex(0);
                getCardSourceImplement().setOldActiveNoteIndexBeforeDelete(0);
                // Отображение пустого текстового поля
                getCardSourceImplement().setDeleteMode(true);
                showEmptyTextFragment();
/*                getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.text_container, new Fragment())
                    .commitNow();*/
                getCardSourceImplement().setDeleteMode(false);
            }
            showListNotes();
        } else if ((oldActiveNoteIndex == 0) && (getCardSourceImplement().size() > 0)) {
            getCardSourceImplement().setActiveNoteIndex(indexChoosedNoteInContextMenu);
            // Удаление заметки
            String deletedNoteName = "\"" + getCardSourceImplement().getCardNote(getCardSourceImplement().getActiveNoteIndex()).getName() + "\" (\"" + getCardSourceImplement().getCardNote(getCardSourceImplement().getActiveNoteIndex()).getDescription() + "\")";
            getCardSourceImplement().removeCardNote(getCardSourceImplement().getActiveNoteIndex());
            Toast.makeText(this, "Заметка " + deletedNoteName + " удалена.", Toast.LENGTH_SHORT).show();

            getCardSourceImplement().setActiveNoteIndex(0);
            getCardSourceImplement().setOldActiveNoteIndexBeforeDelete(0);
            showListNotes();
        }
    }

    // Этот метод добавлен, чтобы можно было протестировать аутентификацию Google
    // Он является искусственным костылём для проверки работоспособности программы. Ведь достаточно только один раз пройти проверку
    @Override
    protected void onDestroy() {
        super.onDestroy();
        completeGoogleAuthorise = false;
        saveSettings(typeSourceData);
    }
}