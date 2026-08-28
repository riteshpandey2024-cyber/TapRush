# Tap Rush — 3-Level Android Game (Android Studio Project)

A simple tap-the-moving-target game with 3 levels, per-level countdown timers,
and level-unlock progression saved on the device.

## How the game works
- **Level 1**: tap the target 10 times within 30 seconds.
- **Level 2**: tap the target 15 times within 25 seconds. Locked until Level 1 is won.
- **Level 3**: tap the target 20 times within 20 seconds. Locked until Level 2 is won.
- Progress (which levels are unlocked) is saved with `SharedPreferences`, so it
  persists even after closing the app. Use the "Reset Progress" button on the
  home screen to start over.

You can change the difficulty/timing per level by editing `levelConfig` in
`GameActivity.kt`.

## How to open and build the APK

1. Install **Android Studio** (Giraffe/Koala or newer) if you don't have it:
   https://developer.android.com/studio
2. Unzip this project.
3. In Android Studio: **File → Open** → select the unzipped `LevelGame` folder.
4. Let Gradle sync finish (Android Studio will auto-download the Gradle
   wrapper and any missing SDK components the first time — make sure you're
   online).
5. Plug in a device (with USB debugging enabled) or start an emulator.
6. Click the green **Run ▶** button, or go to
   **Build → Build Bundle(s) / APK(s) → Build APK(s)** to just generate the
   APK file without running it.
7. The built APK will appear under:
   `app/build/outputs/apk/debug/app-debug.apk`
8. To install manually: `adb install app-debug.apk`, or copy the APK to your
   phone and open it (you may need to allow "install from unknown sources").

### Building a signed release APK (optional, for sharing/publishing)
Use **Build → Generate Signed Bundle / APK** in Android Studio, create (or
choose) a keystore, and follow the wizard. This produces a release APK
suitable for distributing outside the Play Store, or for uploading to the
Play Store as an AAB.

## Project structure
```
LevelGame/
├── app/
│   ├── build.gradle
│   └── src/main/
│       ├── AndroidManifest.xml
│       ├── java/com/example/levelgame/
│       │   ├── MainActivity.kt      # level-select screen, lock/unlock UI
│       │   ├── GameActivity.kt      # gameplay: timer, scoring, win/lose
│       │   └── Prefs.kt             # SharedPreferences unlock storage
│       └── res/
│           ├── layout/activity_main.xml
│           ├── layout/activity_game.xml
│           └── values/ (strings, colors, theme)
├── build.gradle
└── settings.gradle
```

## Customizing
- **Change difficulty**: edit the `levelConfig` map at the top of
  `GameActivity.kt` (targets needed + time limit per level).
- **Add more levels**: add an entry to `levelConfig`, add a button in
  `activity_main.xml`, wire it up in `MainActivity.kt`, and bump
  `Prefs.MAX_LEVEL`.
- **Change the game itself**: the win condition and target behavior live in
  `GameActivity.kt` — swap the tap-the-button mechanic for whatever gameplay
  you want (a maze, a quiz, a runner, etc.) while keeping the same timer +
  unlock scaffolding.
# TopRush
