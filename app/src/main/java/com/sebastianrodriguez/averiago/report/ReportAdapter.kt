package com.sebastianrodriguez.averiago.report

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.sebastianrodriguez.averiago.R
import Entity.Ticket
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ReportAdapter(
    private var tickets: List<Ticket>,
    private val onItemClick: (Ticket) -> Unit
) : RecyclerView.Adapter<ReportAdapter.ReportViewHolder>() {

    inner class ReportViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvCaseNumber: TextView = itemView.findViewById(R.id.tvCaseNumber)
        val chipStatus: Chip = itemView.findViewById(R.id.chipStatus)
        val tvDescription: TextView = itemView.findViewById(R.id.tvDescription)
        val tvDate: TextView = itemView.findViewById(R.id.tvDate)

        fun bind(ticket: Ticket) {
            tvCaseNumber.text = "Caso ${ticket.getFormattedId()}"
            tvDescription.text = "${ticket.category} - ${ticket.subcategory}"

            // Estado
            chipStatus.text = ticket.status.displayName
            try {
                val colorInt = Color.parseColor(ticket.status.color.takeIf { it.isNotBlank() } ?: "#E5E7EB")
                chipStatus.chipBackgroundColor = android.content.res.ColorStateList.valueOf(colorInt)
            } catch (e: Exception) {
                // fallback color
            }

            // Fecha
            val fmt = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
            tvDate.text = fmt.format(Date(ticket.createdAt))

            // Click listener
            itemView.setOnClickListener {
                onItemClick(ticket)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_report, parent, false)
        return ReportViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReportViewHolder, position: Int) {
        holder.bind(tickets[position])
    }

    override fun getItemCount(): Int = tickets.size

    fun updateTickets(newTickets: List<Ticket>) {
        tickets = newTickets
        notifyDataSetChanged()
    }
}