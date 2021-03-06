package com.example.asm.transform

import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.MethodInsnNode

class ToastTransformer : ClassTransformer {
    override fun transform(classNode: ClassNode) {
        println("transform: ${classNode.name}")
        if (classNode.name == SHADOW_TOAST) {
            return
        }
        classNode.methods.forEach { method ->
            method.instructions?.iterator()?.forEach {
                if (it is MethodInsnNode) {
                    if (it.opcode == Opcodes.INVOKEVIRTUAL
                        && it.name == "show"
                        && it.desc == "()V"
                        && it.owner == TOAST) {
                        println("hit: ${it.owner}.${it.name}")
                        it.owner = SHADOW_TOAST
                        it.name = "show"
                        it.desc = "(L$TOAST;)V"
                        it.opcode = Opcodes.INVOKESTATIC
                        it.itf = false
                        println("hit: done")
                    }
                }
            }

        }

        println("transform: done")
    }

    companion object {
        const val TOAST = "android/widget/Toast"
        const val SHADOW_TOAST = "com/example/transform_asm/toast/ShadowToast"
    }
}