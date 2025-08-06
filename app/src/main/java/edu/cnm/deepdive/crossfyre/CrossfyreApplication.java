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
package edu.cnm.deepdive.crossfyre;

import android.app.Application;
import dagger.hilt.android.HiltAndroidApp;

/**
 * Application class for the Crossfyre app.
 * <p>
 * This class initializes application-level resources that cannot be handled by Hilt dependency injection.
 * It must be declared in {@code AndroidManifest.xml} to be properly used by the Android system.
 */
@HiltAndroidApp
public class CrossfyreApplication extends Application {

  /**
   * Called when the application is starting, before any activity, service, or receiver objects
   * have been created.
   */
  @Override
  public void onCreate() {
    super.onCreate();
    // Initialize any application-level resources here if needed.
  }

}
