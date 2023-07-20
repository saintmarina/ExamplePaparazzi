# ExamplePaparazzi
Sample Android native project implementing screenshot testing with Paparazzi

## Introduction

Snapshot tests are a useful tool for making sure your UI does not change unexpectedly.

A typical snapshot test case renders a UI component, takes a snapshot, then compares it to a reference snapshot file stored alongside the test. The test will fail if the two snapshots do not match: either the change is unexpected, or the reference snapshot needs to be updated to the new version of the UI component.


**Paparazzi** is a screenshot UI regression testing framework whose key feature is its ability to render compose components **without needing an emulator**, which makes it friendly to GitHub CI setup.

It achieves this by leveraging the undocumented IDE compose preview facility. Even though this is a brittle hack, it seems that the android compose team is adopting this framework (see commit [here](https://github.com/androidx/androidx/commit/07c3a14b8d488e44dea5d959469b168ff1fa7835)), so maybe there's hope for something good in the far future.

## Limitations to keep in mind

1. Paparazzi can't be placed in a module with other testing dependencies (like Robolectric, Espresso). When placed in the same module Paparazzi breaks tests that are running with the other frameworks. See more about this issue [here](https://github.com/cashapp/paparazzi/issues/622).
2. Paparazzi won't do any network requests to get resources. If you have a components that perform network calls in order to get resources, the screenshot tests will break in mysterious ways!


## How to set up Paparazzi?

Write a snapshot test in the unit test folder:

```kotlin
    import ...

    class MyComponentTest {
        @get:Rule
        val paparazzi = Paparazzi()
    
        @Test
        fun myComponentScreenshotTest() {
            paparazzi.snapshot {
                MyComponentPreview()
            }
        }
    }
```

## How to test a composable larger than the screenshot size?

When you have long scrollable content and you want to fit it in 1 snapshot test, you must add `DeviceConfig` object to the Paparazzi Rule and set its' height parameter to be something large. Here is an example of a test that take a screenshot of long content:

```kotlin
import ...

    class MyComponentTest {
        @get:Rule
        val paparazzi = Paparazzi(deviceConfig = DeviceConfig(screenHeight = 4000))

        @Test
        fun myComponentScreenshotTest() {
            paparazzi.snapshot {
                MyComponentPreview()
            }
        }
    }


```

Here is an example of a random test that implements `DeviceConfig(screenHeight = 4000)`

<img width="158" alt="Screenshot 2023-03-22 at 1 52 08 PM" src="https://user-images.githubusercontent.com/44619718/226994523-72f2d166-b644-47d7-b548-38062747dea0.png">

`DeviceConfig` has other settings that you might want to use. Here are all the options:

<img width="838" alt="Screenshot 2023-03-22 at 1 19 46 PM" src="https://user-images.githubusercontent.com/44619718/226995088-c5abe582-cfb5-4860-a1b9-1bfba8d2947c.png">


## How to test a small composable?

When you have really small compose to snapshot test, you must add `SessionParams.RenderingMode.SHRINK`. Here is an example of a test that take a screenshot of small content:

```kotlin
import ...

    class MyComponentTest {
        @get:Rule
        val paparazzi = Paparazzi(renderingMode = SessionParams.RenderingMode.SHRINK)
    
        @Test
        fun myComponentScreenshotTest() {
            paparazzi.snapshot {
                MyComponentPreview()
            }
        }
    }

```

## How to run the tests?

### Create golden screenshots
Golden screenshots are the ground truth of what components should look like. When adding new Paparazzi tests, one must generate the golden screenshots with the following command:

```bash
# Adjust with your own project module path
./gradlew :myUi:recordPaparazziDebug
```

**Important:** when successfully recorded golden screenshots, the output is the same as when running the tests. Here is an example:

<img width="800" alt="image" src="https://github.com/saintmarina/ExamplePaparazzi/assets/44619718/c466f3aa-d770-46f0-a58c-0061289e7e21">

### Validate against the golden screenshots

```bash
# Adjust with your own project module path
./gradlew :myUi:verifyPaparazziDebug
```

### Example of a failing test

After adding a test, one should always try to break it by slightly modifying the component implementation to validate that we are actually testing something. When a Paparazzi test fails, output diffs are located in `snapshot-test/out/failures`. Here's an example of how the `SampleTypographyTitle` fails when modifying the padding of certain elements:

![image](https://user-images.githubusercontent.com/44619718/204708309-dc639d38-2e71-4e46-9239-53c5bc9d586b.png)

To see why your test failed, checkout Paparazzi report:

<img width="800" alt="Screenshot 2023-07-20 at 3 05 54 PM" src="https://github.com/saintmarina/ExamplePaparazzi/assets/44619718/9ad14995-1290-45a3-ba80-4cb3de49a4cc">

Example of a report:

<img width="800" alt="image" src="https://github.com/saintmarina/ExamplePaparazzi/assets/44619718/7457dcc6-3f2c-441c-a7a8-f0b56bff8da6">

## Troubleshooting

1. **Issue**: Tests fail with `java.io.FileNotFoundException: Missing platform version 33. Install with sdkmanager --install "platforms;android-33"`, but *you know for sure you have SDK 33 installed*.
**Solution**: Make sure that your `ANDROID_HOME=/Users/<User>/Android/Sdk` has no typos and is pointing to a valid location. After you change the variable, restart Android Studio.

2. **Issue**: Running the `recordPaparazziDebug` task generates old screenshot class or function names that no longer exist in your source code.
**Solution**: It is likely that the task is re-using an old cache. To purge this cache, run the task with `./gradlew <My snapshot test>:recordPaparazziDebug --rerun-tasks --no-build-cache`.

3. **Issue**: When running the test you get the following error: `java.lang.UnsupportedOperationException: class redefinition failed: attempted to delete a method`
**Solution**: Update your jdk version to the JDK 11. And don't forget to set new JAVA_HOME variable.
