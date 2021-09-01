package ru.geekbrains.lessions2345.notepadonfragments_2.logic

import android.util.Log
import androidx.annotation.NonNull
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.*
import com.google.firebase.firestore.core.UserData
import java.util.concurrent.Executor

class CardsSourceFirebaseImplKotlinNotWork : CardSource {

    private val CARDS_COLLECTION : String = "cards"
    private val TAG : String = "[CardsSourceFirebaseImpl]"
    private var cardData : CardNote? = null

    // База данных Firebase
    private val store : FirebaseFirestore = FirebaseFirestore.getInstance()

    // Коллекция документов
    private val collection : CollectionReference = store.collection(CARDS_COLLECTION)

    // Загружаемый список карточек
    private var cardsData : MutableList<CardNote>? = null

    public fun init(cardsSourceResponse : CardSourceResponse) : CardSource {
        // Получить всю коллекцию отсортированную по полю "год"
        collection.orderBy(CardNoteMapping.Fields.YEAR, Query.Direction.ASCENDING).get(Source.SERVER)
            .addOnCompleteListener(OnCompleteListener<QuerySnapshot>() {
                // При удачном считывании данных загрузим список карточек
                @Override
                fun onComplete(@NonNull task : Task<QuerySnapshot> ) {
                    if (task.isSuccessful()) {
//                        cardsData = ArrayList<CardNote>()
                        var result = task.getResult()
                        if (result != null) {
                            for (document: QueryDocumentSnapshot in result) {
                                var doc : HashMap<String, Any> = document.getData() as HashMap<String, Any>
                                var id : String = document.id
//                                var cardData : CardNote = CardNoteMapping.Fields.toCardNote(id, doc)
                                cardData = CardNoteMapping.Fields.toCardNote(id, doc)
//                                (cardsData as ArrayList<CardNote>).add(cardData)
                                if (cardsData == null) {
                                    cardsData = MutableList<CardNote>(1){cardData as CardNote}
                                } else {
                                    cardsData?.add(cardData as CardNote)
                                }
                            }
                        }
                        if (cardsData == null) {
                            Log.d(TAG, "success 0 qnt")
                        } else {
                            Log.d(TAG, "success ${cardsData!!.size} qnt")
                        }
                        cardsSourceResponse.initialized(this)
                    } else {
                        Log.d(TAG, "get failed with ", task.exception)
                    }
                }

            })
            .addOnFailureListener(OnFailureListener {
                @Override
                fun onFailure(@NonNull e : Exception) {
                    Log.d(TAG, "get failed with ", e)
                }
            })
        return this
    }

    public fun getCardNoteFirebase(position : Int) : CardNote {
        if (cardsData == null) {
            var emptyCardNote : CardNote = CardNote("Пусто", "Пустая карточка", "Пусто", 0, 0, 0)
            return emptyCardNote
        } else {
            return cardsData!!.get(position)
        }
    }

    public override fun size() : Int {
        if (cardsData == null) {
            return 0
        } else {
            return cardsData!!.size
        }
    }

    public fun deleteCardNoteFirebase(position : Int) {
        // Удалить документ с определенном идентификатором
        if (cardsData != null) {
            collection.document(cardsData!!.get(position).getId()).delete()
            cardsData!!.removeAt(position)
        }
    }

    public fun updateCardNoteFirebase(position : Int, cardData : CardNote) {
        var id : String = cardData.getId();
        // Изменить документ по идентификатору
        collection.document(id).set(CardNoteMapping.Fields.toDocument(cardData));
    }

    public fun addCardNoteFirebase(cardData : CardNote) {
        // Добавить документ
        collection.add(CardNoteMapping.Fields.toDocument(cardData)).addOnSuccessListener(OnSuccessListener<DocumentReference>() {
            @Override
            fun onSuccess(documentReference : DocumentReference) {
                cardData.setId(documentReference.getId())
            }
        })
    }

    public fun clearCardNoteFirebase() {
        if (cardsData != null) {
            for (cardData: CardNote in cardsData!!) {
                collection.document(cardData.getId()).delete()
            }
        }
        cardsData = null
    }

    override fun getCardNote(position: Int): CardNote {
        if (cardsData == null) {
            var emptyCardNote : CardNote = CardNote("Пусто", "Пустая карточка", "Пусто", 0, 0, 0)
            return emptyCardNote
        } else {
            return cardsData!!.get(position)
        }
    }

    override fun getListNotes(): ListNotes {
        TODO("Not yet implemented")
    }

    override fun addCardNote() {
        TODO("Not yet implemented")
    }

    override fun addCardNote(cardNote: CardNote) {
        TODO("Not yet implemented")
    }

    override fun setCardNote(position: Int, cardNote: CardNote) {
        TODO("Not yet implemented")
    }

    override fun setCardNote(position: Int, name: String, description: String) {
        TODO("Not yet implemented")
    }

    override fun setCardNote(position: Int, text: String) {
        TODO("Not yet implemented")
    }

    override fun setCardNote(position: Int, year: Int, month: Int, day: Int) {
        TODO("Not yet implemented")
    }

    override fun removeCardNote(position: Int) {
        TODO("Not yet implemented")
    }
}