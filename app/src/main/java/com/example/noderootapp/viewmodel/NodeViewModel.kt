package com.example.noderootapp.viewmodel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.noderootapp.model.Node


class NodeViewModel : ViewModel() {

    private var _rootNode = MutableLiveData(Node("root"))
    val rootNode: LiveData<Node> = _rootNode

    fun addChild() {
        val copyNode = _rootNode
        copyNode.value!!.addChild()
        _rootNode.value = copyNode.value
    }

    fun deleteChildNode(node: Node) {
        val copyNode = _rootNode
        copyNode.value!!.deleteChild(node)
        _rootNode.value = copyNode.value
    }

    fun deleteCurrentNode() {
        val currentNode = _rootNode.value
        backToParent()
        deleteChildNode(currentNode!!)
    }

    fun selectNode(node: Node) {
        _rootNode.value = node
    }

    fun backToParent() {
        val parent = _rootNode.value?.parent
        _rootNode.value = parent
    }

}

