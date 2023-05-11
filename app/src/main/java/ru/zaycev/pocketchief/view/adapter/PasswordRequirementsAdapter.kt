package ru.zaycev.pocketchief.view.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.zaycev.pocketchief.R
import ru.zaycev.pocketchief.data.PasswordRequirement

class PasswordRequirementsAdapter(
    private val requirements: ArrayList<PasswordRequirement>
) : RecyclerView.Adapter<PasswordRequirementsAdapter.RequirementsViewHolder>() {

    class RequirementsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val requirementText: TextView = itemView.findViewById(R.id.regRequirementText)
        val icon: ImageView = itemView.findViewById(R.id.regRequirementIcon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RequirementsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_password_requirement, parent,false)
        return RequirementsViewHolder(view)
    }

    override fun onBindViewHolder(holder: RequirementsViewHolder, position: Int) {
        holder.run {
            requirementText.text = requirements[position].text

            if (requirements[position].isRight) {
                icon.setImageResource(R.drawable.ic_check_white)
                requirementText.setTextColor(Color.parseColor("#FFFFFFFF"))
            } else {
                icon.setImageResource(R.drawable.ic_cross)
                requirementText.setTextColor(Color.parseColor("#99FFFFFF"))
            }
        }
    }

    override fun getItemCount(): Int {
        return requirements.size
    }

}