plugins {
	alias(libs.plugins.androidApplication)
	alias(libs.plugins.kotlin.android)
	alias(libs.plugins.ksp)
	alias(libs.plugins.hilt.android)
	alias(libs.plugins.compose.compiler)
}

android {
	namespace = "com.vishal2376.snaptick"
	compileSdk = libs.versions.android.compileSdk.get().toInt()

	defaultConfig {
		applicationId = "com.vishal2376.snaptick"
		minSdk = libs.versions.android.minSdk.get().toInt()
		targetSdk = libs.versions.android.targetSdk.get().toInt()
		versionCode = 8
		versionName = "3.0"

		testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
		vectorDrawables {
			useSupportLibrary = true
		}
	}

	buildTypes {
		release {
			isMinifyEnabled = false
			proguardFiles(
				getDefaultProguardFile("proguard-android-optimize.txt"),
				"proguard-rules.pro"
			)
			signingConfig = signingConfigs.getByName("debug")
		}

		debug {
			applicationIdSuffix = ".debug"
			versionNameSuffix = "-debug"
		}
	}
	compileOptions {
		sourceCompatibility = JavaVersion.VERSION_1_8
		targetCompatibility = JavaVersion.VERSION_1_8
	}
	kotlinOptions {
		jvmTarget = "1.8"
	}
	packaging {
		resources {
			excludes += "/META-INF/{AL2.0,LGPL2.1}"
		}
	}

	ksp {
		arg(
			"room.schemaLocation",
			"$projectDir/schemas"
		)
	}

}

dependencies {

	implementation(libs.androidx.core.ktx)
	implementation(libs.androidx.lifecycle.runtime.ktx)
	implementation(libs.androidx.activity.compose)
	implementation(platform(libs.androidx.compose.bom))
	implementation(libs.androidx.ui)
	implementation(libs.androidx.ui.graphics)
	implementation(libs.androidx.ui.tooling.preview)
	implementation(libs.androidx.material3)

	//room
	implementation(libs.androidx.room.runtime)
	implementation(libs.androidx.room.ktx)
	ksp(libs.androidx.room.compiler)

	//hilt
	implementation(libs.hilt.android)
	ksp(libs.hilt.android.compiler)
	ksp(libs.hilt.compiler)
	implementation(libs.hilt.work)

	//navigation
	implementation(libs.androidx.navigation.compose)

	//lifecycle
	implementation(libs.androidx.lifecycle.runtime.compose)

	//time picker
	implementation(libs.wheelpickercompose)

	//material icons extended
	implementation(libs.androidx.material.icons.extended)

	//acra - crash reports
	implementation(libs.acra.mail)
	implementation(libs.acra.dialog)

	//work manager
	implementation(libs.androidx.work.runtime.ktx)

	//data store
	implementation(libs.androidx.datastore.preferences)

	//splash screen
	implementation(libs.androidx.core.splashscreen)

	//gson
	implementation(libs.gson)

	//calender
	implementation(libs.compose)

	//widget
	implementation(libs.androidx.glance.appwidget)
	implementation(libs.androidx.glance.material3)


	testImplementation(libs.junit)
	androidTestImplementation(libs.androidx.junit)
	androidTestImplementation(libs.androidx.espresso.core)
	androidTestImplementation(platform(libs.androidx.compose.bom))
	androidTestImplementation(libs.androidx.ui.test.junit4)
	debugImplementation(libs.androidx.ui.tooling)
	debugImplementation(libs.androidx.ui.test.manifest)
}