package com.example.caesarzonapplication.model.service

import com.example.caesarzonapplication.model.TokenResponse
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

class KeycloakService {

    companion object {
        var myToken: TokenResponse? = null
    }

    fun getAccessToken(username: String, password: String): TokenResponse? {
        val url = URL("http://25.24.244.170:8080/realms/CaesarRealm/protocol/openid-connect/token")
        val connection = url.openConnection() as HttpURLConnection

        connection.requestMethod = "POST"
        connection.doOutput = true
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")

        val postData =
            "username=${URLEncoder.encode(username, "UTF-8")}" +
                    "&password=${URLEncoder.encode(password, "UTF-8")}" +
                    "&grant_type=password" +
                    "&client_id=caesar-app"
        val outputStream = OutputStreamWriter(connection.outputStream)
        outputStream.write(postData)
        outputStream.flush()

        val responseCode = connection.responseCode
        val inputStream: InputStream = if (responseCode == HttpURLConnection.HTTP_OK) {
            connection.inputStream
        } else {
            connection.errorStream
        }

        val reader = BufferedReader(
            InputStreamReader(inputStream)
        )

        val response = StringBuilder()
        var line: String?
        while (reader.readLine().also { line = it } != null) {
            response.append(line)
        }
        reader.close()
        connection.disconnect()
        val gson = Gson()
        myToken = gson.fromJson(response.toString(), TokenResponse::class.java)
        return myToken
    }
}