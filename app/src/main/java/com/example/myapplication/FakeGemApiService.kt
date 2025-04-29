package com.example.myapplication



class FakeGemApiService {
    fun getGems(): List<GemItem> {
        return listOf(
            GemItem(
                1,
                "Natural Blue Sapphire - 11.00 Ratti",
                66000.0,
                imageResId = R.drawable.image1
            ),
            GemItem(
                2,
                "Ceylone Natural Blue Sapphire",
                68000.0,
                imageResId = R.drawable.image2

            ),
            GemItem(
                3,
                "Ruby / Maanik",
                2550.0,
                imageResId = R.drawable.image3
            ),
            GemItem(
                4,
                "Yellow Sapphire",
                51000.0,
                imageResId = R.drawable.image5

            ),
            GemItem(
                5,
                "Pearl - Moti Ring",
                1388.0,
                imageResId = R.drawable.image6
            ),
            GemItem(
                6,
                "Hessonite",
                1038.0,
                imageResId = R.drawable.image7
            ),
            GemItem(
                7,
                "Opal",
                1999.0,
                imageResId = R.drawable.image9
            ),
            GemItem(
                8,
                "Pyrite Silver Rings",
                1500.0,
                imageResId = R.drawable.image4
            )

        )
    }
}