package com.example.salonbookingapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment

class MapFragment : Fragment() {

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
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {
            navigationListener?.setBackButtonVisible(true)

            val tvMapInfo = view.findViewById<TextView>(R.id.tvMapInfo)
            val btnGetDirections = view.findViewById<Button>(R.id.btnGetDirections)
            val btnCallSalon = view.findViewById<Button>(R.id.btnCallSalon)
            val mapPlaceholder = view.findViewById<CardView>(R.id.mapPlaceholder)

            tvMapInfo.text = "📍 123 Beauty Street\nJohannesburg, 2000\n📞 011 123 4567\n🕒 9:00 AM - 7:00 PM"

            val openMaps = {
                try {
                    val uri = Uri.parse("google.navigation:q=Beauty+Salon+Johannesburg")
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    intent.setPackage("com.google.android.apps.maps")

                    if (intent.resolveActivity(requireContext().packageManager) != null) {
                        startActivity(intent)
                    } else {
                        val webIntent = Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://www.google.com/maps/search/?api=1&query=Beauty+Salon+Johannesburg"))
                        startActivity(webIntent)
                    }
                } catch (e: Exception) {
                    Toast.makeText(requireContext(), "Error opening map: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }

            mapPlaceholder.setOnClickListener { openMaps() }
            btnGetDirections.setOnClickListener { openMaps() }

            btnCallSalon.setOnClickListener {
                try {
                    val callIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:0111234567"))
                    startActivity(callIntent)
                } catch (e: Exception) {
                    Toast.makeText(requireContext(), "Error making call: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }

        } catch (e: Exception) {
            Toast.makeText(requireContext(), "MapFragment init error: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }
}
