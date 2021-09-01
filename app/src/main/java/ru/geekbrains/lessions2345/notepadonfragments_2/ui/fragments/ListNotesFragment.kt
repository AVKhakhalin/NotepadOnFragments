package ru.geekbrains.lessions2345.notepadonfragments_2.ui.fragments

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.ContextMenu.ContextMenuInfo
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.geekbrains.lessions2345.notepadonfragments_2.R
import ru.geekbrains.lessions2345.notepadonfragments_2.observe.Publisher
import ru.geekbrains.lessions2345.notepadonfragments_2.observe.PublisherGetter
import ru.geekbrains.lessions2345.notepadonfragments_2.ui.MainActivity
import ru.geekbrains.lessions2345.notepadonfragments_2.ui.Navigation
import ru.geekbrains.lessions2345.notepadonfragments_2.ui.NavigationGetter
import java.util.*

class ListNotesFragment : Fragment(), ListNotesFragmentOnClickListener {
    private var newYear = 0
    private var newMonth = 0
    private var newDay = 0

    private val KEY_INDES_CHOISED_ELEMENT = "ChoisedElement"
    private var indexChoisedElement = 1

    private var listNotesAdapter: ListNotesAdapter? = null
    private var editCardFragment: EditCardFragment? = null

    private var navigation: Navigation? = null
    private var publisher = Publisher()

    companion object {
        @JvmStatic
        fun newInstance() : ListNotesFragment {
            return ListNotesFragment()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Получаем навигатор
        navigation = (context as NavigationGetter).navigation
        // Получаем паблишер, чтобы передать с ним в MainActivity результат выбора пользователя по удалению заметки
        publisher = (context as PublisherGetter).publisher
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Восстановление индекса выбранного элемента
        if (savedInstanceState != null) {
            indexChoisedElement = savedInstanceState.getInt(KEY_INDES_CHOISED_ELEMENT)
        }

        // Установка списка заметок к отображению и реагированию на события
        val view: View = inflater.inflate(R.layout.fragment_list, container, false)
        val recyclerView: RecyclerView = view.findViewById(R.id.recycler_list_container)
        listNotesSetup(recyclerView)
        return view
    }

    // Метод для установки и отображения списка заметок, а также установки на его элементы обработчики событий
    private fun listNotesSetup(recyclerView: RecyclerView) {
        val mainActivity = activity as MainActivity
        // Устанавливаем признак одинаковости элементов списка
        recyclerView.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager
        // Устанавливаем настройки адаптера listNotesAdapter
        setUpListNotesAdapter(mainActivity)
        recyclerView.adapter = listNotesAdapter

        // Восстановление отображения содержимого просматриваемой заметки до удаления другой заметки через контекстное меню
        val oldActiveNoteIndexBeforeDelete =
            mainActivity.cardSourceImplement.oldActiveNoteIndexBeforeDelete
        if (oldActiveNoteIndexBeforeDelete > 0) {
            mainActivity.cardSourceImplement.oldActiveNoteIndexBeforeDelete = 0
            // Загрузка фрагмента c текстом TextFragment
            if (navigation != null) {
                navigation!!.addFragment(TextFragment.newInstance(oldActiveNoteIndexBeforeDelete, false), R.id.text_container, false)
            }
        }
    }

    // Настройки адаптера listNotesAdapter
    private fun setUpListNotesAdapter(mainActivity: MainActivity) {
        listNotesAdapter = ListNotesAdapter(mainActivity.cardSourceImplement.getListNotes(), getResources().getConfiguration().orientation,this)

        // Вешаем обработчики событий при нажатии на имя заметки
        if (listNotesAdapter != null) {
            listNotesAdapter!!.setOnListNotesFragmentOnClickListener_name { view, position ->
                if (position == 0) {
                    mainActivity.cardSourceImplement.addCardNote()
                    // Перезапуск фрагмента со списком для отображения новой заметки
                    updateListNotes()

                    // Загрузка фрагмента c текстом TextFragment
                    indexChoisedElement = 1
                    mainActivity.cardSourceImplement.activeNoteIndex = 1
                    updateTextNote(indexChoisedElement)
                } else {
                    mainActivity.cardSourceImplement.activeNoteIndex = position
                    // Загрузка фрагмента c текстом TextFragment
                    updateTextNote(position)
                }
            }
        }
        // Вешаем обработчики событий при нажатии на дату заметки
        // Показать DatePicker для изменения даты заметки
        if (listNotesAdapter != null) {
            listNotesAdapter!!.setOnListNotesFragmentOnClickListener_date { view, position ->
                showDatePicker(position, mainActivity)
            }
        }
    }

    // Показать DatePicker
    private fun showDatePicker(sendedIndex: Int, mainActivity: MainActivity) {
        // Устанавливаем новую дату
        val calendar = Calendar.getInstance()
        val datePickerDialog =
            OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                calendar[Calendar.YEAR] = year
                calendar[Calendar.MONTH] = monthOfYear
                calendar[Calendar.DAY_OF_MONTH] = dayOfMonth
                newYear = year
                newMonth = monthOfYear + 1
                newDay = dayOfMonth
                mainActivity.cardSourceImplement.setCardNote(sendedIndex, newYear, newMonth, newDay)
                if (listNotesAdapter != null) {
                    listNotesAdapter!!.setListNotesDate(sendedIndex, mainActivity.cardSourceImplement.getCardNote(sendedIndex).getDate())
                    listNotesAdapter!!.notifyItemChanged(sendedIndex)
                }
            }
        // Отображаем диалоговое окно для выбора даты
        if (sendedIndex <= 0) {
            DatePickerDialog(
                requireContext(),
                datePickerDialog,
                calendar[Calendar.YEAR],
                calendar[Calendar.MONTH],
                calendar[Calendar.DAY_OF_MONTH]
            ).show()
        } else {
            DatePickerDialog(
                requireContext(), datePickerDialog,
                mainActivity.cardSourceImplement.getCardNote(sendedIndex).getDateYear(),
                mainActivity.cardSourceImplement.getCardNote(sendedIndex).getDateMonth() - 1,
                mainActivity.cardSourceImplement.getCardNote(sendedIndex).getDateDay()
            ).show()
        }
    }

