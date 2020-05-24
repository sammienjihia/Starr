import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import com.payments.gradle.tasks.GenerateTask
import org.jmailen.gradle.kotlinter.tasks.LintTask

plugins {
	id("org.springframework.boot") version "2.3.0.RELEASE"
	id("io.spring.dependency-management") version "1.0.9.RELEASE"
	kotlin("jvm") version "1.3.72"
	kotlin("plugin.spring") version "1.3.72"
	id("org.jmailen.kotlinter") version "2.3.2"
}

buildscript {
	repositories {
		mavenCentral()
		gradlePluginPortal()
		jcenter()
	}
	dependencies {
		classpath("org.springframework.boot:spring-boot-gradle-plugin")
		classpath(kotlin("gradle-plugin", version = "1.3.50"))
	}
}

group = "com.payments"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_1_8

repositories {
	jcenter()
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("com.squareup.moshi:moshi-kotlin:1.9.2")
	testImplementation("org.springframework.boot:spring-boot-starter-test") {
		exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
	}
}

val generateMappings = listOf(
		mapOf(
			"name" to "generateSampleDTO",
			"inputSpec" to "$rootDir/swagger_specs/sample_request.yml",
			"outputDir" to "$rootDir",
			"modelPackage" to "com.payments.starr.dto"
		)
)
//val importMappings =

val generatorNames = mutableListOf<String>()

generateMappings.forEach {
	val name = it["name"].toString()
	tasks.register(name, type = GenerateTask::class) {
		this.generatorName.value("kotlin")
		this.inputSpec.value(it["inputSpec"].toString())
		this.outputDir.value("$rootDir")
		this.modelPackage.value(it["modelPackage"].toString())
		this.validateSpec.value(true)
//		this.configOptions.value()
//		this.importMappings.value()
	}
	generatorNames.add(name)
}

tasks {
	"lintKotlinMain"(LintTask::class) {
		dependsOn(generatorNames)
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "1.8"
	}
}
