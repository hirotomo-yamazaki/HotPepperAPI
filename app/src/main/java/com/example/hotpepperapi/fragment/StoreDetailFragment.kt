package com.example.hotpepperapi.fragment

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.core.text.HtmlCompat.FROM_HTML_MODE_COMPACT
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.hotpepperapi.R
import com.example.hotpepperapi.databinding.FragmentStoreDetailBinding
import com.example.hotpepperapi.viewModel.ViewModel

class StoreDetailFragment : Fragment() {

    private lateinit var binding: FragmentStoreDetailBinding
    private val viewModel: ViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_store_detail,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.list.observe(viewLifecycleOwner){ list ->
            viewModel.position.observe(viewLifecycleOwner){ position ->

                Glide.with(requireContext())
                    .load(list[position].photo.mobile.l)
                    .into(binding.ivStoreImage)

                binding.tvStoreName.text = list[position].name
                binding.tvGenre.text = list[position].genre.name
                binding.tvStoreAddress.text = list[position].address
                binding.tvStoreAccess.text = list[position].access

                val html = "店舗サイトは<a href=\"${list[position].urls.pc}\">こちら</a>から"
                binding.tvUrl.text =
                    HtmlCompat.fromHtml(html, FROM_HTML_MODE_COMPACT)
                binding.tvUrl.movementMethod = LinkMovementMethod.getInstance()

                viewModel.setStoreLatLng(list[position].lat, list[position].lng)
            }
        }

        binding.tvStoreAddress.setOnClickListener {
            findNavController().navigate(R.id.action_storeDetailFragment_to_mapsFragment)
        }
    }
}