package ru.geekbrains.lessions2345.notepadonfragments_2.ui.fragments

import android.content.res.Configuration
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import ru.geekbrains.lessions2345.notepadonfragments_2.R
import ru.geekbrains.lessions2345.notepadonfragments_2.logic.ListNotes

class ListNotesAdapter(listNotes : ListNotes, orientation : Int, fragment : Fragment) : RecyclerView.Adapter<ListNotesAdapter.ViewHolder>() {

    companion object {
        @JvmField
        var menuContextClickPosition: Int = 0
    }

    private var listNotes : ListNotes? = null
    var orientation = 1

    // Переменные для передачи событий во фрагмент
    private var listener_name : ListNotesFragmentOnClickListener? = null
    private var listener_date : ListNotesFragmentOnClickListener? = null

    // Переменные для контекстного меню
    private var fragment : Fragment? = null

    init {
        this.listNotes = listNotes
        this.orientation = orientation
        this.fragment = fragment
    }

    fun getMenuContextClickPosition() : Int {
        return menuContextClickPosition
    }

    fun setOnListNotesFragmentOnClickListener_name(listener_name : ListNotesFragmentOnClickListener) {
        this.listener_name = listener_name
    }

    fun setOnListNotesFragmentOnClickListener_date(listener_date: ListNotesFragmentOnClickListener) {
        this.listener_date = listener_date
    }

    fun ListNotesAdapter(listNotes: ListNotes, orientation: Int, fragment: Fragment) {
        this.listNotes = listNotes
        this.orientation = orientation
        this.fragment = fragment
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListNotesAdapter.ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.fragment_list_card, parent, false)
        return ViewHolder(view, fragment, listener_name, listener_date)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (position == 0) {
                holder.textView_name.text = listNotes!!.getName(position)
            } else {
                holder.textView_name.text = listNotes!!.getDescription(position)
            }
        } else {
            holder.textView_name.text = listNotes!!.getName(position)
        }
        holder.textView_date.text = listNotes!!.getDate(position)
    }

    override fun getItemCount(): Int {
        return listNotes!!.getSize()
    }

    class ViewHolder(itemView: View, fragment : Fragment?, listener_name : ListNotesFragmentOnClickListener?, listener_date : ListNotesFragmentOnClickListener?) : RecyclerView.ViewHolder(itemView) {
        var textView_name: TextView
        var textView_date: TextView

        init {
            textView_name = itemView.findViewById(R.id.name_note_text)
            textView_date = itemView.findViewById(R.id.date_note_text)

            // Регистрация стартового элемента для контекстного меню
            fragment?.registerForContextMenu(textView_name)

            // Установка обработчиков событий для нажатия на имя заметки
            textView_name.setOnClickListener { v -> listener_name?.onClick(v, adapterPosition) }
            textView_name.setOnLongClickListener {
                menuContextClickPosition = adapterPosition
                if (menuContextClickPosition > 0) {
                    textView_name.showContextMenu(0f, 0f)
                    false
                } else {
                    true
                }
            }

            // Установка обработчика событий для нажатия на дату заметки
            textView_date.setOnClickListener { v -> listener_date?.onClick(v, adapterPosition) }
        }
    }

    fun setListNotesName(index : Int, name : String) {
        if (listNotes != null) {
            listNotes!!.setName(index, name)
        }
    }

    fun setListNotesDescription(index : Int, description : String) {
        if (listNotes != null) {
            listNotes!!.setDescription(index, description!!)
        }
    }

    fun addListNote(index : Int, name : String, description : String, date : String) {
        if (listNotes != null) {
            listNotes!!.addNote(index, name, description, date)
        }
    }

    fun setListNotesDate(index: Int, date: String) {
        if (listNotes != null) {
            listNotes!!.setDate(index, date)
        }
    }
}