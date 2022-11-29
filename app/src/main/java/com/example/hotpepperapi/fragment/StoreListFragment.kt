package com.example.hotpepperapi.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.hotpepperapi.R
import com.example.hotpepperapi.databinding.FragmentStoreListBinding
import com.example.hotpepperapi.viewModel.ViewModel

class StoreListFragment : Fragment() {

    private lateinit var binding: FragmentStoreListBinding
    private val viewModel: ViewModel by activityViewModels()

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

        Log.i("StoreListFragment", "onCreateView")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.progressBarFlag.observe(viewLifecycleOwner) { flag ->
            if (flag == true) {
                showProgressBar()
            } else {
                hideProgressBar()
            }
        }

        viewModel.storeList.observe(viewLifecycleOwner) {
            binding.lv300.adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_list_item_1,
                it
            )
        }

        binding.lv300.setOnItemClickListener { _, _, position, _ ->
            viewModel.setPosition(position)
            findNavController().navigate(R.id.action_storeListFragment_to_storeDetailFragment)
        }

        Log.i("StoreListFragment", "onViewCreated")
    }

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }
}