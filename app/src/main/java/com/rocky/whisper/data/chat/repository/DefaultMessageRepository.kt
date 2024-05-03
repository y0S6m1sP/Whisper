package com.rocky.whisper.data.chat.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.QuerySnapshot
import com.rocky.whisper.data.Constants.COLLECTION_MESSAGES
import com.rocky.whisper.data.Constants.COLLECTION_ROOMS
import com.rocky.whisper.data.Constants.FIELD_LAST_MESSAGE
import com.rocky.whisper.data.Constants.FIELD_LAST_UPDATE
import com.rocky.whisper.data.chat.Message
import com.rocky.whisper.data.chat.local.MessageDao
import com.rocky.whisper.data.chat.toExternal
import com.rocky.whisper.data.chat.toLocal
import com.rocky.whisper.di.ApplicationScope
import com.rocky.whisper.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date
import javax.inject.Inject

class DefaultMessageRepository @Inject constructor(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val messageDao: MessageDao,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
    @ApplicationScope private val scope: CoroutineScope
) : MessageRepository {

    override suspend fun sendMessage(roomId: String, message: String) {
        try {
            val user = auth.currentUser
            user?.uid?.let {
                val doc = db.collection(COLLECTION_ROOMS)
                    .document(roomId)
                    .collection(COLLECTION_MESSAGES).document()
                doc.set(Message(doc.id, it, message, System.currentTimeMillis())).await()
                updateLastMessage(roomId, message)
            }
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    override fun fetchMessage(roomId: String): ListenerRegistration {
        return db.collection(COLLECTION_ROOMS)
            .document(roomId)
            .collection(COLLECTION_MESSAGES)
            .orderBy(FIELD_LAST_UPDATE)
            .addSnapshotListener { snapshot, error ->
                error?.let {
                    Timber.e(error)
                    return@addSnapshotListener
                }

                snapshot?.let {
                    scope.launch(dispatcher) {
                        handleMessageDocumentChanges(it, roomId)
                    }
                }
            }
    }

    private fun handleMessageDocumentChanges(snapshot: QuerySnapshot, roomId: String) {
        scope.launch {
            for (dc in snapshot.documentChanges) {
                val message = dc.document.toObject(Message::class.java).toLocal(roomId)
                when (dc.type) {
                    DocumentChange.Type.ADDED -> {
                        messageDao.insertAll(message)
                    }

                    DocumentChange.Type.MODIFIED -> {
                        messageDao.update(message.id, message.message)
                    }

                    DocumentChange.Type.REMOVED -> {
                        messageDao.delete(message)
                    }
                }
            }
        }
    }

    private suspend fun updateLastMessage(roomId: String, message: String) {
        try {
            val updates = hashMapOf(
                FIELD_LAST_MESSAGE to message,
                FIELD_LAST_UPDATE to System.currentTimeMillis(),
            )

            db.collection((COLLECTION_ROOMS))
                .document(roomId)
                .update(updates as Map<String, Any>).await()
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    override suspend fun observeMessage(roomId: String): Flow<Map<LocalDate, List<Message>>> {
        return messageDao.observeAll(roomId).map {
            it.map { localMessage -> localMessage.toExternal() }
                .sortedBy { message -> message.lastUpdate }
                .groupBy { message ->
                    Date(message.lastUpdate)
                        .toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()
                }
        }
    }

    override suspend fun observeMessageCount(): Flow<Int> {
        return messageDao.observeMessageCount()
    }
}