package com.example.cuims

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import com.google.android.material.card.MaterialCardView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.util.*

/**
 * A professional dashboard screen that displays "Home" content with Dialogs, Pickers and Menus.
 */
class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // 1. Initialize Views
        val btnStudentActions = view.findViewById<View>(R.id.btnStudentActions)
        val tvContextMenuTrigger = view.findViewById<TextView>(R.id.tvContextMenuTrigger)
        
        val cardRegister = view.findViewById<MaterialCardView>(R.id.cardRegisterCourse)
        val cardView = view.findViewById<MaterialCardView>(R.id.cardViewCourses)
        val cardAttendance = view.findViewById<MaterialCardView>(R.id.cardAttendance)
        val cardResults = view.findViewById<MaterialCardView>(R.id.cardResults)
        val cardFees = view.findViewById<MaterialCardView>(R.id.cardFees)
        val cardNotifications = view.findViewById<MaterialCardView>(R.id.cardNotifications)

        // 2. Setup Dashboard Click Listeners with Dialogs & Pickers
        cardRegister.setOnClickListener { showCourseSelectionDialog() }
        cardView.setOnClickListener { showToast("View Courses clicked") }
        cardAttendance.setOnClickListener { showAttendanceDatePicker() }
        cardResults.setOnClickListener { showToast("Results clicked") }
        cardFees.setOnClickListener { showToast("Fees clicked") }
        cardNotifications.setOnClickListener { showReminderTimePicker() }

        // 3. Setup Popup Menu on Button Click
        btnStudentActions.setOnClickListener {
            showPopupMenu(it)
        }

        // 4. Register for Context Menu
        registerForContextMenu(tvContextMenuTrigger)

        return view
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    // --- DIALOG EXAMPLES ---

    private fun showCourseSelectionDialog() {
        val courses = arrayOf("Computer Science", "Mathematics", "Physics", "Engineering", "Arts")
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.select_course)
            .setItems(courses) { _, which ->
                showToast("Registered for ${courses[which]}")
            }
            .show()
    }

    private fun showAttendanceDatePicker() {
        val calendar = Calendar.getInstance()
        DatePickerDialog(
            requireContext(),
            { _, year, month, day ->
                showToast("Checking attendance for: $day/${month + 1}/$year")
            },
            calendar[Calendar.YEAR],
            calendar[Calendar.MONTH],
            calendar[Calendar.DAY_OF_MONTH]
        ).show()
    }

    private fun showReminderTimePicker() {
        val calendar = Calendar.getInstance()
        TimePickerDialog(
            requireContext(),
            { _, hour, minute ->
                showToast("Reminder set for $hour:$minute")
            },
            calendar[Calendar.HOUR_OF_DAY],
            calendar[Calendar.MINUTE],
            true
        ).show()
    }

    private fun showLogoutConfirmation() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.confirm_logout)
            .setMessage(R.string.logout_message)
            .setPositiveButton(R.string.yes) { _, _ ->
                performLogout()
            }
            .setNegativeButton(R.string.no, null)
            .show()
    }

    // --- POPUP MENU LOGIC ---
    private fun showPopupMenu(view: View) {
        val popup = PopupMenu(requireContext(), view)
        popup.menuInflater.inflate(R.menu.popup_menu, popup.menu)

        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.pop_register -> {
                    showCourseSelectionDialog()
                    true
                }
                R.id.pop_view -> {
                    showToast("Loading Students List...")
                    true
                }
                R.id.pop_update -> {
                    showToast("Updating Profile...")
                    true
                }
                R.id.pop_logout -> {
                    showLogoutConfirmation()
                    true
                }
                else -> false
            }
        }
        popup.show()
    }

    // --- CONTEXT MENU LOGIC ---
    override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        requireActivity().menuInflater.inflate(R.menu.context_menu, menu)
        menu.setHeaderTitle("Select Action")
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.ctx_edit -> {
                showToast("Edit Selected")
                true
            }
            R.id.ctx_delete -> {
                showToast("Delete Selected")
                true
            }
            R.id.ctx_view_details -> {
                showToast("Viewing Details")
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    private fun performLogout() {
        val intent = Intent(requireContext(), LoginActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }
}