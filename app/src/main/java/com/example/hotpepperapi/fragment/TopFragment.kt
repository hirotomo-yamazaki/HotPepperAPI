package com.example.hotpepperapi.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.hotpepperapi.R
import com.example.hotpepperapi.databinding.FragmentTopBinding
import com.example.hotpepperapi.viewModel.ViewModel

class TopFragment : Fragment() {

//    private val viewModel: ViewModel by viewModels()
    private lateinit var viewModel: ViewModel
    private lateinit var binding: FragmentTopBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_top, container, false
        )
        viewModel = ViewModelProvider(requireActivity())[ViewModel::class.java]
        binding.dataViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.spinner_list,
            android.R.layout.simple_list_item_1
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_list_item_1)
            binding.spGenre.adapter = adapter
        }

        Log.i("TopFragment", "onCreateView")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSearch.setOnClickListener { checkParam() }

        binding.spGenre.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val parent = p0 as Spinner

                val genreCode = when (parent.selectedItem) {
                    getString(R.string.izakaya) -> "G001"
                    getString(R.string.dinningBar) -> "G002"
                    getString(R.string.sousakuRyori) -> "G003"
                    getString(R.string.washoku) -> "G004"
                    getString(R.string.youshoku) -> "G005"
                    getString(R.string.italian) -> "G006"
                    getString(R.string.chuka) -> "G007"
                    getString(R.string.yakiniku) -> "G008"
                    getString(R.string.kankoku) -> "G017"
                    getString(R.string.asia) -> "G009"
                    getString(R.string.kakkokuryori) -> "G010"
                    getString(R.string.karaoke) -> "G011"
                    getString(R.string.bar) -> "G012"
                    getString(R.string.ramen) -> "G013"
                    getString(R.string.okonomiyaki) -> "G016"
                    getString(R.string.cafe) -> "G014"
                    getString(R.string.other) -> "G015"
                    else -> "G001"
                }

                viewModel.setGenreCode(genreCode)
                Log.i("genreCode", viewModel.genreCode.value.toString())
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                val genreCode = "G001"
                viewModel.setGenreCode(genreCode)
            }
        }

        binding.btn300.setOnClickListener {
            findNavController().navigate(R.id.action_topFragment_to_storeListFragment)
        }

        binding.btn500.setOnClickListener {
            findNavController().navigate(R.id.action_topFragment_to_storeListFragment)
        }
        Log.i("TopFragment", "onViewCreated")
    }

    private fun checkParam() {
        val keyword = binding.etSearchKeyword.text.toString()

        //入力された店名と駅名をviewModelに保存
        viewModel.setKeyword(keyword)
        Log.i("keyword", viewModel.etKeyword.value.toString())
        Log.i("genreCode", viewModel.genreCode.value.toString())

        //viewModelに店名と駅名が保存されていたら画面遷移
        if (!viewModel.etKeyword.value.isNullOrEmpty() && !viewModel.genreCode.value.isNullOrEmpty()) {
            viewModel.getStoreList()
            Log.i("navCon", "画面遷移")
            findNavController().navigate(R.id.action_topFragment_to_storeListFragment)
        }
        Log.i("TopFragment", "checkParam")
    }
}