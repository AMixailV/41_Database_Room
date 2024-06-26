package ru.mixail_akulov.a41_database_room.screens.main.tabs.settings

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import ru.mixail_akulov.a41_database_room.R
import ru.mixail_akulov.a41_database_room.Repositories
import ru.mixail_akulov.a41_database_room.databinding.FragmentSettingsBinding
import ru.mixail_akulov.a41_database_room.utils.observeEvent
import ru.mixail_akulov.a41_database_room.utils.viewModelCreator

class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private lateinit var binding: FragmentSettingsBinding

    private val viewModel by viewModelCreator { SettingsViewModel(Repositories.boxesRepository) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSettingsBinding.bind(view)

        val adapter = setupList()
        viewModel.boxSettings.observe(viewLifecycleOwner) { adapter.renderSettings(it) }

        viewModel.showErrorMessageEvent.observeEvent(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupList(): SettingsAdapter {
        binding.settingsList.layoutManager = LinearLayoutManager(requireContext())
        val adapter = SettingsAdapter(viewModel)
        binding.settingsList.adapter = adapter
        return adapter
    }

}