package com.example.caesarzonapplication.model.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.caesarzonapplication.model.dto.AdminNotificationDTO
import com.example.caesarzonapplication.model.dto.UserNotificationDTO
import com.example.caesarzonapplication.model.service.KeycloakService.Companion.myToken
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import java.net.URL
import java.util.UUID

class NotificationViewModel: ViewModel() {

    /*private val notifyRepository: NotifyRepository = NotifyRepository()
    val adminNotification: StateFlow<List<AdminNotificationDTO>> get() = notifyRepository.notificationAdmin
    val userNotification : StateFlow<List<UserNotificationDTO>> get() = notifyRepository.userNotification

    val client = OkHttpClient()
    val gson = Gson()

    fun deleteNotification(notificationId: UUID, typeUser: Boolean) //true se è user, false se è admin)
    {
        val manageURL = URL("http://25.49.50.144:8090/notify-api/notification?notify-id=$notificationId&isUser=$typeUser")
        viewModelScope.launch(Dispatchers.IO) {
            val request = Request
                .Builder()
                .url(manageURL)
                .delete()
                .addHeader("Authorization", "Bearer ${myToken?.accessToken}")
                .build()
            try{
                val response = client.newCall(request).execute()
                if(response.isSuccessful){
                    if(typeUser) notifyRepository._notificationUser.value = notifyRepository._notificationUser.value.filter { it.id != notificationId }
                    else notifyRepository._notificationAdmin.value = notifyRepository._notificationAdmin.value.filter { it.id != notificationId }
                }
                else{
                    println("Errore nella risposta: "+response.message+"  "+response.code)
                    return@launch
                }
            }catch (e: IOException){
                e.printStackTrace()
            }
        }
    }

    fun exit() {
        TODO("Not yet implemented")
    }*/
}