package com.androiddevs.mvvmnewsapp.ui.util

//Sealed = no one can inherit from this Resource class excepy on 3 classes that are allowed
sealed class Resource<T>(val data :T? =null, val  messege:String? =null) {
    class Success<T>(data: T):Resource<T>(data)
    class Error<T>(messege: String, data: T?=null):Resource<T>(data, messege)
    class Loading<T> :Resource<T>()


}