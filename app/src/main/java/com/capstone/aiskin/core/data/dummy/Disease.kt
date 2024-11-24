package com.capstone.aiskin.core.data.dummy


data class DiseaseItem(val imageUrl: String, val name: String, val description: String)

public fun getDummyDiseases(): List<DiseaseItem> {
    return listOf(
        DiseaseItem(
            "https://www.yalemedicine.org/conditions/psoriasis",
            "Psoriasis",
            "Kondisi kulit kronis dengan bercak merah bersisik."
        ),
        DiseaseItem(
            "https://www.honestdocs.id/apa-bedanya-eksim-dan-infeksi-kulit",
            "Eksim",
            "Iritasi kulit menyebabkan kemerahan dan gatal."
        ),
        DiseaseItem(
            "https://www.sidomunculstore.com/blog/post/cara-menghilangkan-jerawat-di-pipi-penyebab-solusi-dan-tips.html",
            "Jerawat",
            "Penyumbatan pori-pori menyebabkan bintik kecil."
        ),
        DiseaseItem(
            "https://www.halodoc.com/kesehatan/kurap",
            "Kurap",
            "Infeksi jamur berbentuk cincin merah di kulit."
        ),
        DiseaseItem(
            "https://www.associatedskincare.com/blog/why-do-i-have-rosacea",
            "Rosacea",
            "Kemerahan kronis di wajah dengan bintil kecil."
        ),
        DiseaseItem(
            "https://www.mitrakeluarga.com/artikel/gejala-herpes-zoster",
            "Herpes Zoster",
            "Ruam menyakitkan disebabkan oleh virus cacar air."
        ),
        DiseaseItem(
            "https://primayahospital.com/kulit-dan-kelamin/melasma/",
            "Melasma",
            "Bercak coklat pada wajah karena paparan matahari."
        ),
        DiseaseItem(
            "https://id.wikipedia.org/wiki/Urtikaria",
            "Urtikaria",
            "Biduran dengan bercak merah dan gatal."
        ),
        DiseaseItem(
            "https://www.alomedika.com/penyakit/dermatovenereologi/skabies",
            "Skabies",
            "Infeksi kutu menyebabkan rasa gatal di malam hari."
        ),
        DiseaseItem(
            "https://www.google.com/imgres?q=vitiligo&imgurl=https%3A%2F%2Fupload.wikimedia.org%2Fwikipedia%2Fcommons%2F7%2F75%2FVitiligo2.JPG&imgrefurl=https%3A%2F%2Fid.wikipedia.org%2Fwiki%2FVitiligo&docid=TQImtyDJ3RGXbM&tbnid=PmcT5IoQjUUr1M&vet=12ahUKEwjd4K6r4PKJAxV1xjgGHVqMMqEQM3oECB0QAA..i&w=4100&h=2910&hcb=2&ved=2ahUKEwjd4K6r4PKJAxV1xjgGHVqMMqEQM3oECB0QAA",
            "Vitiligo",
            "Hilangnya pigmen kulit menyebabkan bercak putih."
        )
    )
}