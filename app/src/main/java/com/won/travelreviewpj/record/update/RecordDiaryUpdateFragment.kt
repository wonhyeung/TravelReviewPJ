package com.won.travelreviewpj.record.update

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageMetadata
import com.won.travelreviewpj.R
import com.won.travelreviewpj.common.ViewBindingBaseFragment
import com.won.travelreviewpj.common.commonToast
import com.won.travelreviewpj.databinding.FragmentRecordDiaryUpdateBinding
import com.won.travelreviewpj.record.diary.RecordDiary
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.math.roundToInt

class RecordDiaryUpdateFragment : ViewBindingBaseFragment<FragmentRecordDiaryUpdateBinding>(
    FragmentRecordDiaryUpdateBinding::inflate
) {
    private var imageUri: Uri? = null
    private lateinit var imageMimeType: String
    private val viewModel: RecordDiaryUpdateViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fieldSetup()
    }

    private fun fieldSetup() {
        val recordId = arguments?.getString("recordId")

        with(binding) {
            arguments?.let {
                val title = it.getString("title")
                val address = it.getString("address")
                val mapx = it.getString("mapx")
                val mapy = it.getString("mapy")
                etRecordDiaryUpdateTitle.setText(title)
                etRecordDiaryUpdateAddress.setText(address)

                mbRecordDiaryUpdateLocation.setOnClickListener {
                    val action =
                        RecordDiaryUpdateFragmentDirections.actionRecordDiaryUpdateFragmentToRecordDiaryMap(
                            recordId.toString()
                        )
                    findNavController().navigate(action)

                }
                ibRecordDiaryUpdateImage.setOnClickListener {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        getImageFromGallery(Manifest.permission.READ_MEDIA_IMAGES)
                    } else {
                        getImageFromGallery(Manifest.permission.READ_EXTERNAL_STORAGE)
                    }
                }
                tbRecordDiaryUpdate.setOnMenuItemClickListener { item ->
                    if (item.itemId == R.id.btn_update) {
                        uploadImageToFirebaseStorage(imageUri.toString())
                        val recordDiary =
                            RecordDiary(
                                "",
                                etRecordDiaryUpdateTitle.text.toString(),
                                imageUri.toString(),
                                etRecordDiaryUpdateAddress.text.toString(),
                                etRecordDiaryUpdateCompanion.text.toString(),
                                tvRecordDiaryUpdateStartDate.text.toString(),
                                tvRecordDiaryUpdateEndDate.text.toString(),
                                etRecordDiaryUpdateDiary.text.toString(),
                                mapx.toString(),
                                mapy.toString()
                            )
                        insertDiaryFireStore(recordId.toString(), recordDiary)

                    }
                    true
                }
                rlRecordDiaryUpdateDate.setOnClickListener {
                    showDateTimePicker(
                        tvRecordDiaryUpdateStartDate,
                        tvRecordDiaryUpdateDateLine,
                        tvRecordDiaryUpdateEndDate
                    )
                }

            }
        }

    }

    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                imageUri = it.data?.data
                binding.ibRecordDiaryUpdateImage.setImageURI(imageUri)
                binding.ibRecordDiaryUpdateImage.setBackgroundColor(Color.TRANSPARENT)

                imageUri?.let { uri ->
                    imageMimeType = requireActivity().contentResolver.getType(uri) ?: ""
                }

            }
        }

    private val requestPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        when (it) {
            true -> {
                val intent = Intent(Intent.ACTION_PICK)
                intent.setDataAndType(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    "image/*"
                )
                startForResult.launch(intent)
            }

            false -> {
                Toast.makeText(context, "권한이 설정되지 않았습니다", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun insertDiaryFireStore(recordId: String, recordDiary: RecordDiary) {
        lifecycleScope.launch {
            viewModel.insertRecordDiary(recordId, recordDiary)
            val action =
                RecordDiaryUpdateFragmentDirections.actionRecordDiaryUpdateFragmentToFragmentRecordDiary(
                    recordId
                )
            findNavController().navigate(action)
        }
    }

    private fun showDateTimePicker(
        startDateText: TextView,
        line: TextView,
        endDateText: TextView
    ) {
        val startPicker = MaterialDatePicker.Builder.datePicker()
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .build()
        startPicker.addOnPositiveButtonClickListener { startDateValue ->
            val startDate = Calendar.getInstance()
            startDate.timeInMillis = startDateValue

            val endPicker = MaterialDatePicker.Builder.datePicker()
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()
            endPicker.addOnPositiveButtonClickListener { endDateValue ->
                val endDate = Calendar.getInstance()
                endDate.timeInMillis = endDateValue

                startDateText.text =
                    SimpleDateFormat("yyyy년 MM월 dd일", Locale.getDefault()).format(startDate.time)
                viewModel.scheduleStart = startDateText.text.toString().trim()
                line.visibility = View.VISIBLE
                endDateText.text =
                    SimpleDateFormat("yyyy년 MM월 dd일", Locale.getDefault()).format(endDate.time)
                viewModel.scheduleEnd = endDateText.text.toString().trim()
            }
            endPicker.show(childFragmentManager, endPicker.toString())
        }
        startPicker.show(childFragmentManager, startPicker.toString())
    }


    private fun getImageFromGallery(permission: String) {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                permission
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermission.launch(permission)
        } else {
            val intent = Intent(Intent.ACTION_PICK)
            intent.setDataAndType(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                "image/*"
            )
            startForResult.launch(intent)
        }
    }

    private fun uploadImageToFirebaseStorage(uriString: String) {
        val fromFile = Uri.parse(uriString)
        val fileContentType = StorageMetadata.Builder().setContentType(imageMimeType).build()

        val storageRef = FirebaseStorage.getInstance().reference
        val uploadTask = storageRef.child("uploadImages/" + fromFile.lastPathSegment)
            .putFile(fromFile, fileContentType)

        uploadTask.addOnProgressListener { taskSnapshot ->
            Log.e(
                "TAG-Uploading",
                "${(100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount).roundToInt()}% "
            )
        }.addOnPausedListener {
            commonToast(requireContext(), it.toString())
        }.addOnFailureListener {
            commonToast(requireContext(), it.toString())
        }.addOnSuccessListener {
            commonToast(requireContext(), "업로드 성공")
        }
    }


}


