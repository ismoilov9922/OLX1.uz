package uz.pdp.olxuz.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import uz.pdp.olxuz.database.entity.Product
import uz.pdp.olxuz.repository.ProductRepository
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val productRepository: ProductRepository) : ViewModel() {

    private val products = MutableLiveData<List<Product>>()

    fun getProduct(type: String) = productRepository.getProduct(type)

}