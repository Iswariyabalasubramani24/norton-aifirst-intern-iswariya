# norton-aifirst-intern-iswariya

Android Scam Message Detector prototype built using Jetpack Compose + Kotlin with an AI-assisted development workflow.

## 📌 Project Overview

This app allows users to paste or type a suspicious message (SMS, email, snippet, or URL) and receive an AI-powered risk assessment.  
It is inspired by Norton Genie’s scam detection experience.

### 🔧 Built With
- Jetpack Compose for declarative UI  
- Kotlin  
- MVVM Architecture (ViewModel + StateFlow)  
- Retrofit for API communication  
- AI Coding Assistant — Claude CoWork  

## 🔍 Norton 360 Product Observations

### 🧩 UX Pattern
The entire app is driven by a three-layer feature state:  
**not set up → active → risk found**

### 💡 Interesting Observations
- App Security scans newly installed apps automatically in the background  
- Norton is clearly a multi-module app — each feature behaves as an independent module with its own navigation graph  

### ⚠️ Areas for Improvement
- Scan results do not survive process death — reopening the app clears results  
- Protection report UI could be improved and made more responsive across devices  

## 🎯 Why Option B?

After exploring Norton 360, I chose **Norton Genie** because I wanted to prototype a similar experience using a real AI API integrated into a native Android UI.  
This also aligns strongly with the **AI-first engineering focus** of this role.

## ⚙️ Setup Instructions

### ▶️ Step 1 — Open the Project
1. Launch Android Studio Panda 3 Patch 1  
2. Go to **File → Open**  
3. Select the `ScamShield` folder  
4. Allow Gradle to sync  

### 🔑 Step 2 — Configure the API Key

The app uses the **Groq API** for AI scam detection. Add your API key in `local.properties`:

groq.api.key=YOUR_GROQ_API_KEY_HERE 

Note: Your local.properties file already contains the Android SDK path (sdk.dir).
Simply append the groq.api.key line below it.

👉 Get a free API key at: https://console.groq.com

### ▶️ Step 3 — Run the App

Run the app on an emulator or physical device directly from Android Studio.

📦 Key Dependencies
Groq API via Retrofit + OkHttp
(model: llama-3.3-70b-versatile or similar)
Jetpack Compose (BOM 2025.05.00)
Hilt for dependency injection
Kotlin Coroutines for async operations


### 📱 App Screenshots

<p align="center">
  <img src="https://github.com/user-attachments/assets/a53914d0-b00c-490e-8f00-ec7c158a14ee" width="45%" />
  <img src="https://github.com/user-attachments/assets/26f7e834-c36f-4699-a7c7-5f0148ba585b" width="45%" />
</p>
<p align="center">
  <img src="https://github.com/user-attachments/assets/5a70ef2a-e2d5-422e-8419-10ee5f5c18e0" width="45%" />
  <img src="https://github.com/user-attachments/assets/37b83fed-bde3-4c45-964e-3fb8b9d59957" width="45%" />
</p>

## AI Interaction Log

Throughout development, I used AI(claude cowork) as a coding assistant to speed up implementation, improve code quality, and refine areas. I did not use AI as a source of truth; instead, I reviewed, adjusted, and sometimes restructured its output to better fit my architecture and code quality.

### 1. Generating DTOs for the data layer
**Prompt:**  
Implement the data layer with clean architecture and need help generating DTOs for a Groq/OpenAI-style chat completion API. Please create Kotlin data classes including AnalysisRequestDto (with fields model: String, max_tokens: Int defaulting to 512, and messages: List), MessageDto (with role: String and content: String), AnalysisResponseDto (containing a list of ChoiceDto), and ChoiceDto (containing a MessageDto). Ensure the DTOs follow clean Kotlin conventions and are compatible with Retrofit and Gson parsing, with clear structure and readability suitable for a production-quality mobile app.

**Goal:**  
To accelerate the initial setup of the data layer while keeping the API models aligned with the expected request/response structure.

**Response:**  
It generated the DTO classes correctly, but initially grouped multiple DTOs into fewer files.

**My refinement:**  
I refactored the output so that each DTO lived in its own file:
- `MessageDto.kt`
- `AnalysisRequestDto.kt`
- `ChoiceDto.kt`
- `AnalysisResponseDto.kt`

I made this change because separate files made the structure cleaner, easier to navigate, and more aligned with clean architecture practices.

### 2. Setting up networking dependencies and API interface
**Prompt:**  
Add Retrofit 2.11.0, OkHttp 4.12.0, Gson 2.11.0, and kotlinx-coroutines-android 1.10.2 for networking and async GroqApi Retrofit interface in data/remote/api/ — single suspend function: POST
openai/v1/chat/completions, accepts AnalysisRequestDto, returns AnalysisResponseDto.

**Goal:**  
To make sure the dependency setup and the Retrofit interface were both correct and minimal.

**Response:**  
AI gave the dependency list and the Retrofit API interface with the correct request/response types.

**Reviewed the Response:**  
Reviewed the versions and ensured the interface fit my package structure in `data/remote/api`. I also verified that the endpoint and suspend function signature matched the rest of my architecture.

