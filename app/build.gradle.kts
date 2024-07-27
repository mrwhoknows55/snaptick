plugins {
	id("com.android.application")
	id("org.jetbrains.kotlin.android")
	id("com.google.devtools.ksp")
	kotlin("kapt")
	id("com.google.dagger.hilt.android")
	id("org.jetbrains.kotlin.plugin.compose")
}

android {
	namespace = "com.vishal2376.snaptick"
	compileSdk = 34

	defaultConfig {
		applicationId = "com.vishal2376.snaptick"
		minSdk = 26
		targetSdk = 34
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

	implementation("androidx.core:core-ktx:1.13.1")
	implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.4")
	implementation("androidx.activity:activity-compose:1.9.1")
	implementation(platform("androidx.compose:compose-bom:2024.06.00"))
	implementation("androidx.compose.ui:ui")
	implementation("androidx.compose.ui:ui-graphics")
	implementation("androidx.compose.ui:ui-tooling-preview")
	implementation("androidx.compose.material3:material3")

	//room
	implementation("androidx.room:room-runtime:2.6.1")
	annotationProcessor("androidx.room:room-compiler:2.6.1")
	implementation("androidx.room:room-ktx:2.6.1")
	ksp("androidx.room:room-compiler:2.6.1")

	//hilt
	implementation("com.google.dagger:hilt-android:2.51.1")
	kapt("com.google.dagger:hilt-android-compiler:2.51.1")
	kapt("androidx.hilt:hilt-compiler:1.2.0")
	implementation("androidx.hilt:hilt-work:1.2.0")

	//navigation
	implementation("androidx.navigation:navigation-compose:2.7.7")

	//lifecycle
	implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.4")

	//time picker
	implementation("com.github.commandiron:WheelPickerCompose:1.1.11")

	//material icons extended
	implementation("androidx.compose.material:material-icons-extended:1.6.8")

	//acra - crash reports
	implementation("ch.acra:acra-mail:5.11.3")
	implementation("ch.acra:acra-dialog:5.11.3")

	//work manager
	implementation("androidx.work:work-runtime-ktx:2.9.0")

	//data store
	implementation("androidx.datastore:datastore-preferences:1.1.1")

	//splash screen
	implementation("androidx.core:core-splashscreen:1.0.1")

	//gson
	implementation("com.google.code.gson:gson:2.10.1")

	//calender
	implementation("com.kizitonwose.calendar:compose:2.4.1")

	//widget
	implementation("androidx.glance:glance-appwidget:1.1.0")
	implementation("androidx.glance:glance-material3:1.1.0")


	testImplementation("junit:junit:4.13.2")
	androidTestImplementation("androidx.test.ext:junit:1.2.1")
	androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
	androidTestImplementation(platform("androidx.compose:compose-bom:2024.06.00"))
	androidTestImplementation("androidx.compose.ui:ui-test-junit4")
	debugImplementation("androidx.compose.ui:ui-tooling")
	debugImplementation("androidx.compose.ui:ui-test-manifest")
}