package com.gervant08.avitotask.model.tools

import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import com.gervant08.avitotask.R

class ElementItemAnimator: DefaultItemAnimator() {
    //animation when adding an element
    override fun animateAdd(holder: RecyclerView.ViewHolder?): Boolean {
        holder?.itemView?.animation = AnimationUtils.loadAnimation(
                holder?.itemView?.context,
                R.anim.viewholder_add_anim
        )
        return super.animateAdd(holder)
    }

    //animation when deleting an element
    override fun animateRemove(holder: RecyclerView.ViewHolder?): Boolean {
        holder?.itemView?.animation = AnimationUtils.loadAnimation(
                holder?.itemView?.context,
                R.anim.viewholder_remove_anim
        )
        return super.animateRemove(holder)
    }

    override fun getAddDuration(): Long {
        return 500
    }
}