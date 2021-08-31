package ru.geekbrains.lessions2345.notepadonfragments_2.observe

import ru.geekbrains.lessions2345.notepadonfragments_2.model.DeleteAnswersTypes

interface Observer {
    fun completeGoogleAuthorise(completeGoogleAuthorise : Boolean)

    fun updateDatesFromFireBase(numberDownloadedNotes : Int)

    fun updateUserChooseDeleteFile(userDecision : DeleteAnswersTypes)

    fun updateUserChooseDeleteFileFromContextMenu(userDecision : DeleteAnswersTypes, indexChoosedNoteInContextMenu : Int)
}