package com.vendy.mapper.manager

import com.vendy.mapper.model.Profile
import com.vendy.mapper.model.Settings
import java.util.UUID

class ProfileManager {

    private val profiles = mutableListOf<Profile>()

    fun createProfile(name: String, settings: Settings): Profile {
        val profile = Profile(id = UUID.randomUUID().toString(), name = name, settings = settings)
        profiles.add(profile)
        return profile
    }

    fun deleteProfile(id: String) {
        profiles.removeAll { it.id == id }
    }

    fun setActive(id: String) {
        for (i in profiles.indices) {
            profiles[i] = profiles[i].copy(isActive = profiles[i].id == id)
        }
    }

    fun getActive(): Profile? = profiles.firstOrNull { it.isActive }

    fun allProfiles(): List<Profile> = profiles.toList()
}
