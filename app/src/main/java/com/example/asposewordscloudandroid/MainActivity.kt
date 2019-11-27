/*
 * --------------------------------------------------------------------------------
 * <copyright company="Aspose">
 *   Copyright (c) 2019 Aspose.Words for Cloud
 * </copyright>
 * <summary>
 *   Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 * </summary>
 * --------------------------------------------------------------------------------
 */
package com.example.asposewordscloudandroid

import android.Manifest
import android.app.Activity
import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.UserDictionary
import android.view.View
import android.widget.Toast
import com.aspose.words.cloud.ApiCallback
import com.aspose.words.cloud.ApiClient
import com.aspose.words.cloud.ApiException
import com.aspose.words.cloud.api.WordsApi
import com.aspose.words.cloud.model.ClassificationResponse
import com.aspose.words.cloud.model.requests.ClassifyRequest
import com.aspose.words.cloud.model.requests.ConvertDocumentRequest
import com.google.gson.JsonObject
import org.json.JSONObject
import java.lang.Exception
import java.nio.file.Path
import androidx.core.app.ActivityCompat.startActivityForResult
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.net.Uri
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.provider.OpenableColumns
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.*


class MainActivity : AppCompatActivity() {
    companion object {
        const val FILE_SELECT_CODE = 0
        const val REQUEST_WRITE_STORAGE = 112
    }

    var api : WordsApi? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestPermission(this)
        setContentView(R.layout.activity_main)
    }

    private fun showToast(text: String, time: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(this, text, time).show()
    }
    private fun requestPermission(context: Activity) {
        val hasPermission = (ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED);
        if (!hasPermission) {
            ActivityCompat.requestPermissions(
                context,
                Array(1, { Manifest.permission.WRITE_EXTERNAL_STORAGE }),
                REQUEST_WRITE_STORAGE
            );
        }
    }

    private fun pickFile() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"
        startActivityForResult(intent, FILE_SELECT_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        var file: File? = null
        try {
            when (requestCode) {
                FILE_SELECT_CODE -> {
                    if (resultCode == Activity.RESULT_OK) {
                        val filePath =filesDir.absolutePath + "/fileToUpload"
                        val fos = FileOutputStream(filePath)
                        fos.write(contentResolver.openInputStream(data?.data!!)!!.readBytes())
                        fos.flush()
                        fos.close()

                        file = File(filePath)
                    }
                }
            }
        } catch (e: Exception) {
            showToast("ActivityResult: " + e.message)
        }

        val request = ConvertDocumentRequest(file, "pdf", null, null, null, null)
        Thread{
            try {
                val response: File? = api?.convertDocument(request)
                val outputFileName = filesDir.absolutePath + "/" + "converted.pdf"
                val newFile = File(outputFileName)
                val fos = FileOutputStream(newFile)
                fos.write(response!!.readBytes())
                fos.flush()
                fos.close()
            } catch (e: ApiException) {
                runOnUiThread{
                    Toast.makeText(this, "Api(Code:" + e.code.toString() + "): " + e.message, Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                runOnUiThread{
                    Toast.makeText(this, "Exception: " + e.message, Toast.LENGTH_LONG).show()
                }
            }
        }.start()

    }

    fun convertButtonOnClick(view: View){
        var json: String? = null
        try{
            val inputStream: InputStream = assets.open("servercreds.json")
            json = inputStream.bufferedReader().use{it.readText()}
            val jsonObj = JSONObject(json)
            api = WordsApi(ApiClient());
            api?.apiClient!!.setAppKey(jsonObj.getString("AppKey"))
                .setAppSid(jsonObj.getString("AppSid"))
                .setBaseUrl(jsonObj.getString("BaseUrl"))
            pickFile()

        } catch ( e: IOException) {
            Toast.makeText(this, "File servercreds.json is not found", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
        }
    }
}


