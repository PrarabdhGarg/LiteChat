package com.example.litechat.contracts


class AllChatsContractFrag {
    interface CFView{
        fun updateRecyclerViewForFirstTime()
        fun updateRecyclerView()
        fun notifyDataSetChangedToAdapter()
    }
}