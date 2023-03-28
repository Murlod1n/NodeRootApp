package com.example.noderootapp.model

import java.io.Serializable
import java.security.MessageDigest
import java.util.*

data class Node(
    var name: String,
    var children: MutableList<Node> = mutableListOf(),
    var parent: Node? = null
): Serializable {

    fun addChild() {
        children.add(Node(name = setHashName(), parent = this))
    }

    fun deleteChild(child: Node) {
        children.remove(child)
    }

    private fun setHashName(): String {
        val id = UUID.randomUUID().toString()
        val digest = MessageDigest.getInstance("SHA-256")
        val hash = digest.digest(id.toByteArray())

        return hash.takeLast(20).joinToString("") { "%02x".format(it) }
    }
}
