package com.example.app5.ui.home

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.app5.databinding.FragmentHomeBinding
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.squareup.picasso.Picasso
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.lang.ref.WeakReference

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val db = FirebaseDatabase.getInstance().getReference("User")
    val storage = FirebaseStorage.getInstance()
    private val imageReference = Firebase.storage.reference
    private var currentFile: Uri? = null
    private var pwd: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        initialHide()

        val login: String? = activity?.intent?.getStringExtra("user")
        db.child(login!!).get().addOnSuccessListener{
            if(it.exists()){
                pwd = it.child("pwd").value.toString()

                binding.lkName.setText(it.child("name").value.toString())
                binding.lkLastname.setText(it.child("lastname").value.toString())
                binding.lkLogin.setText(login)
                binding.lkDob.setText(it.child("dob").value.toString())
                storage.reference.child("images/"+login).downloadUrl.addOnSuccessListener {
                        uri ->
                    Picasso.with(activity).load(uri).into(binding.lkImg)
                }
            }
        }

        binding.lkEdit.setOnClickListener{
            showEdit()
        }
        binding.lkEditCancel.setOnClickListener{
            hideEdit()
            storage.reference.child("images/"+login).downloadUrl.addOnSuccessListener {
                    uri ->
                Picasso.with(activity).load(uri).into(binding.lkImg)
            }
        }
        binding.lkChange.setOnClickListener{
            showChange()
        }
        binding.lkChangeCancel.setOnClickListener{
            hideChange()
        }
        binding.lkChangeSave.setOnClickListener{
            if(binding.lkPwd1.text.toString() == pwd){
                if(binding.lkPwd2.text.toString() == binding.lkPwd3.text.toString()){
                    val updMap = mapOf(
                        "pwd" to binding.lkPwd2.text.toString(),
                    )
                    db.child(login).updateChildren(updMap)
                    Toast.makeText(context, "Пароль успешно изменен!", Toast.LENGTH_SHORT).show()
                    hideChange()
                }else Toast.makeText(context, "Новый пароль не совпадает с подтверждением", Toast.LENGTH_SHORT).show()
            }else Toast.makeText(context, "Неверный старый пароль", Toast.LENGTH_SHORT).show()
        }
        binding.lkEditSave.setOnClickListener{
            val updMap = mapOf(
                "name" to binding.lkName.text.toString(),
                "lastname" to binding.lkLastname.text.toString(),
                "login" to login,
                "dob" to binding.lkDob.text.toString()
            )
            db.child(login).updateChildren(updMap)
            storage.reference.child("images/"+login).delete()
            uploadImageToStorage(login)
            hideEdit()
        }
        binding.lkStorage.setOnClickListener{
            Intent(Intent.ACTION_GET_CONTENT).also {
                it.type = "image/*"
                imageLauncher.launch(it)
            }
        }
        binding.lkCamera.setOnClickListener{
            checkCameraPermission()
        }

        return root
    }
    private val imageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result ->
        if(result.resultCode == AppCompatActivity.RESULT_OK){
            if(result?.data?.extras == null){
                result?.data?.data?.let{
                    currentFile = it
                    Toast.makeText(context, "Аватар выбран", Toast.LENGTH_SHORT).show()
                }
            }else{
                val r = result?.data?.extras?.get("data") as Bitmap
                var r1: WeakReference<Bitmap> = WeakReference(
                    Bitmap.createScaledBitmap(r,r.height, r.width, false)
                        .copy(Bitmap.Config.RGB_565, true)
                )
                var bm: Bitmap? = r1.get()
                currentFile = saveImage(bm!!, context!!)
            }
            Picasso.with(activity).load(currentFile).into(binding.lkImg)
        }else Toast.makeText(context, "Что-то пошло не так", Toast.LENGTH_SHORT).show()
    }
    private fun uploadImageToStorage(filename: String){
        try {
            currentFile?.let {
                imageReference.child("images/${filename}").putFile(it).addOnCompleteListener{
                    Toast.makeText(context, "Картинка успешно загружена", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener{
                    Toast.makeText(context, "Ошибка загрузки на сервер", Toast.LENGTH_SHORT).show()
                }
            }
        }catch(e: Exception){
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show()
        }
    }
    private fun checkCameraPermission() {
        Dexter.withContext(context)
            .withPermissions(android.Manifest.permission.CAMERA)
            .withListener(
                object : MultiplePermissionsListener {
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
        AlertDialog.Builder(context!!)
            .setMessage("Для установки изображения профиля нужны разрешения к камере")
            .setPositiveButton("Настройки"){_,_->
                try{
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", activity!!.packageName, null)
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
    private fun initialHide(){
        binding.lkEditCancel.visibility = View.GONE
        binding.lkEditSave.visibility = View.GONE
        binding.lkCamera.visibility = View.GONE
        binding.lkStorage.visibility = View.GONE
        binding.lkChangeSave.visibility = View.GONE
        binding.lkChangeCancel.visibility = View.GONE
        binding.lkPwd1.visibility = View.GONE
        binding.lkPwd2.visibility = View.GONE
        binding.lkPwd3.visibility = View.GONE
    }
    private fun showEdit(){
        binding.lkName.isEnabled = true
        binding.lkLastname.isEnabled = true
        binding.lkLogin.isEnabled = true
        binding.lkDob.isEnabled = true
        binding.lkChange.visibility = View.GONE
        binding.lkEdit.visibility = View.GONE
        binding.lkEditSave.visibility = View.VISIBLE
        binding.lkEditCancel.visibility = View.VISIBLE
        binding.lkCamera.visibility = View.VISIBLE
        binding.lkStorage.visibility = View.VISIBLE
    }
    private fun hideEdit(){
        binding.lkName.isEnabled = false
        binding.lkLastname.isEnabled = false
        binding.lkLogin.isEnabled = false
        binding.lkDob.isEnabled = false
        binding.lkChange.visibility = View.VISIBLE
        binding.lkEdit.visibility = View.VISIBLE
        binding.lkEditSave.visibility = View.GONE
        binding.lkEditCancel.visibility = View.GONE
        binding.lkCamera.visibility = View.GONE
        binding.lkStorage.visibility = View.GONE
    }
    private fun showChange(){
        binding.lkChange.visibility = View.GONE
        binding.lkEdit.visibility = View.GONE
        binding.lkChangeSave.visibility = View.VISIBLE
        binding.lkChangeCancel.visibility = View.VISIBLE
        binding.lkPwd1.visibility = View.VISIBLE
        binding.lkPwd2.visibility = View.VISIBLE
        binding.lkPwd3.visibility = View.VISIBLE
    }
    private fun hideChange(){
        binding.lkChange.visibility = View.VISIBLE
        binding.lkEdit.visibility = View.VISIBLE
        binding.lkChangeSave.visibility = View.GONE
        binding.lkChangeCancel.visibility = View.GONE
        binding.lkPwd1.visibility = View.GONE
        binding.lkPwd2.visibility = View.GONE
        binding.lkPwd3.visibility = View.GONE
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}