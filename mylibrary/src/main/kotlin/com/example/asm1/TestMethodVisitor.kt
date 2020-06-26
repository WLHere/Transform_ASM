package com.example.asm1

import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Opcodes.ASM7
import org.objectweb.asm.Opcodes.POP

class TestMethodVisitor(methodVisitor: MethodVisitor) : MethodVisitor(ASM7){

    override fun visitMethodInsn(
        opcode: Int,
        owner: String?,
        name: String?,
        descriptor: String?,
        isInterface: Boolean
    ) {
        println("== TestMethodVisitor, owner = $owner, name = $name")
        mv.visitLdcInsn("[ASM 测试] method in $owner, name = $name")
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, "android/util/Log", "i", "(Ljava/lang/String;Ljava/lang/String;)I", false)
        mv.visitInsn(POP)
        super.visitMethodInsn(opcode, owner, name, descriptor, isInterface)
        mv.visitLdcInsn(" after method exec")
        mv.visitLdcInsn(" method in $owner, name = $name")
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, "android/util/Log", "i", "(Ljava/lang/String;Ljava/lang/String;)I", false)
        mv.visitInsn(POP)
    }
}