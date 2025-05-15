package com.tfdev.inventorymanagement.ui.product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.tfdev.inventorymanagement.R
import com.tfdev.inventorymanagement.data.Product
import com.tfdev.inventorymanagement.databinding.FragmentProductListBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductListFragment : Fragment() {

    private var _binding: FragmentProductListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProductViewModel by viewModels()
    private lateinit var productAdapter: ProductAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupSearchView()
        setupSwipeRefresh()
        setupFab()
        observeUiState()
        setupFragmentResultListener()
    }

    private fun setupRecyclerView() {
        productAdapter = ProductAdapter(
            onItemClick = { product ->
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, ProductDetailFragment.newInstance(product.productId))
                    .addToBackStack(null)
                    .commit()
            },
            onEditClick = { product ->
                showEditDialog(product)
            },
            onDeleteClick = { product ->
                showDeleteConfirmationDialog(product)
            }
        )
        binding.recyclerView.adapter = productAdapter
    }

    private fun setupSearchView() {
        binding.etSearch.addTextChangedListener { editable ->
            editable?.toString()?.let { query ->
                viewModel.searchProducts(query)
            }
        }
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.loadProducts()
        }
    }

    private fun setupFab() {
        binding.fabAdd.setOnClickListener {
            showAddDialog()
        }
    }

    private fun showAddDialog() {
        ProductDialogFragment.newInstance()
            .show(childFragmentManager, "product_dialog")
    }

    private fun showEditDialog(product: Product) {
        ProductDialogFragment.newInstance(product)
            .show(childFragmentManager, "product_dialog")
    }

    private fun setupFragmentResultListener() {
        setFragmentResultListener(ProductDialogFragment.REQUEST_KEY) { _, bundle ->
            val result = bundle.getBoolean(ProductDialogFragment.RESULT_KEY)
            if (result) {
                viewModel.loadProducts()
            }
        }
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collectLatest { state ->
                updateUiState(state)
            }
        }
    }

    private fun updateUiState(state: ProductViewModel.UiState) {
        binding.progressBar.isVisible = state is ProductViewModel.UiState.Loading
        binding.recyclerView.isVisible = state !is ProductViewModel.UiState.Loading

        when (state) {
            is ProductViewModel.UiState.Success -> {
                binding.swipeRefresh.isRefreshing = false
                productAdapter.submitList(state.products)
            }
            is ProductViewModel.UiState.Error -> {
                binding.swipeRefresh.isRefreshing = false
                Snackbar.make(binding.root, state.message, Snackbar.LENGTH_LONG).show()
            }
            is ProductViewModel.UiState.Loading -> {
                // Loading state handled by visibility changes
            }
        }
    }

    private fun showDeleteConfirmationDialog(product: Product) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Ürün Silinecek")
            .setMessage("${product.name} ürününü silmek istediğinizden emin misiniz?")
            .setPositiveButton("Sil") { _, _ ->
                viewModel.deleteProduct(product)
            }
            .setNegativeButton("İptal", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 