package com.masar.masatapp1.UI.Student.MainPage_Student

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.masar.masatapp1.R
import com.masar.masatapp1.databinding.FragmentHomePageStudentBinding


class HomePage_Student_Fragment : Fragment() {
    lateinit var binding : FragmentHomePageStudentBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        requireActivity().window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
        requireActivity().window.statusBarColor = Color.parseColor("#CCCCCC")
        binding = FragmentHomePageStudentBinding.inflate(layoutInflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lineardeleteAccount.setOnClickListener {
            DialogDeleteAccount(requireContext(),requireActivity()).show()
        }

        binding.reseaveSeetStudentCard.setOnClickListener {
            findNavController().navigate(R.id.action_homePage_Student_Fragment_to_pageOne_Book__Fragment)
        }
        binding.infoStudentCard.setOnClickListener {
            findNavController().navigate(R.id.action_homePage_Student_Fragment_to_info_Student_ragment)

        }
        binding.allReseaveStudentCard.setOnClickListener {
            findNavController().navigate(R.id.action_homePage_Student_Fragment_to_all_Book_Student_Fragment)

        }
        binding.FAQStudentCard.setOnClickListener {
            findNavController().navigate(R.id.action_homePage_Student_Fragment_to_FAQ_Student_Fragment)

        }
        binding.settingStudentCard.setOnClickListener {
            findNavController().navigate(R.id.action_homePage_Student_Fragment_to_setting_Student_Fragment)

        }
    }


}