### 3. Implementing the analysis mapper
**Prompt:**  
I need to implement an AnalysisMapper. Please create a function parseAnalysisResult(raw: String): AnalysisResult that processes the AI response.The function should strip markdown code fences (such as `json or `), parse the cleaned string into JSON using Gson, and extract the fields riskLevel, confidence, and explanation. The riskLevel should be mapped case-insensitively to the RiskLevel enum (SAFE, SUSPICIOUS, DANGEROUS), defaulting to SUSPICIOUS if unknown. The confidence should be converted to an integer between 0 and 100, defaulting to 50 if missing or invalid. The explanation should default to "Unable to determine risk level." if not present. The implementation should be defensive and handle malformed or partial responses gracefully while keeping the code clean and readable.  

**Goal:**  
The AI response from the model was not directly usable in the app, so I needed a parsing layer that could convert raw response text into my domain model.

**Response:**  
It generated a first version of the parser that handled code fences, Gson parsing, and default values.

**Refinement:**  
Reviewed the logic and used it as a starting point, but later identified weaknesses in how model output could vary. For example, I saw that output formats could still drift, so treated the parser as a defensive conversion layer rather than assuming the AI would always return perfect JSON.

### 4. Migrating dependency injection to Hilt
**Prompt:**  

Add Hilt Navigation Compose 1.2.0 for hiltViewModel() support in Compose. Just register hilt-navigation-compose in the version catalog and add it as an implementation dependency in the app module.Create a NetworkModule object annotated with @Module and @InstallIn(SingletonComponent::class) that provides a singleton OkHttpClient with  30s connect, 60s read, and 30s write timeouts plus an interceptor that adds a Bearer token from BuildConfig.GROQ_API_KEY, and a singleton GroqApi created from Retrofit using that client. Create an abstract RepositoryModule with @Binds to tie ScanRepositoryImpl to the ScanRepository interface as a singleton. Create ScamMessageDetectorApplication class extending Application annotated with @HiltAndroidApp 

**Goal:**  
Initially used a manual `AppContainer`, but wanted to migrate to a more modern dependency injection approach that is standard in Android apps.

**Response:**  
AI responded with a working Hilt setup with modules and injection annotations.

### 5. Writing and refining unit tests
**Prompt:**  
Write JUnit4 unit tests for ScanViewModel in an Android Kotlin app using kotlinx-coroutines-test with UnconfinedTestDispatcher. The ViewModel has onInputChanged(text) and onAnalyze() methods, and exposes uiState with inputText, isLoading, result, and error fields. onAnalyze() does nothing if input is blank, calls AnalyzeInputUseCase on valid input, and sets either the result or an error message. Cover all the main success and failure flows.    

**Response:**  
AI generated the test structure, including coroutine-based ViewModel tests.

**Refinement:**  
The tests look good but the fake repository is being recreated inside every single test. Extract it into a shared fakeUseCase() helper at the top of the class so tests only pass in the response they need. Also the test for blank input is  missing an assertion that inputText stays empty — add that check.

## AI Code Review
**Prompt 1:**
Please do a code review of the app. It is built with Kotlin, Jetpack Compose, Hilt for dependency injection, Retrofit + OkHttp for networking, and calls the Groq API for AI-based scam detection. Review the code for security issues, correct Hilt scoping and bindings, proper error and exception handling, Compose UI best practices, ViewModel and state management, clean architecture layer separation, and overall code quality. For every issue you find, tell me which file it is in, what the problem is, why it matters, and what I should change. After the review, give me a short summary of the overall code quality and a list of the most important things to fix first.

**Prompt 2:**
Fix the following issues in the app codebase. 1. Add @SerializedName to every field in RequestDto.kt and ResponseDto.kt because release builds are breaking without it due to ProGuard/R8 minification. 2. In ScanRepositoryImpl.kt, re-throw CancellationException before the broad catch (e: Exception) block so coroutine cancellation is not swallowed and incorrectly treated as a normal error. 3. In ScanViewModel.kt, stop exposing raw e.message directly to the UI and instead map exceptions to user-friendly error messages suitable for end users. 4. In AnalysisMapper.kt, replace the unsafe gson.fromJson(..., Map::class.java) cast with JsonParser-based parsing so the JSON handling is safer and more robust.

**Prompt 3:**
Review the fixes. Check if anything was missed, if any new issue was introduced, and whether there is anything else in those same files that could still be improved. Then scan the rest of the codebase for similar patterns that have the same problems and flag those too. Keep the feedback focused and only call out issues that needs attention.

**Response:**
One residual coupling concern
The presentation layer now has a direct dependency on Retrofit. This is the correct pragmatic fix for a small app, but it does couple a ViewModel to the network library. The clean-architecture alternative is to catch HttpException in ScanRepositoryImpl and rethrow a typed domain exception (e.g., a sealed ScanException class), keeping the data layer as the only place that knows about Retrofit. Not a blocker, but worth the note if the app grows.
Nothing else in the wider codebase

## Reflection
In each case, I treated AI output as a starting point rather than final code making the implementation time-saving. The most important part of my workflow was reviewing, restructuring, and refining the generated output so it fit my project architecture, goals of clean code, handling error in an efficient way and to cover all the edge cases. Training the AI with right prompts is a key and that was the learning part. Important part in Code Review is learning, So AI fetches some good information which a developer does not know or aware of.






