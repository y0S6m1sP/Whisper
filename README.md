# Whisper

Whisper is building with MVI architecture with Jetpack Compose.

## Feature

**Whisper** use firebase to connect two users with uuid. User can send message to another user.

### Real-time chat
![chat](./demo/chat.gif)

### Sticky header and save state
![sticky_header](./demo/sticky_header.gif)

### Crop image and Upload to Firebase
![crop_upload](./demo/crop_upload.gif)

## Structure
![structure-w50](./demo/structure.png)

## Build

Put your own `google-services.json` in `app` folder, then build the project.

## Testing (WIP)

## Tech Stack

- [Jetpack Compose](https://developer.android.com/jetpack/compose)
- [Firebase](https://firebase.google.com/)
- [Hilt](https://developer.android.com/training/dependency-injection/hilt-android)
- [Coroutines](https://developer.android.com/kotlin/coroutines)
- [Room](https://developer.android.com/jetpack/androidx/releases/room)
- [Retrofit](https://square.github.io/retrofit/)