package com.example.mywhatsapp.presentation.Contacts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.mywhatsapp.R
import com.example.mywhatsapp.databinding.FragmentContactsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * A simple [Fragment] subclass.
 * Use the [Contacts.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class ContactsFragment : Fragment(), IContactsView {

    private var binding : FragmentContactsBinding? = null
    private var deviceContacts: MutableList<String> = arrayListOf()
    private val contactsViewModel: ContactsViewModel by viewModels()

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentContactsBinding.inflate(inflater, container, false)
        deviceContacts = ContactsManager(requireContext()).getContacts()
        deviceContacts.map {
            it.replace("+91","").replace(" ","")
        }

        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.let {
            if(deviceContacts.isNotEmpty()){
                val newList = ArrayList<String>()
                newList.add("0000000")
                newList.add("1234567890")
                newList.add("0987654321")
                newList.add("5674890212")
                newList.add("0000000")
                newList.add("0000000")
                newList.add("0000000")
                newList.add("0000000")
                newList.add("0000000")
                var contactsAdapter = ContactsAdapter()
                binding!!.recyclerContacts.adapter = contactsAdapter
                contactsViewModel.getAllWhatsAppContacts(newList, this@ContactsFragment)
                CoroutineScope(Dispatchers.IO).launch {
                    contactsViewModel.whatsAppContactsList.collectLatest {
                        withContext(Dispatchers.Main) {
                            if (it.isNotEmpty()) {
                                contactsAdapter.submitList(it)
                                binding!!.subtitleText.text = "${contactsAdapter.itemCount}"
                            }
                        }
                    }
                }

            }
        }
    }

    override fun showError() {
        binding?.let {
            it.progressBar.visibility = View.GONE
        }
        Toast.makeText(context, "Cannot load contacts", Toast.LENGTH_LONG).show()
    }

    override fun showProgressBar() {
        binding?.let {
            it.progressBar.visibility = View.VISIBLE
        }
        Toast.makeText(context, "Cannot load contacts", Toast.LENGTH_LONG).show()
    }

    override fun hideProgressBar() {
        binding?.let {
            it.progressBar.visibility = View.GONE
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Contacts.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            ContactsFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}