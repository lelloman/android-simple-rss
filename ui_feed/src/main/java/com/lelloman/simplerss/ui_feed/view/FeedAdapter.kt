package com.lelloman.simplerss.ui_feed.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.lelloman.simplerss.ui_feed.databinding.ListItemFeedBinding
import com.lelloman.simplerss.ui_feed.viewmodel.FeedViewModel

class FeedAdapter(private val onItemClickListener: (FeedViewModel.FeedListItem) -> Unit) :
    RecyclerView.Adapter<FeedAdapter.ViewHolder>() {

    private val differCallback = object : DiffUtil.ItemCallback<FeedViewModel.FeedListItem>() {
        override fun areItemsTheSame(
            oldItem: FeedViewModel.FeedListItem,
            newItem: FeedViewModel.FeedListItem
        ) = oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: FeedViewModel.FeedListItem,
            newItem: FeedViewModel.FeedListItem
        ) = oldItem == newItem
    }
    private val differ = AsyncListDiffer(this, differCallback)

    fun onNewList(list: List<FeedViewModel.FeedListItem>) = differ.submitList(list)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = LayoutInflater.from(parent.context)
        .let { ListItemFeedBinding.inflate(it, parent, false) }
        .let(::ViewHolder)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(differ.currentList[position])

    override fun getItemCount() = differ.currentList.size

    inner class ViewHolder(private val binding: ListItemFeedBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: FeedViewModel.FeedListItem) {
            with(binding) {
                root.setOnClickListener { onItemClickListener(item) }

                // image.loadImageUrl(item.imageUrl)
                image.visibility = if (item.imageUrl.isNullOrBlank()) View.GONE else View.VISIBLE

                textViewTitle.text = item.title

                // imageViewFavicon.loadFavicon(item.faviconId)
                imageViewFavicon.visibility = if (item.faviconId == null) View.GONE else View.VISIBLE

                textViewDetails.text = "{semanticTimeProvider.getDateTimeString(item.time)} - ${item.sourceName}"

                textViewSubtitle.visibility = if (item.subtitle.isBlank()) View.GONE else View.VISIBLE
                textViewSubtitle.text = item.subtitle
            }
        }
    }
}