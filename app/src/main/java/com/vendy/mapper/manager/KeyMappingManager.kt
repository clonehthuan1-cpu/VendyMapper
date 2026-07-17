package com.vendy.mapper.manager

data class KeyBinding(
    val key: String,
    val action: String,
    val targetX: Float = 0f,
    val targetY: Float = 0f,
    val isActive: Boolean = true
)

class KeyMappingManager {

    private val bindings = mutableMapOf<String, KeyBinding>()

    fun loadPreset(preset: List<KeyBinding>) {
        bindings.clear()
        preset.forEach { bindings[it.key] = it }
    }

    fun setBinding(binding: KeyBinding) {
        bindings[binding.key] = binding
    }

    fun removeBinding(key: String) {
        bindings.remove(key)
    }

    fun resolve(key: String): KeyBinding? = bindings[key]?.takeIf { it.isActive }

    fun allBindings(): List<KeyBinding> = bindings.values.toList()

    companion object {
        val WASD_PRESET = listOf(
            KeyBinding("W", "Swipe Up"),
            KeyBinding("A", "Swipe Left"),
            KeyBinding("S", "Swipe Down"),
            KeyBinding("D", "Swipe Right"),
            KeyBinding("Space", "Jump"),
            KeyBinding("Shift", "Crouch"),
            KeyBinding("E", "Interact"),
            KeyBinding("Q", "Quick Action")
        )
    }
}