    // Сохранение промежуточного состояния при повороте экрана
    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(KEY_INDES_CHOISED_ELEMENT, indexChoisedElement)
        super.onSaveInstanceState(outState)
    }

    // Унаследованный метод от интерфейса ListNotesFragmentOnClickListener. Должен быть, но здесь не используется
    override fun onClick(view: View, position: Int) {}

    // Создание контекстного меню для элементов списка
    override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        requireActivity().menuInflater.inflate(R.menu.menu_context, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val mainActivity = activity as MainActivity
        val position = if (listNotesAdapter != null) listNotesAdapter!!.getMenuContextClickPosition() else 0
        when (item.itemId) {
            R.id.context_menu_action_show_card -> {
                // Отображение карточки заметки через контекстное меню
                mainActivity.cardSourceImplement.activeNoteIndex = position
                editCardFragment = EditCardFragment()
                if (editCardFragment != null) {
                    editCardFragment!!.show(mainActivity.supportFragmentManager, "")
                }
            }
            R.id.context_menu_action_delete_card -> {
                // Удаление карточки через контекстное меню
                // Отображение диалогового фрагмента DeleteFragment с подтверждением удаления заметки из контекстного меню
                val deleteFragment = DeleteFragment().newInstance(true, position)
                deleteFragment.show(requireActivity().supportFragmentManager, "")
            }
        }
        return super.onContextItemSelected(item)
    }

    // Отображение фрагмента с обновлённым списком заметок
    private fun updateListNotes() {
        if (navigation != null) {
            navigation!!.addFragment(ListNotesFragment.newInstance(), R.id.list_container,false)
        }
    }

    // Отображение фрагмента с текстом заметки
    private fun updateTextNote(index: Int) {
        if (navigation != null) {
            navigation!!.addFragment(TextFragment.newInstance(index, true), R.id.text_container, false)
        }
    }
}