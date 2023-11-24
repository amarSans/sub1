package com.example.submisionintermediate.view.main

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.paging.LOGGER
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.submisionintermediate.R
import com.example.submisionintermediate.adapter.StoryAdapter
import com.example.submisionintermediate.databinding.ActivityMainBinding
import com.example.submisionintermediate.view.ViewModelFactory
import com.example.submisionintermediate.view.welcome.WelcomeActivity

class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<MainViewModel> { ViewModelFactory.getInstance(this) }
    private lateinit var binding: ActivityMainBinding
    private lateinit var storyAdapter:StoryAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupObserver()
        setupView()
        setupRecyclerView()
        viewModel.getStories()
    }
    private fun setupObserver(){
        viewModel.storyList.observe(this) { stories ->
            storyAdapter.submitList(stories)
        }

        viewModel.isLoading.observe(this) { isLoading ->
            if (isLoading) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        }

        viewModel.error.observe(this) { error ->
            if (!error.isNullOrBlank()) {

            }
        }

        viewModel.getSession()
        viewModel.userSession.observe(this){user->
            if(user==null||user.isEmpty()){
                startActivity(Intent(this,WelcomeActivity::class.java))
            }
        }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.item_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.action_logout->{
                performLogout()
                true
            }
            else-> super.onOptionsItemSelected(item)
        }
    }
    private fun setupRecyclerView() {
        storyAdapter = StoryAdapter { storyItem ->

        }
        binding.recylerView1.layoutManager = LinearLayoutManager(this)
        binding.recylerView1.adapter = storyAdapter
    }
    private fun performLogout(){
        viewModel.logout()
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
    }
}