package com.example.hotpepperapi.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.hotpepperapi.databinding.FragmentStoreDetailBinding
import com.example.hotpepperapi.viewModel.ViewModel

class StoreDetailFragment : Fragment() {

    private lateinit var binding: FragmentStoreDetailBinding
    private lateinit var viewModel: ViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            com.example.hotpepperapi.R.layout.fragment_store_detail,
            container,
            false
        )
        viewModel = ViewModelProvider(requireActivity())[ViewModel::class.java]
        binding.dataViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.storeList.observe(viewLifecycleOwner) { list ->
            viewModel.position.observe(viewLifecycleOwner) { position ->
                binding.tvStoreName.text = list[position]
            }
        }

        viewModel.storeGenreList.observe(viewLifecycleOwner) { list ->
            viewModel.position.observe(viewLifecycleOwner) { position ->
                binding.tvGenre.text = list[position]
            }
        }

        viewModel.storeAddressList.observe(viewLifecycleOwner) { list ->
            viewModel.position.observe(viewLifecycleOwner) { position ->
                binding.tvStoreAddress.text = list[position]
            }
        }

        viewModel.accessList.observe(viewLifecycleOwner) { list ->
            viewModel.position.observe(viewLifecycleOwner) { position ->
                binding.tvStoreAccess.text = list[position]
            }
        }
    }
}