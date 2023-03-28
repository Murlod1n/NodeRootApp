package com.example.noderootapp.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.noderootapp.databinding.NodeItemBinding
import com.example.noderootapp.model.Node


class NodeAdapter(
    private val nodes: MutableList<Node>,
    val selectChild: (Node) -> Unit,
    val deleteChild: (Node) -> Unit
) : RecyclerView.Adapter<NodeAdapter.NodeViewHolder>() {

    inner class NodeViewHolder(private val binding: NodeItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setData(item: Node) {
            binding.apply {
                childName.text = item.name
                childCard.setOnClickListener { selectChild(item) }
                deleteButton.setOnClickListener { deleteChild(item) }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NodeViewHolder {
        val binding = NodeItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return NodeViewHolder(binding)
    }

    override fun getItemCount() = nodes.size

    override fun onBindViewHolder(
        holder: NodeAdapter.NodeViewHolder,
        position: Int
    ) {
        holder.setData(nodes[position])
    }
}
