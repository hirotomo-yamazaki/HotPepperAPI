package com.example.hotpepperapi.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.Spinner
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.hotpepperapi.R
import com.example.hotpepperapi.databinding.FragmentTopBinding
import com.example.hotpepperapi.setSafeClickListener
import com.example.hotpepperapi.viewModel.ViewModel

class TopFragment : Fragment() {

    private val viewModel: ViewModel by activityViewModels()
    private lateinit var binding: FragmentTopBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_top, container, false
        )

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.spinner_list,
            android.R.layout.simple_list_item_1
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_list_item_1)
            binding.spGenre.adapter = adapter
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSearch.setSafeClickListener { checkParam() }

        binding.spGenre.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
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

        binding.cbCoupon.setOnClickListener { onCheckBoxClicked(it) }
        binding.cbFreeDrink.setOnClickListener { onCheckBoxClicked(it) }
        binding.cbFreeFood.setOnClickListener { onCheckBoxClicked(it) }

        binding.btn300.setSafeClickListener {
            if (viewModel.lat.value == null || viewModel.lng.value == null) {
                showLocationDialog()
            } else {
                checkLocation()
                select()
            }
        }

        viewModel.progressBarFlag.observe(viewLifecycleOwner) { flag ->
            if (flag) {
                showProgressBar()
            } else {
                hideProgressBar()
            }
        }

        Log.i("TopFragment", "onViewCreated")
    }

    private fun checkLocation() {
        viewModel.getByLocation()
    }

    private fun checkParam() {
        val keyword = binding.etSearchKeyword.text.toString()

        //入力された店名と駅名をviewModelに保存
        viewModel.setKeyword(keyword)

        //viewModelに店名と駅名が保存されていたら画面遷移
        if (!binding.etSearchKeyword.text.isNullOrEmpty()) {

            viewModel.getStoreList()
            viewModel.apiFlag.observe(viewLifecycleOwner) {
                if (it) {
                    findNavController().navigate(R.id.action_topFragment_to_storeListFragment)
                }
            }
        } else {
            showEditTextDialog()
        }
    }

    private fun onCheckBoxClicked(view: View) {
        if (view is CheckBox) {
            val checked: Boolean = view.isChecked

            when (view.id) {
                R.id.cb_coupon -> viewModel.couponCheck(checked)
                R.id.cb_free_drink -> viewModel.freeDrinkCheck(checked)
                R.id.cb_free_food -> viewModel.freeFoodCheck(checked)
            }
        }
    }

    private fun showLocationDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Caution")
            .setIcon(R.drawable.ic_baseline_warning_24)
            .setMessage(R.string.dialogMain)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun showEditTextDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Caution")
            .setIcon(R.drawable.ic_baseline_warning_24)
            .setMessage(R.string.dialog)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun select() {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.dialogHowto)
            .setMessage(R.string.selectHowto)
            .setPositiveButton(R.string.showList) { _, _ ->
                viewModel.setMultiple(false)
                findNavController().navigate(R.id.action_topFragment_to_storeListFragment)
            }
            .setNegativeButton(R.string.showMap) { _, _ ->
                findNavController().navigate(R.id.action_topFragment_to_mapsFragment)
            }
            .show()
    }

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }
}