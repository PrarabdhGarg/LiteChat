package com.example.litechat.model;

/**
 * This is a static class that stores all the information related to the current user so that all classes can
 * access the data whenever required without having to wait to download data from the database again and again
 * This class is filled with data as soon as a signied in user opens the app again in the onCreate method of the Home Activity
 * For a new user who signs up, this class gets filled as soon as his number gets verified
 * For an existing user who is signing in, this class gets filled after verification of the phone number as well
 */

public class UserProfileData {
    public static String UserName = " ";
    public static String UserImage = "https://firebasestorage.googleapis.com/v0/b/litechat-3960c.appspot.com/o/images.png?alt=media&token=d73dedf8-4abb-4bf0-bc65-f980f0bf6f7a";
    public static String UserAbout = " ";
    public static String UserNumber = " ";
    public static String UserCurrentActivity = " ";
    public static String UserProfileImage = "https://firebasestorage.googleapis.com/v0/b/litechat-3960c.appspot.com/o/images.png?alt=media&token=d73dedf8-4abb-4bf0-bc65-f980f0bf6f7a";
    public static String UserToken = "";

    /**
     *This method is used to clear all the data that this class is currently holding
     * This is called when a user clicks on the logout button
     * This method is important because if the user does not close the app after logging out, and logs in with a different account
     * then the data of the previous account would be visible until new data has been downloaded from the database. To avoid
     * this situation, this method is called as soon as the user clicks on the logout button
     */

    public static void clearData()
    {
        UserName = " ";
        UserImage = "https://firebasestorage.googleapis.com/v0/b/litechat-3960c.appspot.com/o/images.png?alt=media&token=d73dedf8-4abb-4bf0-bc65-f980f0bf6f7a";
        UserAbout = " ";
        UserNumber = " ";
        UserCurrentActivity = " ";
        UserProfileImage = "https://firebasestorage.googleapis.com/v0/b/litechat-3960c.appspot.com/o/images.png?alt=media&token=d73dedf8-4abb-4bf0-bc65-f980f0bf6f7a";
        UserToken = "";
    }

}

