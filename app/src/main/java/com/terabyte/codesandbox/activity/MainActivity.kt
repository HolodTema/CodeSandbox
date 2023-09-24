package com.terabyte.codesandbox.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.terabyte.codesandbox.databinding.ActivityMainBinding
import com.terabyte.codesandbox.databinding.ItemYourProjectBinding
import com.terabyte.codesandbox.ui.dialog.NewProjectBottomSheetDialog
import com.terabyte.codesandbox.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        viewModel.liveDataProjectNames.observe(this) { projectNames ->
            if (projectNames.isEmpty()) {
                hideAllProjects()
            }
            else {
                showAllProjects()
                configureRecyclerAllProjects(projectNames)
            }
        }

        binding.buttonNewProject.setOnClickListener {
            viewModel.getValidNewProjectName {
                val dialog = NewProjectBottomSheetDialog.newInstance(viewModel.liveDataProjectNames.value, it) { projectName ->
                    createNewProject(projectName)
                }
                dialog.show(supportFragmentManager, NewProjectBottomSheetDialog.TAG)
            }
        }
    }

    private fun configureRecyclerAllProjects(projectsNames: List<String>) {
        binding.recyclerYourProjects.adapter = AllProjectsAdapter(layoutInflater, projectsNames, viewModel)
    }

    private fun showAllProjects() {
        binding.recyclerYourProjects.visibility = View.VISIBLE
        binding.textLabelNoProjectsYet.visibility = View.GONE
        binding.progressUploadAllProjects.visibility = View.GONE
    }

    private fun hideAllProjects() {
        binding.recyclerYourProjects.visibility = View.GONE
        binding.textLabelNoProjectsYet.visibility = View.VISIBLE
        binding.progressUploadAllProjects.visibility = View.GONE
    }

    private fun createNewProject(projectName: String) {

    }

    class AllProjectsAdapter(
        private val inflater: LayoutInflater,
        private val projectsNames: List<String>,
        private val viewModel: MainViewModel
    ) :
        RecyclerView.Adapter<AllProjectsAdapter.Holder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
            return Holder(ItemYourProjectBinding.inflate(inflater, parent, false))
        }

        override fun onBindViewHolder(holder: Holder, position: Int) {
            with(holder.binding) {
                textYourProjectName.text = projectsNames[position]
                root.setOnClickListener {
                    viewModel.openProject(projectsNames[position])
                }
            }
        }

        override fun getItemCount(): Int {
            return projectsNames.size
        }

        class Holder(val binding: ItemYourProjectBinding) : RecyclerView.ViewHolder(binding.root)
    }
}