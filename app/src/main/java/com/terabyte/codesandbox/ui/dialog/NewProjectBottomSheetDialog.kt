package com.terabyte.codesandbox.ui.dialog

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.terabyte.codesandbox.R
import com.terabyte.codesandbox.databinding.BottomSheetNewProjectBinding

class NewProjectBottomSheetDialog : BottomSheetDialogFragment() {
    private var projectNames: List<String>? = null
    private lateinit var validDefaultName: String
    private lateinit var newProjectListener: (String) -> Unit

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        //this code is to make a custom background of bottomSheet
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dialog.setOnShowListener {
            val bottomSheetDialog = it as BottomSheetDialog
            val bottomSheet = bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) ?: return@setOnShowListener
            bottomSheet.setBackgroundColor(Color.TRANSPARENT)
        }
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = BottomSheetNewProjectBinding.inflate(inflater, container, false)

        binding.textLabelBusyName.visibility = View.INVISIBLE

        binding.editNewProjectName.setText(validDefaultName)

        binding.buttonNewProjectWithName.setOnClickListener {
            val name = binding.editNewProjectName.text.toString()
            if (projectNames!=null && projectNames!!.isNotEmpty()) {
                if (name in projectNames!!) {
                    binding.textLabelBusyName.visibility = View.VISIBLE
                    binding.textLabelBusyName.text = getString(R.string.this_name_is_busy)
                } else if (name.isEmpty()) {
                    binding.textLabelBusyName.text = getString(R.string.name_can_not_be_empty)
                    binding.textLabelBusyName.visibility = View.VISIBLE
                } else {
                    binding.textLabelBusyName.visibility = View.INVISIBLE
                    startCreatingProject(name)
                }
            }
            else {
                if (name.isEmpty()) {
                    binding.textLabelBusyName.text = getString(R.string.name_can_not_be_empty)
                    binding.textLabelBusyName.visibility = View.VISIBLE
                } else {
                    binding.textLabelBusyName.visibility = View.INVISIBLE
                    startCreatingProject(name)
                }
            }
        }

        return binding.root
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        view.viewTreeObserver.addOnGlobalLayoutListener(object :
//            ViewTreeObserver.OnGlobalLayoutListener {
//            override fun onGlobalLayout() {
//                view.viewTreeObserver.removeOnGlobalLayoutListener(this)
//
//                val dialog = dialog as BottomSheetDialog
//                val frameLayoutRoot =
//                    dialog.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
//                if (frameLayoutRoot != null) {
//                    val behavior = BottomSheetBehavior.from(frameLayoutRoot)
//                    behavior.state = BottomSheetBehavior.STATE_EXPANDED
//                }
//
//            }
//        })
//    }

    private fun startCreatingProject(projectName: String) {
        dismiss()
        newProjectListener(projectName)
    }

    companion object {
        const val TAG = "NewProjectBottomSheetDialog"

        //actually Android requires having an empty constructor for Fragment classes.
        //And there is the factory method pattern to make constructor empty
        fun newInstance(
            projectNames: List<String>?,
            validDefaultName: String,
            newProjectListener: (String) -> Unit
        ): NewProjectBottomSheetDialog {
            val bottomSheetDialog = NewProjectBottomSheetDialog()
            bottomSheetDialog.projectNames = projectNames
            bottomSheetDialog.validDefaultName = validDefaultName
            bottomSheetDialog.newProjectListener = newProjectListener
            return bottomSheetDialog
        }
    }
}