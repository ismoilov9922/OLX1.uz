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
       categoryList.add(Category("Kiyimlar",
            "clothing",
            "https://firebasestorage.googleapis.com/v0/b/olxuz-b29da.appspot.com/o/category%2Fkiyim.png?alt=media&token=f5e22153-4e40-48f2-b956-cc020dc72dfb"))
        categoryList.add(Category("Transport",
            "car",
            "https://firebasestorage.googleapis.com/v0/b/olxuz-b29da.appspot.com/o/category%2Fcars.jpg?alt=media&token=94dedbe1-a00f-496e-aaf6-1da8df86d55d"))
        categoryList.add(Category("Telefonlar",
            "phone",
            "https://firebasestorage.googleapis.com/v0/b/olxuz-b29da.appspot.com/o/category%2Fphone.jpg?alt=media&token=8f9aaf1d-a334-4846-9beb-2421f6b78ac9"))
        categoryList.add(Category("Ish",
            "work",
            "https://firebasestorage.googleapis.com/v0/b/olxuz-b29da.appspot.com/o/category%2Fsuitcase.png?alt=media&token=840f4fe9-1be1-42dd-b44c-3ef0e50f298b"))
        categoryList.add(Category("Uy jihozlari",
            "house_equipment",
            "https://firebasestorage.googleapis.com/v0/b/olxuz-b29da.appspot.com/o/category%2Fhous_jihoz.jpg?alt=media&token=dcd22bbf-b06d-48d2-886c-cb7a76083490"))
        return categoryList
    }
}