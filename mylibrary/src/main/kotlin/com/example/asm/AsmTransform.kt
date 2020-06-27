package com.example.asm

import com.android.build.api.transform.Format
import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformInvocation
import com.example.asm.transform.ClassTransformer
import org.apache.commons.io.FileUtils
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.tree.ClassNode
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class AsmTransform : Transform() {
    override fun getName(): String {
        return javaClass.simpleName
    }

    override fun getInputTypes(): MutableSet<QualifiedContent.ContentType> {
        return mutableSetOf(QualifiedContent.DefaultContentType.CLASSES)
    }

    override fun isIncremental(): Boolean {
        return true
    }

    override fun getScopes(): MutableSet<in QualifiedContent.Scope> {
        return mutableSetOf(QualifiedContent.Scope.PROJECT)
    }

    override fun transform(transformInvocation: TransformInvocation?) {
        super.transform(transformInvocation)
        if (transformInvocation == null) {
            return
        }
        println("==== ${javaClass.simpleName} ====")
        val outputProvider = transformInvocation.outputProvider
        transformInvocation.inputs.forEach {input ->
            input.jarInputs.forEach {jarInput ->
                println("$jarInput")
                transformJar(
                    jarInput.file,
                    outputProvider.getContentLocation(
                        jarInput.file.absolutePath,
                        jarInput.contentTypes,
                        jarInput.scopes,
                        Format.JAR
                    )
                )
            }
            input.directoryInputs.forEach {directoryInput ->
                println("$directoryInput")
                transformDirectory(
                    directoryInput.file,
                    outputProvider.getContentLocation(
                        directoryInput.name,
                        directoryInput.contentTypes,
                        directoryInput.scopes,
                        Format.DIRECTORY
                    )
                )
            }
        }
    }

    private fun transformJar(input: File, output: File) {
        FileUtils.copyFile(input, output)
    }

    private fun transformDirectory(input: File, output: File) {
        if (output.exists()) {
            FileUtils.forceDelete(output)
        }
        FileUtils.forceMkdir(output)
        val srcDirPath = input.absolutePath
        val destDirPath = output.absolutePath
        input.listFiles()?.forEach {subFile ->
            val destFilePath = subFile.absolutePath.replace(srcDirPath, destDirPath)
            val destFile = File(destFilePath)
            if (subFile.isDirectory) {
                transformDirectory(subFile, destFile)
            } else {
                FileUtils.touch(destFile)
                doTransform(subFile, destFile)
            }
        }
    }

    private fun doTransform(input: File, output: File) {
        println("doTransform")
        try {
            val fis = FileInputStream(input)
            val cr = ClassReader(fis)

            val cn = ClassNode()
            cr.accept(cn, 0)

            println("doTransform 1")
            ClassTransformer.transformers.forEach {
                it.transform(cn)
            }
            println("doTransform 2")

            val cw = ClassWriter(ClassWriter.COMPUTE_FRAMES)
            cn.accept(cw)
            println("doTransform 3")

            val fos = FileOutputStream(output)
            println("doTransform 4")
            fos.write(cw.toByteArray())
            println("doTransform 5")
            fis.close()
            fos.close()
            println("doTransform 7")
        } catch (e: Exception) {
            e.printStackTrace()
        }
        println("doTransform done")
    }
}