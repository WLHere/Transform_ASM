package com.example.asm1

import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Opcodes.ASM7

class TestMethodClassAdapter(classVisitor: ClassVisitor) : ClassVisitor(ASM7), Opcodes {
    override fun visitMethod(
        access: Int,
        name: String?,
        descriptor: String?,
        signature: String?,
        exceptions: Array<out String>?
    ): MethodVisitor? {
        val mv = super.visitMethod(access, name, descriptor, signature, exceptions)
        return if (mv == null) {
            null
        } else {
            TestMethodVisitor(mv)
        }
    }
}