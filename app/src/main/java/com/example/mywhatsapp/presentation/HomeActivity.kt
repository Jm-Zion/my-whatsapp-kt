package com.example.mywhatsapp.presentation

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.core.content.ContextCompat
import com.example.mywhatsapp.R
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.mywhatsapp.databinding.ActivityHomeBinding
import com.example.mywhatsapp.presentation.Calls.CallsFragment
import com.example.mywhatsapp.presentation.Chat.ChatFragment
import com.example.mywhatsapp.presentation.Contacts.ContactsFragment
import com.example.mywhatsapp.presentation.Status.StatusFragment
import com.example.mywhatsapp.util.OutlineProvider
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2

    val fragmentList = arrayListOf(
        CallsFragment(),
        ChatFragment(),
        StatusFragment()
    )

    private var requestReadContactsLauncher: ActivityResultLauncher<String?> =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                this?.let {
                    it.supportFragmentManager.beginTransaction()
                        .add(R.id.contacts_fragment, ContactsFragment(), "contacts_fragment")
                        .addToBackStack("contacts_fragment").commit()
                    binding.contactsFragment.visibility = View.VISIBLE
                }
            } else {
                if (shouldShowRequestPermissionRationale(android.Manifest.permission.READ_CONTACTS)) {
                    launchContactsLauncherOnceAgain()
                }
            }
        }

    private fun launchContactsLauncherOnceAgain() {
        this?.let {
            AlertDialog.Builder(it)
                .setTitle("Permission needed")
                .setMessage("This app needs access to your contacts to function properly")
                .setPositiveButton("OK") { _, _ ->
                    requestReadContactsLauncher.launch(android.Manifest.permission.READ_CONTACTS)
                }
                .setNegativeButton("Cancel", null)
                .create()
                .show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        viewPager = binding.viewPager
        binding.fabShowContacts.outlineProvider = OutlineProvider()
        binding.fabShowContacts.clipToOutline = true
        window.statusBarColor = ContextCompat.getColor(this, R.color.Green1)
        tabLayout = binding.tabLayout

        binding.fabShowContacts.setOnClickListener {
            requestReadContactsLauncher.launch(Manifest.permission.READ_CONTACTS    )
        }
        setContentView(binding.root)
        setUpViewPagerTabLayout()
    }

    private fun setUpViewPagerTabLayout(){
        binding.viewPager.adapter = object: FragmentStateAdapter(supportFragmentManager,lifecycle) {
            override fun getItemCount(): Int {
                return fragmentList.size
            }

            override fun createFragment(position: Int): Fragment {
                return fragmentList.get(position)
            }
        }

        TabLayoutMediator(binding.tabLayout, binding.viewPager){ tab, position ->
            when(position){
                0 -> tab.text = "Contacts"
                1 -> tab.text = "Calls"
                2 -> tab.text = "Status"
            }
        }.attach()


        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                tabLayout.selectTab(tabLayout.getTabAt(position))
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_search -> {
                // Handle search action
                true
            }

            R.id.menu_camera -> {
                // Handle camera action
                true
            }

            R.id.create_new_group -> {
                // Handle more item 1 action
                true
            }

            R.id.create_new_broadcast -> {
                // Handle more item 2 action
                true
            }

            R.id.payments -> {
                // Handle more item 2 action
                true
            }

            R.id.settings -> {
                // Handle more item 2 action
                true
            }

            R.id.starred_message -> {
                // Handle more item 2 action
                true
            }

            R.id.linked_devices -> {
                // Handle more item 2 action
                true
            }

            else -> {
                false
            }
        }
    }

}