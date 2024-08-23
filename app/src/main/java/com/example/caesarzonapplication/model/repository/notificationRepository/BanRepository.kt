package com.example.caesarzonapplication.model.repository.notificationRepository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.caesarzonapplication.model.dao.notificationDao.BanDao
import com.example.caesarzonapplication.model.entities.notificationEntity.Ban
class BanRepository(private val banDao: BanDao) {

    suspend fun addBan(ban: Ban): Boolean {
        return try {
            banDao.addBan(ban)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun getAllBans(): LiveData<List<Ban>> {
        return try {
            val bans = banDao.getAllBans()
            bans
        } catch (e: Exception) {
            val list = emptyList<Ban>()
            e.printStackTrace()
            MutableLiveData(emptyList())
        }
    }

    suspend fun deleteBanByUserUsername(username: String): Boolean {
        return try {
            banDao.deleteBanByUserUsername(username)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}

