plugins {
    id("jvm-convention")
    id("org.openapi.generator")
}

openApiGenerate {
    generatorName.set("kotlin")
    inputSpec.set("${projectDir}/src/main/openapi/swift-order-api.yaml")
    outputDir.set(layout.buildDirectory.dir("generated").get().asFile.absolutePath)
    packageName.set("ru.otus.otuskotlin.yan.swiftorder.api.v1")
    modelPackage.set("ru.otus.otuskotlin.yan.swiftorder.api.v1.models")
    configOptions.set(
        mapOf(
            "serializationLibrary" to "jackson",
            "enumPropertyNaming" to "UPPERCASE",
        )
    )
    globalProperties.set(
        mapOf(
            "models" to "",
            "modelDocs" to "false",
            "apis" to "false",
            "apiDocs" to "false",
            "supportingFiles" to "false",
        )
    )
}

sourceSets {
    main {
        kotlin {
            srcDir(layout.buildDirectory.dir("generated/src/main/kotlin"))
        }
    }
}

tasks.compileKotlin {
    dependsOn("openApiGenerate")
}

dependencies {
    constraints {
        implementation("org.jetbrains.kotlin:kotlin-reflect") {
            version { require("2.2.21") }
            because("jackson-module-kotlin pulls kotlin-reflect:1.7.22; align with project Kotlin version")
        }
    }
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.21.2")
}
