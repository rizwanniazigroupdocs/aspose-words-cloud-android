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
    }
    var api : WordsApi? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestPermission(this)
        setContentView(R.layout.activity_main)
    }

    val REQUEST_WRITE_STORAGE = 112;
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
                Array<String>(1, { Manifest.permission.WRITE_EXTERNAL_STORAGE }),
                REQUEST_WRITE_STORAGE
            );
        }
    }

    fun pickFile() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"
        startActivityForResult(intent, FILE_SELECT_CODE)
    }
    fun getPath(context: Context, uri: Uri) : String?{
        var result: String? = ""
        if("content".equals(uri.scheme, true))
        {
            val projection : Array<String> = arrayOf("_data")
            var cursor: Cursor? = null

            try{
                cursor = context.contentResolver.query(uri, projection, null,null,null)
                val column_index: Int = cursor!!.getColumnIndexOrThrow("_data")
                if (cursor.moveToFirst()){
                    val s = cursor.getType(column_index)
                    showToast("AZAZA: " + s)
                    return ""
                } else {
                    showToast("Cant move to first")
                }
            } catch (e: Exception) {
                showToast("GetPath: " + e.message)
            }
        } else if ("file".equals(uri.scheme, true)){
            return uri.path
        }

        return null
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        var file: File? = null
        try {
            when (requestCode) {
                FILE_SELECT_CODE -> {
                    if (resultCode == Activity.RESULT_OK) {
                        val filePath =filesDir.absolutePath + "fileToUpload"
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
                runOnUiThread {
                    showToast("RESULT!: " + newFile.absolutePath)
                }
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


