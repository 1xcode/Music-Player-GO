package com.iven.musicplayergo.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.afollestad.recyclical.datasource.dataSourceOf
import com.afollestad.recyclical.setup
import com.afollestad.recyclical.withItem
import com.iven.musicplayergo.R
import com.iven.musicplayergo.musicLibrary
import com.iven.musicplayergo.ui.GenericViewHolder
import com.iven.musicplayergo.ui.SongsSheetInterface
import kotlinx.android.synthetic.main.fragment_folders.*
import kotlinx.android.synthetic.main.recycler_view_item.*
import java.io.File

/**
 * A simple [Fragment] subclass.
 * Use the [FoldersFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FoldersFragment : Fragment() {

    private lateinit var mSongsSheetInterface: SongsSheetInterface

    private var mSelectedFolder = ""

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mSongsSheetInterface = activity as SongsSheetInterface
        } catch (e: ClassCastException) {
            throw ClassCastException(activity.toString() + " must implement MyInterface ")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_folders, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (context != null) {

            val folders = musicLibrary.allCategorizedMusicByFolder.keys.toMutableList()
            val dataSource = dataSourceOf(folders)

            // setup{} is an extension method on RecyclerView
            folders_rv.setup {
                withDataSource(dataSource)
                withItem<String, GenericViewHolder>(R.layout.recycler_view_item) {
                    onBind(::GenericViewHolder) { _, item ->

                        // GenericViewHolder is `this` here
                        title.text = item
                        title.isSelected = true

                        //getting parent path of the first song
                        val songRootPath =
                            musicLibrary.allCategorizedMusicByFolder.getValue(item)[0].path
                        val parentPath = File(songRootPath!!).parentFile?.parent

                        subtitle.text = getString(R.string.in_directory, parentPath)
                        subtitle.isSelected = true
                    }

                    onClick {
                        // item is a `val` in `this` here
                        if (::mSongsSheetInterface.isInitialized) {

                            if (mSelectedFolder != item) {
                                mSongsSheetInterface.onPopulateAndShowSheet(
                                    true,
                                    item,
                                    subtitle.text.toString(),
                                    musicLibrary.allCategorizedMusicByFolder.getValue(item)
                                )
                            } else {
                                mSongsSheetInterface.onShowSheet()
                            }
                        }
                    }

                    onLongClick { index ->
                        // item is a `val` in `this` here
                        Log.d("doSomething", "Clicked $index: ${item}")
                    }
                }
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment NowPlaying.
         */
        @JvmStatic
        fun newInstance() = FoldersFragment()
    }
}
