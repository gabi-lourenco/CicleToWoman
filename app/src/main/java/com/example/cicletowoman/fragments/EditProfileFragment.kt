package com.example.cicletowoman.fragments

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.cicletowoman.MyApplication
import com.example.cicletowoman.R
import com.example.cicletowoman.entities.Profile
import kotlin.system.exitProcess
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.cicletowoman.viewmodels.EditProfileViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_edit_profile.view.*

class EditProfileFragment : Fragment() {

    private val vm: EditProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_edit_profile, container, false)

        vm.getUserProfile()

        vm.profileData?.observe(requireActivity()) { profile ->
            profile?.let {
                setDataProfile(profile, view)
            } ?: run {
                setDataProfileEmpty(view)
            }
        }

        view.btnSave.setOnClickListener { saveData(view) }

        return view.rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.profiletoolbar.inflateMenu(R.menu.menu_profile)
        view.profiletoolbar.setTitle(R.string.first_status_cycle_profile_title)
        view.profiletoolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_save -> {
                    saveData(view)
                    true
                }
                R.id.action_delete -> {
                    deleteAccount()
                    true
                }
                else -> {
                    super.onOptionsItemSelected(it)
                }
            }
        }
    }

    private fun saveData(view: View) {
        try {
            if (fieldsFilled(view)) {
                MyApplication.database!!.profileDao().insert(
                    Profile(
                        uid = vm.getUserLoggedUid(),
                        name = view.edtName.text.toString(),
                        weight = view.edtWeigth.text.toString(),
                        height = view.edtHeight.text.toString(),
                        age = view.edtAge.text.toString().toInt(),
                    )
                )

                Toast.makeText(
                    requireActivity(),
                    R.string.first_status_cycle_save_success, Toast.LENGTH_LONG
                ).show()

                findNavController().navigate(R.id.action_editProfileFragment_to_statusCycleFragment)
            } else {
                Toast.makeText(
                    requireActivity(),R.string.first_status_cycle_profile_set_fields,
                    Toast.LENGTH_LONG
                ).show()
            }
        } catch (e: Exception) {
            setDataProfileEmpty(view)
            Toast.makeText(
                requireActivity(), R.string.first_status_cycle_insert_error, Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun deleteAccount() {
        val builder = AlertDialog.Builder(requireActivity())

        builder.setTitle(R.string.first_status_cycle_delete_account_title)
        builder.setMessage(R.string.first_status_cycle_delete_account_message_title)

        builder.setPositiveButton(R.string.first_status_cycle_delete_text_yes) { _, _ ->
            vm.deleteUserCycle()

            if (vm.dataDeleted == true) {
                Toast.makeText(
                    requireActivity(),
                    R.string.first_status_cycle_deleted_account_success,
                    Toast.LENGTH_LONG
                ).show()

                FirebaseAuth.getInstance().signOut()

                requireActivity().moveTaskToBack(true)
                exitProcess(-1)
            } else {
                Toast.makeText(
                    requireActivity(),
                    R.string.first_status_cycle_insert_error,
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        builder.setNegativeButton(R.string.first_status_cycle_delete_text_no) { _, _ -> }

        builder.show()
    }

    private fun fieldsFilled(view: View): Boolean {
        return view.edtName.text.isNotBlank() &&
                view.edtHeight.text.isNotBlank() &&
                view.edtWeigth.text.isNotBlank() &&
                view.edtAge.text.isNotBlank()
    }

    private fun setDataProfile(profile: Profile, view: View) {
        view.apply {
            edtName.setText(profile.name.toString())
            edtHeight.setText(profile.height.toString())
            edtWeigth.setText(profile.weight.toString())
            edtAge.setText(profile.age.toString())
        }
    }

    private fun setDataProfileEmpty(view: View) {
        view.apply {
            edtName.setText(vm.getUserLoggedName())
            edtHeight.hint = getString(R.string.first_status_cycle_profile_height)
            edtWeigth.hint = getString(R.string.first_status_cycle_profile_weigth)
            edtAge.hint = getString(R.string.first_status_cycle_profile_age)
        }
    }
}