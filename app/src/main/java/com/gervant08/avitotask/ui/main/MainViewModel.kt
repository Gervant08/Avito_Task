package com.gervant08.avitotask.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gervant08.avitotask.model.data.Element
import kotlinx.coroutines.*
import kotlin.random.Random

class MainViewModel: ViewModel() {
    private val _mutableElementList = MutableLiveData(defaultElementsList())
    val elementsList: LiveData<ArrayList<Element>> get() = _mutableElementList
    private var coroutineIsRunning = false  // A variable that is used to cancel the launch coroutine, if one is already running
    var previousListSize = 0                // Variable that is needed to compare with the new list
    var newElementIndex = 0                 // Variable used to store the index of the new item that was added to the list
    var deletedElementIndex = 0             // Variable used to store the index of a deleted item that was removed from the list

    object PoolOfDeletedItems{  // Pool Of Deleted Items
        val pool = MutableLiveData<ArrayList<Element>>(arrayListOf())
    }

    fun deleteElement(pool: ArrayList<Element>){
        previousListSize = _mutableElementList.value!!.size                     // Remember the size of the list before deleting the item
        deletedElementIndex = _mutableElementList.value!!.indexOf(pool.last())  // We remember the index of the element we want to delete. The item came last in the list of deleted items

        val newElementList = _mutableElementList.apply {                        // Deleting an element
            this.value?.remove(pool.last())
        }
        _mutableElementList.value = newElementList.value
    }

    fun generateNewElements(){

        if (coroutineIsRunning){ // If one coroutine is already running, then exit the method
            return               // without this condition, a new coroutine will start every time you turn the screen
        }

        viewModelScope.launch {
            while (true) {
                coroutineIsRunning = true
                delay(5_000)
                val element = Element(createNewElementId())  // creating a new element

                val newElementsList = _mutableElementList

                //If the list is empty or there is only one element in it, then just add a new element and remember its index
                if (newElementsList.value?.isEmpty() == true || newElementsList.value?.size == 1) {  //Random.nextInt() it doesn't work with an empty list or dimension = 1( nextInt(0, 0) )
                    previousListSize = newElementsList.value!!.size                                  // Remember the size before adding the element
                    newElementsList.value?.add(element)                                              // Adding an element
                    newElementIndex = newElementsList.value!!.lastIndex                              // Remember its index
                }
                else{
                    val randomIndex = Random.nextInt(0, _mutableElementList.value!!.lastIndex) // A random position is created for the future element
                    previousListSize = newElementsList.value!!.size                                  // Remember the size before adding the element
                    newElementsList.value?.add(randomIndex, element)                                 // Adding an element to a random position
                    newElementIndex = randomIndex                                                    // Remember its index
                }


                _mutableElementList.value = newElementsList.value
            }
        }
    }


    private fun createNewElementId(): Int{
        val id: Int

        if (PoolOfDeletedItems.pool.value!!.isEmpty())                      // If the pool of deleted items is empty, then just increase the item id
            id = _mutableElementList.value?.maxByOrNull { it.id }!!.id + 1
        else{
            id = PoolOfDeletedItems.pool.value!!.first().id                 // Otherwise, we take the id of the first element from the pool

            val newPool = PoolOfDeletedItems.pool.apply {                   // Removing this element from the pool
                this.value?.remove(PoolOfDeletedItems.pool.value?.first())
            }
            PoolOfDeletedItems.pool.value = newPool.value
        }

        return id
    }




    private fun defaultElementsList(): ArrayList<Element> = arrayListOf(
                Element(0),
                Element(1),
                Element(2),
                Element(3),
                Element(4),
                Element(5),
                Element(6),
                Element(7),
                Element(8),
                Element(9),
                Element(10),
                Element(11),
                Element(12),
                Element(13),
                Element(14),
        )

    }

