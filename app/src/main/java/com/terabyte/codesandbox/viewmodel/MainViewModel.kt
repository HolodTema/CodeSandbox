package com.terabyte.codesandbox.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.terabyte.codesandbox.R
import com.terabyte.codesandbox.util.ProjectsHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainViewModel(private val application: Application) : AndroidViewModel(application) {
    val liveDataProjectNames = MutableLiveData<List<String>>()

    init {

        importProjectNamesFromStorage()
    }

    fun openProject(projectName: String) {

    }

    fun createNewProject(projectName: String, listener: ()->Unit) {
        CoroutineScope(Dispatchers.Main).launch {
            val deferred = async(Dispatchers.IO) {
                ProjectsHelper.createNewProject(application.applicationContext, projectName)
                return@async
            }
            deferred.await()
            listener()
        }
    }

    fun getValidNewProjectName(listener: (String) -> Unit) {
        CoroutineScope(Dispatchers.Main).launch {
            val deferred = async(Dispatchers.IO) {
                val names = liveDataProjectNames.value
                val beginning = application.getString(R.string.default_new_project_name_beginning)
                if (names.isNullOrEmpty()) {
                    return@async "$beginning 1"
                } else {
                    var maxIndex = 1
                    for (name in names) {
                        if (name.startsWith("$beginning ") && name.length > beginning.length) {
                            val nameEnding = name.substring("$beginning ".length)
                            var correctNameEnding = true
                            for (ch in nameEnding) {
                                if (ch !in "0123456789") {
                                    correctNameEnding = false
                                }
                            }
                            if (correctNameEnding && nameEnding.toInt() > maxIndex) maxIndex =
                                nameEnding.toInt()
                        }
                    }
                    return@async "$beginning $maxIndex"
                }
            }
            listener(deferred.await())
        }
    }

    private fun importProjectNamesFromStorage() {
        CoroutineScope(Dispatchers.Main).launch {
            val deferred = CoroutineScope(Dispatchers.IO).async {
                return@async ProjectsHelper.getProjectsNames(application.applicationContext)
            }
            liveDataProjectNames.value = deferred.await()
        }
    }


    class Factory(private val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MainViewModel(application) as T
        }
    }
}