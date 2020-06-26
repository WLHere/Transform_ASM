package com.example.asm

import org.gradle.api.Plugin
import org.gradle.api.Project

class MyPlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        project.android.registerTransform(new MyTransform(project))
    }
}