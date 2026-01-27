package com.example.planfollower.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.example.planfollower.ui.fragments.LoginFragmentDirections
import com.example.planfollower.api.RetrofitClient
import com.example.planfollower.api.TokenManager
import com.example.planfollower.databinding.FragmentLoginBinding
import com.example.planfollower.models.LoginRequest
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val savedToken = TokenManager.getToken(requireContext())

        if (savedToken != null) {
            //If exist directly go to main notes page
            val action = LoginFragmentDirections.Companion.actionLoginFragmentToNotesFragment()
            Navigation.findNavController(view).navigate(action)
        }

        binding.btnLogin.setOnClickListener {
            login(it)
        }

        binding.tvRegisterText.setOnClickListener {
            goRegisterPage(it)
        }

    }

    fun login(view: View){
        val email = binding.etEmailAddressLogin.text.toString()
        val password = binding.etPasswordLogin.text.toString()

        if (email.isNotEmpty() && password.isNotEmpty()) {
            val loginRequest = LoginRequest(email, password)
            performLogin(loginRequest)
        } else {
            Toast.makeText(requireContext(), "Please fill all part.", Toast.LENGTH_SHORT).show()
        }


    }

    private fun performLogin(request: LoginRequest) {
        // starting coroutine
        lifecycleScope.launch {
            try {
                // RetrofitClient requests
                val response = RetrofitClient.instance.loginUser(request)

                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    val token = loginResponse?.token

                    // succesfull
                    if (token != null) {
                        //save Token to phone
                        TokenManager.saveToken(requireContext(), token)

                        Log.d("PlanFollower", "Token succesfully saved!")

                        // navigate when everything ok
                        val action = LoginFragmentDirections.Companion.actionLoginFragmentToNotesFragment()
                        Navigation.findNavController(requireView()).navigate(action)
                    }

                } else {
                    // Error: 401, 404 (example)
                    val errorBody = response.errorBody()?.string()
                    Log.e("PlanFollower", "Problem: $errorBody")
                    Toast.makeText(requireContext(), "Login faileed: Check your information.", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                // internet has a connection problem or server is not running
                Log.e("PlanFollower", "COnnection Error: ${e.message}")
                Toast.makeText(requireContext(), "Cannot connected to Server!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun goRegisterPage (view: View){
        val action = LoginFragmentDirections.Companion.actionLoginFragmentToRegisterFragment()
        Navigation.findNavController(view).navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }





}