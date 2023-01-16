package com.android.zchat.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.zchat.Models.Message
import com.android.zchat.R
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

class MessageAdapter(val context: Context, val msgList: ArrayList<Message>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val ITEM_RECEIVE = 1
    private val ITEM_SEND = 2

    class SendViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val sendMessage = itemView.findViewById<TextView>(R.id.send_msg)
    }

    class ReceiveViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val receiveMessage = itemView.findViewById<TextView>(R.id.receive_msg)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == 1){
            val view: View =  LayoutInflater.from(context).inflate(R.layout.receive_msg_layout, parent, false)
            return ReceiveViewHolder(view)
        }
        else{
            val view: View =  LayoutInflater.from(context).inflate(R.layout.send_msg_layout, parent, false)
            return SendViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentMsg = msgList[position]
        if (holder.javaClass == SendViewHolder :: class.java){
            // do for sendViewHolder
            val viewHolder = holder as SendViewHolder
            holder.sendMessage.text = currentMsg.message
        }
        else if (holder.javaClass == ReceiveViewHolder :: class.java){
            // do for receiveViewHolder
            val viewHolder = holder as ReceiveViewHolder
            holder.receiveMessage.text = currentMsg.message
        }
    }

    override fun getItemViewType(position: Int): Int {
        val currentMessage = msgList[position]
        if (FirebaseAuth.getInstance().currentUser?.uid.equals(currentMessage.senderId)){
            return ITEM_SEND
        }
        else{
            return ITEM_RECEIVE
        }

    }

    override fun getItemCount(): Int {
        return msgList.size
    }

}