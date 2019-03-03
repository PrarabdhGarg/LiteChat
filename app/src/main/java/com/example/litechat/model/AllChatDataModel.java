package com.example.litechat.model;

import com.example.litechat.view.adapters.AdapterForFragmentChat;

import java.util.ArrayList;

public class AllChatDataModel {

    public static ArrayList<MessageList> allChatArrayListGroupStatic=new ArrayList<>() ;
    public static ArrayList<MessageList> allChatArrayListN1Static =new ArrayList<>();
    public static ArrayList<MessageList> allChatArrayListN2Static=new ArrayList<>() ;
    public static ArrayList<MessageModel> allChatArrayListPersonalStatic=new ArrayList<>() ;
    public static  String otherUserNumber;
    public static  String currentlyChattingWith;
    public static String documentPathId;
    public static boolean flag=false;
    public static boolean flagPersonalChat=false;
    public static boolean flagOnBackPressed = false;
    public static String userNumberIdPM= " ";//9340243498
    public static  String lastUpdated;
    public static  ArrayList<ChatObject> personalChatList = new ArrayList<>();
    public static ArrayList<String> groupNumbers=new ArrayList<>();
    public static boolean isPresenterCalled = true;
    /**
     * 0 for any other activity than homeActivity
     * 1 for home activity but fragment chat Not open
     * 2 for fragment chat and home activity open
     * otherUserNumber when current user chats with him/her
     *
     * */
    public static  int  chatScreenStatus=2;
    public static  int upadateFragmentChatFirstTime=1;

}

