package com.example.cicletowoman.adpters

import com.example.cicletowoman.model.DayDetails

class ExpandableDayDetailsModel {

    lateinit var dayDetailsParent: DayDetails.Section
    var type : Int
    lateinit var dayDetailsChild : DayDetails.Section.State
    var isExpanded : Boolean
    private var isCloseShown : Boolean

    constructor(
        type : Int,
        dayDetailsParent: DayDetails.Section,
        isExpanded : Boolean = false,
        isCloseShown : Boolean = false
    ) {
        this.type = type
        this.dayDetailsParent = dayDetailsParent
        this.isExpanded = isExpanded
        this.isCloseShown = isCloseShown
    }

    constructor(
        type : Int,
        dayDetailsChild : DayDetails.Section.State,
        isExpanded : Boolean = false,
        isCloseShown : Boolean = false
    ) {
        this.type = type
        this.dayDetailsChild = dayDetailsChild
        this.isExpanded = isExpanded
        this.isCloseShown = isCloseShown
    }

    companion object{
        const val PARENT = 1
        const val CHILD = 2
    }
}