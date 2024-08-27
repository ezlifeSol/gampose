# Gampose Library

Gampose is an Android library designed for developing games and graphical applications using Jetpack
Compose. It provides a range of tools and components for managing game objects, handling collisions,
processing input events, and managing audio.

<img src="https://github.com/ezlifeSol/gampose/blob/main/dino_run.gif" alt="Dino Jumping Over Cactus" width="700"/>
<img src="https://github.com/ezlifeSol/gampose/blob/main/galaxy_invader.gif" alt="Galaxy Invader" height="700"/>

## Features

### GameSpace

- **GameSpace**: A composable function that provides a game loop and rendering environment. It
  handles game updates and drawing operations within a Composable context. The `GameSpace`composable
  integrates seamlessly with Jetpack Compose and provides a framework for creating game loops,
  handling updates, and rendering custom drawings. It also supports lifecycle management to pause
  the game when the app goes to the background.

### Game Objects and Sprites

- **GameObject**: A fundamental component in the game environment that includes properties like
  size, position, rotation angle, and color. It also supports input events such as dragging and
  clicking.
- **GameSprite**: A specialized `GameObject` used to display images. It supports displaying images
  from drawable resources or asset paths.
- **GameAnimSprite**: An advanced `GameObject` for displaying animated sprites. It supports
  animations from sprite sheets or bitmaps, looping, and various interactive events.

### Colliders and Shapes

- **Collider**: Objects that can handle collisions and check for intersections between game objects.
  For example, `CircleCollider` and `RectangleCollider` handle collision detection for circles and
  rectangles respectively.
- **Shape**: Geometric objects such as `Circle` and `Rectangle` that colliders can encompass.
- **ColliderSyncMode**: Enum class for synchronizing colliders with game objects, ensuring accurate
  collision detection.

### Audio System

- **AudioManager**: Provides functionality for playing background music and sound effects in the
  game. It supports playing audio from resource files and managing sound effects.

### Image Management

- **ImageManager**: A singleton object responsible for caching and retrieving images from asset
  paths. It improves performance by avoiding redundant loading of images and stores them in memory
  for quick access.

### Input Handling

- **OnDraggingListener**: Interface for handling drag events from the user.
- **OnCollidingListener**: Interface for handling collision events between colliders.

### Joystick

- **Joystick**: Provides a virtual joystick component with properties such as size, base image, and
  stick image. It supports drag events for controlling the joystick.

### GameVision

- **GameVision**: A class for representing the camera or viewport within the game. It defines the
  position and anchor point of the camera, with properties for `position` and `anchor`.
    - `position`: Position of the camera.
    - `anchor`: Anchor point of the camera.

### GameOutfit

- **GameOutfit**:  A class for representing the visual styling of the game environment. It allows
  customization of the background color of the game space.
    - `backgroundColor`: Color of the game space background.

### GameInput

- **GameInput**: A class for managing user input events on the game screen. It includes listeners for:
    - `onClick`: Callback for click events.
    - `onTap`: Callback for tap events with `Offset` parameter.
    - `onDoubleTap`: Callback for double-tap events with `Offset` parameter.
    - `onLongPress`: Callback for long press events with `Offset` parameter.
    - `onPress`: Callback for press events with `Offset` parameter.
    - `onDragging`: Listener for drag events.

## Getting Started

### Step 1. Add the JitPack repository to your build file

Add the following to your `settings.gradle` or `settings.gradle.kts` file:

#### Groovy DSL (settings.gradle)

```gradle
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        maven { url 'https://jitpack.io' }
    }
}
```

#### Kotlin DSL (settings.gradle.kts)

```kotlin
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        maven(url = "https://jitpack.io")
    }
}
```

### Step 2. Add Dependency

Add the following to your `build.gradle` or `build.gradle.kts` file:

#### Groovy DSL (build.gradle)

```gradle
dependencies {
    implementation 'com.github.ezlifeSol:gampose:1.4.5'
}
```

#### Kotlin DSL (build.gradle.kts)

```kotlin
dependencies {
  implementation("com.github.ezlifeSol:gampose:1.4.5")
}
```

### Example Usage

#### GameObject

```kotlin
GameObject(
    size = GameSize(100f, 100f),
    position = GameVector(50f, 50f),
    anchor = GameAnchor.TopLeft,
    color = Color.Blue,
    onClick = { /* Handle click */ },
    onDragging = detectDragging(
        onDrag = { _, dragAmount -> /* Handle drag */ },
        onDragEnd = { /* Handle drag end */ }
    )
)
```

