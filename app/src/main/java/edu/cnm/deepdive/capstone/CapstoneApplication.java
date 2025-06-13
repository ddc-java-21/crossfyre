/*
 *  Copyright 2025 CNM Ingenuity, Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package edu.cnm.deepdive.capstone;

import android.app.Application;
import dagger.hilt.android.HiltAndroidApp;

/**
 * Initializes (in the {@link #onCreate()} method) application-level resources that cannot be
 * handled with Hilt dependency injection. This class <strong>must</strong> be referenced in
 * {@code AndroidManifest.xml}, or it will not be loaded and used by the Android system.
 */
@HiltAndroidApp
public class CrossfyreApplication extends Application { // TODO Rename this class as aappropriate.

  @Override
  public void onCreate() {
    super.onCreate();
  }

}
