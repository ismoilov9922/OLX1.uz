package uz.pdp.olxuz.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Product(
    @PrimaryKey
    val id: String = "",
    val description: String = "",
    val email: String = "",
    val image: String = "",
    val isLike: String = "false",
    val likeCount: String = "0",
    val latitude: String = "41.326298",
    val longitude: String = "69.228560",
    val viewCount: Int = 0,
    val number: String = "",
    val productName: String = "",
    val salary: String = "",
    val type: String = "",
    val userName: String = "",
    val key: Int = 0,
    val sity: String = "Toshkent",
    val date: String = "72652642",
) : Serializable, Comparable<Product> {
    override fun compareTo(other: Product): Int {
        if (id == "")
            return 0
        return other.id.toLong().compareTo(id.toLong())
    }
}
