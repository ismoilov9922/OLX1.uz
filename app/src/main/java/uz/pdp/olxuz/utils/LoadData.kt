package uz.pdp.olxuz.utils

import uz.pdp.olxuz.models.Category

object LoadData {

    fun loadCategory(): List<Category> {
        val categoryList = ArrayList<Category>()
        categoryList.add(Category("Barcha rukunlar",
            "all",
            "https://firebasestorage.googleapis.com/v0/b/olxuz-b29da.appspot.com/o/category%2Folx_logo.png?alt=media&token=05b508e0-a5d8-40e6-a1a8-73f0db74ad06"))
        categoryList.add(Category("Bolalar dunyosi",
            "baby",
            "https://firebasestorage.googleapis.com/v0/b/olxuz-b29da.appspot.com/o/category%2Fkalyaska.png?alt=media&token=d0ecda46-5e15-4eb7-ad3e-7d7f6065a5dc"))
        categoryList.add(Category("Ko'chmas mulk",
            "house",
            "https://firebasestorage.googleapis.com/v0/b/olxuz-b29da.appspot.com/o/category%2Fkalit.jpg?alt=media&token=3e41a2e5-1d1c-439d-92db-add2428de518"))
        categoryList.add(Category("Transport",
            "car",
            "https://firebasestorage.googleapis.com/v0/b/olxuz-b29da.appspot.com/o/category%2Fcars.jpg?alt=media&token=94dedbe1-a00f-496e-aaf6-1da8df86d55d"))
        return categoryList
    }
}