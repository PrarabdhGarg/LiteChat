package com.example.litechat.presenter

import android.content.Context
import android.provider.ContactsContract
import android.util.Log
import android.widget.Toast
import com.example.litechat.contracts.HomeActivityContract
import com.example.litechat.model.ContactDataModel
import com.example.litechat.model.DataRetriveClass
import com.example.litechat.model.UserProfileData
import com.example.litechat.model.contactsRoom.User
import com.google.firebase.firestore.FirebaseFirestore

class HomeActivityPresenter(viewPassed: HomeActivityContract.View, modelPassed: HomeActivityContract.Model): HomeActivityContract.Presenter{

    private val view = viewPassed
    private val model = modelPassed

    override fun getUserDataOnLogin(number: String) {

        DataRetriveClass().getUserDataFromFirestore(number)
        Toast.makeText(view.passContext() , UserProfileData.UserNumber , Toast.LENGTH_SHORT).show()
    }

    override fun getContacts() {

        val contentResolver = view.passContentResolver()
        val cursor1 = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null)
        if (cursor1?.count ?: 0 > 0){

            while (cursor1 != null && cursor1.moveToNext()) {

                val id = cursor1.getString(cursor1.getColumnIndex(ContactsContract.Contacts._ID))
                val name = cursor1.getString(cursor1.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))

                if (cursor1.getInt(cursor1.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {

                    val cursor2 = contentResolver.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                        arrayOf(id),null)

                    while (cursor2!!.moveToNext()){

                        val number = cursor2.getString(cursor2.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                        val userItem = User(number, name)
                        ContactDataModel.contactList.add(userItem)

                    }
                    cursor2.close()
                }
            }
        }
        cursor1?.close()
        getUsers()

    }

    override fun passUserList(): List<User> {
        return model.roomGetData(view.passContext())
    }

    override fun getUsers() {

        val database = FirebaseFirestore.getInstance()
        database.collection("Users").get().addOnSuccessListener { result ->

            for ( document in result){

                ContactDataModel.usersList.add(document.id)

            }

            compareUserContact()
            model.roomSetData(view.passContext(), ContactDataModel.contactAndUser)

        }
    }


    private fun compareUserContact() {

        for (user in ContactDataModel.usersList){

            for (contact in ContactDataModel.contactList){

                if (contact.mobileNumber == user ||  contact.mobileNumber == "0$user" || contact.mobileNumber == "+91 $user" ){
                    ContactDataModel.contactAndUser.add(contact)
                    Log.d("contact", contact.mobileNumber + contact.name)
                    break
                }
            }
        }
    }

}