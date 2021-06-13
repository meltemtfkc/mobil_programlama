package com.meltemtufekci.myproject.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.meltemtufekci.myproject.R
import com.meltemtufekci.myproject.adapter.UserAdapter
import com.meltemtufekci.myproject.utils.Resource

class ListFragment : Fragment() {

    private lateinit var viewModel: MainViewModel
    private lateinit var navController: NavController
    private lateinit var userAdapter: UserAdapter
    private lateinit var rvUsers: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).mainViewModel

        rvUsers = view.findViewById(R.id.fr_list_rv_users)
        setupRecyclerView()

        navController =
            Navigation.findNavController(requireActivity(), R.id.ac_ma_nav_host_fragment)

        rvUsers = view.findViewById(R.id.fr_list_rv_users)

        viewModel.userRes.observe(viewLifecycleOwner, { response ->
            when (response) {
                is Resource.Loading -> {
                    (requireActivity() as MainActivity).showLoading()
                }
                is Resource.Error -> {
                    (requireActivity() as MainActivity).hideLoading()
                    response.message?.let { message ->
                        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
                    }
                }
                is Resource.Success -> {
                    (requireActivity() as MainActivity).hideLoading()
                    response.data?.let { userResponse ->
                        val userList = userResponse.toList()
                        userAdapter.differ.submitList(userList)
                    }
                }
            }
            response.data = null
            response.message = null
        })

        userAdapter.setUserItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("userItemDetail", it)
            }
            navController.navigate(
                R.id.action_listFragment_to_detailFragment,
                bundle
            )
        }
    }

    private fun setupRecyclerView() {
        userAdapter = UserAdapter()
        rvUsers.apply {
            adapter = userAdapter
            layoutManager = LinearLayoutManager(activity)
            viewModel.getUser()
        }
    }
}