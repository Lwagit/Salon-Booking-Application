package com.example.salonbookingapp

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment

class ReviewsFragment : Fragment() {

    private var navigationListener: NavigationListener? = null
    private lateinit var reviewsContainer: LinearLayout // Container for reviews

    override fun onAttach(context: android.content.Context) {
        super.onAttach(context)
        if (context is NavigationListener)
            navigationListener = context
    }

    override fun onDetach() {
        super.onDetach()
        navigationListener = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_reviews, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {
            navigationListener?.setBackButtonVisible(true)

            val ratingBar = view.findViewById<RatingBar>(R.id.ratingBar)
            val tvRating = view.findViewById<TextView>(R.id.tvRating)
            val btnAddReview = view.findViewById<Button>(R.id.btnAddReview)
            reviewsContainer = view.findViewById(R.id.reviewsContainer) // Your LinearLayout for reviews

            ratingBar.rating = 4.8f
            tvRating.text = "4.8/5 Stars"

            btnAddReview.setOnClickListener {
                showAddReviewDialog()
            }

        } catch (e: Exception) {
            Toast.makeText(requireContext(), "ReviewsFragment error: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun showAddReviewDialog() {
        try {
            val dialogView = LayoutInflater.from(requireContext())
                .inflate(R.layout.dialog_add_review, null)
            val ratingBar = dialogView.findViewById<RatingBar>(R.id.dialogRatingBar)

            AlertDialog.Builder(requireContext())
                .setTitle("Add Your Review")
                .setView(dialogView)
                .setPositiveButton("Submit") { dialog, _ ->
                    val rating = ratingBar.rating
                    if (rating > 0) {
                        addReview("You", rating, "Thank you for your feedback!", "Just now")
                        showThankYouMessage(rating)
                    } else {
                        Toast.makeText(requireContext(), "Please select a rating", Toast.LENGTH_SHORT).show()
                    }
                    dialog.dismiss()
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }
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

    private fun addReview(name: String, rating: Float, comment: String, timeAgo: String) {
        val inflater = LayoutInflater.from(requireContext())
        val reviewCard = inflater.inflate(R.layout.item_review, reviewsContainer, false)

        val tvName = reviewCard.findViewById<TextView>(R.id.tvReviewerName)
        val tvComment = reviewCard.findViewById<TextView>(R.id.tvComment)
        val tvTime = reviewCard.findViewById<TextView>(R.id.tvTimeAgo)
        val ratingBar = reviewCard.findViewById<RatingBar>(R.id.reviewRatingBar)

        tvName.text = name
        tvComment.text = comment
        tvTime.text = timeAgo
        ratingBar.rating = rating

        reviewsContainer.addView(reviewCard, 0) // Add at the top
    }
}
