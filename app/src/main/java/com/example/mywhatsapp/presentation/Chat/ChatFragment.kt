package com.example.mywhatsapp.presentation.Chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mywhatsapp.R
import com.example.mywhatsapp.databinding.FragmentChatBinding
import com.example.mywhatsapp.presentation.IViewStatusHandling
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"



/**
 * A simple [Fragment] subclass.
 * Use the [ChatFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ChatFragment : Fragment(),  IViewStatusHandling{
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var binding : FragmentChatBinding? = null
    private val chatViewModel : ChatViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatBinding.inflate(layoutInflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.let { binding ->
            val chatAdapter = ChatAdapter()
            binding.chatRecycler.layoutManager = LinearLayoutManager(context)
            chatViewModel.fetchAllChats(this)
            binding.chatRecycler.adapter = chatAdapter
            CoroutineScope(Dispatchers.IO).launch {
                chatViewModel.chatList.collectLatest {
                    chatAdapter.submitList(it)
                }
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ChatFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ChatFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun hideProgressBar() {
        binding?.let {
            it.progressBar.visibility = View.GONE
        }
    }

    override fun showError() {
        binding?.let {
            it.progressBar.visibility = View.GONE
        }
    }

    override fun showProgressBar() {
        binding?.let {
            it.progressBar.visibility = View.VISIBLE
        }
    }


}