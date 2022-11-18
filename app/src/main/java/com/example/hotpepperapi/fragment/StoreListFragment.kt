package com.example.hotpepperapi.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.hotpepperapi.R
import com.example.hotpepperapi.databinding.FragmentStoreListBinding
import com.example.hotpepperapi.viewModel.ViewModel

class StoreListFragment : Fragment() {

    private lateinit var binding: FragmentStoreListBinding
    private lateinit var viewModel: ViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_store_list,
            container,
            false
        )
        viewModel = ViewModelProvider(requireActivity())[ViewModel::class.java]
        binding.dataViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner


        viewModel.getStoreList()

        Log.i("StoreListFragment", "onCreateView")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.storeList.observe(viewLifecycleOwner) { list ->
            binding.lv300.adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_list_item_1,
                list
            )
        }

        Log.i("StoreListFragment", "onViewCreated")
    }
}