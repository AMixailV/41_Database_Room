package ru.mixail_akulov.a41_database_room.screens.main.auth

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.textfield.TextInputLayout
import ru.mixail_akulov.a41_database_room.R
import ru.mixail_akulov.a41_database_room.Repositories
import ru.mixail_akulov.a41_database_room.databinding.FragmentSignUpBinding
import ru.mixail_akulov.a41_database_room.model.accounts.entities.SignUpData
import ru.mixail_akulov.a41_database_room.utils.observeEvent
import ru.mixail_akulov.a41_database_room.utils.viewModelCreator

class SignUpFragment : Fragment(R.layout.fragment_sign_up) {

    private lateinit var binding: FragmentSignUpBinding

    private val viewModel by viewModelCreator { SignUpViewModel(Repositories.accountsRepository) }

    private val args by navArgs<SignUpFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSignUpBinding.bind(view)
        binding.createAccountButton.setOnClickListener { onCreateAccountButtonPressed() }

        if (savedInstanceState == null && getEmailArgument() != null) {
            binding.emailEditText.setText(getEmailArgument())
        }

        observeState()
        observeGoBackEvent()
        observeShowSuccessSignUpMessageEvent()
    }

    private fun onCreateAccountButtonPressed() {
        val signUpData = SignUpData(
            email = binding.emailEditText.text.toString(),
            username = binding.usernameEditText.text.toString(),
            password = binding.passwordEditText.text.toString(),
            repeatPassword = binding.repeatPasswordEditText.text.toString(),
        )
        viewModel.signUp(signUpData)
    }

    private fun observeState() = viewModel.state.observe(viewLifecycleOwner) { state ->
        binding.createAccountButton.isEnabled = state.enableViews
        binding.emailTextInput.isEnabled = state.enableViews
        binding.usernameTextInput.isEnabled = state.enableViews
        binding.passwordTextInput.isEnabled = state.enableViews
        binding.repeatPasswordTextInput.isEnabled = state.enableViews

        fillError(binding.emailTextInput, state.emailErrorMessageRes)
        fillError(binding.usernameTextInput, state.usernameErrorMessageRes)
        fillError(binding.passwordTextInput, state.passwordErrorMessageRes)
        fillError(binding.repeatPasswordTextInput, state.repeatPasswordErrorMessageRes)

        binding.progressBar.visibility = if (state.showProgress) View.VISIBLE else View.INVISIBLE
    }

    private fun observeShowSuccessSignUpMessageEvent() = viewModel.showToastEvent.observeEvent(viewLifecycleOwner) {
        Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
    }

    private fun fillError(input: TextInputLayout, @StringRes stringRes: Int) {
        if (stringRes == SignUpViewModel.NO_ERROR_MESSAGE) {
            input.error = null
            input.isErrorEnabled = false
        } else {
            input.error = getString(stringRes)
            input.isErrorEnabled = true
        }
    }

    private fun observeGoBackEvent() = viewModel.goBackEvent.observeEvent(viewLifecycleOwner) {
        findNavController().popBackStack()
    }

    private fun getEmailArgument(): String? = args.email
}