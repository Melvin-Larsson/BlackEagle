package com.inglarna.blackeagle.ui.folder

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.inglarna.blackeagle.ui.SingleFragmentActivity

class FolderActivity : SingleFragmentActivity(){

    private var folderId: Long = -1

    companion object{
        private const val FOLDER_ID = "folderId"
        fun newIntent(context: Context, folderId: Long): Intent {
            val intent = Intent(context, FolderActivity::class.java)
            intent.putExtra(FOLDER_ID, folderId)
            return intent
        }
    }

    override fun createFragment(): Fragment = FolderFragment.newInstance(folderId)

    override fun onCreate(savedInstanceState: Bundle?) {
        folderId = intent.getLongExtra(FOLDER_ID, -1)
        super.onCreate(savedInstanceState)
    }
}