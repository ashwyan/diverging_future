# Diverging Futures

An Augmented Reality (AR) Civic Engagement App designed to expose hidden histories and imagine plural futures through speculative design.

![Diverging Logo](app/src/main/res/drawable/diverging_logo_gif.gif)

## 🌟 Overview

**Diverging Futures** is a place-based experience that invites users to rethink their environment. Rooted in the **Cone of Possibility** by Anthony Dunne and Fiona Raby (2013), the app allows users to stand at specific physical locations and "swipe" through four different speculative lenses to see what was, what is, and what could be.

The project currently focuses on sites within the **UC Berkeley** area, specifically highlighting the Shellmound Site at Faculty Glade.

## 🚀 Key Features

*   **Animated Branding**: Fully integrated animated GIF logo across the splash screen, loading states, and navigation bars.
*   **Cone of Possibility Onboarding**: A 4-step interactive walkthrough explaining the speculative design methodology.
*   **Geo-Locked AR Experience**: Use the phone's camera to activate AR features at specific coordinates.
*   **Four Speculative Lenses**:
    *   **What is?**: Present condition and hidden history.
    *   **What should?**: Desired, achievable native stewardship (Restoring poppy fields).
    *   **What could?**: Imaginative paths and reflection spaces (Native structures).
    *   **What if?**: Radical reimagining and land rematriation (Restored landscapes).
*   **Community Voice & Voting**: Real-time voting on preferred futures, with data synced directly to a Google Spreadsheet.
*   **Civic Petition**: A built-in form for users to sign their support for specific community-led design initiatives.

## 🛠 Technical Stack

*   **Language**: 100% Kotlin
*   **UI Framework**: Jetpack Compose (Modern, declarative UI)
*   **AR Engine**: [SceneView](https://github.com/SceneView/sceneview-android) (ARCore wrapper)
*   **Navigation**: Compose Navigation with custom slide/fade transitions.
*   **Image Loading**: [Coil](https://coil-kt.github.io/coil/) with GIF decoding support.
*   **Backend**: Google Apps Script Webhook integration for real-time data collection.

## 📦 Getting Started

### Prerequisites
*   Android Studio Ladybug or newer.
*   An ARCore-compatible Android device (running Android 8.0 or higher).
*   Active Internet connection for data submission.

### Installation
1.  Clone the repository:
    ```bash
    git clone https://github.com/ashwyan/DivergingFutures.git
    ```
2.  Open the project in Android Studio.
3.  Sync Gradle and build the project.
4.  Run on your physical device.

### Configuration (Analytics & Webhook)
To receive user votes and signatures in your own Google Sheet:
1.  Navigate to `app/src/main/java/com/diverging/futures/data/PlaytestAnalytics.kt`.
2.  Replace the `WEBHOOK_URL` constant with your Google Apps Script deployment URL.
3.  Ensure your Script deployment is set to **"Who has access: Anyone"**.

## 📖 Research Context
This app serves as a tool for "Speculative Civic Engagement." It moves beyond traditional surveys by providing immersive visual evidence of alternative futures, encouraging users to advocate for "Preferable" outcomes rather than just accepting "Probable" ones.

---
Developed at UC Berkeley.
