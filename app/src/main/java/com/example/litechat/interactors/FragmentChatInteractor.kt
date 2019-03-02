package com.example.litechat.interactors

import android.util.Log
import com.example.litechat.contracts.AllChatsContractFrag
import com.example.litechat.model.*
import com.google.firebase.firestore.FirebaseFirestore

import com.example.litechat.model.AllChatDataModel.allChatArrayListGroupStatic
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.QuerySnapshot
import java.time.Instant


class FragmentChatInteractor(p1: AllChatsContractFrag.CFPresenter) : AllChatsContractFrag.CFInteractor {
    private var database: FirebaseFirestore? = null
    private lateinit var p2: AllChatsContractFrag.CFPresenter

    init {
        p2 = p1
    }
    private var allChatArrayListN1 =ArrayList<MessageList>()
    private var allChatArrayListN2 =ArrayList<MessageList>()
    private var messagesOfOnePersonN1 = ArrayList<MessageModel>()
    private var messagesOfOnePersonN2 = ArrayList<MessageModel>()
    private var messagesOfOneGroup = ArrayList<MessageModel>()

    override fun getPersonalChats() {
        Log.d("Run2","getPersonalChts")

        database = FirebaseFirestore.getInstance()

        var currentPersonalChats=ArrayList<String>()
        // get List of all current chats
        database!!.collection("Users").whereEqualTo("number","9826936889").get()
            .addOnSuccessListener { documents ->


                for (doc in documents) {

                    currentPersonalChats = doc["currentChats"] as ArrayList<String>
                    Log.d("chatName", currentPersonalChats.toString())
                }
               //p2.personalChatsDataRecieved(currentPersonalChats)
            }
// get all chats of cutrrent logged in user
        database!!.collection("Chats").whereEqualTo(/*"number1"*/"number1", "9826936889").get().addOnSuccessListener { documents ->

            for (doc in documents) {
                Log.d("Query1", doc.data.toString())

                var x = doc["number2"].toString()
                Log.d("Query2", x)

                doc.reference.collection("messages").orderBy("sentOn").get().addOnSuccessListener { messages ->

                    for (mess in messages) {
                        var obj = mess.toObject(MessageModel::class.java)
                        messagesOfOnePersonN1.add(obj)

                        Log.d("QueryMessages", obj.message.toString() + " \n " + obj.sentOn.toString() + obj.sentBy)
                    }
                    var obj2 = MessageList()
                    obj2!!.otherPerson = x
                    obj2!!.allMessages = messagesOfOnePersonN1

                    AllChatDataModel.allChatArrayListN1Static.add(obj2)
                    Log.d("QueryPrr",  AllChatDataModel.allChatArrayListN1Static.size.toString())

                    Log.d("QueryCalledd",currentPersonalChats.size.toString() +"\n"+allChatArrayListN1.size.toString())

                    //bug luck dependent h yeh to
                     p2.personalChatsDataRecieved(currentPersonalChats)

                    // yha add krwana padega
                }



            }

            Log.d("QueryIII", AllChatDataModel.allChatArrayListN1Static.size.toString())



        }
        /////////////////

       /* arrayOf<Map<String, Any>>().forEach { map ->
            val phoneNo = map["phoneNo"] as String
            val chatId = map["chatId"] as String


        }
*/
        /*database!!.collection("Chats").whereEqualTo("number2", "9826936889").get().addOnSuccessListener { documents ->

                    for (doc in documents) {
                        Log.d("Query6", doc.data.toString())

                        var x = doc["number2"].toString()
                        Log.d("Query7", x)

                doc.reference.collection("messages").orderBy("sentOn").get().addOnSuccessListener { messages ->

                    for (mess in messages) {
                        var obj = mess.toObject(MessageModel::class.java)
                        messagesOfOnePersonN2.add(obj)

                        Log.d("QueryMessages", obj.message.toString() + " \n " + obj.sentOn.toString() + obj.sentBy)
                    }
                    var obj2 = MessageList()
                    obj2!!.otherPerson = x

                    obj2!!.allMessages = messagesOfOnePersonN2
                    Log.d("Query8", messagesOfOnePersonN2[0].message.toString())
                    Log.d("Query9", obj2!!.otherPerson.toString() + "\n" + obj2!!.allMessages.toString())
                    allChatArrayListN2.add(obj2)
                    Log.d("Query10", allChatArrayListN2.size.toString())
                }
            }
            AllChatDataModel.allChatArrayListN2Static=allChatArrayListN2
            p2.personalChatsDataRecievedN2(allChatArrayListN2)
        }
*/

    }
    override fun getGroupChats() {


      /*  var currentGroupChats=ArrayList<String>()

         database!!.collection("Users").whereEqualTo("number","9826936889").get()
             .addOnSuccessListener { documents ->


                 for (doc in documents) {

                     currentGroupChats = doc["currentGroupChats"] as ArrayList<String>
                     Log.d("groupName", currentGroupChats.toString())


                         database!!.collection("Chats").whereEqualTo("name", "GroupName").get()
                             .addOnSuccessListener { documents ->

                                 for (doc in documents) {
                                     Log.d("QueryG6", doc.data.toString())

                                     var x = doc["name"].toString()
                                     Log.d("QueryG7", x)

                                     doc.reference.collection("messages").orderBy("sentOn").get()
                                         .addOnSuccessListener { messages ->

                                             for (mess in messages) {
                                                 var obj = mess.toObject(MessageModel::class.java)

                                                 messagesOfOneGroup.add(obj)

                                                 Log.d(
                                                     "QueryGMessages",
                                                     obj.message.toString() + " \n " + obj.sentOn.toString() + obj.sentBy
                                                 )
                                             }
                                             var obj2 = MessageList()
                                             obj2!!.otherPerson = x // GroupName

                                             obj2!!.allMessages = messagesOfOneGroup
                                             Log.d("QueryG8", messagesOfOneGroup[0].message.toString())
                                             Log.d(
                                                 "QueryG9",
                                                 obj2!!.otherPerson.toString() + "\n" + obj2!!.allMessages.toString()
                                             )
                                             AllChatDataModel.allChatArrayListGroupStatic.add(obj2)
                                         }
                                 }
                             }

                     }
                     p2.groupChatsDataRecieved(currentGroupChats)
                 }

*/

    }

}