#### GameSprite

```kotlin
// Using drawable resource
GameSprite(
    resourceId = R.drawable.example_sprite,
    size = GameSize(100f, 100f),
    position = GameVector(50f, 50f),
    anchor = GameAnchor.TopLeft,
    scale = GameScale(1f, 1f),
    angle = 0f,
    onClick = { /* Handle click */ }
)

// Using asset path
GameSprite(
    assetPath = "images/example_sprite.png",
    size = GameSize(100f, 100f),
    position = GameVector(50f, 50f),
    anchor = GameAnchor.TopLeft,
    scale = GameScale(1f, 1f),
    angle = 0f,
    onClick = { /* Handle click */ }
)

// Using bitmap
GameSprite(
    bitmap = myBitmap,
    size = GameSize(100f, 100f),
    position = GameVector(50f, 50f),
    anchor = GameAnchor.TopLeft,
    scale = GameScale(1f, 1f),
    angle = 0f,
    onClick = { /* Handle click */ }
)
```

#### GameAnimSprite

```kotlin
// Using sprite sheets
GameAnimSprite(
    assetPath = "images/example_spritesheet.png",
    col = 4,
    row = 4,
    step = 0.1f,
    size = GameSize(100f, 100f),
    position = GameVector(50f, 50f),
    anchor = GameAnchor.TopLeft,
    loop = true,
    onClick = { /* Handle click */ }
)

// Using bitmaps
GameAnimSprite(
    bitmaps = listOf(bitmapFrame1, bitmapFrame2, bitmapFrame3),
    step = 0.1f,
    size = GameSize(100f, 100f),
    position = GameVector(50f, 50f),
    anchor = GameAnchor.TopLeft,
    loop = true,
    onClick = { /* Handle click */ }
)
```

#### GameSpace

```kotlin
GameSpace(
    modifier = Modifier.fillMaxSize()
) {
    // Dino properties
    val dinoSprite = "dino/dino_jump.webp"
    val dinoSize = GameSize(200f, 200f)
    val dinoAnchor = GameAnchor.BottomLeft
    val dinoPosition by remember {
        mutableStateOf(GameVector(300f, gameSize.height))
    }
    // Draw dino sprite
    GameSprite(
        assetPath = dinoSprite,
        size = dinoSize,
        position = dinoPosition,
        anchor = dinoAnchor,
    )
}
```

<img src="https://github.com/ezlifeSol/gampose/blob/main/dino_example.jpg" alt="Dino Standing Demo" width="500"/>

#### Joystick

```kotlin
Joystick(
    position = GameVector(100f, 100f),
    size = GameSize(400f, 400f),
    stickSize = GameSize(200f, 200f),
    onDragging = { direction -> /* Handle joystick direction */ }
)
```

## Documentation

### `GameObject`

`GameObject` is a composable function for creating and managing game objects. It allows you to
specify various properties such as size, position, anchor, scale, angle, and color. It supports
collision detection, input events (clicks and dragging), and custom drawing.

**Parameters:**

- `modifier`: Modifier for styling the game object.
- `size`: The size of the game object.
- `position`: The position of the game object.
- `anchor`: The anchor point for positioning.
- `scale`: The scale of the game object.
- `angle`: The rotation angle of the game object.
- `color`: The color of the game object.
- `collider`: Optional collider for collision detection.
- `otherColliders`: Optional array of other colliders to check for collisions.
- `onColliding`: Optional listener for collision events.
- `onClick`: Optional callback for click events.
- `onTap`: Optional callback for tap events.
- `onDoubleTap`: Optional callback for double-tap events.
- `onLongPress`: Optional callback for long press events.
- `onPress`: Optional callback for press events.
- `onDragging`: Optional listener for dragging events.
- `content`: Composable content to be drawn inside the game object.

### `GameSprite`

`GameSprite` is a specialized version of `GameObject` for displaying images. It uses drawable
resources, asset paths, or bitmaps to render sprites and inherits all properties and functionality
from `GameObject`.

**Parameters:**

- `resourceId`: The drawable resource ID of the sprite image.
- `assetPath`: The path to the asset image. If `resourceId` is not provided, this will be used to
  load the image.
- `bitmap`: The bitmap image to display. If `resourceId` and `assetPath` are not provided, this will
  be used.
