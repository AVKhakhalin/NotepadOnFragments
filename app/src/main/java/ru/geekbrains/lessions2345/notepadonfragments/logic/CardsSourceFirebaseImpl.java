package ru.geekbrains.lessions2345.notepadonfragments.logic;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CardsSourceFirebaseImpl implements CardSource {

    private static final String CARDS_COLLECTION = "cards";
    private static final String TAG = "[CardsSourceFirebaseImpl]";
    CardNote cardData;

    // База данных Firestore
    private FirebaseFirestore store = FirebaseFirestore.getInstance();

    // Коллекция документов
    private CollectionReference collection = store.collection(CARDS_COLLECTION);

    // Загружаемый список карточек
    private List<CardNote> cardsData = new ArrayList<CardNote>();

    public CardSource init(final CardSourceResponse cardsSourceResponse) {
        // Получить всю коллекцию отсортированную по полю "год"
        collection.orderBy(CardNoteMapping.Fields.YEAR, Query.Direction.ASCENDING).get(Source.SERVER)
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                // При удачном считывании данных загрузим список карточек
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        cardsData = new ArrayList<CardNote>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Map<String, Object> doc = document.getData();
                            String id = document.getId();
//                            CardNote cardData = CardNoteMapping.toCardNote(id, doc);
                            cardData = CardNoteMapping.toCardNote(id, doc);
                            cardsData.add(cardData);
                        }
                        Log.d(TAG, "success " + cardsData.size() + " qnt");
                        cardsSourceResponse.initialized(CardsSourceFirebaseImpl.this);
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            })
            .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "get failed with ", e);
            }
        });
        return this;
    }

    public CardNote getCardNoteFirebase(int position) {
        return cardsData.get(position);
    }

    @Override
    public int size() {
        if (cardsData == null){
            return 0;
        }
        return cardsData.size();
    }

    public void deleteCardNoteFirebase(int position) {
        // Удалить документ с определенном идентификатором
        collection.document(cardsData.get(position).getId()).delete();
        cardsData.remove(position);
    }

    public void updateCardNoteFirebase(int position, CardNote cardData) {
        String id = cardData.getId();
        // Изменить документ по идентификатору
        collection.document(id).set(CardNoteMapping.toDocument(cardData));
    }

    public void addCardNoteFirebase(final CardNote cardData) {
        // Добавить документ
        collection.add(CardNoteMapping.toDocument(cardData)).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                cardData.setId(documentReference.getId());
            }
        });
    }

    public void clearCardNoteFirebase() {
        for (CardNote cardData: cardsData) {
            collection.document(cardData.getId()).delete();
        }
        cardsData = new ArrayList<CardNote>();
    }

    @Override
    public CardNote getCardNote(int position) {
        return cardsData.get(position);
    }

    @Override
    public ListNotes getListNotes() {
        return null;
    }

    @Override
    public void addCardNote() {

    }

    @Override
    public void addCardNote(CardNote cardNote) {

    }

    @Override
    public void setCardNote(int position, CardNote cardNote) {

    }

    @Override
    public void setCardNote(int position, String name, String description) {

    }

    @Override
    public void setCardNote(int position, String text) {

    }

    @Override
    public void setCardNote(int position, int year, int month, int day) {

    }

    @Override
    public void removeCardNote(int position) {

    }
}
