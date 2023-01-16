package com.android.zchat.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.zchat.Adapters.MessageAdapter
import com.android.zchat.R
import com.android.zchat.databinding.ActivityChatBinding
import com.android.zchat.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase

class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding
    lateinit var msgAdapter : MessageAdapter
    lateinit var messageList : ArrayList<com.android.zchat.Models.Message>

    private var receiverRoom: String? = null
    private var senderRoom: String? = null

    private lateinit var mDbRef : DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val name = intent.getStringExtra("name")
        val receiverUid = intent.getStringExtra("uid")
        val senderUid = Firebase.auth.currentUser?.uid

        mDbRef = FirebaseDatabase.getInstance().getReference()

        // make unique room for sender and receiver
        senderRoom = receiverUid + senderUid
        receiverRoom = senderUid + receiverUid

        supportActionBar?.title = name

        messageList = ArrayList()
        msgAdapter = MessageAdapter(this, messageList)
        binding.chatRecycler.layoutManager = LinearLayoutManager(this)
        binding.chatRecycler.adapter = msgAdapter

        // add data to recyclerView
        mDbRef.child("chats").child(senderRoom!!).child("messages")
            .addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    messageList.clear()
                    for (postSnapshot in snapshot.children){
                        val message = postSnapshot.getValue(com.android.zchat.Models.Message::class.java)
                        messageList.add(message!!)
                    }
                    msgAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })

        // add message to database
        binding.sendBtn.setOnClickListener{
            val message = binding.msgBox.text.toString()
            val messageObject = com.android.zchat.Models.Message(message, senderUid!!)

            // update sender room and receiver room
            mDbRef.child("chats").child(senderRoom!!).child("messages").push()
                .setValue(messageObject).addOnSuccessListener {
                    mDbRef.child("chats").child(receiverRoom!!).child("messages").push()
                        .setValue(messageObject)
                }
            binding.msgBox.setText("")
        }

    }
}