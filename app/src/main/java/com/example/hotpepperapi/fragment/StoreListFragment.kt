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

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.list.observe(viewLifecycleOwner){ list ->
            if (!list.isNullOrEmpty()){

                val nameList = mutableListOf<String>()
                for (i in list.indices){
                    nameList += list[i].name
                }

                binding.lv300.adapter = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_list_item_1,
                    nameList
                )

                binding.lv300.visibility = View.VISIBLE
                binding.tvErrorMsg.visibility = View.GONE

            }else{
                binding.lv300.visibility = View.GONE
                binding.tvErrorMsg.visibility = View.VISIBLE
            }
        }


        binding.lv300.setOnItemClickListener { _, _, position, _ ->
            viewModel.setPosition(position)
            findNavController().navigate(R.id.action_storeListFragment_to_storeDetailFragment)
        }

        Log.i("StoreList", "onViewCreated")
    }
}