package com.example.litechat.view.fragments

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_status.*
import android.content.Intent
import android.graphics.Color
import android.graphics.Rect
import android.icu.lang.UCharacter.GraphemeClusterBreak.V
import android.net.Uri
import android.preference.PreferenceManager
import android.text.Editable
import android.text.style.LineHeightSpan
import android.util.Log
import android.view.WindowManager
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.litechat.R
import com.example.litechat.contracts.StatusContract
import com.example.litechat.listeners.BoomListener
import com.example.litechat.model.AllChatDataModel
import com.example.litechat.model.ContactListModel
import com.example.litechat.model.UserDataModel
import com.example.litechat.model.UserProfileData
import com.example.litechat.presenter.StatusFragmentPresenter
import com.example.litechat.view.activities.*
import com.example.litechat.view.adapters.StatusAdapter
import com.google.firebase.auth.FirebaseAuth
import com.nightonke.boommenu.BoomButtons.HamButton
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.fragment_status.view.*
import java.lang.Exception


class FragmentStatus: Fragment() , StatusContract.View{

    var stausFragmentPresenter : StatusFragmentPresenter? = null
    val REQUEST_IMAGE_GET = 1   //Request code for getting image from the gallery is 1
    var maps : ArrayList<Pair<String, String>> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d("ViewPager" , "onCreateView of FragmentStatus called")
        val view = inflater.inflate(com.example.litechat.R.layout.fragment_status, container, false)
        stausFragmentPresenter = StatusFragmentPresenter(this)

        val bmbListener1 = BoomListener()
        bmbListener1.setCustomObjectListener(object: BoomListener.Boom{
            override fun doThis() {
                //AllChatDataModel.upadateFragmentChatFirstTime = 1
                startActivity(Intent(activity, ProfileActivity::class.java))
            }

        })

        val bmbListener2 = BoomListener()
        bmbListener2.setCustomObjectListener(object: BoomListener.Boom{
            override fun doThis() {
                //AllChatDataModel.upadateFragmentChatFirstTime = 1
                startActivity(Intent(activity, NewGroupChatActivity::class.java))
            }

        })

        val bmbListener3 = BoomListener()
        bmbListener3.setCustomObjectListener(object: BoomListener.Boom{
            override fun doThis() {
                //AllChatDataModel.upadateFragmentChatFirstTime = 1
                startActivity(Intent(activity, DeveloperActivity::class.java))
            }

        })

        val bmbListener4 = BoomListener()
        bmbListener4.setCustomObjectListener(object: BoomListener.Boom{
            override fun doThis() {
                //AllChatDataModel.upadateFragmentChatFirstTime = 1
                FirebaseAuth.getInstance().signOut()
                PreferenceManager.getDefaultSharedPreferences(context).edit().putString("CurrentUserNumber" , "").apply()
                UserProfileData.clearData()
                startActivity(Intent(activity , LoginActivity::class.java))
            }

        })

        val icons = arrayOf(R.drawable.ic_profile, R.drawable.ic_group, R.drawable.ic_dev, R.drawable.ic_logout)
        val text = arrayOf("PROFILE", "NEW GROUP", "DEVELOPERS", "LOGOUT")
        val bmbListener = arrayOf(bmbListener1, bmbListener2, bmbListener3, bmbListener4)
        view.bmbStatus.bringToFront()

        for(i in 0 until view.bmbStatus.buttonPlaceEnum.buttonNumber()){

            val builder: HamButton.Builder = HamButton.Builder()
                .normalImageRes(icons[i])
                .normalText(text[i])
                .pieceColor(Color.WHITE)
                .shadowEffect(true)
                .rippleEffect(true)
                .textSize(24)
                .textPadding(Rect(40,0,0,0))
                .imagePadding(Rect(20,0,20,0))
                .listener { index ->
                    bmbListener[index].listener!!.doThis()
                }

            view.bmbStatus.addBuilder(builder)
        }

        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("ViewPager" , "onCreate of FragmentStatus called")
        try {
            view!!.RecyclerStatus.adapter!!.notifyDataSetChanged()
        }catch (e : Exception){

        }
        super.onCreate(savedInstanceState)
    }


    override fun onStart() {
        super.onStart()
        Log.d("ViewPager" , "onStart of FragmentStatus Called")
        //Make and Call a function to get and display data
        view!!.RecyclerStatus.adapter = StatusAdapter(context!! , maps)
        Glide.with(context!!).load(UserProfileData.UserImage).into(view!!.statusImageView).onLoadFailed(context!!.getDrawable(R.drawable.profile))//On starting the fragment, load the current image in the image view, whose Uri is stored locally
        view!!.currentActivityTextView.setText(UserProfileData.UserCurrentActivity)

        stausFragmentPresenter!!.getInfoForRecyclerView()

        view!!.EditStatusImageView!!.setOnClickListener {
            Toast.makeText(context , "Clicked" , Toast.LENGTH_SHORT).show()
            view!!.EditStatusDoneImageView!!.visibility = View.VISIBLE
            view!!.EditStatusImageView!!.visibility = View.INVISIBLE
            view!!.currentActivityTextView.inputType = InputType.TYPE_TEXT_FLAG_MULTI_LINE
        }

        view!!.EditStatusDoneImageView!!.setOnClickListener {
            view!!.EditStatusDoneImageView!!.visibility = View.INVISIBLE
            view!!.EditStatusImageView!!.visibility = View.VISIBLE
            view!!.currentActivityTextView.inputType = InputType.TYPE_NULL
            //Think of some method to prevent user input while data is getting updated on fireStore
            stausFragmentPresenter!!.updateUserInfo(activity = view!!.currentActivityTextView.text.toString())
        }

        changeStatusImageButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                type = "image/*"
            }
            activity!!.startActivityForResult(intent, REQUEST_IMAGE_GET)
        }
    }

    override fun onNewStatusImageSelected(reference : Uri)
    {
        Toast.makeText(context , "Please wait while we update the photo" , Toast.LENGTH_SHORT).show()
        stausFragmentPresenter!!.updateStatusImage(reference)
    }

    override fun onNewDataRecivedForRecyclerView(maps1 : ArrayList<Pair<String, String>>)
    {
        maps.clear()
        maps.addAll(maps1)
        try {
            view!!.RecyclerStatus.adapter!!.notifyDataSetChanged()
        }catch (e : Exception){

        }
    }

    override fun getCurrentContext(): Context {
        return view!!.context
    }

    override fun setStatusImageView(path: String) {
        view!!.statusImageView.setImageURI(Uri.parse(path))
        view!!.statusLoader.visibility = View.INVISIBLE
    }

}