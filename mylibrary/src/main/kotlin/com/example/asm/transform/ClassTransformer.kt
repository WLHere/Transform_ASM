package com.example.asm.transform

import org.objectweb.asm.tree.ClassNode

interface ClassTransformer {
    fun transform(classNode: ClassNode)

    companion object {
        val transformers = listOf<ClassTransformer>(
            ToastTransformer()
        )
    }
}