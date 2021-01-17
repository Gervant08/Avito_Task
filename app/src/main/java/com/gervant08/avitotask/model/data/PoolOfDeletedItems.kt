package com.gervant08.avitotask.model.data

import androidx.lifecycle.MutableLiveData

object PoolOfDeletedItems {
    val pool = MutableLiveData<ArrayList<Element>>(arrayListOf())
}