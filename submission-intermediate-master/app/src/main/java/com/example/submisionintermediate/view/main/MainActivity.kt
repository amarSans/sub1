package com.example.submisionintermediate.view.main

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.submisionintermediate.R
import com.example.submisionintermediate.adapter.StoryAdapter
import com.example.submisionintermediate.data.database.storyItem
import com.example.submisionintermediate.databinding.ActivityMainBinding
import com.example.submisionintermediate.view.ViewModelFactory
import com.example.submisionintermediate.view.addStory.tambahStory
import com.example.submisionintermediate.view.detailActivity.DetailActivity
import com.example.submisionintermediate.view.login.LoginActivity
import com.example.submisionintermediate.view.maps.MapsActivity

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
        binding.imgAdd.setOnClickListener{
            tombolAdd()
        }
    }
    @OptIn(ExperimentalPagingApi::class)
    private fun setupPaging(token: String) {
        viewModel.getPagingStories(token).observe(this) { pagingData ->
            storyAdapter.submitData(lifecycle, pagingData)
        }
    }
    private fun tombolAdd(){
        val intent=Intent(this,tambahStory::class.java)
        startActivity(intent)
    }
    private fun setupObserver(){


        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar2.visibility = if (isLoading == true) View.VISIBLE else View.GONE
        }

        viewModel.error.observe(this) { error ->
            if (!error.isNullOrBlank()) {
            }
        }

        viewModel.getSession()
        viewModel.userSession.observe(this){user->
            if(user.isNullOrEmpty()){
                startActivity(Intent(this,LoginActivity::class.java))
            }else{
                setupPaging(user)
            }
        }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.item_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.action_map->{
                startMapActivity()
                true
            }
            R.id.action_logout->{
                performLogout()
                true
            }

            else-> super.onOptionsItemSelected(item)
        }
    }
    private fun startMapActivity() {
        val intent = Intent(this, MapsActivity::class.java)
        startActivity(intent)
    }
    private fun setupRecyclerView() {
        storyAdapter = StoryAdapter { storyItem ->
            navigateToDetailActivity(storyItem)
        }
        binding.recylerView1.layoutManager = LinearLayoutManager(this)
        binding.recylerView1.adapter = storyAdapter
    }
    private fun navigateToDetailActivity(StoryItem: storyItem) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("thisitem", StoryItem)
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this)
        startActivity(intent, options.toBundle())


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