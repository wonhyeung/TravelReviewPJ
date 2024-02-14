package com.won.travelreviewpj.record.diary.update

import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageMetadata
import com.jakewharton.rxbinding4.view.clicks
import com.won.travelreviewpj.R
import com.won.travelreviewpj.common.ADDRESS
import com.won.travelreviewpj.common.FOLDER_ID
import com.won.travelreviewpj.common.IMAGE
import com.won.travelreviewpj.common.MAPX
import com.won.travelreviewpj.common.MAPY
import com.won.travelreviewpj.common.MODE
import com.won.travelreviewpj.common.MODE_ADD
import com.won.travelreviewpj.common.MODE_EDIT
import com.won.travelreviewpj.common.RECORD_ID
import com.won.travelreviewpj.common.TITLE
import com.won.travelreviewpj.common.ViewBindingBaseFragment
import com.won.travelreviewpj.common.commonToast
import com.won.travelreviewpj.common.currentDateFormat
import com.won.travelreviewpj.databinding.FragmentRecordDiaryUpdateBinding
import com.won.travelreviewpj.record.diary.RecordDiary
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.coroutines.launch
import java.util.Calendar

/**
 * Record diary update fragment
 *
 * 방문한 여행지 작성,수정
 */
class RecordDiaryUpdateFragment : ViewBindingBaseFragment<FragmentRecordDiaryUpdateBinding>(FragmentRecordDiaryUpdateBinding::inflate) {

