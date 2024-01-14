import net.ltgt.gradle.errorprone.errorprone

plugins {
	java
	id("org.springframework.boot") version "3.2.0"
	id("io.spring.dependency-management") version "1.1.4"
	id("net.ltgt.errorprone") version "3.1.0"
	id("checkstyle")
}

group = "com.emse.spring"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
    testImplementation("junit:junit:4.13.1")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa") // libs to use JPA in your project
	implementation("com.h2database:h2") // libs to use a H2 database
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.2.0")
	implementation("org.springframework.boot:spring-boot-starter-security")
	testImplementation("org.springframework.security:spring-security-test")
	val errorproneVersion = "2.23.0"
	"errorprone"("com.google.errorprone:error_prone_core:$errorproneVersion")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.javadoc {
	source = sourceSets["main"].allJava
	classpath = configurations["compileClasspath"]
	options {
		// Additional options can be set here
		memberLevel = JavadocMemberLevel.PUBLIC
		locale = "en_US"
		encoding = "UTF-8"
	}
}

tasks.withType<JavaCompile>().configureEach {
	options.compilerArgs = options.compilerArgs + "-parameters"
	options.errorprone {
		disableAllChecks.set(true)
		error(
				"MissingOverride",
		)
	}
}

checkstyle {
	toolVersion = "10.11.0"
	configFile = file("config/checkstyle.xml")
}
