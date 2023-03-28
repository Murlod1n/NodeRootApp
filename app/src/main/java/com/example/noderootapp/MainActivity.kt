package com.example.noderootapp

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.noderootapp.adapter.NodeAdapter
import com.example.noderootapp.databinding.ActivityMainBinding
import com.example.noderootapp.model.Node
import com.example.noderootapp.viewmodel.NodeViewModel
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

class MainActivity : AppCompatActivity() {
    private val nodeViewModel: NodeViewModel by viewModels()
    private lateinit var nodeAdapter: NodeAdapter
    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPreferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (nodeViewModel.rootNode.value!!.parent == null) {
                    finishAffinity()
                } else {
                    nodeViewModel.backToParent()
                }
            }
        })

        loadNodeFromStorage()
        initScreen()
        setNodeObserve()
        setContentView(binding.root)
    }


    override fun onStop() {
        super.onStop()
        val editor = sharedPreferences.edit()

        val byteArrayOutputStream = ByteArrayOutputStream()
        val objectOutputStream = ObjectOutputStream(byteArrayOutputStream)
        objectOutputStream.writeObject(nodeViewModel.rootNode.value)
        val nodeAsByteArray = byteArrayOutputStream.toByteArray()

        editor.putString("NODE", Base64.encodeToString(nodeAsByteArray, Base64.DEFAULT))
        editor.apply()
    }


    private fun setNodeObserve() {
        nodeViewModel.rootNode.observe(this) {
            nodeAdapter = NodeAdapter(
                nodeViewModel.rootNode.value!!.children,
                { nodeViewModel.selectNode(it) },
                { nodeViewModel.deleteChildNode(it) }
            )
            binding.apply {
                recyclerView.adapter = nodeAdapter
                nameTextView.text = nodeViewModel.rootNode.value!!.name
                if (nodeViewModel.rootNode.value!!.parent != null) {
                    deleteButton.setOnClickListener {
                        nodeViewModel.deleteCurrentNode()
                    }
                    deleteButton.visibility = View.VISIBLE
                } else {
                    deleteButton.visibility = View.GONE
                }
            }
        }
    }


    private fun initScreen() {
        nodeAdapter = NodeAdapter(
            nodeViewModel.rootNode.value!!.children,
            { nodeViewModel.selectNode(it) },
            { nodeViewModel.deleteChildNode(it) }
        )
        binding.apply {
            recyclerView.adapter = nodeAdapter
            recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
            addChildButton.setOnClickListener {
                nodeViewModel.addChild()
            }
            if (nodeViewModel.rootNode.value!!.parent != null) {
                deleteButton.setOnClickListener {
                    nodeViewModel.deleteCurrentNode()
                }
                deleteButton.visibility = View.VISIBLE
            } else {
                deleteButton.visibility = View.GONE
            }
            nameTextView.text = nodeViewModel.rootNode.value!!.name
        }
    }


    private fun loadNodeFromStorage() {
        sharedPreferences = this.getSharedPreferences("nodes", Context.MODE_PRIVATE)
        val nodeAsBase64 = sharedPreferences.getString("NODE", null)

        if(nodeAsBase64 != null) {
            val nodeAsByteArray = Base64.decode(nodeAsBase64, Base64.DEFAULT)
            val byteArrayInputStream = ByteArrayInputStream(nodeAsByteArray)
            val objectInputStream = ObjectInputStream(byteArrayInputStream)
            val node = objectInputStream.readObject() as? Node
            if (node != null) nodeViewModel.selectNode(node)
        }
    }
}
