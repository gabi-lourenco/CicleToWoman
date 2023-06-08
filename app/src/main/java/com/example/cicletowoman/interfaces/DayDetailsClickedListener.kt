package com.example.cicletowoman.interfaces

import com.example.cicletowoman.model.DayDetails

interface DayDetailsClickedListener {
    fun onItemClick(stateText : String, sectionChild : DayDetails.Section.State)
}