package com.codinghub.goodhabitbeta.setting

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.codinghub.goodhabitbeta.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }
}