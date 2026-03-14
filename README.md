## Translation App (Translate_ObjectText)

An Android application for real‑time language assistance, combining text translation, image‑based OCR translation, grammar correction, and two‑way voice conversation translation.

## Overview

This project is a multi‑feature translation app built in Java for Android.  
It lets users:
- Translate typed text between many languages (online + on‑device ML models).
- Capture or scan images and translate detected text.
- Run grammar/spell correction on input text before translation.
- Hold bilingual voice conversations with automatic speech recognition and text‑to‑speech.

The app consists of a main Android application module (`app`) and a document scanning / OpenCV‑based library module (`scanlibrary`).

## Features

- **Text translation**
  - Translate free‑form text between multiple languages.
  - Language selection with swappable source/target.
  - Optional offline models using Google ML Kit Translate.
- **Grammar correction**
  - Uses the Sapling API to detect and correct grammar/spelling errors before translation.
- **Image / object text translation**
  - Capture from camera or select from gallery.
  - Document‑style scanning and perspective correction using `scanlibrary` (OpenCV).
  - Cloud Vision OCR to extract text, then send to translation.
- **Conversation mode**
  - Two‑way voice conversations.
  - Speech‑to‑text using Android Speech Recognizer.
  - Automatic translation and text‑to‑speech playback in each participant’s language.
- **Text‑to‑Speech (TTS)**
  - Listen to translated text in the selected language.
- **Offline model management**
  - Download / delete ML Kit language models per language.
  - Display of currently downloaded models.
- **Error handling & UX**
  - Network‑aware error messages.
  - Connection diagnostics and simple dialogs for common issues.

## Tech Stack

- **Platform**
  - Android app, minSdk **28**, targetSdk **31**
  - Gradle wrapper **7.2**, Android Gradle Plugin **7.1.1**
- **Language**
  - Java (Android)
- **Frameworks & Libraries (app module)**
  - AndroidX AppCompat, Material Components, ConstraintLayout, RecyclerView, Media, Legacy Support v4
  - Google ML Kit: `com.google.mlkit:translate`
  - Google HTTP/JSON client & Vision API bindings:
    - `com.google.api-client:google-api-client-android`
    - `com.google.http-client:google-http-client-gson`
    - `com.google.apis:google-api-services-vision`
  - Networking: `com.android.volley:volley`
  - Collections: `org.apache.commons:commons-collections4`
  - Image UI: `de.hdodenhof:circleimageview`
  - MultiDex support
- **Scanning / Computer Vision (scanlibrary module)**
  - Custom scan library using native OpenCV binaries (`.so` and `.a` libs)
  - JNI bindings and OpenCV Java SDK (under `scanlibrary/src/main/jni/sdk/java`)
- **Build & Tools**
  - Gradle (multi‑module)
  - Android Studio / IntelliJ IDEA
- **External Services**
  - **Google Cloud Translation API**
  - **Google Cloud Vision API (OCR)**
  - **Sapling API** for grammar checking

## Project Structure

High‑level tree (simplified):

```text
Translation App/
  Translation App/
    app/
      build.gradle
      src/
        main/
          AndroidManifest.xml
          java/com/example/translate_objecttext/
            MainActivity.java
            HomeActivity.java
            ConversationActivity.java
            ObjectTextCurved.java
            TranslateFragment.java
            fragment_text_crv.java
            ObjectFragment_crv.java
            TranslateViewModel.java
            Helper.java
            Constant.java
            ... (UI helpers, fragments, models)
          res/
            layout/ (activities, fragments, bottom sheet, etc.)
            values/ (strings, colors, dimens, themes)
            drawable*/ (icons, shapes)
            menu/ (app & bottom navigation menus)
            xml/provider_paths.xml
        androidTest/, test/
    scanlibrary/
      build.gradle
      src/
        main/
          AndroidManifest.xml
          java/com/scanlibrary/
            ScanActivity.java
            ScanFragment.java
            ResultFragment.java
            Utils.java
            ... (scanner UI & helpers)
          jni/
            sdk/ (OpenCV headers, Java bindings, etc.)
          libs/
            armeabi*/ x86*/ ... (OpenCV native libs)
          res/ (scan UI layouts, drawables, menus)
    build.gradle
    settings.gradle
    gradle.properties
    gradle/wrapper/gradle-wrapper.properties
LICENSE
app-debug.apk
```

