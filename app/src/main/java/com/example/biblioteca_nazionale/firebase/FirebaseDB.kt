package com.example.biblioteca_nazionale.firebase

import android.content.ContentValues
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.MutableLiveData
import com.example.biblioteca_nazionale.model.UserSettings
import com.example.biblioteca_nazionale.model.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore


class FirebaseDB {


    companion object {
        val firebaseAuth = FirebaseAuth.getInstance()
        val db = com.google.firebase.ktx.Firebase.firestore

        // Prendo il riferimento allo user corrente -> codice UID
        val user = firebaseAuth.currentUser
    }

    fun writeUidAndEmail() {

        val newUser = Users(user?.uid.toString(), user?.email.toString(), UserSettings(null, null))
        db.collection("utenti").document(user?.uid.toString())
            .set(newUser)
            .addOnSuccessListener { /*Log.d("/HomePageActivity", "DocumentSnapshot successfully written!")*/ }
            .addOnFailureListener { /*Log.d("/HomePageActivity", "Error writing document")*/ }
    }

    var userInfoLiveData: MutableLiveData<DocumentSnapshot> = MutableLiveData()
    fun getAllUserInfoFromUid(uid: String): MutableLiveData<DocumentSnapshot> {

        val docRef = db.collection("utenti").document(uid)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    //  Log.d("/IMPORTANTE", "DocumentSnapshot data: ${document.data}")
                    userInfoLiveData.value = document


                } else {
                    // Log.d("/FirebaseDB", "Documento vuoto")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("/FirebaseDB", "Errore lettura dati !!!")
            }

        return userInfoLiveData
    }


    var allUserInfoLiveData: MutableLiveData<ArrayList<DocumentSnapshot>> = MutableLiveData()
    fun getAllUserFromDB(): MutableLiveData<ArrayList<DocumentSnapshot>> {
        val allDoc = db.collection("utenti").get()
            .addOnSuccessListener { allDocument ->
            for (document in allDocument) {
                //  Log.d("/IMPORTANTE", "DocumentSnapshot data: ${document.data}")
                allUserInfoLiveData.value?.add(document)
                // TODO LUCA: PROBLEMA DB CHECK SE UN UTENTE E' GIA REGISTRATO OPPURE NO
                Log.d("size allUserInfoLiveData", allUserInfoLiveData.value?.size.toString())

            }
        }
            .addOnFailureListener {
                Log.e(
                    "/FirebaseDB",
                    "Errore nella lettura di tutti i document"
                )
            }
        return allUserInfoLiveData
    }




    fun saveNewUser(newUser: Users){

        db.collection("utenti").document(newUser.UID)
            .set(newUser)
            .addOnSuccessListener { Log.d("/HomePageActivity", "DocumentSnapshot successfully written!") }
            .addOnFailureListener {Log.d("/HomePageActivity", "Error writing document") }

    }

    fun getCurrentEmail(): String = user?.email.toString()

    fun getCurrentUid(): String = user?.uid.toString()

    /*
    fun updateSettings(currentUser: Users){

        currentUser.userSettings = UserSettings(libriPrenotati , recensioni)
        val campi = hashMapOf(
            currentUser.email to "email",
            currentUser.UID to "uid" ,
            currentUser.userSettings.libriPrenotati.toString() to "libri prenotati",
            currentUser.userSettings.recensioni.toString() to "recensioni"
        )
        db.collection("utenti").document(currentUser.UID).set(campi)
            .addOnSuccessListener {
                // Operazione completata con successo
            }
            .addOnFailureListener { e ->
                // Gestione dell'errore
            }

    } */
/*
    var userAllLiveData: MutableLiveData<DocumentSnapshot> =  MutableLiveData()
    fun readUserFromDb(uid: String): MutableLiveData<DocumentSnapshot> {

        val docRef = db.collection("utenti").document(uid)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d("/FirebaseDB", "DocumentSnapshot data: ${document.data}")
                    userAllLiveData.value = document

                } else {
                    Log.d("/FirebaseDB", "Documento vuoto")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("/FirebaseDB", "Errore lettura dati !!!")
            }
        return userAllLiveData
    } */


    fun updateBookPrenoted(newUser: Users){
        db.collection("utenti").document("provaUser").delete()
        db.collection("utenti").document(newUser.UID).set(newUser)
       // db.collection("utenti").document(newUser.UID).update("libri prenotati", newUser.userSettings.libriPrenotati)
    }

    fun deleteBookPrenoted(newUser: Users){
        // TODO LUCA: In futuro vedere se si riesce a trovare un metodo per fare direttamente l'update
        db.collection("utenti").document(newUser.UID).delete()
        //db.collection("utenti").add(newUser)
        db.collection("utenti").document(newUser.UID).set(newUser)
    }

    fun addCommentUserSide(newUser: Users){
        db.collection("utenti").document(newUser.UID).delete()
        db.collection("utenti").document(newUser.UID).set(newUser)
    }

    fun removeCommentUserSide(newUser: Users){
        db.collection("utenti").document(newUser.UID).delete()
        db.collection("utenti").document(newUser.UID).set(newUser)
    }

    var bookInfoLiveData: MutableLiveData<DocumentSnapshot> =  MutableLiveData()
    fun getAllBookInfoFromId(idLibro: String): MutableLiveData<DocumentSnapshot> {

        val docRef = db.collection("libri").document("ID_LIBRO")
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d("/FirebaseDB", "DocumentSnapshot data: ${document.data}")
                    bookInfoLiveData.value = document

                } else {
                    Log.d("/FirebaseDB", "Documento vuoto")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("/FirebaseDB", "Errore lettura dati !!!")
            }
        return bookInfoLiveData
    }

    fun getExpirationDate(isbn: String, callback: (String?) -> Unit) {
        firebaseAuth.currentUser?.let { user ->
            db.collection("utenti").document(user.uid).get().addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val libriPrenotati = documentSnapshot.get("userSettings.libriPrenotati") as? Map<String, Any>
                    libriPrenotati?.forEach { (_, libroValue) ->
                        val libro = libroValue as? List<Any>
                        val libroIsbn = libro?.get(0) as? String
                        val dataScadenza = libro?.get(3) as? String
                        if (libroIsbn == isbn) {
                            callback(dataScadenza)
                            return@addOnSuccessListener
                        }
                    }
                }
                // Se non viene trovato un libro con l'ISBN corrispondente, la callback viene chiamata con il valore null
                callback(null)
            }.addOnFailureListener {
                callback(null)
            }
        }
    }
}