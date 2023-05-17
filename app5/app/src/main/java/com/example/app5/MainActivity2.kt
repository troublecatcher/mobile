package com.example.app5

import android.app.Activity
import android.app.DatePickerDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.Ringtone
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.telephony.TelephonyCallback.CallDisconnectCauseListener
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.squareup.picasso.Picasso
import java.io.Console
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.lang.ref.WeakReference
import java.net.URI
import java.util.Calendar
import kotlin.math.log

class MainActivity2 : AppCompatActivity() {

    private val db = FirebaseDatabase.getInstance().getReference("User")
    private val imageReference = Firebase.storage.reference
    private var currentFile: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        val name = findViewById<EditText>(R.id.registerETName)
        val lastname = findViewById<EditText>(R.id.registerETLastname)
        val login = findViewById<EditText>(R.id.registerETLogin)
        val password = findViewById<EditText>(R.id.registerETPassword)
        val confirmpassword = findViewById<EditText>(R.id.registerETConfirmpassword)
        val dateofbirth = findViewById<EditText>(R.id.registerETDateofbirth)

        fun areFieldsFilledOut(): Boolean {
            if(    name.text.toString() == ""
                || lastname.text.toString() == ""
                || login.text.toString() == ""
                || password.text.toString() == ""
                || confirmpassword.text.toString() == ""
                || dateofbirth.text.toString() == ""

            )
                return false
            return true
        }
        fun pwdMatchesPwdconfirm(): Boolean{
            if(password.text.toString() == confirmpassword.text.toString())
                return true
            return false
        }
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        findViewById<Button>(R.id.button).setOnClickListener{
            val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener{view: DatePicker?, year: Int, month: Int, dayOfMonth: Int ->
                findViewById<TextView>(R.id.registerETDateofbirth).setText(""+dayOfMonth+"."+(month+1)+"."+year)
            }, year, month, day)
            dpd.show()
        }
        findViewById<Button>(R.id.registerBTNPictureupload).setOnClickListener{
            Intent(Intent.ACTION_GET_CONTENT).also {
                it.type = "image/*"
                imageLauncher.launch(it)
            }
        }
        findViewById<Button>(R.id.registerBTNPicturetake).setOnClickListener{
            checkCameraPermission()
        }
        findViewById<Button>(R.id.registerBTNLogin).setOnClickListener{
            val i = Intent(this@MainActivity2, MainActivity::class.java)
            startActivity(i)
        }
        findViewById<Button>(R.id.registerBTNFinalize).setOnClickListener{
            if(areFieldsFilledOut()){
                if(pwdMatchesPwdconfirm()) {
                    db.child(login.text.toString()).get().addOnSuccessListener{
                        if(!it.exists()){
                            val user = User(
                                name.text.toString(),
                                lastname.text.toString(),
                                login.text.toString(),
                                password.text.toString(),
                                dateofbirth.text.toString(),
                            )
                            db.child(login.text.toString()).setValue(user)
                            if(currentFile != null)
                                uploadImageToStorage(login.text.toString())
                            Toast.makeText(this, "Вы зарегистрированы", Toast.LENGTH_SHORT).show()
                            val i = Intent(this@MainActivity2, MainActivity3::class.java)
                            i.putExtra("user", login.text.toString())
                            startActivity(i)
                        }else Toast.makeText(this, "Пользователь с таким логином уже существует", Toast.LENGTH_SHORT).show()
                    }
                }else Toast.makeText(this, "Пароли не совпадают", Toast.LENGTH_SHORT).show()
            }else Toast.makeText(this, "Не все поля заполнены", Toast.LENGTH_SHORT).show()
        }
    }
    private val imageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        result ->
        if(result.resultCode == RESULT_OK){

            if(result?.data?.extras == null){
                result?.data?.data?.let{
                    currentFile = it
                    Toast.makeText(this, "Аватар выбран", Toast.LENGTH_SHORT).show()
                }
            }else{
                val r = result?.data?.extras?.get("data") as Bitmap
                var r1: WeakReference<Bitmap> = WeakReference(
                    Bitmap.createScaledBitmap(r,r.height, r.width, false)
                        .copy(Bitmap.Config.RGB_565, true)
                )
                var bm: Bitmap? = r1.get()
                currentFile = saveImage(bm!!, MainActivity2@this)
                Toast.makeText(this, "Вы сделали фотографию на аватар", Toast.LENGTH_SHORT).show()
            }
        }else Toast.makeText(this, "Что-то пошло не так", Toast.LENGTH_SHORT).show()
    }
    private fun uploadImageToStorage(filename: String){
        try {
            currentFile?.let {
                imageReference.child("images/${filename}").putFile(it).addOnCompleteListener{
                    Toast.makeText(this, "Картинка успешно загружена", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener{
                    Toast.makeText(this, "Ошибка загрузки на сервер", Toast.LENGTH_SHORT).show()
                }
            }
        }catch(e: Exception){
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
        }
    }
    private fun checkCameraPermission() {
        Dexter.withContext(this)
            .withPermissions(android.Manifest.permission.CAMERA)
            .withListener(
                object : MultiplePermissionsListener{
                    override fun onPermissionsChecked(p0: MultiplePermissionsReport?) {
                        p0?.let{
                            if(p0.areAllPermissionsGranted()){
                                Intent(MediaStore.ACTION_IMAGE_CAPTURE).also {
                                    imageLauncher.launch(it)
                                }
                            }
                        }

                    }

                    override fun onPermissionRationaleShouldBeShown(
                        p0: MutableList<PermissionRequest>?,
                        p1: PermissionToken?
                    ) {
                        showRotationalDialogForPermission()
                    }

                }
            ).onSameThread().check()
    }

    private fun showRotationalDialogForPermission(){
        AlertDialog.Builder(this)
            .setMessage("Для установки изображения профиля нужны разрешения к камере")
            .setPositiveButton("Настройки"){_,_->
                try{
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
                    startActivity(intent)
                }catch (e: ActivityNotFoundException){
                    e.printStackTrace()
                }
            }
            .setNegativeButton("Отмена"){dialog, _->
                dialog.dismiss()
            }.show()
    }
    private fun saveImage(image: Bitmap, context: Context): Uri? {
        var imagesFolder = File(context.cacheDir, "images")
        var uri: Uri? = null
        try {
            imagesFolder.mkdirs()
            var file = File(imagesFolder, "captured_image.jpg")
            var stream = FileOutputStream(file)
            image.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.flush()
            stream.close()
            uri = FileProvider.getUriForFile(context.applicationContext, "com.example.app5"+".provider", file)
        }catch (e: FileNotFoundException){
            e.printStackTrace()
        }catch (e: IOException){
        e.printStackTrace()
        }
        return uri
    }
}