- `size`: The size of the sprite.
- `position`: The position of the sprite.
- `anchor`: The anchor point for positioning.
- `scale`: The scale of the sprite.
- `angle`: The rotation angle of the sprite.
- `color`: The color of the sprite.
- `collider`: Optional collider for collision detection.
- `otherColliders`: Optional array of other colliders to check for collisions.
- `onColliding`: Optional listener for collision events.
- `onClick`: Optional callback for click events.
- `onTap`: Optional callback for tap events.
- `onDoubleTap`: Optional callback for double-tap events.
- `onLongPress`: Optional callback for long press events.
- `onPress`: Optional callback for press events.
- `onDragging`: Optional listener for dragging events.

### `GameAnimSprite`

`GameAnimSprite` is a versatile extension of `GameObject` for displaying animated sprites using
sprite sheets. It supports both sprite sheets (using `assetPath`) and direct bitmaps for animations.
This function is ideal for rendering frame-by-frame animations, handling looping, and updating
frames based on elapsed time.

**Parameters:**

- `assetPath`: The path to the sprite sheet asset image if using sprite sheets. This parameter is
  not required if `bitmaps` is provided.
- `col`: The number of columns in the sprite sheet if using `assetPath`. This parameter is not
  required if `bitmaps` is provided.
- `row`: The number of rows in the sprite sheet if using `assetPath`. This parameter is not required
  if `bitmaps` is provided.
- `bitmaps`: A list of `ImageBitmap` objects representing the animation frames. This parameter is
  not required if `assetPath` is provided.
- `step`: The time in seconds between each frame update, determining the speed of the animation.
- `size`: The size of the animated sprite.
- `position`: The position of the animated sprite.
- `anchor`: The anchor point for positioning the animated sprite.
- `loop`: A boolean indicating whether the animation should loop when it reaches the end.
- `scale`: The scale of the animated sprite.
- `angle`: The rotation angle of the animated sprite.
- `color`: The color of the animated sprite.
- `collider`: Optional collider for collision detection.
- `otherColliders`: Optional array of other colliders to check for collisions.
- `onColliding`: Optional listener for collision events.
- `onClick`: Optional callback for click events.
- `onTap`: Optional callback for tap events.
- `onDoubleTap`: Optional callback for double-tap events.
- `onLongPress`: Optional callback for long press events.
- `onPress`: Optional callback for press events.
- `onDragging`: Optional listener for dragging events.

### `Joystick`

`Joystick` provides a virtual joystick component with a base and a movable stick. It allows for
interactive joystick controls with drag events.

**Parameters:**

- `modifier`: Modifier for styling the joystick.
- `position`: The position of the joystick.
- `sprite`: The drawable resource ID for the joystick base image.
- `size`: The size of the joystick base.
- `stickSprite`: The drawable resource ID for the joystick stick image.
- `stickSize`: The size of the joystick stick.
- `anchor`: The anchor point for positioning.
- `onDragging`: Callback for handling joystick drag events.

### `AudioManager`

`AudioManager` is a singleton object responsible for managing audio playback in the game. It handles
music playback using MediaPlayer and sound effects using SoundPool.

**Methods:**

- `playMusic(context: Context, @RawRes resId: Int, loop: Boolean = false, volume: Float = 1f)`:Plays
  background music from a raw resource. Parameters include context, resource ID, loop flag, and
  volume level.
- `startMusic()`: Starts

the currently loaded music if it was paused.

- `pauseMusic()`: Pauses the currently playing music.
- `stopMusic()`: Stops and releases the currently playing music.
- `registerSounds

(context: Context, @RawRes vararg resIds: Int)`: Registers sound effects from raw resources with
SoundPool.

- `playSound(@RawRes resId: Int)`: Plays a registered sound effect.
- `releaseAll()`: Releases all resources used by MediaPlayer and SoundPool.

### `ImageManager`

`ImageManager` is a singleton object that manages image caching and retrieval to improve performance
when loading images from assets in Jetpack Compose.

**Methods:**

- `getBitmap(context: Context, assetPath: String): ImageBitmap`: Retrieves an ImageBitmap from the
  cache or loads it from assets if not already cached.
- `splitSprite(context: Context, assetPath: String, col: Int, row: Int): List<ImageBitmap>`: Splits
  an image from assets into a grid of smaller images and caches the resulting images.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Contact

For any questions or issues, please open an issue on
the [GitHub repository](https://github.com/ezlifeSol/gampose) or
contact [ezlifesol2022@gmail.com](mailto:ezlifesol2022@gmail.com).
