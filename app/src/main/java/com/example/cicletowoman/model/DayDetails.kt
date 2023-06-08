package com.example.cicletowoman.model

data class DayDetails(
    val details: List<Section>
) {
    data class Section(
        val country: String, // Humor
        val states: List<State>
    ) {
        data class State(
            val name: String // Calma
        )
    }
}