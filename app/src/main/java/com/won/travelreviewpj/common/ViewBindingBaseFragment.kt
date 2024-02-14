package com.won.travelreviewpj.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

/**
 * 본 앱에서 사용하는 Fragment 의 BaseFragment 선언
 */
typealias FragmentInflate<T> = (LayoutInflater, ViewGroup?, Boolean) -> T

abstract class ViewBindingBaseFragment<VB : ViewBinding>(
    private val inflate: FragmentInflate<VB>
) : Fragment() {

    private var _binding: VB? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflate.invoke(inflater, container, false)
        return binding.root
    }

    /**
     * On destroy view
     * Fragment 에서 뷰바인딩을 사용하고 lifecycle 시 보이지 않을때 자원 관리를 위해
     * 선언한다
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}