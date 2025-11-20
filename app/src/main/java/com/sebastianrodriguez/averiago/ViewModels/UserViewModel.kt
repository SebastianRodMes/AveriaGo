package com.sebastianrodriguez.averiago.ViewModels

import Entity.User
import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.LiveData

class UserViewModel : ViewModel() {
    // Datos privados (solo el ViewModel puede modificarlos)
    private val _currentUser = MutableLiveData<User?>()
    val currentUser: LiveData<User?> = _currentUser

    fun setUser(user: User){
        _currentUser.value  = user
    }
    fun clearUser(){
        _currentUser.value  = null
    }

    fun getEmail(): String? = _currentUser.value?.email
    fun getFullName(): String? = _currentUser.value?.fullName

}