## Prerequisites

- **Tools**
  - Android Studio (Arctic Fox or newer recommended)
  - JDK 8 or compatible Java runtime
  - Android SDK Platform 31 and Build‑Tools (matching `compileSdk 31`)
  - A physical Android device or emulator with:
    - Android 9 (API 28) or higher
    - Camera and microphone (for image translation and voice conversation)
- **Cloud Credentials**
  - Google Cloud project with:
    - Cloud Translation API enabled
    - Cloud Vision API enabled
  - Sapling API account and keys

## Installation

1. **Clone or copy the project**

   ```bash
   git clone <your-repo-url>
   cd "Translation App/Translation App"
   ```

2. **Open in Android Studio**
   - `File` → `Open...` → select the inner `Translation App` directory that contains `settings.gradle`.
   - Let Android Studio sync Gradle and download dependencies.

3. **Configure Android SDK**
   - In `File` → `Project Structure` → `SDK Location`, ensure an SDK with API level **31** is installed.

4. **Configure signing (optional for debug)**
   - Debug builds use the default debug keystore.
   - For release builds, configure a signing config in `app/build.gradle` or via Android Studio.

## Configuration

### API Keys & Endpoints

All keys in the repository are placeholders and must be replaced with real values before running in production.

- **Google Cloud Translation & Detection**
  - Defined in `Constant.java`:
    - `BASE_URL`, `BASE_URL1` – Translation endpoint (`https://translation.googleapis.com/language/translate/v2`)
    - `DETECT_URL` – Detect‑language endpoint (`/detect`)
    - `KEY` – Google Cloud API key
  - Replace `YOUR_API_KEY_HERE` with your Translation API key:

  ```java
  public static final String BASE_URL =
      "https://translation.googleapis.com/language/translate/v2?key=YOUR_API_KEY";
  public static final String KEY = "YOUR_API_KEY";
  public static final String BASE_URL1 = "...";
  public static final String DETECT_URL = "...";
  ```

- **Google Cloud Vision (OCR)**
  - Used in `ObjectFragment_crv` via `CLOUD_VISION_API_KEY = Constant.KEY;`
  - Ensure the same `KEY` has Vision API access or introduce a separate key if desired.

- **Sapling Grammar API**
  - In `TranslateFragment`:
    - `key1`, `key2` are used to call `https://api.sapling.ai/api/v1/edits`.
  - Replace with your Sapling API keys and consider moving them to a secure configuration source for production builds.

### Android Permissions

The app requests:

- `INTERNET`
- `READ_EXTERNAL_STORAGE`, `WRITE_EXTERNAL_STORAGE`
- `CAMERA`
- `RECORD_AUDIO`

Runtime permissions are requested where required (camera, storage, microphone).  
Ensure target devices grant these permissions for full functionality.

### Optional: Environment / Build‑time config

For production, you may wish to:

- Move API keys into `local.properties` or Gradle `buildConfigField`s.
- Use different endpoints/keys for debug vs release in `app/build.gradle`.

## Usage

### Running the App

1. Connect an Android device or start an emulator (API 28+).
2. In Android Studio, select the `app` run configuration.
3. Click **Run** or use:

   ```bash
   ./gradlew :app:installDebug
   ./gradlew :app:assembleDebug
   ```

4. Launch the app (named **Translation App**) on the device.

### Main Flows

