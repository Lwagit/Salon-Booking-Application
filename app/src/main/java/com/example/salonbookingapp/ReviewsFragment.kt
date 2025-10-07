package com.example.salonbookingapp

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment

class ReviewsFragment : Fragment() {

    private var navigationListener: NavigationListener? = null

    override fun onAttach(context: android.content.Context) {
        super.onAttach(context)
        if (context is NavigationListener) navigationListener = context
    }

    override fun onDetach() {
        super.onDetach()
        navigationListener = null
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_reviews, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {
            navigationListener?.setBackButtonVisible(true)

            val ratingBar = view.findViewById<RatingBar>(R.id.ratingBar)
            val tvRating = view.findViewById<TextView>(R.id.tvRating)
            val btnAddReview = view.findViewById<Button>(R.id.btnAddReview)

            ratingBar.rating = 4.8f
            tvRating.text = "4.8/5 Stars"

            btnAddReview.setOnClickListener { showAddReviewDialog() }

        } catch (e: Exception) {
            Toast.makeText(requireContext(), "ReviewsFragment error: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun showAddReviewDialog() {
        try {
            val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_review, null)
            val ratingBar = dialogView.findViewById<RatingBar>(R.id.dialogRatingBar)

            AlertDialog.Builder(requireContext())
                .setTitle("Add Your Review")
                .setView(dialogView)
                .setPositiveButton("Submit") { dialog, _ ->
                    val rating = ratingBar.rating
                    if (rating > 0) showThankYouMessage(rating)
                    else Toast.makeText(requireContext(), "Please select a rating", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                }
                .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
                .show()
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Dialog error: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun showThankYouMessage(rating: Float) {
        val message = when (rating.toInt()) {
            5 -> "⭐⭐⭐⭐⭐ Thank you for the perfect rating!"
            4 -> "⭐⭐⭐⭐ Thank you for your great rating!"
            3 -> "⭐⭐⭐ Thank you for your feedback."
            else -> "Thank you for your review!"
        }

        AlertDialog.Builder(requireContext())
            .setTitle("Review Submitted")
            .setMessage("$message\nYour rating: $rating/5 stars")
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .show()
    }
}
