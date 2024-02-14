package com.won.travelreviewpj.record.diary.detail

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.os.Environment
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.jakewharton.rxbinding4.view.clicks
import com.won.travelreviewpj.R
import com.won.travelreviewpj.common.FOLDER_ID
import com.won.travelreviewpj.common.MODE_EDIT
import com.won.travelreviewpj.common.RECORD_ID
import com.won.travelreviewpj.common.ViewBindingBaseFragment
import com.won.travelreviewpj.common.currentTimeFormat
import com.won.travelreviewpj.databinding.FragmentRecordDiaryDetailBinding
import io.reactivex.rxjava3.disposables.CompositeDisposable
import java.io.File
import java.io.FileOutputStream

/**
 * Record diary detail fragment
 *
 * 방문한 여행지 대한 세부 정보
 */
class RecordDiaryDetailFragment : ViewBindingBaseFragment<FragmentRecordDiaryDetailBinding>(FragmentRecordDiaryDetailBinding::inflate) {

    private val viewModel: RecordDiaryDetailViewModel by viewModels()
    private val compositeDisposable = CompositeDisposable()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fieldSetUp()
    }

    private fun fieldSetUp() {
        val id = arguments?.getString(FOLDER_ID)
        val recordId = arguments?.getString(RECORD_ID)

        with(binding) {
            tbRecordDiaryDetail.setNavigationOnClickListener {
                findNavController().popBackStack()
            }
            id?.let {
                viewModel.findRecordDiary(id).observe(viewLifecycleOwner) { recordDiary ->
                    Glide.with(requireContext())
                        .load(recordDiary?.image)
                        .centerCrop()
                        .into(ivRecordDiaryDetailImage)
                    tbRecordDiaryDetail.title = recordDiary?.name
                    tvRecordDiaryDetailAddressInfo.text = recordDiary?.address
                    tvRecordDiaryDetailCompanionInfo.text = recordDiary?.companion
                    tvRecordDiaryDetailStartDate.text = recordDiary?.startDate
                    tvRecordDiaryDetailDateLine.visibility = View.VISIBLE
                    tvRecordDiaryDetailEndDate.text = recordDiary?.endDate
                    tvRecordDiaryDetailDiaryInfo.text = recordDiary?.diary

                    compositeDisposable.add(
                    tbRecordDiaryDetail.menu.findItem(R.id.btn_build).clicks()
                        .subscribe {
                            val action =
                                RecordDiaryDetailFragmentDirections.actionRecordDetailFragmentToRecordDiaryUpdateFragment(
                                    recordId.toString(), id, MODE_EDIT
                                )
                            findNavController().navigate(action)
                        }
                    )
                    compositeDisposable.add(
                        tbRecordDiaryDetail.menu.findItem(R.id.btn_share).clicks()
                            .subscribe {
                                shareLayout()
                            }
                    )
                }
            }
        }
    }

    /**
     * View to bitmap
     *
     * View Bitmap 변환
     */
    private fun viewToBitmap(view: View): Bitmap {
        val colorRes = R.color.colorPrimary
        val color = ContextCompat.getColor(requireContext(), colorRes)
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.RGB_565)

        with(Canvas(bitmap)) {
            drawColor(color)
            view.draw(this)
        }
        return bitmap
    }

    /**
     * Share layout
     *
     * 이미지 파일 공유
     */
    private fun shareLayout() {
        val bitmap = viewToBitmap(binding.root)
        val imageFile = saveBitmapToFile(bitmap)
        val imageUri = FileProvider.getUriForFile(
            requireContext(),
            "${requireContext().packageName}.fileprovider",
            imageFile
        )

        with(Intent(Intent.ACTION_SEND)){
            type = "image/*"
            putExtra(Intent.EXTRA_STREAM, imageUri)
            startActivity(Intent.createChooser(this, resources.getString(R.string.share)))
        }
    }


    /**
     * Save bitmap to file
     *
     * Bitmap 이미지 파일로 저장
     */
    private fun saveBitmapToFile(bitmap: Bitmap): File {
        val timeStamp = currentTimeFormat()
        val directory = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val imageFile = File(directory, String.format("image_%s.jpg", timeStamp))

        FileOutputStream(imageFile).use {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
        }
        return imageFile
    }

    override fun onDestroyView() {
        super.onDestroyView()
        compositeDisposable.clear()
    }
}