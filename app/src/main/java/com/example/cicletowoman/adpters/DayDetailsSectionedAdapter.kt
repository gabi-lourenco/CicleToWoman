package com.example.cicletowoman.adpters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cicletowoman.R
import com.example.cicletowoman.interfaces.DayDetailsClickedListener
import com.example.cicletowoman.model.DayDetails
import kotlinx.android.synthetic.main.expandable_child_item.view.*
import kotlinx.android.synthetic.main.expandable_parent_item.view.*

class DayDetailsSectionedAdapter(
    var countryClickedListener: DayDetailsClickedListener,
    var countryStateModelList: MutableList<ExpandableDayDetailsModel>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var isFirstItemExpanded : Boolean = true
    private var actionLock = false
    lateinit var countryName: String

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            ExpandableDayDetailsModel.PARENT -> {
                SectionStateParentViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.expandable_parent_item, parent, false)
                )
            }

            ExpandableDayDetailsModel.CHILD -> {
                SectionStateChildViewHolder(
                    LayoutInflater
                        .from(parent.context)
                        .inflate(R.layout.expandable_child_item, parent, false)
                )
            }

            else -> {
                SectionStateParentViewHolder(
                    LayoutInflater
                        .from(parent.context)
                        .inflate(R.layout.expandable_parent_item, parent, false)
                )
            }
        }
    }

    override fun getItemCount(): Int = countryStateModelList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val row = countryStateModelList[position]
        when(row.type){
            ExpandableDayDetailsModel.PARENT -> {
                (holder as SectionStateParentViewHolder).countryName.text = row.dayDetailsParent.country
                countryName = row.dayDetailsParent.country
                holder.closeImage.visibility = View.GONE
                holder.upArrowImg.visibility = View.GONE
            }


            ExpandableDayDetailsModel.CHILD -> {
                (holder as SectionStateChildViewHolder).stateName.text = row.dayDetailsChild.name
                countryName.let {
                    holder.layout.tag = it
                }
                holder.stateName.tag = row.dayDetailsChild
                holder.layout.setOnClickListener {
                    var countryInfo = holder.stateName.tag
                    countryClickedListener.onItemClick(holder.layout.tag.toString(),
                        countryInfo as DayDetails.Section.State
                    )
                }
            }
        }

    }

    override fun getItemViewType(position: Int): Int = countryStateModelList[position].type

    class SectionStateParentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var layout = itemView.country_item_parent_container
        internal var countryName : TextView = itemView.section_name
        internal var closeImage = itemView.close_arrow
        internal var upArrowImg = itemView.up_arrow
    }

    class SectionStateChildViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var layout = itemView.country_item_child_container
        internal var stateName : TextView = itemView.state_name
    }
}