- **Home screen (`HomeActivity`)**
  - **Translate Text**: opens `MainActivity` → `TranslateFragment`.
  - **Translate from Picture**: opens `ObjectTextCurved` (picture translation mode).

- **Text Translation**
  - Type text into the source field.
  - Choose source and target languages from the spinners.
  - Optionally download offline models for either language.
  - Tap **Translate**; text is first corrected via Sapling, then translated.
  - Tap the speaker icon to have the translated text read aloud.

- **Picture Translation**
  - Choose camera or gallery to provide an image.
  - Use the scanner UI (crop rectangle) to select the document or text region.
  - Confirm the crop; OCR runs, then the recognized text is translated to the selected language.

- **Conversation Mode**
  - Open **Conversation** from the menu or the conversation icon.
  - Select source and target languages.
  - Use the source/target microphone buttons to speak.
  - Speech is recognized, translated, displayed in both panes, and read aloud with TTS.

## API Endpoints (External Services)

- **Google Cloud Translation API**
  - **Method**: `POST`
  - **URL**: `https://translation.googleapis.com/language/translate/v2?key=<API_KEY>`
  - **Body (JSON)**:

    ```json
    {
      "q": "Text to translate",
      "target": "es"
    }
    ```

  - **Usage**: text and conversation translation (`TranslateFragment`, `ConversationActivity`).

- **Google Cloud Translation Detect API**
  - **Method**: `POST`
  - **URL**: `https://translation.googleapis.com/language/translate/v2/detect?key=<API_KEY>`
  - **Body**:

    ```json
    { "q": "Text whose language should be detected" }
    ```

  - **Usage**: optional detection helper in `TranslateFragment`.

- **Sapling Grammar API**
  - **Method**: `POST`
  - **URL**: `https://api.sapling.ai/api/v1/edits`
  - **Body**:

    ```json
    {
      "key": "<SAPLING_API_KEY>",
      "text": "Original user input",
      "session_id": "test_session"
    }
    ```

  - **Usage**: returns a list of `edits` which are applied locally to produce corrected text.

- **Google Cloud Vision API (OCR)**
  - **Method**: `POST`
  - **URL**: `https://vision.googleapis.com/v1/images:annotate?key=<API_KEY>`
  - **Body**: constructed in `ObjectFragment_crv` with a `TEXT_DETECTION` feature and base64‑encoded image.
  - **Usage**: extract text from captured/scanned images.

## Example Usage

- **Translate English sentence to Spanish**
  - Open **Translate Text**.
  - Set **Source** to `English`, **Target** to `Spanish`.
  - Type: `I would like a cup of coffee.` and tap **Translate**.
  - Wait for grammar correction + translation; read the translated sentence or tap the speaker icon.

- **Translate a printed document**
  - Open **Translate from Picture**.
  - Take a photo of the document or choose one from the gallery.
  - Adjust the crop area so that only the document is selected and confirm.
  - The detected text is displayed and translated to the chosen language.

- **Bilingual conversation**
  - Open **Conversation**.
  - Set **Source language** to `English`, **Target language** to `French`.
  - Tap the **source microphone** and speak; your sentence appears in English and French, and the French text is spoken aloud.
  - Tap the **target microphone** for the other participant; the process is mirrored in the opposite direction.

## Contributing

- **Fork & branch**
  - Fork the repository and create a feature branch:

    ```bash
    git checkout -b feature/my-change
    ```

- **Code style**
  - Follow standard Android/Java best practices.
  - Keep API keys and secrets out of version control where possible.

- **Testing**
  - Run unit tests and instrumentation tests where applicable:

    ```bash
    ./gradlew testDebug
    ./gradlew connectedDebugAndroidTest
    ```

- **Pull requests**
  - Describe the motivation and changes clearly.
  - Include screenshots or screen recordings for UI changes when possible.

## License

This project is licensed under the **MIT License**.  
See the `LICENSE` file in the repository root for full license text.