    private lateinit var imageMimeType: String
    private val viewModel: RecordDiaryUpdateViewModel by viewModels()
    private val compositeDisposable = CompositeDisposable()
    private var selectedImageUri: Uri? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mode = arguments?.getString(MODE) ?: MODE_ADD
        if (mode == MODE_EDIT) {
            editFieldSetup()
        } else {
            addFieldSetup()
        }
        activity?.window?.setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN
        )
    }

    /**
     * Edit field setup
     *
     * 기존에 작성한 일기 수정
     */
    private fun editFieldSetup() {
        val recordId = arguments?.getString(RECORD_ID)
        val args = arguments
        val id = args?.getString(FOLDER_ID)
        with(binding) {
            id?.let {
                viewModel.findRecordDiary(id).observe(viewLifecycleOwner) { recordDiary ->
                    args.let {
                        val title = it.getString(TITLE)
                        val address = it.getString(ADDRESS)
                        val mapx = it.getString(MAPX)
                        val mapy = it.getString(MAPY)
                        val imageSrc = it.getString(IMAGE)

                        tbRecordDiaryUpdate.setNavigationOnClickListener {
                            val action =
                                RecordDiaryUpdateFragmentDirections.actionRecordDiaryUpdateFragmentToFragmentRecordDiary(
                                    recordId.toString()
                                )
                            findNavController().navigate(action)

                        }

                        mbRecordDiaryUpdateLocation.visibility = View.GONE
                        etRecordDiaryUpdateTitle.setText(recordDiary?.name)
                        etRecordDiaryUpdateAddress.setText(recordDiary?.address)
                        etRecordDiaryUpdateCompanion.setText(recordDiary?.companion)
                        tvRecordDiaryUpdateStartDate.text = recordDiary?.startDate
                        tvRecordDiaryUpdateDateLine.visibility = View.INVISIBLE
                        tvRecordDiaryUpdateEndDate.text = recordDiary?.endDate
                        etRecordDiaryUpdateDiary.setText(recordDiary?.diary)
                        ibRecordDiaryUpdateImage.setImageURI(recordDiary?.image?.toUri())
                        ibRecordDiaryUpdateImage.setBackgroundColor(Color.TRANSPARENT)

                        compositeDisposable.add(
                            ibRecordDiaryUpdateImage.clicks()
                                .subscribe {
                                    pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                                }
                        )
                        compositeDisposable.add(
                            rlRecordDiaryUpdateDate.clicks()
                                .subscribe {
                                    showDateTimePicker(
                                        tvRecordDiaryUpdateStartDate,
                                        tvRecordDiaryUpdateDateLine,
                                        tvRecordDiaryUpdateEndDate
                                    )
                                }
                        )
                        compositeDisposable.add(
                            tbRecordDiaryUpdate.menu.findItem(R.id.btn_update).clicks()
                                .subscribe {
                                    uploadImageToFirebaseStorage()
                                    val updateRecordDiary =
                                        RecordDiary(
                                            id,
                                            etRecordDiaryUpdateTitle.text.toString(),
                                            selectedImageUri.toString(),
                                            etRecordDiaryUpdateAddress.text.toString(),
                                            etRecordDiaryUpdateCompanion.text.toString(),
                                            tvRecordDiaryUpdateStartDate.text.toString(),
                                            tvRecordDiaryUpdateEndDate.text.toString(),
                                            etRecordDiaryUpdateDiary.text.toString(),
                                            mapx.toString(),
                                            mapy.toString()
                                        )

                                    if (selectedImageUri == null) {
                                        commonToast(resources.getString(R.string.request_image))
                                    } else {
                                        updateDiaryFireStore(updateRecordDiary)
                                    }
                                }
                        )
                    }
                }
            }

        }
    }

    /**
     * Add field setup
     *
     * 새로운 일기 작성
     */
    private fun addFieldSetup() {
        val recordId = arguments?.getString(RECORD_ID)
        with(binding) {
            arguments?.let {
                val title = it.getString(TITLE)
                val address = it.getString(ADDRESS)
                val mapx = it.getString(MAPX)
                val mapy = it.getString(MAPY)
                etRecordDiaryUpdateTitle.setText(title)
                etRecordDiaryUpdateAddress.setText(address)

                tbRecordDiaryUpdate.setNavigationOnClickListener {
                    val action =
                        RecordDiaryUpdateFragmentDirections.actionRecordDiaryUpdateFragmentToFragmentRecordDiary(
                            recordId.toString()
                        )
                    findNavController().navigate(action)
                }
                compositeDisposable.add(
                    mbRecordDiaryUpdateLocation.clicks()
                        .subscribe {
                            val action =
                                RecordDiaryUpdateFragmentDirections.actionRecordDiaryUpdateFragmentToRecordDiaryMap(
                                    recordId.toString(), ""
                                )
                            findNavController().navigate(action)
                        }
                )
                compositeDisposable.add(
                    ibRecordDiaryUpdateImage.clicks()
                        .subscribe {
                            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                        }
                )
                compositeDisposable.add(
                    rlRecordDiaryUpdateDate.clicks()
                        .subscribe {
                            showDateTimePicker(
                                tvRecordDiaryUpdateStartDate,
                                tvRecordDiaryUpdateDateLine,
                                tvRecordDiaryUpdateEndDate
                            )
                        }
                )
                compositeDisposable.add(
                    tbRecordDiaryUpdate.menu.findItem(R.id.btn_update).clicks()
                        .subscribe {
                            uploadImageToFirebaseStorage()
                            val recordDiary =
                                RecordDiary(
                                    "",
                                    etRecordDiaryUpdateTitle.text.toString(),
                                    selectedImageUri.toString(),
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
                )
            }
        }
    }

    /**
     * Update diary fire store
     *
     * @param recordDiary 일기 정보
     * 수정한 일기 FireStore 업로드
     */
    private fun updateDiaryFireStore(recordDiary: RecordDiary) {
        lifecycleScope.launch {
            viewModel.updateRecordDiary(recordDiary)
            val folderId = recordDiary.id
            val action =
                RecordDiaryUpdateFragmentDirections.actionRecordDiaryUpdateFragmentToRecordDetailFragment(
                    folderId
                )
            findNavController().navigate(action)
        }
    }

    /**
     * Insert diary fire store
     *
     * @param recordId 폴더 식별 정보
     * @param recordDiary 일기 정보
     * 새로운 일기 FireStore 업로드
     */
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

    /**
     * Show date time picker
     *
     * TimePicker 사용해 날짜 선택
     */
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

                startDateText.text = currentDateFormat().format(startDate.time)
                viewModel.scheduleStart = startDateText.text.toString().trim()
                line.visibility = View.VISIBLE
                endDateText.text = currentDateFormat().format(endDate.time)
                viewModel.scheduleEnd = endDateText.text.toString().trim()
            }
            endPicker.show(childFragmentManager, endPicker.toString())
        }
        startPicker.show(childFragmentManager, startPicker.toString())
    }

    /**
     * Pick media
     *
     * 갤러리에서 이미지 가져오기
     */
    private val pickMedia =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                binding.ibRecordDiaryUpdateImage.setImageURI(uri)
                binding.ibRecordDiaryUpdateImage.setBackgroundColor(Color.TRANSPARENT)

                imageMimeType = requireActivity().contentResolver.getType(uri) ?: ""
                selectedImageUri = uri
            } else {
                commonToast(getString(R.string.request_image))
            }
        }


    /**
     * Upload image to firebase storage
     *
     * FirebaseStorage 이미지 업로드
     */
    private fun uploadImageToFirebaseStorage() {
        selectedImageUri?.let { uri ->
        val fileContentType = StorageMetadata.Builder().setContentType(imageMimeType).build()
        val storageRef = FirebaseStorage.getInstance().reference
        val uploadTask = storageRef.child("uploadImages/" + uri.lastPathSegment)
            .putFile(uri, fileContentType)

        uploadTask.addOnProgressListener { _ ->
        }.addOnSuccessListener {
            commonToast(getString(R.string.success_upload))
        }
    } ?: run {
            commonToast(getString(R.string.request_image))
        }
    }
}


