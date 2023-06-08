package com.example.cicletowoman.bottomsheet

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cicletowoman.R
import com.example.cicletowoman.adpters.DayDetailsSectionedAdapter
import com.example.cicletowoman.adpters.ExpandableDayDetailsModel
import com.example.cicletowoman.interfaces.DayDetailsClickedListener
import com.example.cicletowoman.model.DayDetails
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.bottom_sheet_edit_day_history.*
import kotlinx.android.synthetic.main.bottom_sheet_edit_day_history.view.*

class EditDayBottomSheetDialog : BottomSheetDialogFragment(), DayDetailsClickedListener {

    var dayDetailsStateSectionedAdapter : DayDetailsSectionedAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.bottom_sheet_edit_day_history, container, false)

        val data = prepareDataForSectionedAdapter(
            DayDetails(
                listOf(
                    DayDetails.Section(
                        "Humor", listOf(
                            DayDetails.Section.State("Bom"),
                            DayDetails.Section.State("Ruim"),
                            DayDetails.Section.State("Mal")
                        )
                    ),
                    DayDetails.Section(
                        "Sintomas", listOf(
                            DayDetails.Section.State("Acne"),
                            DayDetails.Section.State("Bem"),
                            DayDetails.Section.State("Náusea")
                        )
                    )
                )
            )
        )

        populateAdapterWithInfo(data, view)

        return view
    }

    private fun prepareDataForSectionedAdapter(
        stateCapital: DayDetails)
    : MutableList<ExpandableDayDetailsModel> {

        var expandableCountryModel = mutableListOf<ExpandableDayDetailsModel>()

        for (states in stateCapital.details) {
            expandableCountryModel.add(
                ExpandableDayDetailsModel(ExpandableDayDetailsModel.PARENT,states)
            )
            for(capitals in states.states) {
                expandableCountryModel.add(
                    ExpandableDayDetailsModel(ExpandableDayDetailsModel.CHILD,capitals)
                )
            }
        }

        return expandableCountryModel
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun populateAdapterWithInfo(
        expandableDayDetailsList : MutableList<ExpandableDayDetailsModel>,
        view: View)
    {
        dayDetailsStateSectionedAdapter = DayDetailsSectionedAdapter(this, expandableDayDetailsList)
        dayDetailsStateSectionedAdapter?.let {
            view.day_details_state_sectioned_list_rv.layoutManager = LinearLayoutManager(context)
            view.day_details_state_sectioned_list_rv.adapter = it
            view.day_details_state_sectioned_list_rv.addItemDecoration(
                DividerItemDecoration(this.context,
                    DividerItemDecoration.VERTICAL)
            )
            it.notifyDataSetChanged()
        }
    }

    companion object {
        const val TAG = "ModalBottomSheetEditDay"
    }

    override fun onItemClick(stateText: String, sectionChild: DayDetails.Section.State) {
        Toast.makeText(
            requireActivity(),
            "Clicked on $stateText with info ${sectionChild.name} and ${sectionChild.name}",
            Toast.LENGTH_LONG
        ).show()
    }
}