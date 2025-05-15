package com.tfdev.inventorymanagement.ui.product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import com.google.android.material.textfield.TextInputEditText
import com.tfdev.inventorymanagement.data.Product
import com.tfdev.inventorymanagement.databinding.DialogProductBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductDialogFragment : DialogFragment() {

    private var _binding: DialogProductBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProductViewModel by viewModels()
    private var product: Product? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        product = arguments?.getParcelable(ARG_PRODUCT)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        setupClickListeners()
    }

    private fun setupViews() {
        product?.let { product ->
            binding.apply {
                etProductName.setText(product.name)
                etProductDescription.setText(product.description)
                etProductStock.setText(product.stock.toString())
                etProductPrice.setText(product.price.toString())
                etCategoryId.setText(product.categoryId.toString())
                etSupplierId.setText(product.supplierId.toString())
                btnSave.text = "Güncelle"
                dialogTitle.text = "Ürün Düzenle"
            }
        }
    }

    private fun setupClickListeners() {
        binding.btnSave.setOnClickListener {
            if (validateInputs()) {
                val newProduct = createProductFromInputs()
                if (product == null) {
                    viewModel.addProduct(newProduct)
                } else {
                    viewModel.updateProduct(newProduct.copy(productId = product!!.productId))
                }
                setFragmentResult(REQUEST_KEY, bundleOf(RESULT_KEY to true))
                dismiss()
            }
        }

        binding.btnCancel.setOnClickListener {
            dismiss()
        }
    }

    private fun validateInputs(): Boolean {
        val inputs = listOf(
            binding.etProductName to "Ürün adı boş olamaz",
            binding.etProductDescription to "Ürün açıklaması boş olamaz",
            binding.etProductStock to "Stok miktarı boş olamaz",
            binding.etProductPrice to "Fiyat boş olamaz",
            binding.etCategoryId to "Kategori ID boş olamaz",
            binding.etSupplierId to "Tedarikçi ID boş olamaz"
        )

        var isValid = true
        inputs.forEach { (input, error) ->
            if (input.text.isNullOrBlank()) {
                input.error = error
                isValid = false
            }
        }
        return isValid
    }

    private fun createProductFromInputs(): Product {
        return Product(
            name = binding.etProductName.text.toString(),
            description = binding.etProductDescription.text.toString(),
            stock = binding.etProductStock.text.toString().toInt(),
            price = binding.etProductPrice.text.toString().toDouble(),
            categoryId = binding.etCategoryId.text.toString().toInt(),
            supplierId = binding.etSupplierId.text.toString().toInt()
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_PRODUCT = "arg_product"
        const val REQUEST_KEY = "product_dialog_request"
        const val RESULT_KEY = "product_dialog_result"

        fun newInstance(product: Product? = null): ProductDialogFragment {
            return ProductDialogFragment().apply {
                arguments = bundleOf(ARG_PRODUCT to product)
            }
        }
    }
} 