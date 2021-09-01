package ru.geekbrains.lessions2345.notepadonfragments_2.ui.fragments

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.annotation.NonNull
import androidx.fragment.app.DialogFragment
import ru.geekbrains.lessions2345.notepadonfragments_2.R
import ru.geekbrains.lessions2345.notepadonfragments_2.model.DeleteAnswersTypes
import ru.geekbrains.lessions2345.notepadonfragments_2.observe.Publisher
import ru.geekbrains.lessions2345.notepadonfragments_2.observe.PublisherGetter

class DeleteFragment : DialogFragment(), DialogInterface.OnClickListener {
    private var buttonYes : Button? = null
    private var buttonNo : Button? = null
    private var publisher = Publisher()
    private var deleteNoteFromContextMenu : Boolean = false
    private var indexChoosedNoteInContextMenu : Int = 0

    fun newInstance(deleteNoteFromContextMenu : Boolean, position : Int) : DeleteFragment {
        val deleteFragment = DeleteFragment()
        deleteFragment.setDeleteNoteFromContextMenu(deleteNoteFromContextMenu)
        deleteFragment.setIndexChoosedNoteInContextMenu(position)
        return deleteFragment
    }

    fun setIndexChoosedNoteInContextMenu(indexChoosedNoteInContextMenu : Int) {
        this.indexChoosedNoteInContextMenu = indexChoosedNoteInContextMenu
    }

    fun setDeleteNoteFromContextMenu(deleteNoteFromContextMenu : Boolean) {
        this.deleteNoteFromContextMenu = deleteNoteFromContextMenu
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_delete_yes_no, null)
        initView(view)
        return view
    }

    override fun onAttach(@NonNull context: Context) {
        super.onAttach(context)
        // Получаем паблишер, чтобы передать с ним в MainActivity результат выбора пользователя по удалению заметки
        publisher = (context as PublisherGetter).publisher
    }

    private fun initView(view: View) {
        buttonYes = view.findViewById(R.id.button_yes)
        if (buttonYes != null) {
            buttonYes!!.setOnClickListener(View.OnClickListener { view: View ->
                onYes(view)
            })
        }
        buttonNo = view.findViewById(R.id.button_no)
        if (buttonNo != null) {
            buttonNo!!.setOnClickListener(View.OnClickListener { view: View ->
                onNo(view)
            })
        }
    }

    // Результат нажатия на кнопку отмены действия
    private fun onNo(view: View) {
        publisher.notifySingle(DeleteAnswersTypes.NO)
        dismiss()
    }

    // Результат нажатия на кнопку подтверждения действия
    fun onYes(v: View) {
        if (deleteNoteFromContextMenu == false) {
            publisher.notifySingle(DeleteAnswersTypes.YES)
        } else {
            deleteNoteFromContextMenu = false
            publisher.notifySingle(DeleteAnswersTypes.YES, indexChoosedNoteInContextMenu)
        }
        dismiss()
    }

    override fun onClick(dialog: DialogInterface?, which: Int) {}
}