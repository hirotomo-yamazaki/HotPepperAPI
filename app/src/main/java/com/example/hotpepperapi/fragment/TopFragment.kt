package com.example.hotpepperapi.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.hotpepperapi.R
import com.example.hotpepperapi.databinding.FragmentTopBinding
import com.example.hotpepperapi.viewModel.TopViewModel

class TopFragment : Fragment() {

    private lateinit var viewModel: TopViewModel
    private lateinit var binding: FragmentTopBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_top, container, false
        )

        viewModel = ViewModelProvider(this)[TopViewModel::class.java]

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSearch.setOnClickListener {
            checkParam()
        }

        binding.btn300.setOnClickListener {
            findNavController().navigate(R.id.action_topFragment_to_storeListFragment)
        }

        binding.btn500.setOnClickListener {
            findNavController().navigate(R.id.action_topFragment_to_storeListFragment)
        }
    }

    private fun checkParam() {
        viewModel.checkParam(binding.etSearch.text.toString())

        Log.i("etStore", "${viewModel.etStore.value}")
        if (viewModel.etStore.value!!.isNotEmpty()) {
            findNavController().navigate(R.id.action_topFragment_to_storeDetailFragment)
        }
    }
}