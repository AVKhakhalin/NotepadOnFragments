package ru.geekbrains.lessions2345.notepadonfragments_2.ui.fragments

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import ru.geekbrains.lessions2345.notepadonfragments_2.R
import ru.geekbrains.lessions2345.notepadonfragments_2.logic.CardNote
import ru.geekbrains.lessions2345.notepadonfragments_2.model.Constants
import ru.geekbrains.lessions2345.notepadonfragments_2.ui.MainActivity
import ru.geekbrains.lessions2345.notepadonfragments_2.ui.Navigation
import ru.geekbrains.lessions2345.notepadonfragments_2.ui.NavigationGetter
import java.util.*

class EditCardFragment : DialogFragment(), DialogInterface.OnClickListener {
    private val calendar = Calendar.getInstance(TimeZone.getDefault())
    private var datePicker : DatePicker? = null
    private var editText_name : EditText? = null
    private var editText_description: EditText? = null
    private var activeIndex : Int = 0
    private var buttonOk : Button? = null
    private var textView_title : TextView? = null
    private var navigation: Navigation? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Получаем навигатор
        navigation = (context as NavigationGetter).navigation
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_edit_card, null)
        val mainActivity = getActivity() as MainActivity
        initView(view, mainActivity)
        return view
    }

    override fun onClick(dialog: DialogInterface, which: Int) {}

    private fun initView(view: View, mainActivity: MainActivity) {
        activeIndex = mainActivity.cardSourceImplement.activeNoteIndex
        textView_title = view.findViewById(R.id.card_note_title)
        editText_name = view.findViewById(R.id.card_name_note_text)
        editText_description = view.findViewById(R.id.card_description_note_text)
        buttonOk = view.findViewById(R.id.button_ok)
        if (buttonOk != null) {
            buttonOk!!.setOnClickListener(View.OnClickListener { view: View ->
                onOk(view)
            })
        }
        datePicker = view.findViewById(R.id.inputDate)
        if (activeIndex <= 0) {
            if ((buttonOk != null) && (textView_title != null) && (editText_name != null) && (editText_description != null)) {
                buttonOk!!.setText(Constants.NAME_EMPTY_NOTE_ADD)
                textView_title!!.setText("Карточка новой заметки")
                editText_name!!.setText(Constants.NAME_EMPTY_NOTE)
                editText_description!!.setText(Constants.DESCRIPTION_EMPTY_NOTE)
            }
        } else {
            if ((editText_name != null) && (editText_description != null)) {
                editText_name!!.setText(mainActivity.cardSourceImplement.getCardNote(activeIndex).getName())
                editText_description!!.setText(mainActivity.cardSourceImplement.getCardNote(activeIndex).getDescription())
            }
        }
        view.findViewById<View>(R.id.button_cancel).setOnClickListener { view: View ->
            onCancel(view)
        }
        val year: Int
        val month: Int
        val day: Int
        if (activeIndex <= 0) {
            year = calendar[Calendar.YEAR]
            month = calendar[Calendar.MONTH]
            day = calendar[Calendar.DAY_OF_MONTH]
        } else {
            year = mainActivity.cardSourceImplement.getCardNote(activeIndex).getDateYear()
            month = mainActivity.cardSourceImplement.getCardNote(activeIndex).getDateMonth() - 1
            day = mainActivity.cardSourceImplement.getCardNote(activeIndex).getDateDay()
        }
        if (datePicker != null) {
            datePicker!!.init(year, month, day, null)
        }
    }

    // Результат нажатия на кнопку отмены действия
    private fun onCancel(view: View) {
        dismiss()
    }

    // Результат нажатия на кнопку подтверждения действия
    fun onOk(v: View?) {
        val mainActivity = getActivity() as MainActivity
        if (activeIndex <= 0) {
            mainActivity.cardSourceImplement.addCardNote(
                CardNote(
                    editText_name!!.text.toString(),
                    editText_description!!.text.toString(),
                    "",
                    datePicker!!.year,
                    datePicker!!.month + 1,
                    datePicker!!.dayOfMonth
                )
            )
        } else {
            mainActivity.cardSourceImplement.setCardNote(
                activeIndex,
                editText_name!!.text.toString(),
                editText_description!!.text.toString()
            )
            mainActivity.cardSourceImplement.setCardNote(
                activeIndex,
                datePicker!!.year,
                datePicker!!.month + 1,
                datePicker!!.dayOfMonth
            )
        }
        // Перезапуск фрагмента со списком для отображения новой заметки
        navigation!!.addFragment(ListNotesFragment.newInstance(), R.id.list_container, false)
        dismiss()
    }
}