package com.terabyte.codesandbox.util

import android.content.Context

object ProjectsHelper {

    fun getProjectsNames(context: Context): List<String> {
        return context.fileList().toList()
    }

    fun createNewProject(context: Context, projectName: String, ) {
        //create dir hierarchy
        val dirProjectRoot = context.getDir(projectName, Context.MODE_PRIVATE)
        val dirClasses = context.getDir("$projectName/classes", Context.MODE_PRIVATE)
        val dirScene = context.getDir("$projectName/classes/scene", Context.MODE_PRIVATE)
    }
}