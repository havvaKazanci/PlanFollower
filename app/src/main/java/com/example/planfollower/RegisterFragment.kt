package com.example.planfollower

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.example.planfollower.api.RetrofitClient
import com.example.planfollower.databinding.FragmentDetailBinding
import com.example.planfollower.databinding.FragmentRegisterBinding
import kotlinx.coroutines.launch

class RegisterFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnRegister.setOnClickListener {
            register(it)
        }


    }

    fun register(view: View){

        val name = binding.etRegisterName.text.toString().trim()
        val surname = binding.etRegisterSurname.text.toString().trim()
        val email = binding.etRegisterEmail.text.toString().trim()
        val password = binding.etRegisterPassword.text.toString().trim()
        val passwordConfirm = binding.etRegisterPasswordConfirm.text.toString().trim()

        if (name.isEmpty() || surname.isEmpty() || email.isEmpty() || password.isEmpty() || passwordConfirm.isEmpty()) {
            Toast.makeText(requireContext(), "Fill all parts!", Toast.LENGTH_SHORT).show()
            return@register
        }

        if (password != passwordConfirm) {
            Toast.makeText(requireContext(), "Passwords are not the same!", Toast.LENGTH_SHORT).show()
            return@register
        }

        val registerRequest = RegisterRequest(name, surname, email, password)
        performRegister(registerRequest)

    }


    private fun performRegister(request: RegisterRequest) {
        lifecycleScope.launch {
            try {
                //post by RetrofitClient
                val response = RetrofitClient.instance.registerUser(request)

                if (response.isSuccessful) {
                    //Registration is successful
                    Toast.makeText(requireContext(), "Registration process is successful, you can login now.", Toast.LENGTH_LONG).show()

                    //Go to login page when process ok. (Registration ok)
                    val action = RegisterFragmentDirections.actionRegisterFragmentToLoginFragment()
                    Navigation.findNavController(requireView()).navigate(action)
                } else {
                    //Backend errors such as this email is already in the system
                    val errorBody = response.errorBody()?.string()
                    Log.e("PlanFollower", "Registration Error: $errorBody")
                    Toast.makeText(requireContext(), "Registration failed: Invalid, used email.", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                //Server is not running or there is a connection fail.
                Log.e("PlanFollower", "Connection error: ${e.message}")
                Toast.makeText(requireContext(), "Cannot connected to server!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}