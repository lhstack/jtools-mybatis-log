package com.jtools.mybatis.log.jtoolsmybatislog

import com.intellij.execution.Executor
import com.intellij.execution.configurations.JavaParameters
import com.intellij.execution.configurations.RunProfile
import com.intellij.execution.runners.JavaProgramPatcher
import java.io.File
import java.nio.file.Files

class MybatisLogJavaProgramPatcher : JavaProgramPatcher() {
    override fun patchJavaParameters(executor: Executor, runProfile: RunProfile, parameters: JavaParameters) {

        try {
            val jar =
                MybatisLogJavaProgramPatcher::class.java.classLoader.getResourceAsStream("META-INF/agent.jar")
            jar?.use {
                val bytes = it.readBytes()
                val dir = System.getProperty("user.home") + "/.jtools/jtools-mybatis-log"
                File(dir).apply {
                    if (!this.exists()) {
                        this.mkdirs()
                    }
                    File(this, "agent.jar").apply {
                        if (this.exists()) {
                            val existBytes = Files.readAllBytes(this.toPath())
                            if (existBytes.size != bytes.size) {
                                Files.write(this.toPath(), bytes)
                            }
                        } else {
                            Files.write(this.toPath(), bytes)
                        }
                    }
                }
                bytes
            }
        } catch (ignore: Throwable) {

        }
        parameters.vmParametersList.add("-javaagent:${System.getProperty("user.home") + "/.jtools/jtools-mybatis-log/agent.jar"}")
    }
}